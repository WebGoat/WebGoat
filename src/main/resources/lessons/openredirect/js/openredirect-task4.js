/*
 * Autofill helper for Open Redirect Task 4 (double encoding bypass)
 * Injects a crafted payload that passes first decode but changes host after second decode.
 */
(function () {
  function ready(fn) {
    if (document.readyState !== 'loading') {
      fn();
    } else {
      document.addEventListener('DOMContentLoaded', fn);
    }
  }

  ready(function () {
    const btn = document.getElementById('task4-autofill');
    const input = document.getElementById('t4target');
    if (!btn || !input) return;

    const payload = btn.dataset.sample || 'https://webgoat.local%2540evil.com';
    btn.addEventListener('click', function (e) {
      e.preventDefault();
      input.value = payload;
      input.classList.add('highlight-autofill');
      setTimeout(() => input.classList.remove('highlight-autofill'), 1200);
    });
  });
})();
