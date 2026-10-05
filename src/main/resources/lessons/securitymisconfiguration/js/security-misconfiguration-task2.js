(function(){
  const triggerButton = document.getElementById('trigger-debug');
  const configButton = document.getElementById('fetch-config');
  const debugOutput = document.getElementById('debug-output');
  const configOutput = document.getElementById('config-output');
  const tokenField = document.getElementById('token');

  if (triggerButton) {
    triggerButton.addEventListener('click', async () => {
      triggerButton.disabled = true;
      debugOutput.textContent = 'Loading stack trace...';
      try {
        const response = await fetch(triggerButton.getAttribute('data-url'));
        const text = await response.text();
        debugOutput.textContent = text;
      } catch (err) {
        debugOutput.textContent = 'Request failed: ' + err;
      } finally {
        triggerButton.disabled = false;
      }
    });
  }

  if (configButton) {
    configButton.addEventListener('click', async () => {
      configButton.disabled = true;
      configOutput.classList.remove('d-none');
      configOutput.textContent = 'Querying /config ...';
      try {
        const url = new URL(configButton.getAttribute('data-url'), window.location.origin);
        url.searchParams.set('token', tokenField.value || '');
        const response = await fetch(url, { method: 'GET' });
        const text = await response.text();
        configOutput.textContent = text;
      } catch (err) {
        configOutput.textContent = 'Request failed: ' + err;
      } finally {
        configButton.disabled = false;
      }
    });
  }
})();
