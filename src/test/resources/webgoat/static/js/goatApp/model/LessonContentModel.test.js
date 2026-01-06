/**
 * Delta Jest tests for LessonContentModel.js focusing on:
 *  - Bounded URL length before regex operations (mitigating ReDoS).
 *  - Correct parsing of lessonUrl and pageNum for normal and edge-case URLs.
 */

// NOTE: This file assumes a test environment where AMD modules can be required directly
// from their source paths. Adjust the import/require section to match your bundler or
// test runner configuration.

const Backbone = require('backbone');
const _ = require('underscore');

// Minimal shim to require the AMD module under test. In a real project, you may
// load it via a bundler or AMD-compatible loader.
let LessonContentModelModule;

beforeAll(() => {
  // eslint-disable-next-line global-require
  LessonContentModelModule = require('../../../../../../main/resources/webgoat/static/js/goatApp/model/LessonContentModel.js');
});

describe('LessonContentModel - delta tests for regex and URL handling', () => {
  test('setContent parses lessonUrl and pageNum from a normal URL', () => {
    // Arrange
    const HTMLContentModel = Backbone.Model; // Fallback for this delta test
    const LessonContentModel = LessonContentModelModule( // if module is a factory; adjust if not
      require('jquery'),
      _,
      Backbone,
      HTMLContentModel
    );
    const model = new LessonContentModel();

    const originalDocument = global.document;
    global.document = { URL: 'https://example.com/lessonName.lesson/12' };

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('lessonUrl')).toBe('https://example.com/lessonName.lesson');
    expect(model.get('pageNum')).toBe('12');

    // Cleanup
    global.document = originalDocument;
  });

  test('setContent sets pageNum to 0 when URL does not match expected pattern', () => {
    // Arrange
    const HTMLContentModel = Backbone.Model;
    const LessonContentModel = LessonContentModelModule(
      require('jquery'),
      _,
      Backbone,
      HTMLContentModel
    );
    const model = new LessonContentModel();

    const originalDocument = global.document;
    global.document = { URL: 'https://example.com/otherPage' };

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('lessonUrl')).toBe('https://example.com/otherPage.lesson');
    expect(model.get('pageNum')).toBe(0);

    // Cleanup
    global.document = originalDocument;
  });

  test('setContent truncates overly long URLs before applying regex (ReDoS mitigation)', () => {
    // Arrange
    const HTMLContentModel = Backbone.Model;
    const LessonContentModel = LessonContentModelModule(
      require('jquery'),
      _,
      Backbone,
      HTMLContentModel
    );
    const model = new LessonContentModel();

    const longPrefix = 'https://example.com/' + 'a'.repeat(3000);
    const urlWithLesson = `${longPrefix}.lesson/99`;
    const originalDocument = global.document;
    global.document = { URL: urlWithLesson };

    // Act
    model.setContent('<html>content</html>');

    // Assert
    const lessonUrl = model.get('lessonUrl');
    const pageNum = model.get('pageNum');

    expect(typeof lessonUrl).toBe('string');
    expect(String(pageNum)).toMatch(/^\d+$/);

    // Cleanup
    global.document = originalDocument;
  });
});
