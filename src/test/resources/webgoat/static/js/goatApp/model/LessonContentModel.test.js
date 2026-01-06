const vm = require('vm');

function loadModuleWithLocation(pathname) {
  const sandbox = {
    window: { location: { href: `https://example.com${pathname}`, pathname } },
    defineDeps: null,
    defineFactory: null,
    define: function (deps, factory) {
      this.defineDeps = deps;
      this.defineFactory = factory;
    },
    moduleExports: null
  };

  const fs = require('fs');
  const code = fs.readFileSync(
    require('path').resolve(__dirname, '../../../../../main/resources/webgoat/static/js/goatApp/model/LessonContentModel.js'),
    'utf8'
  );

  vm.runInNewContext(code, sandbox);

  const HTMLContentModel = function () {};
  HTMLContentModel.extend = function (def) {
    function Model() {
      this.attributes = {};
      if (typeof def.initialize === 'function') {
        def.initialize.apply(this, arguments);
      }
    }
    Model.prototype = {
      set: function (key, value) {
        this.attributes[key] = value;
      },
      get: function (key) {
        return this.attributes[key];
      },
      trigger: function () {
      }
    };
    Object.assign(Model.prototype, def);
    return Model;
  };

  const factory = sandbox.defineFactory;
  const module = factory(
    {},
    {},
    { Model: function () {}, prototype: { fetch: function () {} } },
    HTMLContentModel
  );

  return { Module: module, window: sandbox.window };
}

describe('LessonContentModel URL helper behavior (delta tests)', () => {
  test("'/context/lesson1.lesson' yields lessonUrl '/context/lesson1.lesson' and pageNum 0", () => {
    const { Module } = loadModuleWithLocation('/context/lesson1.lesson');

    const model = new Module();
    model.setContent('<html></html>');

    expect(model.get('lessonUrl')).toBe('/context/lesson1.lesson');
    expect(model.get('pageNum')).toBe(0);
  });

  test("'/context/lesson1.lesson/12' yields lessonUrl '/context/lesson1.lesson' and pageNum '12'", () => {
    const { Module } = loadModuleWithLocation('/context/lesson1.lesson/12');

    const model = new Module();
    model.setContent('<html></html>');

    expect(model.get('lessonUrl')).toBe('/context/lesson1.lesson');
    expect(model.get('pageNum')).toBe('12');
  });
});
