/**
 * Delta Jest tests for jwt-refresh.js focusing on:
 *  - Removal of hard-coded password in the AJAX payload.
 *  - Password now sourced via getJwtLoginPassword().
 *  - login() still issues an AJAX call with the expected payload.
 */

jest.mock('jquery', () => {
  const actualJQuery = jest.requireActual('jquery');
  actualJQuery.ajax = jest.fn().mockReturnValue({
    success: (cb) => cb({ access_token: 'a', refresh_token: 'r' })
  });
  return actualJQuery;
});

const $ = require('jquery');

describe('jwt-refresh.js - delta security tests', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    global.localStorage = (function () {
      let store = {};
      return {
        getItem: (key) => store[key] || null,
        setItem: (key, value) => { store[key] = String(value); },
        removeItem: (key) => { delete store[key]; },
        clear: () => { store = {}; }
      };
    })();

    global.window = global.window || {};
    delete global.window.webgoatConfig;
  });

  test('getJwtLoginPassword returns configured value when webgoatConfig is present', () => {
    global.window.webgoatConfig = { jwtLoginPassword: 'CONFIG_SECRET' };

    jest.isolateModules(() => {
      // eslint-disable-next-line global-require
      require('../../../../../../main/resources/lessons/jwt/js/jwt-refresh.js');

      expect(typeof global.getJwtLoginPassword).toBe('function');

      const pwd = global.getJwtLoginPassword();

      expect(pwd).toBe('CONFIG_SECRET');
    });
  });

  test('getJwtLoginPassword falls back to non-sensitive placeholder when no config is provided', () => {
    jest.isolateModules(() => {
      // eslint-disable-next-line global-require
      require('../../../../../../main/resources/lessons/jwt/js/jwt-refresh.js');

      const pwd = global.getJwtLoginPassword();

      expect(pwd).not.toBe('bm5nhSkxCXZkKRy4');
      expect(pwd).toBe('CHANGE_ME_JWT_PASSWORD');
    });
  });

  test('login uses getJwtLoginPassword() instead of hard-coded literal in AJAX payload', () => {
    jest.isolateModules(() => {
      // eslint-disable-next-line global-require
      require('../../../../../../main/resources/lessons/jwt/js/jwt-refresh.js');

      const passwordSpy = jest.spyOn(global, 'getJwtLoginPassword').mockReturnValue('RUNTIME_PWD');

      global.login('Jerry');

      expect(passwordSpy).toHaveBeenCalled();

      expect($.ajax).toHaveBeenCalledTimes(1);
      const ajaxCallArg = $.ajax.mock.calls[0][0];

      expect(ajaxCallArg.type).toBe('POST');
      expect(ajaxCallArg.url).toBe('JWT/refresh/login');
      expect(ajaxCallArg.contentType).toBe('application/json');

      const body = JSON.parse(ajaxCallArg.data);
      expect(body.user).toBe('Jerry');
      expect(body.password).toBe('RUNTIME_PWD');
      expect(body.password).not.toBe('bm5nhSkxCXZkKRy4');
    });
  });
});
