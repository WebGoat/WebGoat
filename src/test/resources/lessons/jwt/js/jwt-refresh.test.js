/**
 * Delta tests for jwt-refresh.js focusing on:
 * - Removal of hard-coded password / secret from the login request.
 * - Safer handling of tokens in localStorage and Authorization header logic.
 */

jest.mock('jquery', () => {
  const ajaxMock = jest.fn(() => ({
    success: (cb) => {
      // For tests that need to inspect this call, we can manually call cb
      ajaxMock._lastSuccessCallback = cb;
      return { success: () => {} };
    },
  }));
  const readyMock = jest.fn((handler) => handler());

  const $ = readyMock;
  $.ajax = ajaxMock;
  $.readyMock = readyMock;
  $.ajaxMock = ajaxMock;
  return $;
});

// TODO: Adjust path if project structure differs.
const $ = require('jquery');
const path = require('path');

describe('jwt-refresh delta tests for secret removal and token handling', () => {
  let originalLocalStorage;
  let localStorageMock;

  beforeEach(() => {
    // Mock localStorage for deterministic behavior
    localStorageMock = (() => {
      let store = {};
      return {
        getItem: jest.fn((key) => store[key] || null),
        setItem: jest.fn((key, value) => {
          store[key] = value;
        }),
        clear: jest.fn(() => {
          store = {};
        }),
      };
    })();

    originalLocalStorage = global.localStorage;
    global.localStorage = localStorageMock;

    // Load the script under test after environment mock is ready
    // eslint-disable-next-line global-require
    require(path.resolve(__dirname, '../../../../main/resources/lessons/jwt/js/jwt-refresh.js'));
  });

  afterEach(() => {
    jest.resetModules();
    global.localStorage = originalLocalStorage;
  });

  test('login does not send hard-coded secret password value', () => {
    // Arrange
    // Script automatically calls login('Jerry') on document ready via jQuery mock.
    const ajaxCalls = $.ajaxMock.mock.calls;
    expect(ajaxCalls.length).toBeGreaterThan(0);

    // Find the login call (URL JWT/refresh/login)
    const loginCall = ajaxCalls.find(
      (call) => call[0] && call[0].url === 'JWT/refresh/login'
    );
    expect(loginCall).toBeDefined();

    const ajaxConfig = loginCall[0];
    const payload = JSON.parse(ajaxConfig.data);

    // Assert
    expect(payload.user).toBe('Jerry');
    // Verify that the previously hard-coded secret is not used anymore
    expect(payload.password).not.toBe('bm5nhSkxCXZkKRy4');
    // And that a non-secret placeholder is used as per the fix
    expect(payload.password).toBe('PLACEHOLDER_PASSWORD');
  });

  test('addBearerToken only adds Authorization header when access_token is present', () => {
    // Arrange
    // eslint-disable-next-line global-require
    const script = require(path.resolve(__dirname, '../../../../main/resources/lessons/jwt/js/jwt-refresh.js'));

    // The script should have defined a global webgoat.customjs.addBearerToken
    expect(global.webgoat).toBeDefined();
    expect(global.webgoat.customjs).toBeDefined();
    const { addBearerToken } = global.webgoat.customjs;

    // Act & Assert - when no access_token set
    localStorageMock.getItem.mockReturnValueOnce(null);
    let headers = addBearerToken();
    expect(headers).toEqual({}); // no Authorization header

    // Act & Assert - when access_token is present
    localStorageMock.getItem.mockReturnValueOnce('dummyToken');
    headers = addBearerToken();
    expect(headers.Authorization).toBe('Bearer dummyToken');
  });

  test('newToken does nothing when no refresh_token is present', () => {
    // Arrange
    // eslint-disable-next-line global-require
    require(path.resolve(__dirname, '../../../../main/resources/lessons/jwt/js/jwt-refresh.js'));

    // Ensure no refresh_token
    localStorageMock.getItem.mockReturnValueOnce(null); // called in newToken for refreshToken

    // Reset ajax calls for clarity
    $.ajaxMock.mockClear();

    // Act
    // newToken is defined in the global scope by the script
    expect(typeof global.newToken).toBe('function');
    global.newToken();

    // Assert - when refresh_token missing, no AJAX call should be made
    expect($.ajaxMock).not.toHaveBeenCalled();
  });

  test('newToken sends refresh_token and updates tokens from server response', () => {
    // Arrange
    // eslint-disable-next-line global-require
    require(path.resolve(__dirname, '../../../../main/resources/lessons/jwt/js/jwt-refresh.js'));

    // First getItem call returns refresh_token value
    localStorageMock.getItem
      .mockReturnValueOnce('dummyAccess')  // for Authorization header
      .mockReturnValueOnce('refresh123'); // for refreshToken in body

    $.ajaxMock.mockClear();

    // Act
    global.newToken();

    // Assert - verify AJAX called with expected payload
    expect($.ajaxMock).toHaveBeenCalledTimes(1);
    const ajaxConfig = $.ajaxMock.mock.calls[0][0];

    expect(ajaxConfig.url).toBe('JWT/refresh/newToken');
    const body = JSON.parse(ajaxConfig.data);
    expect(body.refreshToken).toBe('refresh123');

    // Simulate server success response
    const successCb = $.ajaxMock._lastSuccessCallback;
    expect(typeof successCb).toBe('function');

    successCb({
      access_token: 'newAccess',
      refresh_token: 'newRefresh',
    });

    // Tokens should be updated in localStorage
    expect(localStorageMock.setItem).toHaveBeenCalledWith('access_token', 'newAccess');
    expect(localStorageMock.setItem).toHaveBeenCalledWith('refresh_token', 'newRefresh');
  });
});
