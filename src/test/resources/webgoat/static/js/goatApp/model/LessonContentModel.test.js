// File path (derived by replacing 'main' with 'test'):
// src/test/resources/webgoat/static/js/goatApp/model/LessonContentModel.test.js

// TODO: Adjust the relative require path as needed based on your module loader / bundler setup.
const jsdom = require('jsdom');
const { JSDOM } = jsdom;

// In the real project this would be loaded via RequireJS/AMD; for delta testing we assume
// the module has been bundled or adapted to CommonJS for Jest.
// eslint-disable-next-line global-require
const LessonContentModel = require('../../../../../../main/resources/webgoat/static/js/goatApp/model/LessonContentModel.js');

describe('LessonContentModel delta tests for URL regex behavior', () => {
  let model;
  let originalURL;

  beforeAll(() => {
    // Set up a basic DOM environment for the code under test
    const dom = new JSDOM('<!doctype html><html><body></body></html>', {
      url: 'http://localhost',
    });
    global.window = dom.window;
    global.document = dom.window.document;
  });

  beforeEach(() => {
    // Preserve original URL to restore after test, if any
    originalURL = global.document.URL;

    // The HTMLContentModel parent is not the focus of this delta test.
    // We assume LessonContentModel can be constructed without complex setup.
    // TODO: If HTMLContentModel requires options or a specific Backbone setup,
    // adjust the test to create it via the real Backbone model factory.
    model = new LessonContentModel();
  });

  afterEach(() => {
    // Restore original URL if modified
    if (originalURL) {
      // JSDOM exposes location; updating href resets URL.
      global.window.location.href = originalURL;
    }
  });

  test('setContent extracts pageNum when URL ends with .lesson/<digits>', () => {
    // Arrange
    const url = 'http://localhost/WebGoat/lesson/1234';
    global.window.location.href = url;

    // Spy on trigger to ensure method completes and fires expected event
    const triggerSpy = jest.spyOn(model, 'trigger').mockImplementation(() => {});

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('pageNum')).toBe('1234');
    // Ensure lessonUrl normalization still works as intended
    expect(model.get('lessonUrl')).toBe('http://localhost/WebGoat/lesson.lesson');
    expect(triggerSpy).toHaveBeenCalledWith('content:loaded', model, true);

    triggerSpy.mockRestore();
  });

  test('setContent sets pageNum to 0 when URL does not match .lesson/<digits> pattern', () => {
    // Arrange
    const url = 'http://localhost/WebGoat/lesson';
    global.window.location.href = url;

    const triggerSpy = jest.spyOn(model, 'trigger').mockImplementation(() => {});

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('pageNum')).toBe(0);
    expect(model.get('lessonUrl')).toBe('http://localhost/WebGoat/lesson.lesson');
    expect(triggerSpy).toHaveBeenCalledWith('content:loaded', model, true);

    triggerSpy.mockRestore();
  });

  test('setContent uses loadHelps=false when explicitly provided', () => {
    // Arrange
    const url = 'http://localhost/WebGoat/lesson/7';
    global.window.location.href = url;
    const triggerSpy = jest.spyOn(model, 'trigger').mockImplementation(() => {});

    // Act
    model.setContent('<html>content</html>', false);

    // Assert
    expect(model.get('pageNum')).toBe('7');
    expect(triggerSpy).toHaveBeenCalledWith('content:loaded', model, false);

    triggerSpy.mockRestore();
  });
});
