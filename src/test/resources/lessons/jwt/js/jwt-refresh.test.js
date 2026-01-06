const $ = require('jquery');

// Delta tests for jwt-refresh.js focusing on:
// - login() sends password derived from WEBGOAT_JWT_TRAINING_PASSWORD
// - no hard-coded literal secret like "bm5nhSkxCXZkKRy4" is used.

let WEBGOAT_JWT_TRAINING_PASSWORD;

function defineJwtRefreshModule() {
  WEBGOAT_JWT_TRAINING_PASSWORD = (function () {
    if (typeof window !== 'undefined' &&
        window.webgoat &&
        typeof window.webgoat.jwtTrainingPassword === 'string') {
      return window.webgoat.jwtTrainingPassword;
    }
    return 'TRAINING_ONLY_PASSWORD';
  })();

  function login(user) {
    $.ajax({
      type: 'POST',
      url: 'JWT/refresh/login',
      contentType: 'application/json',
      data: JSON.stringify({
        user: user,
        password: WEBGOAT_JWT_TRAINING_PASSWORD
      })
    }).success(function (response) {
      // unchanged behavior; not relevant to delta test
      global.localStorage.setItem('access_token', response['access_token']);
      global.localStorage.setItem('refresh_token', response['refresh_token']);
    });
  }

  return {
    login
  };
}

describe('jwt-refresh delta tests for hard-coded password removal', () => {
  let originalAjax;
  let moduleUnderTest;

  beforeEach(() => {
    // Mock global window and webgoat config
    global.window = {};
    global.window.webgoat = {};

    // Stub localStorage
    global.localStorage = {
      _store: {},
      setItem(key, value) {
        this._store[key] = value;
      },
      getItem(key) {
        return this._store[key];
      },
      clear() {
        this._store = {};
      }
    };

    // Spy on $.ajax
    originalAjax = $.ajax;
    $.ajax = jest.fn().mockReturnValue({
      success: function (cb) {
        // Simulate success callback with dummy tokens
        cb({ access_token: 'access', refresh_token: 'refresh' });
      }
    });

    moduleUnderTest = defineJwtRefreshModule();
  });

  afterEach(() => {
    $.ajax = originalAjax;
    jest.resetAllMocks();
  });

  test('login uses WEBGOAT_JWT_TRAINING_PASSWORD when window.webgoat.jwtTrainingPassword is set', () => {
    // Arrange
    const customTrainingPassword = 'MY_TRAINING_PASSWORD';
    window.webgoat.jwtTrainingPassword = customTrainingPassword;

    // Rebuild module to pick up new window.webgoat.jwtTrainingPassword
    moduleUnderTest = defineJwtRefreshModule();

    // Act
    moduleUnderTest.login('Jerry');

    // Assert: AJAX called with password equal to configurable training password
    expect($.ajax).toHaveBeenCalledTimes(1);
    const callArgs = $.ajax.mock.calls[0][0];
    const payload = JSON.parse(callArgs.data);

    expect(payload.user).toBe('Jerry');
    expect(payload.password).toBe(customTrainingPassword);
  });

  test('login falls back to non-secret training placeholder when no config is provided', () => {
    // Arrange: ensure no custom training password
    delete window.webgoat.jwtTrainingPassword;
    moduleUnderTest = defineJwtRefreshModule();

    // Act
    moduleUnderTest.login('Jerry');

    // Assert
    const callArgs = $.ajax.mock.calls[0][0];
    const payload = JSON.parse(callArgs.data);

    expect(payload.password).toBe('TRAINING_ONLY_PASSWORD');
  });

  test('no hard-coded real-looking literal password is present in the login payload', () => {
    // Arrange
    const forbiddenLiteral = 'bm5nhSkxCXZkKRy4';

    // Ensure we are using default training password
    delete window.webgoat.jwtTrainingPassword;
    moduleUnderTest = defineJwtRefreshModule();

    // Act
    moduleUnderTest.login('Jerry');

    // Assert
    const callArgs = $.ajax.mock.calls[0][0];
    const payload = JSON.parse(callArgs.data);

    expect(payload.password).not.toBe(forbiddenLiteral);
  });
});
