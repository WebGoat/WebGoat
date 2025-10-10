(function(){
  const envButton = document.getElementById('fetch-env');
  const healthButton = document.getElementById('fetch-health');
  const output = document.getElementById('actuator-output');

  const callEndpoint = async (button) => {
    if (!button || !output) {
      return;
    }
    button.disabled = true;
    output.textContent = `Calling ${button.textContent} ...`;
    try {
      const response = await fetch(button.getAttribute('data-url'));
      const text = await response.text();
      output.textContent = text;
    } catch (err) {
      output.textContent = 'Request failed: ' + err;
    } finally {
      button.disabled = false;
    }
  };

  if (envButton) {
    envButton.addEventListener('click', () => callEndpoint(envButton));
  }
  if (healthButton) {
    healthButton.addEventListener('click', () => callEndpoint(healthButton));
  }
})();
