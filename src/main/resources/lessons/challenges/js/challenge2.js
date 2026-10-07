(function () {
  "use strict";

  const importForm = document.getElementById("partner-feed-import");
  if (!importForm) return;

  document.getElementById("partner-feed-bulletin-url").textContent =
    document.getElementById("partner-feed-bulletin-link").href;
  document.getElementById("partner-feed-diagnostic-url").textContent =
    document.getElementById("partner-feed-diagnostic-link").href;

  importForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const preview = document.getElementById("partner-feed-preview");
    try {
      const response = await fetch(importForm.action, {
        method: "POST",
        body: new FormData(importForm),
      });
      const result = await response.json();
      preview.textContent = response.ok ? result.preview : result.error;
    } catch (error) {
      preview.textContent = `Import failed: ${error.message}`;
    }
  });
})();
