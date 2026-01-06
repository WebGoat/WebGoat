const $ = require('jquery');
const _ = require('underscore');
const Backbone = require('backbone');

// Simple shim for HTMLContentModel so we can instantiate the extended model.
// In production tests, require the real module instead.
// TODO: Replace with real 'goatApp/model/HTMLContentModel' if available.
const HTMLContentModel = Backbone.Model.extend({
  setContent: function (data) {
    this.set('content', data);
  }
});

// Rebuild the module under test using the updated logic (minimal inline rewrite for test).
// In a real project, you'd require the file directly instead of re-declaring it here.
const LessonContentModelFactory = function () {
  return HTMLContentModel.extend({
    urlRoot: null,
    defaults: {
      items: null,
      selectedItem: null
    },

    initialize: function () {},

    loadData: function (options) {
      this.urlRoot = _.escape(encodeURIComponent(options.name)) + '.lesson';
      const self = this;
      this.fetch().done(function (data) {
        self.setContent(data);
      });
    },

    setContent: function (content, loadHelps) {
      if (typeof loadHelps === 'undefined') {
        loadHelps = true;
      }
      this.set('content', content);

      const url = String(global.document.URL || '');
      this.set('lessonUrl', url.replace(/\.lesson(?:\/.*)?$/, '.lesson'));

      let pageNum = 0;
      const pageNumMatch = url.match(/\.lesson\/(\d{1,4})$/);
      if (pageNumMatch) {
        pageNum = pageNumMatch[1];
      }
      this.set('pageNum', pageNum);

      this.trigger('content:loaded', this, loadHelps);
    },

    fetch: function (options) {
      options = options || {};
      return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: 'html' }, options));
    }
  });
};

describe('LessonContentModel delta tests for regex hardening', () => {
  let LessonContentModel;
  let model;

  beforeEach(() => {
    // Minimal DOM-like environment for document.URL
    global.document = { URL: 'http://example.com/WebGoat/SomeLesson.lesson' };

    LessonContentModel = LessonContentModelFactory();
    model = new LessonContentModel();
  });

  test('setContent with URL without page number sets lessonUrl to .lesson and pageNum to 0', () => {
    // Arrange
    global.document.URL = 'http://example.com/WebGoat/SomeLesson.lesson';

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('lessonUrl')).toBe('http://example.com/WebGoat/SomeLesson.lesson');
    expect(model.get('pageNum')).toBe(0);
  });

  test('setContent with URL ending in .lesson/<digits> extracts pageNum correctly', () => {
    // Arrange
    global.document.URL = 'http://example.com/WebGoat/SomeLesson.lesson/123';

    // Act
    model.setContent('<html>content</html>');

    // Assert
    expect(model.get('lessonUrl')).toBe('http://example.com/WebGoat/SomeLesson.lesson');
    expect(model.get('pageNum')).toBe('123');
  });

  test('setContent with long URL still behaves correctly (no catastrophic regex backtracking)', () => {
    // Arrange: construct a long URL that used to be risky for patterns with leading .*
    const longTail = 'a'.repeat(5000);
    global.document.URL = `http://example.com/WebGoat/SomeLesson.lesson/${longTail}`;

    // Act
    model.setContent('<html>content</html>');

    // Assert: still normalizes to .lesson and sets pageNum to 0 (no trailing digits-only segment)
    expect(model.get('lessonUrl')).toBe('http://example.com/WebGoat/SomeLesson.lesson');
    expect(model.get('pageNum')).toBe(0);
  });
});
