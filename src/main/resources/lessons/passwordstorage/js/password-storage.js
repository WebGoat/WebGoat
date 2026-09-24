(function () {
  "use strict";

  const encoder = new TextEncoder();

  function toHex(bytes) {
    return Array.from(bytes, (byte) => byte.toString(16).padStart(2, "0")).join("");
  }

  function fromBase64(value) {
    const padded = value.padEnd(Math.ceil(value.length / 4) * 4, "=");
    const binary = atob(padded);
    return Uint8Array.from(binary, (character) => character.charCodeAt(0));
  }

  async function sha256(bytes) {
    return new Uint8Array(await crypto.subtle.digest("SHA-256", bytes));
  }

  async function loadJson(element) {
    const response = await fetch(element.dataset.url);
    if (!response.ok) {
      throw new Error(`Request failed with status ${response.status}`);
    }
    return response.json();
  }

  function renderRecords(container, data) {
    const table = document.createElement("table");
    const header = document.createElement("tr");
    const hasSalt = data.records.some((record) => record.salt);
    ["User", "Role", ...(hasSalt ? ["Salt, Base64"] : []), "Hash"].forEach((label) => {
      const cell = document.createElement("th");
      cell.textContent = label;
      header.appendChild(cell);
    });
    table.appendChild(header);

    data.records.forEach((record) => {
      const row = document.createElement("tr");
      const values = [record.username, record.role, ...(hasSalt ? [record.salt] : []), record.hash];
      values.forEach((value, index) => {
        const cell = document.createElement("td");
        if (index === values.length - 1 || (hasSalt && index === values.length - 2)) {
          const code = document.createElement("code");
          code.textContent = value;
          cell.appendChild(code);
        } else {
          cell.textContent = value;
        }
        row.appendChild(cell);
      });
      table.appendChild(row);
    });

    const scheme = document.createElement("p");
    scheme.textContent = `Storage scheme: ${data.scheme}`;
    container.replaceChildren(scheme, table);
  }

  async function initialiseStage1() {
    const dump = document.getElementById("password-storage-stage1-dump");
    if (!dump) return;
    const data = await loadJson(dump);
    renderRecords(dump, data);
    document.getElementById("password-storage-stage1-crack").addEventListener("click", async () => {
      const output = document.getElementById("password-storage-stage1-output");
      const target = data.records.find((record) => record.username === "maria");
      for (const candidate of data.wordlist) {
        const candidateHash = toHex(await sha256(encoder.encode(candidate)));
        if (candidateHash === target.hash) {
          output.textContent = `Match found after ${data.wordlist.indexOf(candidate) + 1} candidates: ${candidate}`;
          return;
        }
      }
      output.textContent = "No supplied candidate matched.";
    });
  }

  async function initialiseStage2() {
    const dump = document.getElementById("password-storage-stage2-dump");
    if (!dump) return;
    const data = await loadJson(dump);
    renderRecords(dump, data);
    document.getElementById("password-storage-stage2-crack").addEventListener("click", async () => {
      const output = document.getElementById("password-storage-stage2-output");
      const target = data.records.find((record) => record.username === "maria");
      const salt = fromBase64(target.salt);
      for (const candidate of data.wordlist) {
        const password = encoder.encode(candidate);
        const input = new Uint8Array(password.length + salt.length);
        input.set(password);
        input.set(salt, password.length);
        const candidateHash = toHex(await sha256(input));
        if (candidateHash === target.hash) {
          output.textContent = `Public salt used. Match found after ${data.wordlist.indexOf(candidate) + 1} candidates: ${candidate}`;
          return;
        }
      }
      output.textContent = "No supplied candidate matched.";
    });
  }

  async function initialiseStage3() {
    const dump = document.getElementById("password-storage-stage3-dump");
    if (!dump) return;
    const data = await loadJson(dump);
    dump.textContent = `${data.username} (${data.role})\n${data.encodedHash}`;
  }

  async function initialiseStage4() {
    const dump = document.getElementById("password-storage-stage4-dump");
    if (!dump) return;
    const data = await loadJson(dump);
    dump.textContent = JSON.stringify(data, null, 2);
  }

  function initialiseStage5() {
    const loadButton = document.getElementById("password-storage-load-config");
    if (!loadButton) return;
    loadButton.addEventListener("click", async () => {
      const response = await fetch(loadButton.dataset.url);
      document.getElementById("password-storage-config-output").textContent = await response.text();
    });

    const crackButton = document.getElementById("password-storage-stage5-crack");
    crackButton.addEventListener("click", async () => {
      const output = document.getElementById("password-storage-stage5-output");
      const pepper = document.getElementById("password-storage-pepper").value;
      output.textContent = "Testing the bounded training wordlist...";
      const response = await fetch(`${crackButton.dataset.url}?pepper=${encodeURIComponent(pepper)}`);
      const result = await response.json();
      output.textContent = result.matched
        ? `Match found after ${result.candidatesTested} candidates: ${result.password}`
        : result.message;
    });
  }

  function initialise() {
    Promise.all([initialiseStage1(), initialiseStage2(), initialiseStage3(), initialiseStage4()])
      .catch((error) => console.error("Unable to load password storage training data", error));
    initialiseStage5();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initialise);
  } else {
    initialise();
  }
})();
