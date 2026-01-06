const vm = require('vm');
const path = require('path');
const fs = require('fs');

describe('jwt-refresh.js placeholder usage (delta tests)', () => {
  function loadJwtRefreshIntoSandbox() {
    const ajaxMock = jest.fn().mockReturnValue({ success: jest.fn(function (cb) { return cb({}); }) });

    const sandbox = {
      window: {},
      localStorage: {
        store: {},
        setItem(key, value) { this.store[key] = value; },
        getItem(key) { return this.store[key]; }
      },
      webgoat: { customjs: {} },
      $: { ajax: ajaxMock },
      document: {},
      console: console,
      setTimeout,
      clearTimeout
    };

    const code = fs.readFileSync(
      path.resolve(__dirname, '../../../../../main/resources/lessons/jwt/js/jwt-refresh.js'),
      'utf8'
    );

    vm.runInNewContext(code, sandbox);

    return { sandbox, ajaxMock };
  }

  test('login uses placeholder constant as password value in AJAX payload', () => {
    const { ajaxMock } = loadJwtRefreshIntoSandbox();

    expect(ajaxMock).toHaveBeenCalled();

    const callArgs = ajaxMock.mock.calls[0][0];
    const body = JSON.parse(callArgs.data);

    expect(body.user).toBe('Jerry');
    expect(body.password).toBe('***REPLACE_WITH_SECURE_SERVER_SIDE_AUTH***');
  });

  test('no hard-coded secret value like "bm5nhSkxCXZkKRy4" appears in AJAX payload', () => {
    const { ajaxMock } = loadJwtRefreshIntoSandbox();

    expect(ajaxMock).toHaveBeenCalled();
    const callArgs = ajaxMock.mock.calls[0][0];
    const body = JSON.parse(callArgs.data);

    expect(body.password).not.toBe('bm5nhSkxCXZkKRy4');
  });
});
