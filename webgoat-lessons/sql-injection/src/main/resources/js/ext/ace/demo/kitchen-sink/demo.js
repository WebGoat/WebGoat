define("ace/ext/rtl",["require","exports","module","ace/lib/dom","ace/lib/lang","ace/editor","ace/config"], function(require, exports, module) {
"use strict";
var dom = require("ace/lib/dom");
var lang = require("ace/lib/lang");

var commands = [{
    name: "leftToRight",
    bindKey: { win: "Ctrl-Alt-Shift-L", mac: "Command-Alt-Shift-L" },
    exec: function(editor) {
        editor.session.$bidiHandler.setRtlDirection(editor, false);
    },
    readOnly: true
}, {
    name: "rightToLeft",
    bindKey: { win: "Ctrl-Alt-Shift-R",  mac: "Command-Alt-Shift-R" },
    exec: function(editor) {
        editor.session.$bidiHandler.setRtlDirection(editor, true);
    },
    readOnly: true
}];

var Editor = require("../editor").Editor;
require("../config").defineOptions(Editor.prototype, "editor", {
    rtlText: {
        set: function(val) {
            if (val) {
                this.on("session", onChange);
                this.on("changeSelection", onChangeSelection);
                this.renderer.on("afterRender", updateLineDirection);
                this.commands.on("exec", onCommandEmitted);
                this.commands.addCommands(commands);
            } else {
                this.off("session", onChange);
                this.off("changeSelection", onChangeSelection);
                this.renderer.off("afterRender", updateLineDirection);
                this.commands.off("exec", onCommandEmitted);
                this.commands.removeCommands(commands);
                clearTextLayer(this.renderer);
            }
            this.renderer.updateFull();
        }
    }
});
function onChangeSelection(e, editor) {
    var lead = editor.getSelection().lead;
    if (editor.session.$bidiHandler.isRtlLine(lead.row)) {
        if (lead.column === 0) {
            if (editor.session.$bidiHandler.isMoveLeftOperation && lead.row > 0) {
                editor.getSelection().moveCursorTo(lead.row - 1, editor.session.getLine(lead.row - 1).length);
            } else {
                if (editor.getSelection().isEmpty())
                    lead.column += 1;
                else
                    lead.setPosition(lead.row, lead.column + 1);
            }
        }
    }
}

function onCommandEmitted(commadEvent) {
    commadEvent.editor.session.$bidiHandler.isMoveLeftOperation = /gotoleft|selectleft|backspace|removewordleft/.test(commadEvent.command.name);
}
function onChange(delta, session) {
    session.$bidiHandler.currentRow = null;
    if (session.$bidiHandler.isRtlLine(delta.start.row) && delta.action === 'insert' && delta.lines.length > 1) {
        for (var row = delta.start.row; row < delta.end.row; row++) {
            if (session.getLine(row + 1).charAt(0) !== session.$bidiHandler.RLE)
                session.getDocument().$lines[row + 1] = session.$bidiHandler.RLE + session.getLine(row + 1);
        }
    }
}

function updateLineDirection(e, renderer) {
    var session = renderer.session;
    var $bidiHandler = session.$bidiHandler;
    var cells = renderer.$textLayer.$lines.cells;
    var width = renderer.layerConfig.width - renderer.layerConfig.padding + "px";
    cells.forEach(function(cell) {
        var style = cell.element.style;
        if ($bidiHandler && $bidiHandler.isRtlLine(cell.row)) {
            style.direction = "rtl";
            style.textAlign = "right";
            style.width = width;
        } else {
            style.direction = "";
            style.textAlign = "";
            style.width = "";
        }
    });
}

function clearTextLayer(renderer) {
    var lines = renderer.$textLayer.$lines;
    lines.cells.forEach(clear);
    lines.cellCache.forEach(clear);
    function clear(cell) {
        var style = cell.element.style;
        style.direction = style.textAlign = style.width = "";
    }
}

});

define("kitchen-sink/inline_editor",["require","exports","module","ace/line_widgets","ace/editor","ace/virtual_renderer","ace/lib/dom","ace/commands/default_commands"], function(require, exports, module) {
"use strict";

var LineWidgets = require("ace/line_widgets").LineWidgets;
var Editor = require("ace/editor").Editor;
var Renderer = require("ace/virtual_renderer").VirtualRenderer;
var dom = require("ace/lib/dom");


require("ace/commands/default_commands").commands.push({
    name: "openInlineEditor",
    bindKey: "F3",
    exec: function(editor) {
        var split = window.env.split;
        var s = editor.session;
        var inlineEditor = new Editor(new Renderer());
        var splitSession = split.$cloneSession(s);

        var row = editor.getCursorPosition().row;
        if (editor.session.lineWidgets && editor.session.lineWidgets[row]) {
            editor.session.lineWidgets[row].destroy();
            return;
        }
        
        var rowCount = 10;
        var w = {
            row: row, 
            fixedWidth: true,
            el: dom.createElement("div"),
            editor: inlineEditor
        };
        var el = w.el;
        el.appendChild(inlineEditor.container);

        if (!editor.session.widgetManager) {
            editor.session.widgetManager = new LineWidgets(editor.session);
            editor.session.widgetManager.attach(editor);
        }
        
        var h = rowCount*editor.renderer.layerConfig.lineHeight;
        inlineEditor.container.style.height = h + "px";

        el.style.position = "absolute";
        el.style.zIndex = "4";
        el.style.borderTop = "solid blue 2px";
        el.style.borderBottom = "solid blue 2px";
        
        inlineEditor.setSession(splitSession);
        editor.session.widgetManager.addLineWidget(w);
        
        var kb = {
            handleKeyboard:function(_,hashId, keyString) {
                if (hashId === 0 && keyString === "esc") {
                    w.destroy();
                    return true;
                }
            }
        };
        
        w.destroy = function() {
            editor.keyBinding.removeKeyboardHandler(kb);
            s.widgetManager.removeLineWidget(w);
        };
        
        editor.keyBinding.addKeyboardHandler(kb);
        inlineEditor.keyBinding.addKeyboardHandler(kb);
        inlineEditor.setTheme("ace/theme/solarized_light");
    }
});
});

define("ace/test/asyncjs/assert",["require","exports","module","ace/lib/oop"], function(require, exports, module) {
//
//
//
//
//
var oop = require("ace/lib/oop");
var pSlice = Array.prototype.slice;
var assert = exports;
assert.AssertionError = function AssertionError(options) {
  this.name = 'AssertionError';
  this.message = options.message;
  this.actual = options.actual;
  this.expected = options.expected;
  this.operator = options.operator;
  var stackStartFunction = options.stackStartFunction || fail;

  if (Error.captureStackTrace) {
    Error.captureStackTrace(this, stackStartFunction);
  }
};
oop.inherits(assert.AssertionError, Error);

toJSON = function(obj) {
    if (typeof JSON !== "undefined")
        return JSON.stringify(obj);
    else
        return obj.toString();
}

assert.AssertionError.prototype.toString = function() {
  if (this.message) {
    return [this.name + ':', this.message].join(' ');
  } else {
    return [this.name + ':',
            toJSON(this.expected),
            this.operator,
            toJSON(this.actual)].join(' ');
  }
};
assert.AssertionError.__proto__ = Error.prototype;
function fail(actual, expected, message, operator, stackStartFunction) {
  throw new assert.AssertionError({
    message: message,
    actual: actual,
    expected: expected,
    operator: operator,
    stackStartFunction: stackStartFunction
  });
}
assert.fail = fail;
assert.ok = function ok(value, message) {
  if (!!!value) fail(value, true, message, '==', assert.ok);
};
assert.equal = function equal(actual, expected, message) {
  if (actual != expected) fail(actual, expected, message, '==', assert.equal);
};
assert.notEqual = function notEqual(actual, expected, message) {
  if (actual == expected) {
    fail(actual, expected, message, '!=', assert.notEqual);
  }
};
assert.deepEqual = function deepEqual(actual, expected, message) {
  if (!_deepEqual(actual, expected)) {
    fail(actual, expected, message, 'deepEqual', assert.deepEqual);
  }
};

function _deepEqual(actual, expected) {
  if (actual === expected) {
    return true;

  } else if (typeof Buffer !== "undefined" && Buffer.isBuffer(actual) && Buffer.isBuffer(expected)) {
    if (actual.length != expected.length) return false;

    for (var i = 0; i < actual.length; i++) {
      if (actual[i] !== expected[i]) return false;
    }

    return true;
  } else if (actual instanceof Date && expected instanceof Date) {
    return actual.getTime() === expected.getTime();
  } else if (typeof actual != 'object' && typeof expected != 'object') {
    return actual == expected;
  } else {
    return objEquiv(actual, expected);
  }
}

function isUndefinedOrNull(value) {
  return value === null || value === undefined;
}

function isArguments(object) {
  return Object.prototype.toString.call(object) == '[object Arguments]';
}

function objEquiv(a, b) {
  if (isUndefinedOrNull(a) || isUndefinedOrNull(b))
    return false;
  if (a.prototype !== b.prototype) return false;
  if (isArguments(a)) {
    if (!isArguments(b)) {
      return false;
    }
    a = pSlice.call(a);
    b = pSlice.call(b);
    return _deepEqual(a, b);
  }
  try {
    var ka = Object.keys(a),
        kb = Object.keys(b),
        key, i;
  } catch (e) {//happens when one is a string literal and the other isn't
    return false;
  }
  if (ka.length != kb.length)
    return false;
  ka.sort();
  kb.sort();
  for (i = ka.length - 1; i >= 0; i--) {
    if (ka[i] != kb[i])
      return false;
  }
  for (i = ka.length - 1; i >= 0; i--) {
    key = ka[i];
    if (!_deepEqual(a[key], b[key])) return false;
  }
  return true;
}
assert.notDeepEqual = function notDeepEqual(actual, expected, message) {
  if (_deepEqual(actual, expected)) {
    fail(actual, expected, message, 'notDeepEqual', assert.notDeepEqual);
  }
};
assert.strictEqual = function strictEqual(actual, expected, message) {
  if (actual !== expected) {
    fail(actual, expected, message, '===', assert.strictEqual);
  }
};
assert.notStrictEqual = function notStrictEqual(actual, expected, message) {
  if (actual === expected) {
    fail(actual, expected, message, '!==', assert.notStrictEqual);
  }
};

function expectedException(actual, expected) {
  if (!actual || !expected) {
    return false;
  }

  if (expected instanceof RegExp) {
    return expected.test(actual);
  } else if (actual instanceof expected) {
    return true;
  } else if (expected.call({}, actual) === true) {
    return true;
  }

  return false;
}

function _throws(shouldThrow, block, expected, message) {
  var actual;

  if (typeof expected === 'string') {
    message = expected;
    expected = null;
  }

  try {
    block();
  } catch (e) {
    actual = e;
  }

  message = (expected && expected.name ? ' (' + expected.name + ').' : '.') +
            (message ? ' ' + message : '.');

  if (shouldThrow && !actual) {
    fail('Missing expected exception' + message);
  }

  if (!shouldThrow && expectedException(actual, expected)) {
    fail('Got unwanted exception' + message);
  }

  if ((shouldThrow && actual && expected &&
      !expectedException(actual, expected)) || (!shouldThrow && actual)) {
    throw actual;
  }
}
assert.throws = function(block, /*optional*/error, /*optional*/message) {
  _throws.apply(this, [true].concat(pSlice.call(arguments)));
};
assert.doesNotThrow = function(block, /*optional*/error, /*optional*/message) {
  _throws.apply(this, [false].concat(pSlice.call(arguments)));
};

assert.ifError = function(err) { if (err) {throw err;}};

});

define("ace/test/asyncjs/async",["require","exports","module"], function(require, exports, module) {

var STOP = exports.STOP = {}

exports.Generator = function(source) {
    if (typeof source == "function")
        this.source = {
            next: source
        }
    else
        this.source = source
}

;(function() {
    this.next = function(callback) {
        this.source.next(callback)
    }

    this.map = function(mapper) {
        if (!mapper)
            return this
            
        mapper = makeAsync(1, mapper)
        
        var source = this.source
        this.next = function(callback) {
            source.next(function(err, value) {
                if (err)
                    callback(err)
                else {
                    mapper(value, function(err, value) {
                        if (err)
                            callback(err)
                        else
                            callback(null, value)
                    })
                }
            })
        }
        return new this.constructor(this)
    }
    
    this.filter = function(filter) {
        if (!filter)
            return this
            
        filter = makeAsync(1, filter)
        
        var source = this.source
        this.next = function(callback) {
            source.next(function handler(err, value) {
                if (err)
                    callback(err)
                else {
                    filter(value, function(err, takeIt) {
                        if (err)
                            callback(err)
                        else if (takeIt)
                            callback(null, value)
                        else
                            source.next(handler)
                    })
                }
            })
        }
        return new this.constructor(this)
    }

    this.slice = function(begin, end) {
        var count = -1
        if (!end || end < 0)
            var end = Infinity
        
        var source = this.source
        this.next = function(callback) {
            source.next(function handler(err, value) {
                count++
                if (err)
                    callback(err)
                else if (count >= begin && count < end)
                    callback(null, value)
                else if (count >= end)
                    callback(STOP)
                else
                    source.next(handler)
            })
        }
        return new this.constructor(this)
    }
    
    this.reduce = function(reduce, initialValue) {
        reduce = makeAsync(3, reduce)

        var index = 0
        var done = false
        var previousValue = initialValue
        
        var source = this.source
        this.next = function(callback) {
            if (done)
                return callback(STOP)

            if (initialValue === undefined) {
                source.next(function(err, currentValue) {
                    if (err)
                        return callback(err, previousValue)
                    
                    previousValue = currentValue
                    reduceAll()
                })
            }
            else
                reduceAll()

            function reduceAll() {
                source.next(function handler(err, currentValue) {                    
                    if (err) {
                        done = true
                        if (err == STOP)                            
                            return callback(null, previousValue)
                        else
                            return(err)
                    }
                    reduce(previousValue, currentValue, index++, function(err, value) {
                        previousValue = value
                        source.next(handler)
                    })
                })
            }            
        }
        return new this.constructor(this)
    }
    
    this.forEach =
    this.each = function(fn) {
        fn = makeAsync(1, fn)
            
        var source = this.source
        this.next = function(callback) {
            source.next(function handler(err, value) {
                if (err) 
                    callback(err)
                else {
                    fn(value, function(err) {
                        callback(err, value)
                    })
                }
            })
        }
        return new this.constructor(this)
    }
    
    this.some = function(condition) {
        condition = makeAsync(1, condition)
        
        var source = this.source
        var done = false
        this.next = function(callback) {
            if (done)
                return callback(STOP)
            
            source.next(function handler(err, value) {
                if (err)
                    return callback(err)
                    
                condition(value, function(err, result) {
                    if (err) {
                        done = true
                        if (err == STOP)
                            callback(null, false)
                        else
                            callback(err)
                    }                        
                    else if (result) {
                        done = true
                        callback(null, true)
                    }
                    else 
                        source.next(handler)
                })
            })
        }
        return new this.constructor(this)
    }
    
    this.every = function(condition) {
        condition = makeAsync(1, condition)
        
        var source = this.source
        var done = false
        this.next = function(callback) {
            if (done)
                return callback(STOP)
            
            source.next(function handler(err, value) {
                if (err)
                    return callback(err)
                    
                condition(value, function(err, result) {
                    if (err) {
                        done = true
                        if (err == STOP)
                            callback(null, true)
                        else
                            callback(err)
                    }                        
                    else if (!result) {
                        done = true
                        callback(null, false)
                    }
                    else 
                        source.next(handler)
                })
            })
        }
        return new this.constructor(this)
    }
    
    this.call = function(context) {
        var source = this.source
        return this.map(function(fn, next) {
            fn = makeAsync(0, fn, context)
            fn.call(context, function(err, value) {
                next(err, value)
            })
        })
    }
    
    this.concat = function(generator) {
        var generators = [this]
        generators.push.apply(generators, arguments)
        var index = 0
        var source = generators[index++]
        
        return new this.constructor(function(callback) {            
            source.next(function handler(err, value) {
                if (err) {
                    if (err == STOP) {
                        source = generators[index++]
                        if (!source)
                            return callback(STOP)
                        else
                            return source.next(handler)
                    }
                    else
                        return callback(err)
                }
                else
                    return callback(null, value)
            })
        })
    }
    
    this.zip = function(generator) {
        var generators = [this]
        generators.push.apply(generators, arguments)
        
        return new this.constructor(function(callback) {
            exports.list(generators)
                .map(function(gen, next) {                    
                    gen.next(next)
                })
                .toArray(callback)
        })
    }
    
    this.expand = function(inserter, constructor) {
       if (!inserter)
            return this
            
        var inserter = makeAsync(1, inserter)
        var constructor = constructor || this.constructor
        var source = this.source;
        var spliced = null;
        
        return new constructor(function next(callback) {
            if (!spliced) {
                source.next(function(err, value) {
                    if (err)
                        return callback(err)
                        
                    inserter(value, function(err, toInsert) {
                        if (err)
                            return callback(err)
                            
                        spliced = toInsert                        
                        next(callback)
                    })

                })
            } 
            else {
                spliced.next(function(err, value) {
                    if (err == STOP) {
                        spliced = null
                        return next(callback)
                    }
                    else if (err)
                        return callback(err)
                    
                    callback(err, value)
                })
            }
        })
    }

    this.sort = function(compare) {
        var self = this
        var arrGen
        this.next = function(callback) {
            if (arrGen)
                return arrGen.next(callback)

            self.toArray(function(err, arr) {
                if (err)
                    callback(err)
                else {
                    arrGen = exports.list(arr.sort(compare))
                    arrGen.next(callback)
                }
            })            
        }
        return new this.constructor(this)
    }

    this.join = function(separator) {
        return this.$arrayOp(Array.prototype.join, separator !== undefined ? [separator] : null)
    }
    
    this.reverse = function() {
        return this.$arrayOp(Array.prototype.reverse)
    }
    
    this.$arrayOp = function(arrayMethod, args) {
        var self = this
        var i = 0
        this.next = function(callback) {
            if (i++ > 0)
                return callback(STOP)
                
            self.toArray(function(err, arr) {
                if (err)
                    callback(err, "")
                else {
                    if (args)
                        callback(null, arrayMethod.apply(arr, args))
                    else
                        callback(null, arrayMethod.call(arr))
                }
            })
        }
        return new this.constructor(this)
        
    }
    
    this.end = function(breakOnError, callback) {
        if (!callback) {
            callback = arguments[0]
            breakOnError = true
        }

        var source = this.source
        var last
        var lastError
        source.next(function handler(err, value) {
            if (err) {
                if (err == STOP)
                    callback && callback(lastError, last)
                else if (!breakOnError) {
                    lastError = err
                    source.next(handler)
                }
                else
                    callback && callback(err, value)
            }
            else  {
                last = value
                source.next(handler)
            }
        })
    }

    this.toArray = function(breakOnError, callback) {
        if (!callback) {
            callback = arguments[0]
            breakOnError = true
        }
        
        var values = []
        var errors = []
        var source = this.source
        
        source.next(function handler(err, value) {
            if (err) {
                if (err == STOP) {
                    if (breakOnError)
                        return callback(null, values)
                    else {
                        errors.length = values.length
                        return callback(errors, values)
                    }
                }
                else {
                    if (breakOnError)
                        return callback(err)
                    else
                        errors[values.length] = err
                }
            }

            values.push(value)
            source.next(handler)
        })
    }

}).call(exports.Generator.prototype)

var makeAsync = exports.makeAsync = function(args, fn, context) {
    if (fn.length > args) 
        return fn
    else {
        return function() {
            var value
            var next = arguments[args]
            try {
                value = fn.apply(context || this, arguments)
            } catch(e) {
                return next(e)
            }
            next(null, value)
        }
    }
}

exports.list = function(arr, construct) {
    var construct = construct || exports.Generator
    var i = 0
    var len = arr.length
    
    return new construct(function(callback) {
        if (i < len)
            callback(null, arr[i++])
        else
            callback(STOP)
    })
}

exports.values = function(map, construct) {
    var values = []
    for (var key in map) 
        values.push(map[key])
        
    return exports.list(values, construct)
}

exports.keys = function(map, construct) {
    var keys = []
    for (var key in map) 
        keys.push(key)
        
    return exports.list(keys, construct)
}
exports.range = function(start, stop, step, construct) {
    var construct = construct || exports.Generator
    start = start || 0
    step = step || 1
    
    if (stop === undefined || stop === null)
        stop = step > 0 ? Infinity : -Infinity
        
    var value = start
    
    return new construct(function(callback) {
        if (step > 0 && value >= stop || step < 0 && value <= stop)
            callback(STOP)
        else {
            var current = value
            value += step
            callback(null, current)
        }
    })
}

exports.concat = function(first, varargs) {
    if (arguments.length > 1)
        return first.concat.apply(first, Array.prototype.slice.call(arguments, 1))
    else
        return first
}

exports.zip = function(first, varargs) {
    if (arguments.length > 1)
        return first.zip.apply(first, Array.prototype.slice.call(arguments, 1))
    else
        return first.map(function(item, next) {
            next(null, [item])
        })
}


exports.plugin = function(members, constructors) {
    if (members) {
        for (var key in members) {
            exports.Generator.prototype[key] = members[key]
        }
    }

    if (constructors) {
        for (var key in constructors) {
            exports[key] = constructors[key]
        }
    }    
}

})

define("ace/test/mockrenderer",["require","exports","module"], function(require, exports, module) {
"use strict";

var MockRenderer = exports.MockRenderer = function(visibleRowCount) {
    if (typeof document == "object") {
        this.container = document.createElement("div");
        this.scroller = document.createElement("div");
    }
    this.visibleRowCount = visibleRowCount || 20;

    this.layerConfig = {
        firstVisibleRow : 0,
        lastVisibleRow : this.visibleRowCount
    };

    this.isMockRenderer = true;

    this.$gutter = {};
};


MockRenderer.prototype.getFirstVisibleRow = function() {
    return this.layerConfig.firstVisibleRow;
};

MockRenderer.prototype.getLastVisibleRow = function() {
    return this.layerConfig.lastVisibleRow;
};

MockRenderer.prototype.getFirstFullyVisibleRow = function() {
    return this.layerConfig.firstVisibleRow;
};

MockRenderer.prototype.getLastFullyVisibleRow = function() {
    return this.layerConfig.lastVisibleRow;
};

MockRenderer.prototype.getContainerElement = function() {
    return this.container;
};

MockRenderer.prototype.getMouseEventTarget = function() {
    return this.container;
};

MockRenderer.prototype.getTextAreaContainer = function() {
    return this.container;
};

MockRenderer.prototype.addGutterDecoration = function() {
};

MockRenderer.prototype.removeGutterDecoration = function() {
};

MockRenderer.prototype.moveTextAreaToCursor = function() {
};

MockRenderer.prototype.setSession = function(session) {
    this.session = session;
};

MockRenderer.prototype.getSession = function(session) {
    return this.session;
};

MockRenderer.prototype.setTokenizer = function() {
};

MockRenderer.prototype.on = function() {
};

MockRenderer.prototype.updateCursor = function() {
};

MockRenderer.prototype.animateScrolling = function(fromValue, callback) {
    callback && callback();
};

MockRenderer.prototype.scrollToX = function(scrollTop) {};
MockRenderer.prototype.scrollToY = function(scrollLeft) {};

MockRenderer.prototype.scrollToLine = function(line, center) {
    var lineHeight = 16;
    var row = 0;
    for (var l = 1; l < line; l++) {
        row += this.session.getRowLength(l-1);
    }

    if (center) {
        row -= this.visibleRowCount / 2;
    }
    this.scrollToRow(row);
};

MockRenderer.prototype.scrollSelectionIntoView = function() {
};

MockRenderer.prototype.scrollCursorIntoView = function() {
    var cursor = this.session.getSelection().getCursor();
    if (cursor.row < this.layerConfig.firstVisibleRow) {
        this.scrollToRow(cursor.row);
    }
    else if (cursor.row > this.layerConfig.lastVisibleRow) {
        this.scrollToRow(cursor.row);
    }
};

MockRenderer.prototype.scrollToRow = function(row) {
    var row = Math.min(this.session.getLength() - this.visibleRowCount, Math.max(0,
                                                                          row));
    this.layerConfig.firstVisibleRow = row;
    this.layerConfig.lastVisibleRow = row + this.visibleRowCount;
};

MockRenderer.prototype.getScrollTopRow = function() {
  return this.layerConfig.firstVisibleRow;
};

MockRenderer.prototype.draw = function() {
};

MockRenderer.prototype.onChangeTabSize = function(startRow, endRow) {
};

MockRenderer.prototype.updateLines = function(startRow, endRow) {
};

MockRenderer.prototype.updateBackMarkers = function() {
};

MockRenderer.prototype.updateFrontMarkers = function() {
};

MockRenderer.prototype.updateBreakpoints = function() {
};

MockRenderer.prototype.onResize = function() {
};

MockRenderer.prototype.updateFull = function() {
};

MockRenderer.prototype.updateText = function() {
};

MockRenderer.prototype.showCursor = function() {
};

MockRenderer.prototype.visualizeFocus = function() {
};

MockRenderer.prototype.setAnnotations = function() {
};

MockRenderer.prototype.setStyle = function() {
};

MockRenderer.prototype.unsetStyle = function() {
};

MockRenderer.prototype.textToScreenCoordinates = function() {
    return {
        pageX: 0,
        pageY: 0
    };
};

MockRenderer.prototype.screenToTextCoordinates = function() {
    return {
        row: 0,
        column: 0
    };
};

MockRenderer.prototype.adjustWrapLimit = function () {

};

});

define("kitchen-sink/dev_util",["require","exports","module","ace/lib/dom","ace/lib/event","ace/range","ace/edit_session","ace/undomanager","ace/lib/oop","ace/lib/dom","ace/range","ace/editor","ace/test/asyncjs/assert","ace/test/asyncjs/async","ace/undomanager","ace/edit_session","ace/test/mockrenderer","ace/lib/event_emitter"], function(require, exports, module) {
var dom = require("ace/lib/dom");
var event = require("ace/lib/event");
var Range = require("ace/range").Range;
var EditSession = require("ace/edit_session").EditSession;
var UndoManager = require("ace/undomanager").UndoManager;
function warn() {
    var s = (new Error()).stack || "";
    s = s.split("\n");
    if (s[1] == "Error") s.shift(); // remove error description on chrome
    s.shift(); // remove warn
    s.shift(); // remove the getter
    s = s.join("\n");
    if (!/at Object.InjectedScript.|@debugger eval|snippets:\/{3}|<anonymous>:\d+:\d+/.test(s)) {
        console.error("trying to access to global variable");
    }
}
function def(o, key, get) {
    try {
        Object.defineProperty(o, key, {
            configurable: true, 
            get: get,
            set: function(val) {
                delete o[key];
                o[key] = val;
            }
        });
    } catch(e) {
        console.error(e);
    }
}
def(window, "ace", function(){ warn(); return window.env.editor });
def(window, "editor", function(){ warn(); return window.env.editor == logEditor ? editor : window.env.editor });
def(window, "session", function(){ return window.editor.session });
def(window, "split", function(){ warn(); return window.env.split });


def(window, "devUtil", function(){ warn(); return exports });
exports.showTextArea = function(argument) {
    dom.importCssString("\
      .ace_text-input {\
        position: absolute;\
        z-index: 10!important;\
        width: 6em!important;\
        height: 1em;\
        opacity: 1!important;\
        background: rgba(0, 92, 255, 0.11);\
        border: none;\
        font: inherit;\
        padding: 0 1px;\
        margin: 0 -1px;\
        text-indent: 0em;\
    }\
    ");
};

exports.addGlobals = function() {
    window.oop = require("ace/lib/oop");
    window.dom = require("ace/lib/dom");
    window.Range = require("ace/range").Range;
    window.Editor = require("ace/editor").Editor;
    window.assert = require("ace/test/asyncjs/assert");
    window.asyncjs = require("ace/test/asyncjs/async");
    window.UndoManager = require("ace/undomanager").UndoManager;
    window.EditSession = require("ace/edit_session").EditSession;
    window.MockRenderer = require("ace/test/mockrenderer").MockRenderer;
    window.EventEmitter = require("ace/lib/event_emitter").EventEmitter;
    
    window.getSelection = getSelection;
    window.setSelection = setSelection;
    window.testSelection = testSelection;
    window.setValue = setValue;
    window.testValue = testValue;
};

function getSelection(editor) {
    var data = editor.multiSelect.toJSON();
    if (!data.length) data = [data];
    data = data.map(function(x) {
        var a, c;
        if (x.isBackwards) {
            a = x.end;
            c = x.start;
        } else {
            c = x.end;
            a = x.start;
        }
        return Range.comparePoints(a, c) 
            ? [a.row, a.column, c.row, c.column]
            : [a.row, a.column];
    });
    return data.length > 1 ? data : data[0];
}
function setSelection(editor, data) {
    if (typeof data[0] == "number")
        data = [data];
    editor.selection.fromJSON(data.map(function(x) {
        var start = {row: x[0], column: x[1]};
        var end = x.length == 2 ? start : {row: x[2], column: x[3]};
        var isBackwards = Range.comparePoints(start, end) > 0;
        return isBackwards ? {
            start: end,
            end: start,
            isBackwards: true
        } : {
            start: start,
            end: end,
            isBackwards: true
        };
    }));
}
function testSelection(editor, data) {
    assert.equal(getSelection(editor) + "", data + "");
}
function setValue(editor, value) {
    editor.setValue(value, 1);
}
function testValue(editor, value) {
    assert.equal(editor.getValue(), value);
}

 
var editor;
var logEditor;
var logSession
exports.openLogView = function() {
    exports.addGlobals();
    var sp = window.env.split;
    sp.setSplits(1);
    sp.setSplits(2);
    sp.setOrientation(sp.BESIDE);
    editor = sp.$editors[0];
    logEditor = sp.$editors[1];
    
    if (!logSession) {
        logSession = new EditSession(localStorage.lastTestCase || "", "ace/mode/javascript");
        logSession.setUndoManager(new UndoManager)
    }
    logEditor.setSession(logSession);
    logEditor.session.foldAll();
    logEditor.on("input", save);
}
exports.record = function() {
    exports.addGlobals();
    exports.openLogView();
    
    logEditor.setValue("var Range = require(\"ace/range\").Range;\n"
        + getSelection + "\n"
        + testSelection + "\n"
        + setSelection + "\n"
        + testValue + "\n"
        + setValue + "\n"
        + "\n//-------------------------------------\n", 1);
    logEditor.session.foldAll();

    addAction({
        type: "setValue",
        data: editor.getValue()
    });
    addAction({
        type: "setSelection",
        data: getSelection(editor)
    });
    editor.commands.on("afterExec", onAfterExec);
    editor.on("mouseup", onMouseUp);
    editor.selection.on("beforeEndOperation", onBeforeEndOperation);
    editor.session.on("change", reportChange);
    editor.selection.on("changeCursor", reportCursorChange);
    editor.selection.on("changeSelection", reportSelectionChange);
}

exports.stop = function() {
    save();
    editor.commands.off("afterExec", onAfterExec);
    editor.off("mouseup", onMouseUp);
    editor.off("beforeEndOperation", onBeforeEndOperation);
    editor.session.off("change", reportChange);
    editor.selection.off("changeCursor", reportCursorChange);
    editor.selection.off("changeSelection", reportSelectionChange);
    logEditor.off("input", save);
}
exports.closeLogView = function() {
    exports.stop(); 
    var sp = window.env.split;
    sp.setSplits(1);
}

exports.play = function() {
    exports.openLogView();
    exports.stop();
    var code = logEditor ? logEditor.getValue() : localStorage.lastTestCase;
    var fn = new Function("editor", "debugger;\n" + code);
    fn(editor);
}
var reportChange = reportEvent.bind(null, "change");
var reportCursorChange = reportEvent.bind(null, "CursorChange");
var reportSelectionChange = reportEvent.bind(null, "SelectionChange");

function save() {
    localStorage.lastTestCase = logEditor.getValue();
}

function reportEvent(name) {
    addAction({
        type: "event",
        source: name
    });
} 
function onSelection() {
    addAction({
        type: "event",
        data: "change",
        source: "operationEnd"
    });
} 
function onBeforeEndOperation() {
    addAction({
        type: "setSelection",
        data: getSelection(editor),
        source: "operationEnd"
    });
} 
function onMouseUp() {
    addAction({
        type: "setSelection",
        data: getSelection(editor),
        source: "mouseup"
    });
}
function onAfterExec(e) {
    addAction({
        type: "exec",
        data: e
    });
    addAction({
        type: "value",
        data: editor.getValue()
    });
    addAction({
        type: "selection",
        data: getSelection(editor)
    });
}

function addAction(a) {
    var str = toString(a);
    if (str) {
        logEditor.insert(str + "\n");
        logEditor.renderer.scrollCursorIntoView();
    }
}

var lastValue = "";
function toString(x) {
    var str = "";
    var data = x.data;
    switch (x.type) {
        case "exec": 
            str = 'editor.execCommand("' 
                + data.command.name
                + (data.args ? '", ' + JSON.stringify(data.args) : '"')
            + ')';
            break;
        case "setSelection":
            str = 'setSelection(editor, ' + JSON.stringify(data)  + ')';
            break;
        case "setValue":
            if (lastValue != data) {
                lastValue = data;
                str = 'editor.setValue(' + JSON.stringify(data) + ', -1)';
            }
            else {
                return;
            }
            break;
        case "selection":
            str = 'testSelection(editor, ' + JSON.stringify(data) + ')';
            break;
        case "value":
            if (lastValue != data) {
                lastValue = data;
                str = 'testValue(editor, ' + JSON.stringify(data) + ')';
            }
            else  {
                return;
            }
            break;
    }
    return str + (x.source ? " // " + x.source : "");
}

exports.getUI = function(container) {
    return ["div", {},
        " Test ", 
        ["button", {onclick: exports.openLogView}, "O"],
        ["button", {onclick: exports.record}, "Record"],
        ["button", {onclick: exports.stop}, "Stop"],
        ["button", {onclick: exports.play}, "Play"],
        ["button", {onclick: exports.closeLogView}, "X"],
    ];
};


var ignoreEvents = false;
exports.textInputDebugger = {
    position: 2000,
    onchange: function(value) {
        var sp = env.split;
        if (sp.getSplits() == 2) {
            sp.setSplits(1);
        }
        if (env.textarea) {
            if (env.textarea.detach)
                env.textarea.detach();
            env.textarea.oldParent.appendChild(env.textarea);
            env.textarea.className = env.textarea.oldClassName;
            env.textarea = null;
        }
        if (value) {
            this.showConsole();
        }
    },
    showConsole: function() {
        var sp = env.split;
        sp.setSplits(2);
        sp.setOrientation(sp.BELOW);
        
        var editor = sp.$editors[0];
        var text = editor.textInput.getElement();
        text.oldParent = text.parentNode;
        text.oldClassName = text.className;
        text.className = "text-input-debug";
        document.body.appendChild(text);
        env.textarea = text;
        
        var addToLog = function(e) {
            if (ignoreEvents) return;
            var data = {
                _: e.type, 
                range: [text.selectionStart, text.selectionEnd], 
                value: text.value, 
                key: e.key && {
                    code: e.code,
                    key: e.key, 
                    keyCode: e.keyCode
                },
                modifier: event.getModifierString(e) || undefined
            };
            log.navigateFileEnd();
            var str = JSON.stringify(data).replace(/"(\w+)":/g, " $1: ");
            log.insert(str + ",\n");
            log.renderer.scrollCursorIntoView();
        };
        var events = ["select", "input", "keypress", "keydown", "keyup", 
            "compositionstart", "compositionupdate", "compositionend", "cut", "copy", "paste"
        ];
        events.forEach(function(name) {
            text.addEventListener(name, addToLog, true);
        });
        function onMousedown(ev) {
            if (ev.domEvent.target == text)
                ev.$pos = editor.getCursorPosition();
        }
        text.detach = function() {
            delete text.value;
            delete text.setSelectionRange;
            
            events.forEach(function(name) {
                text.removeEventListener(name, addToLog, true);
            });
            editor.off("mousedown", onMousedown);
        };
        editor.on("mousedown", onMousedown);
        
        text.__defineSetter__("value", function(v) {
            this.__proto__.__lookupSetter__("value").call(this, v); 
            console.log(v);
        });
        text.__defineGetter__("value", function(v) {
            var v = this.__proto__.__lookupGetter__("value").call(this); 
            return v;
        });
        text.setSelectionRange = function(start, end) {
            ignoreEvents = true;
            this.__proto__.setSelectionRange.call(this, start, end)
            ignoreEvents = false;
        }
        
        var log = sp.$editors[1];
        if (!this.session)
            this.session = new EditSession("");
        log.setSession(this.session);
        editor.focus();
    },
    getValue: function() {
        return !!env.textarea;
    }
}

});

define("ace/ext/modelist",["require","exports","module"], function(require, exports, module) {
"use strict";

var modes = [];
function getModeForPath(path) {
    var mode = modesByName.text;
    var fileName = path.split(/[\/\\]/).pop();
    for (var i = 0; i < modes.length; i++) {
        if (modes[i].supportsFile(fileName)) {
            mode = modes[i];
            break;
        }
    }
    return mode;
}

var Mode = function(name, caption, extensions) {
    this.name = name;
    this.caption = caption;
    this.mode = "ace/mode/" + name;
    this.extensions = extensions;
    var re;
    if (/\^/.test(extensions)) {
        re = extensions.replace(/\|(\^)?/g, function(a, b){
            return "$|" + (b ? "^" : "^.*\\.");
        }) + "$";
    } else {
        re = "^.*\\.(" + extensions + ")$";
    }

    this.extRe = new RegExp(re, "gi");
};

Mode.prototype.supportsFile = function(filename) {
    return filename.match(this.extRe);
};
var supportedModes = {
    ABAP:        ["abap"],
    ABC:         ["abc"],
    ActionScript:["as"],
    ADA:         ["ada|adb"],
    Apache_Conf: ["^htaccess|^htgroups|^htpasswd|^conf|htaccess|htgroups|htpasswd"],
    AsciiDoc:    ["asciidoc|adoc"],
    ASL:         ["dsl|asl"],
    Assembly_x86:["asm|a"],
    AutoHotKey:  ["ahk"],
    BatchFile:   ["bat|cmd"],
    Bro:         ["bro"],
    C_Cpp:       ["cpp|c|cc|cxx|h|hh|hpp|ino"],
    C9Search:    ["c9search_results"],
    Cirru:       ["cirru|cr"],
    Clojure:     ["clj|cljs"],
    Cobol:       ["CBL|COB"],
    coffee:      ["coffee|cf|cson|^Cakefile"],
    ColdFusion:  ["cfm"],
    CSharp:      ["cs"],
    Csound_Document: ["csd"],
    Csound_Orchestra: ["orc"],
    Csound_Score: ["sco"],
    CSS:         ["css"],
    Curly:       ["curly"],
    D:           ["d|di"],
    Dart:        ["dart"],
    Diff:        ["diff|patch"],
    Dockerfile:  ["^Dockerfile"],
    Dot:         ["dot"],
    Drools:      ["drl"],
    Edifact:     ["edi"],
    Eiffel:      ["e|ge"],
    EJS:         ["ejs"],
    Elixir:      ["ex|exs"],
    Elm:         ["elm"],
    Erlang:      ["erl|hrl"],
    Forth:       ["frt|fs|ldr|fth|4th"],
    Fortran:     ["f|f90"],
    FSharp:      ["fsi|fs|ml|mli|fsx|fsscript"],
    FTL:         ["ftl"],
    Gcode:       ["gcode"],
    Gherkin:     ["feature"],
    Gitignore:   ["^.gitignore"],
    Glsl:        ["glsl|frag|vert"],
    Gobstones:   ["gbs"],
    golang:      ["go"],
    GraphQLSchema: ["gql"],
    Groovy:      ["groovy"],
    HAML:        ["haml"],
    Handlebars:  ["hbs|handlebars|tpl|mustache"],
    Haskell:     ["hs"],
    Haskell_Cabal:     ["cabal"],
    haXe:        ["hx"],
    Hjson:       ["hjson"],
    HTML:        ["html|htm|xhtml|vue|we|wpy"],
    HTML_Elixir: ["eex|html.eex"],
    HTML_Ruby:   ["erb|rhtml|html.erb"],
    INI:         ["ini|conf|cfg|prefs"],
    Io:          ["io"],
    Jack:        ["jack"],
    Jade:        ["jade|pug"],
    Java:        ["java"],
    JavaScript:  ["js|jsm|jsx"],
    JSON:        ["json"],
    JSONiq:      ["jq"],
    JSP:         ["jsp"],
    JSSM:        ["jssm|jssm_state"],
    JSX:         ["jsx"],
    Julia:       ["jl"],
    Kotlin:      ["kt|kts"],
    LaTeX:       ["tex|latex|ltx|bib"],
    LESS:        ["less"],
    Liquid:      ["liquid"],
    Lisp:        ["lisp"],
    LiveScript:  ["ls"],
    LogiQL:      ["logic|lql"],
    LSL:         ["lsl"],
    Lua:         ["lua"],
    LuaPage:     ["lp"],
    Lucene:      ["lucene"],
    Makefile:    ["^Makefile|^GNUmakefile|^makefile|^OCamlMakefile|make"],
    Markdown:    ["md|markdown"],
    Mask:        ["mask"],
    MATLAB:      ["matlab"],
    Maze:        ["mz"],
    MEL:         ["mel"],
    MIXAL:       ["mixal"],
    MUSHCode:    ["mc|mush"],
    MySQL:       ["mysql"],
    Nix:         ["nix"],
    NSIS:        ["nsi|nsh"],
    ObjectiveC:  ["m|mm"],
    OCaml:       ["ml|mli"],
    Pascal:      ["pas|p"],
    Perl:        ["pl|pm"],
    pgSQL:       ["pgsql"],
    PHP_Laravel_blade: ["blade.php"],
    PHP:         ["php|phtml|shtml|php3|php4|php5|phps|phpt|aw|ctp|module"],
    Puppet:      ["epp|pp"],
    Pig:         ["pig"],
    Powershell:  ["ps1"],
    Praat:       ["praat|praatscript|psc|proc"],
    Prolog:      ["plg|prolog"],
    Properties:  ["properties"],
    Protobuf:    ["proto"],
    Python:      ["py"],
    R:           ["r"],
    Razor:       ["cshtml|asp"],
    RDoc:        ["Rd"],
    Red:         ["red|reds"],
    RHTML:       ["Rhtml"],
    RST:         ["rst"],
    Ruby:        ["rb|ru|gemspec|rake|^Guardfile|^Rakefile|^Gemfile"],
    Rust:        ["rs"],
    SASS:        ["sass"],
    SCAD:        ["scad"],
    Scala:       ["scala"],
    Scheme:      ["scm|sm|rkt|oak|scheme"],
    SCSS:        ["scss"],
    SH:          ["sh|bash|^.bashrc"],
    SJS:         ["sjs"],
    Slim:        ["slim|skim"],
    Smarty:      ["smarty|tpl"],
    snippets:    ["snippets"],
    Soy_Template:["soy"],
    Space:       ["space"],
    SQL:         ["sql"],
    SQLServer:   ["sqlserver"],
    Stylus:      ["styl|stylus"],
    SVG:         ["svg"],
    Swift:       ["swift"],
    Tcl:         ["tcl"],
    Terraform:   ["tf", "tfvars", "terragrunt"],
    Tex:         ["tex"],
    Text:        ["txt"],
    Textile:     ["textile"],
    Toml:        ["toml"],
    TSX:         ["tsx"],
    Twig:        ["twig|swig"],
    Typescript:  ["ts|typescript|str"],
    Vala:        ["vala"],
    VBScript:    ["vbs|vb"],
    Velocity:    ["vm"],
    Verilog:     ["v|vh|sv|svh"],
    VHDL:        ["vhd|vhdl"],
    Wollok:      ["wlk|wpgm|wtest"],
    XML:         ["xml|rdf|rss|wsdl|xslt|atom|mathml|mml|xul|xbl|xaml"],
    XQuery:      ["xq"],
    YAML:        ["yaml|yml"],
    Django:      ["html"]
};

var nameOverrides = {
    ObjectiveC: "Objective-C",
    CSharp: "C#",
    golang: "Go",
    C_Cpp: "C and C++",
    Csound_Document: "Csound Document",
    Csound_Orchestra: "Csound",
    Csound_Score: "Csound Score",
    coffee: "CoffeeScript",
    HTML_Ruby: "HTML (Ruby)",
    HTML_Elixir: "HTML (Elixir)",
    FTL: "FreeMarker",
    PHP_Laravel_blade: "PHP (Blade Template)"
};
var modesByName = {};
for (var name in supportedModes) {
    var data = supportedModes[name];
    var displayName = (nameOverrides[name] || name).replace(/_/g, " ");
    var filename = name.toLowerCase();
    var mode = new Mode(filename, displayName, data[0]);
    modesByName[filename] = mode;
    modes.push(mode);
}

module.exports = {
    getModeForPath: getModeForPath,
    modes: modes,
    modesByName: modesByName
};

});

define("kitchen-sink/file_drop",["require","exports","module","ace/config","ace/lib/event","ace/ext/modelist","ace/editor"], function(require, exports, module) {

var config = require("ace/config");
var event = require("ace/lib/event");
var modelist = require("ace/ext/modelist");

module.exports = function(editor) {
    event.addListener(editor.container, "dragover", function(e) {
        var types = e.dataTransfer.types;
        if (types && Array.prototype.indexOf.call(types, 'Files') !== -1)
            return event.preventDefault(e);
    });

    event.addListener(editor.container, "drop", function(e) {
        var file;
        try {
            file = e.dataTransfer.files[0];
            if (window.FileReader) {
                var reader = new FileReader();
                reader.onload = function() {
                    var mode = modelist.getModeForPath(file.name);
                    editor.session.doc.setValue(reader.result);
                    editor.session.setMode(mode.mode);
                    editor.session.modeName = mode.name;
                };
                reader.readAsText(file);
            }
            return event.preventDefault(e);
        } catch(err) {
            return event.stopEvent(e);
        }
    });
};

var Editor = require("ace/editor").Editor;
config.defineOptions(Editor.prototype, "editor", {
    loadDroppedFile: {
        set: function() { module.exports(this); },
        value: true
    }
});

});

define("ace/theme/textmate",["require","exports","module","ace/lib/dom"], function(require, exports, module) {
"use strict";

exports.isDark = false;
exports.cssClass = "ace-tm";
exports.cssText = ".ace-tm .ace_gutter {\
background: #f0f0f0;\
color: #333;\
}\
.ace-tm .ace_print-margin {\
width: 1px;\
background: #e8e8e8;\
}\
.ace-tm .ace_fold {\
background-color: #6B72E6;\
}\
.ace-tm {\
background-color: #FFFFFF;\
color: black;\
}\
.ace-tm .ace_cursor {\
color: black;\
}\
.ace-tm .ace_invisible {\
color: rgb(191, 191, 191);\
}\
.ace-tm .ace_storage,\
.ace-tm .ace_keyword {\
color: blue;\
}\
.ace-tm .ace_constant {\
color: rgb(197, 6, 11);\
}\
.ace-tm .ace_constant.ace_buildin {\
color: rgb(88, 72, 246);\
}\
.ace-tm .ace_constant.ace_language {\
color: rgb(88, 92, 246);\
}\
.ace-tm .ace_constant.ace_library {\
color: rgb(6, 150, 14);\
}\
.ace-tm .ace_invalid {\
background-color: rgba(255, 0, 0, 0.1);\
color: red;\
}\
.ace-tm .ace_support.ace_function {\
color: rgb(60, 76, 114);\
}\
.ace-tm .ace_support.ace_constant {\
color: rgb(6, 150, 14);\
}\
.ace-tm .ace_support.ace_type,\
.ace-tm .ace_support.ace_class {\
color: rgb(109, 121, 222);\
}\
.ace-tm .ace_keyword.ace_operator {\
color: rgb(104, 118, 135);\
}\
.ace-tm .ace_string {\
color: rgb(3, 106, 7);\
}\
.ace-tm .ace_comment {\
color: rgb(76, 136, 107);\
}\
.ace-tm .ace_comment.ace_doc {\
color: rgb(0, 102, 255);\
}\
.ace-tm .ace_comment.ace_doc.ace_tag {\
color: rgb(128, 159, 191);\
}\
.ace-tm .ace_constant.ace_numeric {\
color: rgb(0, 0, 205);\
}\
.ace-tm .ace_variable {\
color: rgb(49, 132, 149);\
}\
.ace-tm .ace_xml-pe {\
color: rgb(104, 104, 91);\
}\
.ace-tm .ace_entity.ace_name.ace_function {\
color: #0000A2;\
}\
.ace-tm .ace_heading {\
color: rgb(12, 7, 255);\
}\
.ace-tm .ace_list {\
color:rgb(185, 6, 144);\
}\
.ace-tm .ace_meta.ace_tag {\
color:rgb(0, 22, 142);\
}\
.ace-tm .ace_string.ace_regex {\
color: rgb(255, 0, 0)\
}\
.ace-tm .ace_marker-layer .ace_selection {\
background: rgb(181, 213, 255);\
}\
.ace-tm.ace_multiselect .ace_selection.ace_start {\
box-shadow: 0 0 3px 0px white;\
}\
.ace-tm .ace_marker-layer .ace_step {\
background: rgb(252, 255, 0);\
}\
.ace-tm .ace_marker-layer .ace_stack {\
background: rgb(164, 229, 101);\
}\
.ace-tm .ace_marker-layer .ace_bracket {\
margin: -1px 0 0 -1px;\
border: 1px solid rgb(192, 192, 192);\
}\
.ace-tm .ace_marker-layer .ace_active-line {\
background: rgba(0, 0, 0, 0.07);\
}\
.ace-tm .ace_gutter-active-line {\
background-color : #dcdcdc;\
}\
.ace-tm .ace_marker-layer .ace_selected-word {\
background: rgb(250, 250, 255);\
border: 1px solid rgb(200, 200, 250);\
}\
.ace-tm .ace_indent-guide {\
background: url(\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAACCAYAAACZgbYnAAAAE0lEQVQImWP4////f4bLly//BwAmVgd1/w11/gAAAABJRU5ErkJggg==\") right repeat-y;\
}\
";
exports.$id = "ace/theme/textmate";

var dom = require("../lib/dom");
dom.importCssString(exports.cssText, exports.cssClass);
});

define("ace/ext/whitespace",["require","exports","module","ace/lib/lang"], function(require, exports, module) {
"use strict";

var lang = require("../lib/lang");
exports.$detectIndentation = function(lines, fallback) {
    var stats = [];
    var changes = [];
    var tabIndents = 0;
    var prevSpaces = 0;
    var max = Math.min(lines.length, 1000);
    for (var i = 0; i < max; i++) {
        var line = lines[i];
        if (!/^\s*[^*+\-\s]/.test(line))
            continue;

        if (line[0] == "\t") {
            tabIndents++;
            prevSpaces = -Number.MAX_VALUE;
        } else {
            var spaces = line.match(/^ */)[0].length;
            if (spaces && line[spaces] != "\t") {
                var diff = spaces - prevSpaces;
                if (diff > 0 && !(prevSpaces%diff) && !(spaces%diff))
                    changes[diff] = (changes[diff] || 0) + 1;
    
                stats[spaces] = (stats[spaces] || 0) + 1;
            }
            prevSpaces = spaces;
        }
        while (i < max && line[line.length - 1] == "\\")
            line = lines[i++];
    }
    
    function getScore(indent) {
        var score = 0;
        for (var i = indent; i < stats.length; i += indent)
            score += stats[i] || 0;
        return score;
    }

    var changesTotal = changes.reduce(function(a,b){return a+b;}, 0);

    var first = {score: 0, length: 0};
    var spaceIndents = 0;
    for (var i = 1; i < 12; i++) {
        var score = getScore(i);
        if (i == 1) {
            spaceIndents = score;
            score = stats[1] ? 0.9 : 0.8;
            if (!stats.length)
                score = 0;
        } else
            score /= spaceIndents;

        if (changes[i])
            score += changes[i] / changesTotal;

        if (score > first.score)
            first = {score: score, length: i};
    }

    if (first.score && first.score > 1.4)
        var tabLength = first.length;

    if (tabIndents > spaceIndents + 1) {
        if (tabLength == 1 || spaceIndents < tabIndents / 4 || first.score < 1.8)
            tabLength = undefined;
        return {ch: "\t", length: tabLength};
    }
    if (spaceIndents > tabIndents + 1)
        return {ch: " ", length: tabLength};
};

exports.detectIndentation = function(session) {
    var lines = session.getLines(0, 1000);
    var indent = exports.$detectIndentation(lines) || {};

    if (indent.ch)
        session.setUseSoftTabs(indent.ch == " ");

    if (indent.length)
        session.setTabSize(indent.length);
    return indent;
};
exports.trimTrailingSpace = function(session, options) {
    var doc = session.getDocument();
    var lines = doc.getAllLines();
    
    var min = options && options.trimEmpty ? -1 : 0;
    var cursors = [], ci = -1;
    if (options && options.keepCursorPosition) {
        if (session.selection.rangeCount) {
            session.selection.rangeList.ranges.forEach(function(x, i, ranges) {
               var next = ranges[i + 1];
               if (next && next.cursor.row == x.cursor.row)
                  return;
              cursors.push(x.cursor);
            });
        } else {
            cursors.push(session.selection.getCursor());
        }
        ci = 0;
    }
    var cursorRow = cursors[ci] && cursors[ci].row;

    for (var i = 0, l=lines.length; i < l; i++) {
        var line = lines[i];
        var index = line.search(/\s+$/);

        if (i == cursorRow) {
            if (index < cursors[ci].column && index > min)
               index = cursors[ci].column;
            ci++;
            cursorRow = cursors[ci] ? cursors[ci].row : -1;
        }

        if (index > min)
            doc.removeInLine(i, index, line.length);
    }
};

exports.convertIndentation = function(session, ch, len) {
    var oldCh = session.getTabString()[0];
    var oldLen = session.getTabSize();
    if (!len) len = oldLen;
    if (!ch) ch = oldCh;

    var tab = ch == "\t" ? ch: lang.stringRepeat(ch, len);

    var doc = session.doc;
    var lines = doc.getAllLines();

    var cache = {};
    var spaceCache = {};
    for (var i = 0, l=lines.length; i < l; i++) {
        var line = lines[i];
        var match = line.match(/^\s*/)[0];
        if (match) {
            var w = session.$getStringScreenWidth(match)[0];
            var tabCount = Math.floor(w/oldLen);
            var reminder = w%oldLen;
            var toInsert = cache[tabCount] || (cache[tabCount] = lang.stringRepeat(tab, tabCount));
            toInsert += spaceCache[reminder] || (spaceCache[reminder] = lang.stringRepeat(" ", reminder));

            if (toInsert != match) {
                doc.removeInLine(i, 0, match.length);
                doc.insertInLine({row: i, column: 0}, toInsert);
            }
        }
    }
    session.setTabSize(len);
    session.setUseSoftTabs(ch == " ");
};

exports.$parseStringArg = function(text) {
    var indent = {};
    if (/t/.test(text))
        indent.ch = "\t";
    else if (/s/.test(text))
        indent.ch = " ";
    var m = text.match(/\d+/);
    if (m)
        indent.length = parseInt(m[0], 10);
    return indent;
};

exports.$parseArg = function(arg) {
    if (!arg)
        return {};
    if (typeof arg == "string")
        return exports.$parseStringArg(arg);
    if (typeof arg.text == "string")
        return exports.$parseStringArg(arg.text);
    return arg;
};

exports.commands = [{
    name: "detectIndentation",
    exec: function(editor) {
        exports.detectIndentation(editor.session);
    }
}, {
    name: "trimTrailingSpace",
    exec: function(editor, args) {
        exports.trimTrailingSpace(editor.session, args);
    }
}, {
    name: "convertIndentation",
    exec: function(editor, arg) {
        var indent = exports.$parseArg(arg);
        exports.convertIndentation(editor.session, indent.ch, indent.length);
    }
}, {
    name: "setIndentation",
    exec: function(editor, arg) {
        var indent = exports.$parseArg(arg);
        indent.length && editor.session.setTabSize(indent.length);
        indent.ch && editor.session.setUseSoftTabs(indent.ch == " ");
    }
}];

});

define("kitchen-sink/doclist",["require","exports","module","ace/edit_session","ace/undomanager","ace/lib/net","ace/ext/modelist"], function(require, exports, module) {
"use strict";

var EditSession = require("ace/edit_session").EditSession;
var UndoManager = require("ace/undomanager").UndoManager;
var net = require("ace/lib/net");

var modelist = require("ace/ext/modelist");
var fileCache = {};

function initDoc(file, path, doc) {
    if (doc.prepare)
        file = doc.prepare(file);

    var session = new EditSession(file);
    session.setUndoManager(new UndoManager());
    doc.session = session;
    doc.path = path;
    session.name = doc.name;
    if (doc.wrapped) {
        session.setUseWrapMode(true);
        session.setWrapLimitRange(80, 80);
    }
    var mode = modelist.getModeForPath(path);
    session.modeName = mode.name;
    session.setMode(mode.mode);
    return session;
}


function makeHuge(txt) {
    for (var i = 0; i < 5; i++)
        txt += txt;
    return txt;
}

var docs = {
    "docs/javascript.js": {order: 1, name: "JavaScript"},

    "docs/latex.tex": {name: "LaTeX", wrapped: true},
    "docs/markdown.md": {name: "Markdown", wrapped: true},
    "docs/mushcode.mc": {name: "MUSHCode", wrapped: true},
    "docs/pgsql.pgsql": {name: "pgSQL", wrapped: true},
    "docs/plaintext.txt": {name: "Plain Text", prepare: makeHuge, wrapped: true},
    "docs/sql.sql": {name: "SQL", wrapped: true},

    "docs/textile.textile": {name: "Textile", wrapped: true},

    "docs/c9search.c9search_results": "C9 Search Results",
    "docs/mel.mel": "MEL",
    "docs/Nix.nix": "Nix"
};

var ownSource = {
};

var hugeDocs = require.toUrl ? {
    "build/src/ace.js": "",
    "build/src-min/ace.js": ""
} : {
    "src/ace.js": "",
    "src-min/ace.js": ""
};

modelist.modes.forEach(function(m) {
    var ext = m.extensions.split("|")[0];
    if (ext[0] === "^") {
        path = ext.substr(1);
    } else {
        var path = m.name + "." + ext;
    }
    path = "docs/" + path;
    if (!docs[path]) {
        docs[path] = {name: m.caption};
    } else if (typeof docs[path] == "object" && !docs[path].name) {
        docs[path].name = m.caption;
    }
});



if (window.require && window.require.s) try {
    for (var path in window.require.s.contexts._.defined) {
        if (path.indexOf("!") != -1)
            path = path.split("!").pop();
        else
            path = path + ".js";
        ownSource[path] = "";
    }
} catch(e) {}

function sort(list) {
    return list.sort(function(a, b) {
        var cmp = (b.order || 0) - (a.order || 0);
        return cmp || a.name && a.name.localeCompare(b.name);
    });
}

function prepareDocList(docs) {
    var list = [];
    for (var path in docs) {
        var doc = docs[path];
        if (typeof doc != "object")
            doc = {name: doc || path};

        doc.path = path;
        doc.desc = doc.name.replace(/^(ace|docs|demo|build)\//, "");
        if (doc.desc.length > 18)
            doc.desc = doc.desc.slice(0, 7) + ".." + doc.desc.slice(-9);

        fileCache[doc.name.toLowerCase()] = doc;
        list.push(doc);
    }

    return list;
}

function loadDoc(name, callback) {
    var doc = fileCache[name.toLowerCase()];
    if (!doc)
        return callback(null);

    if (doc.session)
        return callback(doc.session);
    var path = doc.path;
    var parts = path.split("/");
    if (parts[0] == "docs")
        path = "demo/kitchen-sink/" + path;
    else if (parts[0] == "ace")
        path = "lib/" + path;

    net.get(path, function(x) {
        initDoc(x, path, doc);
        callback(doc.session);
    });
}

function saveDoc(name, callback) {
    var doc = fileCache[name.toLowerCase()] || name;
    if (!doc || !doc.session)
        return callback("Unknown document: " + name);

    var path = doc.path;
    var parts = path.split("/");
    if (parts[0] == "docs")
        path = "demo/kitchen-sink/" + path;
    else if (parts[0] == "ace")
        path = "lib/" + path;

    upload(path, doc.session.getValue(), callback);
}

function upload(url, data, callback) {
    var absUrl = net.qualifyURL(url);
    if (/^file:/.test(absUrl))
        absUrl = "http://localhost:8888/" + url;
    url = absUrl;
    if (!/^https?:/.test(url))
        return callback(new Error("Unsupported url scheme"));
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", url, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            callback(!/^2../.test(xhr.status));
        }
    };
    xhr.send(data);
}

module.exports = {
    fileCache: fileCache,
    docs: sort(prepareDocList(docs)),
    ownSource: prepareDocList(ownSource),
    hugeDocs: prepareDocList(hugeDocs),
    initDoc: initDoc,
    loadDoc: loadDoc,
    saveDoc: saveDoc
};
module.exports.all = {
    "Mode Examples": module.exports.docs,
    "Huge documents": module.exports.hugeDocs,
    "own source": module.exports.ownSource
};

});

define("kitchen-sink/layout",["require","exports","module","ace/lib/dom","ace/lib/event","ace/edit_session","ace/undomanager","ace/virtual_renderer","ace/editor","ace/multi_select","ace/theme/textmate"], function(require, exports, module) {
"use strict";

var dom = require("ace/lib/dom");
var event = require("ace/lib/event");

var EditSession = require("ace/edit_session").EditSession;
var UndoManager = require("ace/undomanager").UndoManager;
var Renderer = require("ace/virtual_renderer").VirtualRenderer;
var Editor = require("ace/editor").Editor;
var MultiSelect = require("ace/multi_select").MultiSelect;

dom.importCssString("\
splitter {\
    border: 1px solid #C6C6D2;\
    width: 0px;\
    cursor: ew-resize;\
    z-index:10}\
splitter:hover {\
    margin-left: -2px;\
    width:3px;\
    border-color: #B5B4E0;\
}\
", "splitEditor");

exports.edit = function(el) {
    if (typeof(el) == "string")
        el = document.getElementById(el);

    var editor = new Editor(new Renderer(el, require("ace/theme/textmate")));

    editor.resize();
    event.addListener(window, "resize", function() {
        editor.resize();
    });
    return editor;
};


var SplitRoot = function(el, theme, position, getSize) {
    el.style.position = position || "relative";
    this.container = el;
    this.getSize = getSize || this.getSize;
    this.resize = this.$resize.bind(this);

    event.addListener(el.ownerDocument.defaultView, "resize", this.resize);
    this.editor = this.createEditor();
};

(function(){
    this.createEditor = function() {
        var el = document.createElement("div");
        el.className = this.$editorCSS;
        el.style.cssText = "position: absolute; top:0px; bottom:0px";
        this.$container.appendChild(el);
        var session = new EditSession("");
        var editor = new Editor(new Renderer(el, this.$theme));

        this.$editors.push(editor);
        editor.setFontSize(this.$fontSize);
        return editor;
    };
    this.$resize = function() {
        var size = this.getSize(this.container);
        this.rect = {
            x: size.left,
            y: size.top,
            w: size.width,
            h: size.height
        };
        this.item.resize(this.rect);
    };
    this.getSize = function(el) {
        return el.getBoundingClientRect();
    };
    this.destroy = function() {
        var win = this.container.ownerDocument.defaultView;
        event.removeListener(win, "resize", this.resize);
    };


}).call(SplitRoot.prototype);



var Split = function(){

};
(function(){
    this.execute = function(options) {
        this.$u.execute(options);
    };

}).call(Split.prototype);



exports.singleLineEditor = function(el) {
    var renderer = new Renderer(el);
    el.style.overflow = "hidden";

    renderer.screenToTextCoordinates = function(x, y) {
        var pos = this.pixelToScreenCoordinates(x, y);
        return this.session.screenToDocumentPosition(
            Math.min(this.session.getScreenLength() - 1, Math.max(pos.row, 0)),
            Math.max(pos.column, 0)
        );
    };

    renderer.$maxLines = 4;

    renderer.setStyle("ace_one-line");
    var editor = new Editor(renderer);
    editor.session.setUndoManager(new UndoManager());

    editor.setShowPrintMargin(false);
    editor.renderer.setShowGutter(false);
    editor.renderer.setHighlightGutterLine(false);
    editor.$mouseHandler.$focusWaitTimout = 0;

    return editor;
};



});

define("kitchen-sink/util",["require","exports","module","ace/lib/dom","ace/lib/event","ace/edit_session","ace/undomanager","ace/virtual_renderer","ace/editor","ace/multi_select"], function(require, exports, module) {
"use strict";

var dom = require("ace/lib/dom");
var event = require("ace/lib/event");

var EditSession = require("ace/edit_session").EditSession;
var UndoManager = require("ace/undomanager").UndoManager;
var Renderer = require("ace/virtual_renderer").VirtualRenderer;
var Editor = require("ace/editor").Editor;
var MultiSelect = require("ace/multi_select").MultiSelect;

var urlOptions = {}
try {
    window.location.search.slice(1).split(/[&]/).forEach(function(e) {
        var parts = e.split("=");
        urlOptions[decodeURIComponent(parts[0])] = decodeURIComponent(parts[1]);
    });
} catch(e) {
    console.error(e);
}
exports.createEditor = function(el) {
    return new Editor(new Renderer(el));
};

exports.getOption = function(name) {
    if (urlOptions[name])
        return urlOptions[name];
    return localStorage && localStorage.getItem(name);
};

exports.saveOption = function(name, value) {
    if (value == false)
        value = "";
    localStorage && localStorage.setItem(name, value);
};

exports.createSplitEditor = function(el) {
    if (typeof(el) == "string")
        el = document.getElementById(el);

    var e0 = document.createElement("div");
    var s = document.createElement("splitter");
    var e1 = document.createElement("div");
    el.appendChild(e0);
    el.appendChild(e1);
    el.appendChild(s);
    e0.style.position = e1.style.position = s.style.position = "absolute";
    el.style.position = "relative";
    var split = {$container: el};

    split.editor0 = split[0] = new Editor(new Renderer(e0));
    split.editor1 = split[1] = new Editor(new Renderer(e1));
    split.splitter = s;

    s.ratio = 0.5;

    split.resize = function resize(){
        var height = el.parentNode.clientHeight - el.offsetTop;
        var total = el.clientWidth;
        var w1 = total * s.ratio;
        var w2 = total * (1- s.ratio);
        s.style.left = w1 - 1 + "px";
        s.style.height = el.style.height = height + "px";

        var st0 = split[0].container.style;
        var st1 = split[1].container.style;
        st0.width = w1 + "px";
        st1.width = w2 + "px";
        st0.left = 0 + "px";
        st1.left = w1 + "px";

        st0.top = st1.top = "0px";
        st0.height = st1.height = height + "px";

        split[0].resize();
        split[1].resize();
    };

    split.onMouseDown = function(e) {
        var rect = el.getBoundingClientRect();
        var x = e.clientX;
        var y = e.clientY;

        var button = e.button;
        if (button !== 0) {
            return;
        }

        var onMouseMove = function(e) {
            x = e.clientX;
            y = e.clientY;
        };
        var onResizeEnd = function(e) {
            clearInterval(timerId);
        };

        var onResizeInterval = function() {
            s.ratio = (x - rect.left) / rect.width;
            split.resize();
        };

        event.capture(s, onMouseMove, onResizeEnd);
        var timerId = setInterval(onResizeInterval, 40);

        return e.preventDefault();
    };



    event.addListener(s, "mousedown", split.onMouseDown);
    event.addListener(window, "resize", split.resize);
    split.resize();
    return split;
};
exports.stripLeadingComments = function(str) {
    if(str.slice(0,2)=='/*') {
        var j = str.indexOf('*/')+2;
        str = str.substr(j);
    }
    return str.trim() + "\n";
};
function saveOptionFromElement(el, val) {
    if (!el.onchange && !el.onclick)
        return;

    if ("checked" in el) {
        localStorage && localStorage.setItem(el.id, el.checked ? 1 : 0);
    }
    else {
        localStorage && localStorage.setItem(el.id, el.value);
    }
}

exports.bindCheckbox = function(id, callback, noInit) {
    if (typeof id == "string")
        var el = document.getElementById(id);
    else {
        var el = id;
        id = el.id;
    }
    var el = document.getElementById(id);
    
    if (urlOptions[id])
        el.checked = urlOptions[id] == "1";
    else if (localStorage && localStorage.getItem(id))
        el.checked = localStorage.getItem(id) == "1";

    var onCheck = function() {
        callback(!!el.checked);
        saveOptionFromElement(el);
    };
    el.onclick = onCheck;
    noInit || onCheck();
    return el;
};

exports.bindDropdown = function(id, callback, noInit) {
    if (typeof id == "string")
        var el = document.getElementById(id);
    else {
        var el = id;
        id = el.id;
    }
    
    if (urlOptions[id])
        el.value = urlOptions[id];
    else if (localStorage && localStorage.getItem(id))
        el.value = localStorage.getItem(id);

    var onChange = function() {
        callback(el.value);
        saveOptionFromElement(el);
    };

    el.onchange = onChange;
    noInit || onChange();
};

exports.fillDropdown = function(el, values) {
    if (typeof el == "string")
        el = document.getElementById(el);

    dropdown(values).forEach(function(e) {
        el.appendChild(e);
    });
};

function elt(tag, attributes, content) {
    var el = dom.createElement(tag);
    if (typeof content == "string") {
        el.appendChild(document.createTextNode(content));
    } else if (content) {
        content.forEach(function(ch) {
            el.appendChild(ch);
        });
    }

    for (var i in attributes)
        el.setAttribute(i, attributes[i]);
    return el;
}

function optgroup(values) {
    return values.map(function(item) {
        if (typeof item == "string")
            item = {name: item, caption: item};
        return elt("option", {value: item.value || item.name}, item.caption || item.desc);
    });
}

function dropdown(values) {
    if (Array.isArray(values))
        return optgroup(values);

    return Object.keys(values).map(function(i) {
        return elt("optgroup", {"label": i}, optgroup(values[i]));
    });
}


});

define("ace/ext/elastic_tabstops_lite",["require","exports","module","ace/editor","ace/config"], function(require, exports, module) {
"use strict";

var ElasticTabstopsLite = function(editor) {
    this.$editor = editor;
    var self = this;
    var changedRows = [];
    var recordChanges = false;
    this.onAfterExec = function() {
        recordChanges = false;
        self.processRows(changedRows);
        changedRows = [];
    };
    this.onExec = function() {
        recordChanges = true;
    };
    this.onChange = function(delta) {
        if (recordChanges) {
            if (changedRows.indexOf(delta.start.row) == -1)
                changedRows.push(delta.start.row);
            if (delta.end.row != delta.start.row)
                changedRows.push(delta.end.row);
        }
    };
};

(function() {
    this.processRows = function(rows) {
        this.$inChange = true;
        var checkedRows = [];

        for (var r = 0, rowCount = rows.length; r < rowCount; r++) {
            var row = rows[r];

            if (checkedRows.indexOf(row) > -1)
                continue;

            var cellWidthObj = this.$findCellWidthsForBlock(row);
            var cellWidths = this.$setBlockCellWidthsToMax(cellWidthObj.cellWidths);
            var rowIndex = cellWidthObj.firstRow;

            for (var w = 0, l = cellWidths.length; w < l; w++) {
                var widths = cellWidths[w];
                checkedRows.push(rowIndex);
                this.$adjustRow(rowIndex, widths);
                rowIndex++;
            }
        }
        this.$inChange = false;
    };

    this.$findCellWidthsForBlock = function(row) {
        var cellWidths = [], widths;
        var rowIter = row;
        while (rowIter >= 0) {
            widths = this.$cellWidthsForRow(rowIter);
            if (widths.length == 0)
                break;

            cellWidths.unshift(widths);
            rowIter--;
        }
        var firstRow = rowIter + 1;
        rowIter = row;
        var numRows = this.$editor.session.getLength();

        while (rowIter < numRows - 1) {
            rowIter++;

            widths = this.$cellWidthsForRow(rowIter);
            if (widths.length == 0)
                break;

            cellWidths.push(widths);
        }

        return { cellWidths: cellWidths, firstRow: firstRow };
    };

    this.$cellWidthsForRow = function(row) {
        var selectionColumns = this.$selectionColumnsForRow(row);
        var tabs = [-1].concat(this.$tabsForRow(row));
        var widths = tabs.map(function(el) { return 0; } ).slice(1);
        var line = this.$editor.session.getLine(row);

        for (var i = 0, len = tabs.length - 1; i < len; i++) {
            var leftEdge = tabs[i]+1;
            var rightEdge = tabs[i+1];

            var rightmostSelection = this.$rightmostSelectionInCell(selectionColumns, rightEdge);
            var cell = line.substring(leftEdge, rightEdge);
            widths[i] = Math.max(cell.replace(/\s+$/g,'').length, rightmostSelection - leftEdge);
        }

        return widths;
    };

    this.$selectionColumnsForRow = function(row) {
        var selections = [], cursor = this.$editor.getCursorPosition();
        if (this.$editor.session.getSelection().isEmpty()) {
            if (row == cursor.row)
                selections.push(cursor.column);
        }

        return selections;
    };

    this.$setBlockCellWidthsToMax = function(cellWidths) {
        var startingNewBlock = true, blockStartRow, blockEndRow, maxWidth;
        var columnInfo = this.$izip_longest(cellWidths);

        for (var c = 0, l = columnInfo.length; c < l; c++) {
            var column = columnInfo[c];
            if (!column.push) {
                console.error(column);
                continue;
            }
            column.push(NaN);

            for (var r = 0, s = column.length; r < s; r++) {
                var width = column[r];
                if (startingNewBlock) {
                    blockStartRow = r;
                    maxWidth = 0;
                    startingNewBlock = false;
                }
                if (isNaN(width)) {
                    blockEndRow = r;

                    for (var j = blockStartRow; j < blockEndRow; j++) {
                        cellWidths[j][c] = maxWidth;
                    }
                    startingNewBlock = true;
                }

                maxWidth = Math.max(maxWidth, width);
            }
        }

        return cellWidths;
    };

    this.$rightmostSelectionInCell = function(selectionColumns, cellRightEdge) {
        var rightmost = 0;

        if (selectionColumns.length) {
            var lengths = [];
            for (var s = 0, length = selectionColumns.length; s < length; s++) {
                if (selectionColumns[s] <= cellRightEdge)
                    lengths.push(s);
                else
                    lengths.push(0);
            }
            rightmost = Math.max.apply(Math, lengths);
        }

        return rightmost;
    };

    this.$tabsForRow = function(row) {
        var rowTabs = [], line = this.$editor.session.getLine(row),
            re = /\t/g, match;

        while ((match = re.exec(line)) != null) {
            rowTabs.push(match.index);
        }

        return rowTabs;
    };

    this.$adjustRow = function(row, widths) {
        var rowTabs = this.$tabsForRow(row);

        if (rowTabs.length == 0)
            return;

        var bias = 0, location = -1;
        var expandedSet = this.$izip(widths, rowTabs);

        for (var i = 0, l = expandedSet.length; i < l; i++) {
            var w = expandedSet[i][0], it = expandedSet[i][1];
            location += 1 + w;
            it += bias;
            var difference = location - it;

            if (difference == 0)
                continue;

            var partialLine = this.$editor.session.getLine(row).substr(0, it );
            var strippedPartialLine = partialLine.replace(/\s*$/g, "");
            var ispaces = partialLine.length - strippedPartialLine.length;

            if (difference > 0) {
                this.$editor.session.getDocument().insertInLine({row: row, column: it + 1}, Array(difference + 1).join(" ") + "\t");
                this.$editor.session.getDocument().removeInLine(row, it, it + 1);

                bias += difference;
            }

            if (difference < 0 && ispaces >= -difference) {
                this.$editor.session.getDocument().removeInLine(row, it + difference, it);
                bias += difference;
            }
        }
    };
    this.$izip_longest = function(iterables) {
        if (!iterables[0])
            return [];
        var longest = iterables[0].length;
        var iterablesLength = iterables.length;

        for (var i = 1; i < iterablesLength; i++) {
            var iLength = iterables[i].length;
            if (iLength > longest)
                longest = iLength;
        }

        var expandedSet = [];

        for (var l = 0; l < longest; l++) {
            var set = [];
            for (var i = 0; i < iterablesLength; i++) {
                if (iterables[i][l] === "")
                    set.push(NaN);
                else
                    set.push(iterables[i][l]);
            }

            expandedSet.push(set);
        }


        return expandedSet;
    };
    this.$izip = function(widths, tabs) {
        var size = widths.length >= tabs.length ? tabs.length : widths.length;

        var expandedSet = [];
        for (var i = 0; i < size; i++) {
            var set = [ widths[i], tabs[i] ];
            expandedSet.push(set);
        }
        return expandedSet;
    };

}).call(ElasticTabstopsLite.prototype);

exports.ElasticTabstopsLite = ElasticTabstopsLite;

var Editor = require("../editor").Editor;
require("../config").defineOptions(Editor.prototype, "editor", {
    useElasticTabstops: {
        set: function(val) {
            if (val) {
                if (!this.elasticTabstops)
                    this.elasticTabstops = new ElasticTabstopsLite(this);
                this.commands.on("afterExec", this.elasticTabstops.onAfterExec);
                this.commands.on("exec", this.elasticTabstops.onExec);
                this.on("change", this.elasticTabstops.onChange);
            } else if (this.elasticTabstops) {
                this.commands.removeListener("afterExec", this.elasticTabstops.onAfterExec);
                this.commands.removeListener("exec", this.elasticTabstops.onExec);
                this.removeListener("change", this.elasticTabstops.onChange);
            }
        }
    }
});

});

define("ace/occur",["require","exports","module","ace/lib/oop","ace/range","ace/search","ace/edit_session","ace/search_highlight","ace/lib/dom"], function(require, exports, module) {
"use strict";

var oop = require("./lib/oop");
var Range = require("./range").Range;
var Search = require("./search").Search;
var EditSession = require("./edit_session").EditSession;
var SearchHighlight = require("./search_highlight").SearchHighlight;
function Occur() {}

oop.inherits(Occur, Search);

(function() {
    this.enter = function(editor, options) {
        if (!options.needle) return false;
        var pos = editor.getCursorPosition();
        this.displayOccurContent(editor, options);
        var translatedPos = this.originalToOccurPosition(editor.session, pos);
        editor.moveCursorToPosition(translatedPos);
        return true;
    };
    this.exit = function(editor, options) {
        var pos = options.translatePosition && editor.getCursorPosition();
        var translatedPos = pos && this.occurToOriginalPosition(editor.session, pos);
        this.displayOriginalContent(editor);
        if (translatedPos)
            editor.moveCursorToPosition(translatedPos);
        return true;
    };

    this.highlight = function(sess, regexp) {
        var hl = sess.$occurHighlight = sess.$occurHighlight || sess.addDynamicMarker(
                new SearchHighlight(null, "ace_occur-highlight", "text"));
        hl.setRegexp(regexp);
        sess._emit("changeBackMarker"); // force highlight layer redraw
    };

    this.displayOccurContent = function(editor, options) {
        this.$originalSession = editor.session;
        var found = this.matchingLines(editor.session, options);
        var lines = found.map(function(foundLine) { return foundLine.content; });
        var occurSession = new EditSession(lines.join('\n'));
        occurSession.$occur = this;
        occurSession.$occurMatchingLines = found;
        editor.setSession(occurSession);
        this.$useEmacsStyleLineStart = this.$originalSession.$useEmacsStyleLineStart;
        occurSession.$useEmacsStyleLineStart = this.$useEmacsStyleLineStart;
        this.highlight(occurSession, options.re);
        occurSession._emit('changeBackMarker');
    };

    this.displayOriginalContent = function(editor) {
        editor.setSession(this.$originalSession);
        this.$originalSession.$useEmacsStyleLineStart = this.$useEmacsStyleLineStart;
    };
    this.originalToOccurPosition = function(session, pos) {
        var lines = session.$occurMatchingLines;
        var nullPos = {row: 0, column: 0};
        if (!lines) return nullPos;
        for (var i = 0; i < lines.length; i++) {
            if (lines[i].row === pos.row)
                return {row: i, column: pos.column};
        }
        return nullPos;
    };
    this.occurToOriginalPosition = function(session, pos) {
        var lines = session.$occurMatchingLines;
        if (!lines || !lines[pos.row])
            return pos;
        return {row: lines[pos.row].row, column: pos.column};
    };

    this.matchingLines = function(session, options) {
        options = oop.mixin({}, options);
        if (!session || !options.needle) return [];
        var search = new Search();
        search.set(options);
        return search.findAll(session).reduce(function(lines, range) {
            var row = range.start.row;
            var last = lines[lines.length-1];
            return last && last.row === row ?
                lines :
                lines.concat({row: row, content: session.getLine(row)});
        }, []);
    };

}).call(Occur.prototype);

var dom = require('./lib/dom');
dom.importCssString(".ace_occur-highlight {\n\
    border-radius: 4px;\n\
    background-color: rgba(87, 255, 8, 0.25);\n\
    position: absolute;\n\
    z-index: 4;\n\
    box-sizing: border-box;\n\
    box-shadow: 0 0 4px rgb(91, 255, 50);\n\
}\n\
.ace_dark .ace_occur-highlight {\n\
    background-color: rgb(80, 140, 85);\n\
    box-shadow: 0 0 4px rgb(60, 120, 70);\n\
}\n", "incremental-occur-highlighting");

exports.Occur = Occur;

});

define("ace/commands/occur_commands",["require","exports","module","ace/config","ace/occur","ace/keyboard/hash_handler","ace/lib/oop"], function(require, exports, module) {

var config = require("../config"),
    Occur = require("../occur").Occur;
var occurStartCommand = {
    name: "occur",
    exec: function(editor, options) {
        var alreadyInOccur = !!editor.session.$occur;
        var occurSessionActive = new Occur().enter(editor, options);
        if (occurSessionActive && !alreadyInOccur)
            OccurKeyboardHandler.installIn(editor);
    },
    readOnly: true
};

var occurCommands = [{
    name: "occurexit",
    bindKey: 'esc|Ctrl-G',
    exec: function(editor) {
        var occur = editor.session.$occur;
        if (!occur) return;
        occur.exit(editor, {});
        if (!editor.session.$occur) OccurKeyboardHandler.uninstallFrom(editor);
    },
    readOnly: true
}, {
    name: "occuraccept",
    bindKey: 'enter',
    exec: function(editor) {
        var occur = editor.session.$occur;
        if (!occur) return;
        occur.exit(editor, {translatePosition: true});
        if (!editor.session.$occur) OccurKeyboardHandler.uninstallFrom(editor);
    },
    readOnly: true
}];

var HashHandler = require("../keyboard/hash_handler").HashHandler;
var oop = require("../lib/oop");


function OccurKeyboardHandler() {}

oop.inherits(OccurKeyboardHandler, HashHandler);

(function() {

    this.isOccurHandler = true;

    this.attach = function(editor) {
        HashHandler.call(this, occurCommands, editor.commands.platform);
        this.$editor = editor;
    };

    var handleKeyboard$super = this.handleKeyboard;
    this.handleKeyboard = function(data, hashId, key, keyCode) {
        var cmd = handleKeyboard$super.call(this, data, hashId, key, keyCode);
        return (cmd && cmd.command) ? cmd : undefined;
    };

}).call(OccurKeyboardHandler.prototype);

OccurKeyboardHandler.installIn = function(editor) {
    var handler = new this();
    editor.keyBinding.addKeyboardHandler(handler);
    editor.commands.addCommands(occurCommands);
};

OccurKeyboardHandler.uninstallFrom = function(editor) {
    editor.commands.removeCommands(occurCommands);
    var handler = editor.getKeyboardHandler();
    if (handler.isOccurHandler)
        editor.keyBinding.removeKeyboardHandler(handler);
};

exports.occurStartCommand = occurStartCommand;

});

define("ace/commands/incremental_search_commands",["require","exports","module","ace/config","ace/lib/oop","ace/keyboard/hash_handler","ace/commands/occur_commands"], function(require, exports, module) {

var config = require("../config");
var oop = require("../lib/oop");
var HashHandler = require("../keyboard/hash_handler").HashHandler;
var occurStartCommand = require("./occur_commands").occurStartCommand;
exports.iSearchStartCommands = [{
    name: "iSearch",
    bindKey: {win: "Ctrl-F", mac: "Command-F"},
    exec: function(editor, options) {
        config.loadModule(["core", "ace/incremental_search"], function(e) {
            var iSearch = e.iSearch = e.iSearch || new e.IncrementalSearch();
            iSearch.activate(editor, options.backwards);
            if (options.jumpToFirstMatch) iSearch.next(options);
        });
    },
    readOnly: true
}, {
    name: "iSearchBackwards",
    exec: function(editor, jumpToNext) { editor.execCommand('iSearch', {backwards: true}); },
    readOnly: true
}, {
    name: "iSearchAndGo",
    bindKey: {win: "Ctrl-K", mac: "Command-G"},
    exec: function(editor, jumpToNext) { editor.execCommand('iSearch', {jumpToFirstMatch: true, useCurrentOrPrevSearch: true}); },
    readOnly: true
}, {
    name: "iSearchBackwardsAndGo",
    bindKey: {win: "Ctrl-Shift-K", mac: "Command-Shift-G"},
    exec: function(editor) { editor.execCommand('iSearch', {jumpToFirstMatch: true, backwards: true, useCurrentOrPrevSearch: true}); },
    readOnly: true
}];
exports.iSearchCommands = [{
    name: "restartSearch",
    bindKey: {win: "Ctrl-F", mac: "Command-F"},
    exec: function(iSearch) {
        iSearch.cancelSearch(true);
    }
}, {
    name: "searchForward",
    bindKey: {win: "Ctrl-S|Ctrl-K", mac: "Ctrl-S|Command-G"},
    exec: function(iSearch, options) {
        options.useCurrentOrPrevSearch = true;
        iSearch.next(options);
    }
}, {
    name: "searchBackward",
    bindKey: {win: "Ctrl-R|Ctrl-Shift-K", mac: "Ctrl-R|Command-Shift-G"},
    exec: function(iSearch, options) {
        options.useCurrentOrPrevSearch = true;
        options.backwards = true;
        iSearch.next(options);
    }
}, {
    name: "extendSearchTerm",
    exec: function(iSearch, string) {
        iSearch.addString(string);
    }
}, {
    name: "extendSearchTermSpace",
    bindKey: "space",
    exec: function(iSearch) { iSearch.addString(' '); }
}, {
    name: "shrinkSearchTerm",
    bindKey: "backspace",
    exec: function(iSearch) {
        iSearch.removeChar();
    }
}, {
    name: 'confirmSearch',
    bindKey: 'return',
    exec: function(iSearch) { iSearch.deactivate(); }
}, {
    name: 'cancelSearch',
    bindKey: 'esc|Ctrl-G',
    exec: function(iSearch) { iSearch.deactivate(true); }
}, {
    name: 'occurisearch',
    bindKey: 'Ctrl-O',
    exec: function(iSearch) {
        var options = oop.mixin({}, iSearch.$options);
        iSearch.deactivate();
        occurStartCommand.exec(iSearch.$editor, options);
    }
}, {
    name: "yankNextWord",
    bindKey: "Ctrl-w",
    exec: function(iSearch) {
        var ed = iSearch.$editor,
            range = ed.selection.getRangeOfMovements(function(sel) { sel.moveCursorWordRight(); }),
            string = ed.session.getTextRange(range);
        iSearch.addString(string);
    }
}, {
    name: "yankNextChar",
    bindKey: "Ctrl-Alt-y",
    exec: function(iSearch) {
        var ed = iSearch.$editor,
            range = ed.selection.getRangeOfMovements(function(sel) { sel.moveCursorRight(); }),
            string = ed.session.getTextRange(range);
        iSearch.addString(string);
    }
}, {
    name: 'recenterTopBottom',
    bindKey: 'Ctrl-l',
    exec: function(iSearch) { iSearch.$editor.execCommand('recenterTopBottom'); }
}, {
    name: 'selectAllMatches',
    bindKey: 'Ctrl-space',
    exec: function(iSearch) {
        var ed = iSearch.$editor,
            hl = ed.session.$isearchHighlight,
            ranges = hl && hl.cache ? hl.cache
                .reduce(function(ranges, ea) {
                    return ranges.concat(ea ? ea : []); }, []) : [];
        iSearch.deactivate(false);
        ranges.forEach(ed.selection.addRange.bind(ed.selection));
    }
}, {
    name: 'searchAsRegExp',
    bindKey: 'Alt-r',
    exec: function(iSearch) {
        iSearch.convertNeedleToRegExp();
    }
}].map(function(cmd) {
    cmd.readOnly = true;
    cmd.isIncrementalSearchCommand = true;
    cmd.scrollIntoView = "animate-cursor";
    return cmd;
});

function IncrementalSearchKeyboardHandler(iSearch) {
    this.$iSearch = iSearch;
}

oop.inherits(IncrementalSearchKeyboardHandler, HashHandler);

(function() {

    this.attach = function(editor) {
        var iSearch = this.$iSearch;
        HashHandler.call(this, exports.iSearchCommands, editor.commands.platform);
        this.$commandExecHandler = editor.commands.addEventListener('exec', function(e) {
            if (!e.command.isIncrementalSearchCommand)
                return iSearch.deactivate();
            e.stopPropagation();
            e.preventDefault();
            var scrollTop = editor.session.getScrollTop();
            var result = e.command.exec(iSearch, e.args || {});
            editor.renderer.scrollCursorIntoView(null, 0.5);
            editor.renderer.animateScrolling(scrollTop);
            return result;
        });
    };

    this.detach = function(editor) {
        if (!this.$commandExecHandler) return;
        editor.commands.removeEventListener('exec', this.$commandExecHandler);
        delete this.$commandExecHandler;
    };

    var handleKeyboard$super = this.handleKeyboard;
    this.handleKeyboard = function(data, hashId, key, keyCode) {
        if (((hashId === 1/*ctrl*/ || hashId === 8/*command*/) && key === 'v')
         || (hashId === 1/*ctrl*/ && key === 'y')) return null;
        var cmd = handleKeyboard$super.call(this, data, hashId, key, keyCode);
        if (cmd.command) { return cmd; }
        if (hashId == -1) {
            var extendCmd = this.commands.extendSearchTerm;
            if (extendCmd) { return {command: extendCmd, args: key}; }
        }
        return false;
    };

}).call(IncrementalSearchKeyboardHandler.prototype);


exports.IncrementalSearchKeyboardHandler = IncrementalSearchKeyboardHandler;

});

define("ace/incremental_search",["require","exports","module","ace/lib/oop","ace/range","ace/search","ace/search_highlight","ace/commands/incremental_search_commands","ace/lib/dom","ace/commands/command_manager","ace/editor","ace/config"], function(require, exports, module) {
"use strict";

var oop = require("./lib/oop");
var Range = require("./range").Range;
var Search = require("./search").Search;
var SearchHighlight = require("./search_highlight").SearchHighlight;
var iSearchCommandModule = require("./commands/incremental_search_commands");
var ISearchKbd = iSearchCommandModule.IncrementalSearchKeyboardHandler;
function IncrementalSearch() {
    this.$options = {wrap: false, skipCurrent: false};
    this.$keyboardHandler = new ISearchKbd(this);
}

oop.inherits(IncrementalSearch, Search);
function isRegExp(obj) {
    return obj instanceof RegExp;
}

function regExpToObject(re) {
    var string = String(re),
        start = string.indexOf('/'),
        flagStart = string.lastIndexOf('/');
    return {
        expression: string.slice(start+1, flagStart),
        flags: string.slice(flagStart+1)
    };
}

function stringToRegExp(string, flags) {
    try {
        return new RegExp(string, flags);
    } catch (e) { return string; }
}

function objectToRegExp(obj) {
    return stringToRegExp(obj.expression, obj.flags);
}
(function() {

    this.activate = function(ed, backwards) {
        this.$editor = ed;
        this.$startPos = this.$currentPos = ed.getCursorPosition();
        this.$options.needle = '';
        this.$options.backwards = backwards;
        ed.keyBinding.addKeyboardHandler(this.$keyboardHandler);
        this.$originalEditorOnPaste = ed.onPaste; ed.onPaste = this.onPaste.bind(this);
        this.$mousedownHandler = ed.addEventListener('mousedown', this.onMouseDown.bind(this));
        this.selectionFix(ed);
        this.statusMessage(true);
    };

    this.deactivate = function(reset) {
        this.cancelSearch(reset);
        var ed = this.$editor;
        ed.keyBinding.removeKeyboardHandler(this.$keyboardHandler);
        if (this.$mousedownHandler) {
            ed.removeEventListener('mousedown', this.$mousedownHandler);
            delete this.$mousedownHandler;
        }
        ed.onPaste = this.$originalEditorOnPaste;
        this.message('');
    };

    this.selectionFix = function(editor) {
        if (editor.selection.isEmpty() && !editor.session.$emacsMark) {
            editor.clearSelection();
        }
    };

    this.highlight = function(regexp) {
        var sess = this.$editor.session,
            hl = sess.$isearchHighlight = sess.$isearchHighlight || sess.addDynamicMarker(
                new SearchHighlight(null, "ace_isearch-result", "text"));
        hl.setRegexp(regexp);
        sess._emit("changeBackMarker"); // force highlight layer redraw
    };

    this.cancelSearch = function(reset) {
        var e = this.$editor;
        this.$prevNeedle = this.$options.needle;
        this.$options.needle = '';
        if (reset) {
            e.moveCursorToPosition(this.$startPos);
            this.$currentPos = this.$startPos;
        } else {
            e.pushEmacsMark && e.pushEmacsMark(this.$startPos, false);
        }
        this.highlight(null);
        return Range.fromPoints(this.$currentPos, this.$currentPos);
    };

    this.highlightAndFindWithNeedle = function(moveToNext, needleUpdateFunc) {
        if (!this.$editor) return null;
        var options = this.$options;
        if (needleUpdateFunc) {
            options.needle = needleUpdateFunc.call(this, options.needle || '') || '';
        }
        if (options.needle.length === 0) {
            this.statusMessage(true);
            return this.cancelSearch(true);
        }
        options.start = this.$currentPos;
        var session = this.$editor.session,
            found = this.find(session),
            shouldSelect = this.$editor.emacsMark ?
                !!this.$editor.emacsMark() : !this.$editor.selection.isEmpty();
        if (found) {
            if (options.backwards) found = Range.fromPoints(found.end, found.start);
            this.$editor.selection.setRange(Range.fromPoints(shouldSelect ? this.$startPos : found.end, found.end));
            if (moveToNext) this.$currentPos = found.end;
            this.highlight(options.re);
        }

        this.statusMessage(found);

        return found;
    };

    this.addString = function(s) {
        return this.highlightAndFindWithNeedle(false, function(needle) {
            if (!isRegExp(needle))
              return needle + s;
            var reObj = regExpToObject(needle);
            reObj.expression += s;
            return objectToRegExp(reObj);
        });
    };

    this.removeChar = function(c) {
        return this.highlightAndFindWithNeedle(false, function(needle) {
            if (!isRegExp(needle))
              return needle.substring(0, needle.length-1);
            var reObj = regExpToObject(needle);
            reObj.expression = reObj.expression.substring(0, reObj.expression.length-1);
            return objectToRegExp(reObj);
        });
    };

    this.next = function(options) {
        options = options || {};
        this.$options.backwards = !!options.backwards;
        this.$currentPos = this.$editor.getCursorPosition();
        return this.highlightAndFindWithNeedle(true, function(needle) {
            return options.useCurrentOrPrevSearch && needle.length === 0 ?
                this.$prevNeedle || '' : needle;
        });
    };

    this.onMouseDown = function(evt) {
        this.deactivate();
        return true;
    };

    this.onPaste = function(text) {
        this.addString(text);
    };

    this.convertNeedleToRegExp = function() {
        return this.highlightAndFindWithNeedle(false, function(needle) {
            return isRegExp(needle) ? needle : stringToRegExp(needle, 'ig');
        });
    };

    this.convertNeedleToString = function() {
        return this.highlightAndFindWithNeedle(false, function(needle) {
            return isRegExp(needle) ? regExpToObject(needle).expression : needle;
        });
    };

    this.statusMessage = function(found) {
        var options = this.$options, msg = '';
        msg += options.backwards ? 'reverse-' : '';
        msg += 'isearch: ' + options.needle;
        msg += found ? '' : ' (not found)';
        this.message(msg);
    };

    this.message = function(msg) {
        if (this.$editor.showCommandLine) {
            this.$editor.showCommandLine(msg);
            this.$editor.focus();
        } else {
            console.log(msg);
        }
    };

}).call(IncrementalSearch.prototype);


exports.IncrementalSearch = IncrementalSearch;

var dom = require('./lib/dom');
dom.importCssString && dom.importCssString("\
.ace_marker-layer .ace_isearch-result {\
  position: absolute;\
  z-index: 6;\
  box-sizing: border-box;\
}\
div.ace_isearch-result {\
  border-radius: 4px;\
  background-color: rgba(255, 200, 0, 0.5);\
  box-shadow: 0 0 4px rgb(255, 200, 0);\
}\
.ace_dark div.ace_isearch-result {\
  background-color: rgb(100, 110, 160);\
  box-shadow: 0 0 4px rgb(80, 90, 140);\
}", "incremental-search-highlighting");
var commands = require("./commands/command_manager");
(function() {
    this.setupIncrementalSearch = function(editor, val) {
        if (this.usesIncrementalSearch == val) return;
        this.usesIncrementalSearch = val;
        var iSearchCommands = iSearchCommandModule.iSearchStartCommands;
        var method = val ? 'addCommands' : 'removeCommands';
        this[method](iSearchCommands);
    };
}).call(commands.CommandManager.prototype);
var Editor = require("./editor").Editor;
require("./config").defineOptions(Editor.prototype, "editor", {
    useIncrementalSearch: {
        set: function(val) {
            this.keyBinding.$handlers.forEach(function(handler) {
                if (handler.setupIncrementalSearch) {
                    handler.setupIncrementalSearch(this, val);
                }
            });
            this._emit('incrementalSearchSettingChanged', {isEnabled: val});
        }
    }
});

});

define("kitchen-sink/token_tooltip",["require","exports","module","ace/lib/dom","ace/lib/oop","ace/lib/event","ace/range","ace/tooltip"], function(require, exports, module) {
"use strict";

var dom = require("ace/lib/dom");
var oop = require("ace/lib/oop");
var event = require("ace/lib/event");
var Range = require("ace/range").Range;
var Tooltip = require("ace/tooltip").Tooltip;

function TokenTooltip (editor) {
    if (editor.tokenTooltip)
        return;
    Tooltip.call(this, editor.container);
    editor.tokenTooltip = this;
    this.editor = editor;

    this.update = this.update.bind(this);
    this.onMouseMove = this.onMouseMove.bind(this);
    this.onMouseOut = this.onMouseOut.bind(this);
    event.addListener(editor.renderer.scroller, "mousemove", this.onMouseMove);
    event.addListener(editor.renderer.content, "mouseout", this.onMouseOut);
}

oop.inherits(TokenTooltip, Tooltip);

(function(){
    this.token = {};
    this.range = new Range();
    
    this.update = function() {
        this.$timer = null;
        
        var r = this.editor.renderer;
        if (this.lastT - (r.timeStamp || 0) > 1000) {
            r.rect = null;
            r.timeStamp = this.lastT;
            this.maxHeight = window.innerHeight;
            this.maxWidth = window.innerWidth;
        }

        var canvasPos = r.rect || (r.rect = r.scroller.getBoundingClientRect());
        var offset = (this.x + r.scrollLeft - canvasPos.left - r.$padding) / r.characterWidth;
        var row = Math.floor((this.y + r.scrollTop - canvasPos.top) / r.lineHeight);
        var col = Math.round(offset);

        var screenPos = {row: row, column: col, side: offset - col > 0 ? 1 : -1};
        var session = this.editor.session;
        var docPos = session.screenToDocumentPosition(screenPos.row, screenPos.column);
        var token = session.getTokenAt(docPos.row, docPos.column);

        if (!token && !session.getLine(docPos.row)) {
            token = {
                type: "",
                value: "",
                state: session.bgTokenizer.getState(0)
            };
        }
        if (!token) {
            session.removeMarker(this.marker);
            this.hide();
            return;
        }

        var tokenText = token.type;
        if (token.state)
            tokenText += "|" + token.state;
        if (token.merge)
            tokenText += "\n  merge";
        if (token.stateTransitions)
            tokenText += "\n  " + token.stateTransitions.join("\n  ");

        if (this.tokenText != tokenText) {
            this.setText(tokenText);
            this.width = this.getWidth();
            this.height = this.getHeight();
            this.tokenText = tokenText;
        }

        this.show(null, this.x, this.y);

        this.token = token;
        session.removeMarker(this.marker);
        this.range = new Range(docPos.row, token.start, docPos.row, token.start + token.value.length);
        this.marker = session.addMarker(this.range, "ace_bracket", "text");
    };
    
    this.onMouseMove = function(e) {
        this.x = e.clientX;
        this.y = e.clientY;
        if (this.isOpen) {
            this.lastT = e.timeStamp;
            this.setPosition(this.x, this.y);
        }
        if (!this.$timer)
            this.$timer = setTimeout(this.update, 100);
    };

    this.onMouseOut = function(e) {
        if (e && e.currentTarget.contains(e.relatedTarget))
            return;
        this.hide();
        this.editor.session.removeMarker(this.marker);
        this.$timer = clearTimeout(this.$timer);
    };

    this.setPosition = function(x, y) {
        if (x + 10 + this.width > this.maxWidth)
            x = window.innerWidth - this.width - 10;
        if (y > window.innerHeight * 0.75 || y + 20 + this.height > this.maxHeight)
            y = y - this.height - 30;

        Tooltip.prototype.setPosition.call(this, x + 10, y + 20);
    };

    this.destroy = function() {
        this.onMouseOut();
        event.removeListener(this.editor.renderer.scroller, "mousemove", this.onMouseMove);
        event.removeListener(this.editor.renderer.content, "mouseout", this.onMouseOut);
        delete this.editor.tokenTooltip;
    };

}).call(TokenTooltip.prototype);

exports.TokenTooltip = TokenTooltip;

});

define("ace/split",["require","exports","module","ace/lib/oop","ace/lib/lang","ace/lib/event_emitter","ace/editor","ace/virtual_renderer","ace/edit_session"], function(require, exports, module) {
"use strict";

var oop = require("./lib/oop");
var lang = require("./lib/lang");
var EventEmitter = require("./lib/event_emitter").EventEmitter;

var Editor = require("./editor").Editor;
var Renderer = require("./virtual_renderer").VirtualRenderer;
var EditSession = require("./edit_session").EditSession;


var Split = function(container, theme, splits) {
    this.BELOW = 1;
    this.BESIDE = 0;

    this.$container = container;
    this.$theme = theme;
    this.$splits = 0;
    this.$editorCSS = "";
    this.$editors = [];
    this.$orientation = this.BESIDE;

    this.setSplits(splits || 1);
    this.$cEditor = this.$editors[0];


    this.on("focus", function(editor) {
        this.$cEditor = editor;
    }.bind(this));
};

(function(){

    oop.implement(this, EventEmitter);

    this.$createEditor = function() {
        var el = document.createElement("div");
        el.className = this.$editorCSS;
        el.style.cssText = "position: absolute; top:0px; bottom:0px";
        this.$container.appendChild(el);
        var editor = new Editor(new Renderer(el, this.$theme));

        editor.on("focus", function() {
            this._emit("focus", editor);
        }.bind(this));

        this.$editors.push(editor);
        editor.setFontSize(this.$fontSize);
        return editor;
    };

    this.setSplits = function(splits) {
        var editor;
        if (splits < 1) {
            throw "The number of splits have to be > 0!";
        }

        if (splits == this.$splits) {
            return;
        } else if (splits > this.$splits) {
            while (this.$splits < this.$editors.length && this.$splits < splits) {
                editor = this.$editors[this.$splits];
                this.$container.appendChild(editor.container);
                editor.setFontSize(this.$fontSize);
                this.$splits ++;
            }
            while (this.$splits < splits) {
                this.$createEditor();
                this.$splits ++;
            }
        } else {
            while (this.$splits > splits) {
                editor = this.$editors[this.$splits - 1];
                this.$container.removeChild(editor.container);
                this.$splits --;
            }
        }
        this.resize();
    };
    this.getSplits = function() {
        return this.$splits;
    };
    this.getEditor = function(idx) {
        return this.$editors[idx];
    };
    this.getCurrentEditor = function() {
        return this.$cEditor;
    };
    this.focus = function() {
        this.$cEditor.focus();
    };
    this.blur = function() {
        this.$cEditor.blur();
    };
    this.setTheme = function(theme) {
        this.$editors.forEach(function(editor) {
            editor.setTheme(theme);
        });
    };
    this.setKeyboardHandler = function(keybinding) {
        this.$editors.forEach(function(editor) {
            editor.setKeyboardHandler(keybinding);
        });
    };
    this.forEach = function(callback, scope) {
        this.$editors.forEach(callback, scope);
    };


    this.$fontSize = "";
    this.setFontSize = function(size) {
        this.$fontSize = size;
        this.forEach(function(editor) {
           editor.setFontSize(size);
        });
    };

    this.$cloneSession = function(session) {
        var s = new EditSession(session.getDocument(), session.getMode());

        var undoManager = session.getUndoManager();
        s.setUndoManager(undoManager);
        s.setTabSize(session.getTabSize());
        s.setUseSoftTabs(session.getUseSoftTabs());
        s.setOverwrite(session.getOverwrite());
        s.setBreakpoints(session.getBreakpoints());
        s.setUseWrapMode(session.getUseWrapMode());
        s.setUseWorker(session.getUseWorker());
        s.setWrapLimitRange(session.$wrapLimitRange.min,
                            session.$wrapLimitRange.max);
        s.$foldData = session.$cloneFoldData();

        return s;
    };
    this.setSession = function(session, idx) {
        var editor;
        if (idx == null) {
            editor = this.$cEditor;
        } else {
            editor = this.$editors[idx];
        }
        var isUsed = this.$editors.some(function(editor) {
           return editor.session === session;
        });

        if (isUsed) {
            session = this.$cloneSession(session);
        }
        editor.setSession(session);
        return session;
    };
    this.getOrientation = function() {
        return this.$orientation;
    };
    this.setOrientation = function(orientation) {
        if (this.$orientation == orientation) {
            return;
        }
        this.$orientation = orientation;
        this.resize();
    };
    this.resize = function() {
        var width = this.$container.clientWidth;
        var height = this.$container.clientHeight;
        var editor;

        if (this.$orientation == this.BESIDE) {
            var editorWidth = width / this.$splits;
            for (var i = 0; i < this.$splits; i++) {
                editor = this.$editors[i];
                editor.container.style.width = editorWidth + "px";
                editor.container.style.top = "0px";
                editor.container.style.left = i * editorWidth + "px";
                editor.container.style.height = height + "px";
                editor.resize();
            }
        } else {
            var editorHeight = height / this.$splits;
            for (var i = 0; i < this.$splits; i++) {
                editor = this.$editors[i];
                editor.container.style.width = width + "px";
                editor.container.style.top = i * editorHeight + "px";
                editor.container.style.left = "0px";
                editor.container.style.height = editorHeight + "px";
                editor.resize();
            }
        }
    };

}).call(Split.prototype);

exports.Split = Split;
});

define("ace/ext/menu_tools/overlay_page",["require","exports","module","ace/lib/dom"], function(require, exports, module) {
'use strict';
var dom = require("../../lib/dom");
var cssText = "#ace_settingsmenu, #kbshortcutmenu {\
background-color: #F7F7F7;\
color: black;\
box-shadow: -5px 4px 5px rgba(126, 126, 126, 0.55);\
padding: 1em 0.5em 2em 1em;\
overflow: auto;\
position: absolute;\
margin: 0;\
bottom: 0;\
right: 0;\
top: 0;\
z-index: 9991;\
cursor: default;\
}\
.ace_dark #ace_settingsmenu, .ace_dark #kbshortcutmenu {\
box-shadow: -20px 10px 25px rgba(126, 126, 126, 0.25);\
background-color: rgba(255, 255, 255, 0.6);\
color: black;\
}\
.ace_optionsMenuEntry:hover {\
background-color: rgba(100, 100, 100, 0.1);\
transition: all 0.3s\
}\
.ace_closeButton {\
background: rgba(245, 146, 146, 0.5);\
border: 1px solid #F48A8A;\
border-radius: 50%;\
padding: 7px;\
position: absolute;\
right: -8px;\
top: -8px;\
z-index: 100000;\
}\
.ace_closeButton{\
background: rgba(245, 146, 146, 0.9);\
}\
.ace_optionsMenuKey {\
color: darkslateblue;\
font-weight: bold;\
}\
.ace_optionsMenuCommand {\
color: darkcyan;\
font-weight: normal;\
}\
.ace_optionsMenuEntry input, .ace_optionsMenuEntry button {\
vertical-align: middle;\
}\
.ace_optionsMenuEntry button[ace_selected_button=true] {\
background: #e7e7e7;\
box-shadow: 1px 0px 2px 0px #adadad inset;\
border-color: #adadad;\
}\
.ace_optionsMenuEntry button {\
background: white;\
border: 1px solid lightgray;\
margin: 0px;\
}\
.ace_optionsMenuEntry button:hover{\
background: #f0f0f0;\
}";
dom.importCssString(cssText);
module.exports.overlayPage = function overlayPage(editor, contentElement, top, right, bottom, left) {
    top = top ? 'top: ' + top + ';' : '';
    bottom = bottom ? 'bottom: ' + bottom + ';' : '';
    right = right ? 'right: ' + right + ';' : '';
    left = left ? 'left: ' + left + ';' : '';

    var closer = document.createElement('div');
    var contentContainer = document.createElement('div');

    function documentEscListener(e) {
        if (e.keyCode === 27) {
            closer.click();
        }
    }

    closer.style.cssText = 'margin: 0; padding: 0; ' +
        'position: fixed; top:0; bottom:0; left:0; right:0;' +
        'z-index: 9990; ' +
        'background-color: rgba(0, 0, 0, 0.3);';
    closer.addEventListener('click', function() {
        document.removeEventListener('keydown', documentEscListener);
        closer.parentNode.removeChild(closer);
        editor.focus();
        closer = null;
    });
    document.addEventListener('keydown', documentEscListener);

    contentContainer.style.cssText = top + right + bottom + left;
    contentContainer.addEventListener('click', function(e) {
        e.stopPropagation();
    });

    var wrapper = dom.createElement("div");
    wrapper.style.position = "relative";
    
    var closeButton = dom.createElement("div");
    closeButton.className = "ace_closeButton";
    closeButton.addEventListener('click', function() {
        closer.click();
    });
    
    wrapper.appendChild(closeButton);
    contentContainer.appendChild(wrapper);
    
    contentContainer.appendChild(contentElement);
    closer.appendChild(contentContainer);
    document.body.appendChild(closer);
    editor.blur();
};

});

define("ace/ext/themelist",["require","exports","module","ace/lib/fixoldbrowsers"], function(require, exports, module) {
"use strict";
require("ace/lib/fixoldbrowsers");

var themeData = [
    ["Chrome"         ],
    ["Clouds"         ],
    ["Crimson Editor" ],
    ["Dawn"           ],
    ["Dreamweaver"    ],
    ["Eclipse"        ],
    ["GitHub"         ],
    ["IPlastic"       ],
    ["Solarized Light"],
    ["TextMate"       ],
    ["Tomorrow"       ],
    ["XCode"          ],
    ["Kuroir"],
    ["KatzenMilch"],
    ["SQL Server"           ,"sqlserver"               , "light"],
    ["Ambiance"             ,"ambiance"                ,  "dark"],
    ["Chaos"                ,"chaos"                   ,  "dark"],
    ["Clouds Midnight"      ,"clouds_midnight"         ,  "dark"],
    ["Dracula"              ,""                        ,  "dark"],
    ["Cobalt"               ,"cobalt"                  ,  "dark"],
    ["Gruvbox"              ,"gruvbox"                 ,  "dark"],
    ["Green on Black"       ,"gob"                     ,  "dark"],
    ["idle Fingers"         ,"idle_fingers"            ,  "dark"],
    ["krTheme"              ,"kr_theme"                ,  "dark"],
    ["Merbivore"            ,"merbivore"               ,  "dark"],
    ["Merbivore Soft"       ,"merbivore_soft"          ,  "dark"],
    ["Mono Industrial"      ,"mono_industrial"         ,  "dark"],
    ["Monokai"              ,"monokai"                 ,  "dark"],
    ["Pastel on dark"       ,"pastel_on_dark"          ,  "dark"],
    ["Solarized Dark"       ,"solarized_dark"          ,  "dark"],
    ["Terminal"             ,"terminal"                ,  "dark"],
    ["Tomorrow Night"       ,"tomorrow_night"          ,  "dark"],
    ["Tomorrow Night Blue"  ,"tomorrow_night_blue"     ,  "dark"],
    ["Tomorrow Night Bright","tomorrow_night_bright"   ,  "dark"],
    ["Tomorrow Night 80s"   ,"tomorrow_night_eighties" ,  "dark"],
    ["Twilight"             ,"twilight"                ,  "dark"],
    ["Vibrant Ink"          ,"vibrant_ink"             ,  "dark"]
];


exports.themesByName = {};
exports.themes = themeData.map(function(data) {
    var name = data[1] || data[0].replace(/ /g, "_").toLowerCase();
    var theme = {
        caption: data[0],
        theme: "ace/theme/" + name,
        isDark: data[2] == "dark",
        name: name
    };
    exports.themesByName[name] = theme;
    return theme;
});

});

define("ace/ext/options",["require","exports","module","ace/ext/menu_tools/overlay_page","ace/lib/dom","ace/lib/oop","ace/lib/event_emitter","ace/ext/modelist","ace/ext/themelist"], function(require, exports, module) {
"use strict";
var overlayPage = require('./menu_tools/overlay_page').overlayPage;

 
var dom = require("../lib/dom");
var oop = require("../lib/oop");
var EventEmitter = require("../lib/event_emitter").EventEmitter;
var buildDom = dom.buildDom;

var modelist = require("./modelist");
var themelist = require("./themelist");

var themes = { Bright: [], Dark: [] };
themelist.themes.forEach(function(x) {
    themes[x.isDark ? "Dark" : "Bright"].push({ caption: x.caption, value: x.theme });
});

var modes = modelist.modes.map(function(x){ 
    return { caption: x.caption, value: x.mode }; 
});


var optionGroups = {
    Main: {
        Mode: {
            path: "mode",
            type: "select",
            items: modes
        },
        Theme: {
            path: "theme",
            type: "select",
            items: themes
        },
        "Keybinding": {
            type: "buttonBar",
            path: "keyboardHandler",
            items: [
                { caption : "Ace", value : null },
                { caption : "Vim", value : "ace/keyboard/vim" },
                { caption : "Emacs", value : "ace/keyboard/emacs" }
            ]
        },
        "Font Size": {
            path: "fontSize",
            type: "number",
            defaultValue: 12,
            defaults: [
                {caption: "12px", value: 12},
                {caption: "24px", value: 24}
            ]
        },
        "Soft Wrap": {
            type: "buttonBar",
            path: "wrap",
            items: [
               { caption : "Off",  value : "off" },
               { caption : "View", value : "free" },
               { caption : "margin", value : "printMargin" },
               { caption : "40",   value : "40" }
            ]
        },
        "Cursor Style": {
            path: "cursorStyle",
            items: [
               { caption : "Ace",    value : "ace" },
               { caption : "Slim",   value : "slim" },
               { caption : "Smooth", value : "smooth" },
               { caption : "Smooth And Slim", value : "smooth slim" },
               { caption : "Wide",   value : "wide" }
            ]
        },
        "Folding": {
            path: "foldStyle",
            items: [
                { caption : "Manual", value : "manual" },
                { caption : "Mark begin", value : "markbegin" },
                { caption : "Mark begin and end", value : "markbeginend" }
            ]
        },
        "Soft Tabs": [{
            path: "useSoftTabs"
        }, {
            path: "tabSize",
            type: "number",
            values: [2, 3, 4, 8, 16]
        }],
        "Overscroll": {
            type: "buttonBar",
            path: "scrollPastEnd",
            items: [
               { caption : "None",  value : 0 },
               { caption : "Half",   value : 0.5 },
               { caption : "Full",   value : 1 }
            ]
        }
    },
    More: {
        "Atomic soft tabs": {
            path: "navigateWithinSoftTabs"
        },
        "Enable Behaviours": {
            path: "behavioursEnabled"
        },
        "Full Line Selection": {
            type: "checkbox",
            values: "text|line",
            path: "selectionStyle"
        },
        "Highlight Active Line": {
            path: "highlightActiveLine"
        },
        "Show Invisibles": {
            path: "showInvisibles"
        },
        "Show Indent Guides": {
            path: "displayIndentGuides"
        },
        "Persistent Scrollbar": [{
            path: "hScrollBarAlwaysVisible"
        }, {
            path: "vScrollBarAlwaysVisible"
        }],
        "Animate scrolling": {
            path: "animatedScroll"
        },
        "Show Gutter": {
            path: "showGutter"
        },
        "Show Line Numbers": {
            path: "showLineNumbers"
        },
        "Relative Line Numbers": {
            path: "relativeLineNumbers"
        },
        "Fixed Gutter Width": {
            path: "fixedWidthGutter"
        },
        "Show Print Margin": [{
            path: "showPrintMargin"
        }, {
            type: "number",
            path: "printMarginColumn"
        }],
        "Indented Soft Wrap": {
            path: "indentedSoftWrap"
        },
        "Highlight selected word": {
            path: "highlightSelectedWord"
        },
        "Fade Fold Widgets": {
            path: "fadeFoldWidgets"
        },
        "Use textarea for IME": {
            path: "useTextareaForIME"
        },
        "Merge Undo Deltas": {
            path: "mergeUndoDeltas",
            items: [
               { caption : "Always",  value : "always" },
               { caption : "Never",   value : "false" },
               { caption : "Timed",   value : "true" }
            ]
        },
        "Elastic Tabstops": {
            path: "useElasticTabstops"
        },
        "Incremental Search": {
            path: "useIncrementalSearch"
        },
        "Read-only": {
            path: "readOnly"
        },
        "Copy without selection": {
            path: "copyWithEmptySelection"
        },
        "Live Autocompletion": {
            path: "enableLiveAutocompletion"
        }
    }
};


var OptionPanel = function(editor, element) {
    this.editor = editor;
    this.container = element || document.createElement("div");
    this.groups = [];
    this.options = {};
};

(function() {
    
    oop.implement(this, EventEmitter);
    
    this.add = function(config) {
        if (config.Main)
            oop.mixin(optionGroups.Main, config.Main);
        if (config.More)
            oop.mixin(optionGroups.More, config.More);
    };
    
    this.render = function() {
        this.container.innerHTML = "";
        buildDom(["table", {id: "controls"}, 
            this.renderOptionGroup(optionGroups.Main),
            ["tr", null, ["td", {colspan: 2},
                ["table", {id: "more-controls"}, 
                    this.renderOptionGroup(optionGroups.More)
                ]
            ]]
        ], this.container);
    };
    
    this.renderOptionGroup = function(group) {
        return Object.keys(group).map(function(key, i) {
            var item = group[key];
            if (!item.position)
                item.position = i / 10000;
            if (!item.label)
                item.label = key;
            return item;
        }).sort(function(a, b) {
            return a.position - b.position;
        }).map(function(item) {
            return this.renderOption(item.label, item);
        }, this);
    };
    
    this.renderOptionControl = function(key, option) {
        var self = this;
        if (Array.isArray(option)) {
            return option.map(function(x) {
                return self.renderOptionControl(key, x);
            });
        }
        var control;
        
        var value = self.getOption(option);
        
        if (option.values && option.type != "checkbox") {
            if (typeof option.values == "string")
                option.values = option.values.split("|");
            option.items = option.values.map(function(v) {
                return { value: v, name: v };
            });
        }
        
        if (option.type == "buttonBar") {
            control = ["div", option.items.map(function(item) {
                return ["button", { 
                    value: item.value, 
                    ace_selected_button: value == item.value, 
                    onclick: function() {
                        self.setOption(option, item.value);
                        var nodes = this.parentNode.querySelectorAll("[ace_selected_button]");
                        for (var i = 0; i < nodes.length; i++) {
                            nodes[i].removeAttribute("ace_selected_button");
                        }
                        this.setAttribute("ace_selected_button", true);
                    } 
                }, item.desc || item.caption || item.name];
            })];
        } else if (option.type == "number") {
            control = ["input", {type: "number", value: value || option.defaultValue, style:"width:3em", oninput: function() {
                self.setOption(option, parseInt(this.value));
            }}];
            if (option.defaults) {
                control = [control, option.defaults.map(function(item) {
                    return ["button", {onclick: function() {
                        var input = this.parentNode.firstChild;
                        input.value = item.value;
                        input.oninput();
                    }}, item.caption];
                })];
            }
        } else if (option.items) {
            var buildItems = function(items) {
                return items.map(function(item) {
                    return ["option", { value: item.value || item.name }, item.desc || item.caption || item.name];
                });
            };
            
            var items = Array.isArray(option.items) 
                ? buildItems(option.items)
                : Object.keys(option.items).map(function(key) {
                    return ["optgroup", {"label": key}, buildItems(option.items[key])];
                });
            control = ["select", { id: key, value: value, onchange: function() {
                self.setOption(option, this.value);
            } }, items];
        } else {
            if (typeof option.values == "string")
                option.values = option.values.split("|");
            if (option.values) value = value == option.values[1];
            control = ["input", { type: "checkbox", id: key, checked: value || null, onchange: function() {
                var value = this.checked;
                if (option.values) value = option.values[value ? 1 : 0];
                self.setOption(option, value);
            }}];
            if (option.type == "checkedNumber") {
                control = [control, []];
            }
        }
        return control;
    };
    
    this.renderOption = function(key, option) {
        if (option.path && !option.onchange && !this.editor.$options[option.path])
            return;
        this.options[option.path] = option;
        var safeKey = "-" + option.path;
        var control = this.renderOptionControl(safeKey, option);
        return ["tr", {class: "ace_optionsMenuEntry"}, ["td",
            ["label", {for: safeKey}, key]
        ], ["td", control]];
    };
    
    this.setOption = function(option, value) {
        if (typeof option == "string")
            option = this.options[option];
        if (value == "false") value = false;
        if (value == "true") value = true;
        if (value == "null") value = null;
        if (value == "undefined") value = undefined;
        if (typeof value == "string" && parseFloat(value).toString() == value)
            value = parseFloat(value);
        if (option.onchange)
            option.onchange(value);
        else if (option.path)
            this.editor.setOption(option.path, value);
        this._signal("setOption", {name: option.path, value: value});
    };
    
    this.getOption = function(option) {
        if (option.getValue)
            return option.getValue();
        return this.editor.getOption(option.path);
    };
    
}).call(OptionPanel.prototype);

exports.OptionPanel = OptionPanel;

});

define("ace/ext/statusbar",["require","exports","module","ace/lib/dom","ace/lib/lang"], function(require, exports, module) {
"use strict";
var dom = require("ace/lib/dom");
var lang = require("ace/lib/lang");

var StatusBar = function(editor, parentNode) {
    this.element = dom.createElement("div");
    this.element.className = "ace_status-indicator";
    this.element.style.cssText = "display: inline-block;";
    parentNode.appendChild(this.element);

    var statusUpdate = lang.delayedCall(function(){
        this.updateStatus(editor);
    }.bind(this)).schedule.bind(null, 100);
    
    editor.on("changeStatus", statusUpdate);
    editor.on("changeSelection", statusUpdate);
    editor.on("keyboardActivity", statusUpdate);
};

(function(){
    this.updateStatus = function(editor) {
        var status = [];
        function add(str, separator) {
            str && status.push(str, separator || "|");
        }

        add(editor.keyBinding.getStatusText(editor));
        if (editor.commands.recording)
            add("REC");
        
        var sel = editor.selection;
        var c = sel.lead;
        
        if (!sel.isEmpty()) {
            var r = editor.getSelectionRange();
            add("(" + (r.end.row - r.start.row) + ":"  +(r.end.column - r.start.column) + ")", " ");
        }
        add(c.row + ":" + c.column, " ");        
        if (sel.rangeCount)
            add("[" + sel.rangeCount + "]", " ");
        status.pop();
        this.element.textContent = status.join("");
    };
}).call(StatusBar.prototype);

exports.StatusBar = StatusBar;

});

define("ace/snippets",["require","exports","module","ace/lib/oop","ace/lib/event_emitter","ace/lib/lang","ace/range","ace/anchor","ace/keyboard/hash_handler","ace/tokenizer","ace/lib/dom","ace/editor"], function(require, exports, module) {
"use strict";
var oop = require("./lib/oop");
var EventEmitter = require("./lib/event_emitter").EventEmitter;
var lang = require("./lib/lang");
var Range = require("./range").Range;
var Anchor = require("./anchor").Anchor;
var HashHandler = require("./keyboard/hash_handler").HashHandler;
var Tokenizer = require("./tokenizer").Tokenizer;
var comparePoints = Range.comparePoints;

var SnippetManager = function() {
    this.snippetMap = {};
    this.snippetNameMap = {};
};

(function() {
    oop.implement(this, EventEmitter);
    
    this.getTokenizer = function() {
        function TabstopToken(str, _, stack) {
            str = str.substr(1);
            if (/^\d+$/.test(str) && !stack.inFormatString)
                return [{tabstopId: parseInt(str, 10)}];
            return [{text: str}];
        }
        function escape(ch) {
            return "(?:[^\\\\" + ch + "]|\\\\.)";
        }
        SnippetManager.$tokenizer = new Tokenizer({
            start: [
                {regex: /:/, onMatch: function(val, state, stack) {
                    if (stack.length && stack[0].expectIf) {
                        stack[0].expectIf = false;
                        stack[0].elseBranch = stack[0];
                        return [stack[0]];
                    }
                    return ":";
                }},
                {regex: /\\./, onMatch: function(val, state, stack) {
                    var ch = val[1];
                    if (ch == "}" && stack.length) {
                        val = ch;
                    }else if ("`$\\".indexOf(ch) != -1) {
                        val = ch;
                    } else if (stack.inFormatString) {
                        if (ch == "n")
                            val = "\n";
                        else if (ch == "t")
                            val = "\n";
                        else if ("ulULE".indexOf(ch) != -1) {
                            val = {changeCase: ch, local: ch > "a"};
                        }
                    }

                    return [val];
                }},
                {regex: /}/, onMatch: function(val, state, stack) {
                    return [stack.length ? stack.shift() : val];
                }},
                {regex: /\$(?:\d+|\w+)/, onMatch: TabstopToken},
                {regex: /\$\{[\dA-Z_a-z]+/, onMatch: function(str, state, stack) {
                    var t = TabstopToken(str.substr(1), state, stack);
                    stack.unshift(t[0]);
                    return t;
                }, next: "snippetVar"},
                {regex: /\n/, token: "newline", merge: false}
            ],
            snippetVar: [
                {regex: "\\|" + escape("\\|") + "*\\|", onMatch: function(val, state, stack) {
                    stack[0].choices = val.slice(1, -1).split(",");
                }, next: "start"},
                {regex: "/(" + escape("/") + "+)/(?:(" + escape("/") + "*)/)(\\w*):?",
                 onMatch: function(val, state, stack) {
                    var ts = stack[0];
                    ts.fmtString = val;

                    val = this.splitRegex.exec(val);
                    ts.guard = val[1];
                    ts.fmt = val[2];
                    ts.flag = val[3];
                    return "";
                }, next: "start"},
                {regex: "`" + escape("`") + "*`", onMatch: function(val, state, stack) {
                    stack[0].code = val.splice(1, -1);
                    return "";
                }, next: "start"},
                {regex: "\\?", onMatch: function(val, state, stack) {
                    if (stack[0])
                        stack[0].expectIf = true;
                }, next: "start"},
                {regex: "([^:}\\\\]|\\\\.)*:?", token: "", next: "start"}
            ],
            formatString: [
                {regex: "/(" + escape("/") + "+)/", token: "regex"},
                {regex: "", onMatch: function(val, state, stack) {
                    stack.inFormatString = true;
                }, next: "start"}
            ]
        });
        SnippetManager.prototype.getTokenizer = function() {
            return SnippetManager.$tokenizer;
        };
        return SnippetManager.$tokenizer;
    };

    this.tokenizeTmSnippet = function(str, startState) {
        return this.getTokenizer().getLineTokens(str, startState).tokens.map(function(x) {
            return x.value || x;
        });
    };

    this.$getDefaultValue = function(editor, name) {
        if (/^[A-Z]\d+$/.test(name)) {
            var i = name.substr(1);
            return (this.variables[name[0] + "__"] || {})[i];
        }
        if (/^\d+$/.test(name)) {
            return (this.variables.__ || {})[name];
        }
        name = name.replace(/^TM_/, "");

        if (!editor)
            return;
        var s = editor.session;
        switch(name) {
            case "CURRENT_WORD":
                var r = s.getWordRange(); 
            case "SELECTION":
            case "SELECTED_TEXT":
                return s.getTextRange(r);
            case "CURRENT_LINE":
                return s.getLine(editor.getCursorPosition().row);
            case "PREV_LINE": // not possible in textmate
                return s.getLine(editor.getCursorPosition().row - 1);
            case "LINE_INDEX":
                return editor.getCursorPosition().column;
            case "LINE_NUMBER":
                return editor.getCursorPosition().row + 1;
            case "SOFT_TABS":
                return s.getUseSoftTabs() ? "YES" : "NO";
            case "TAB_SIZE":
                return s.getTabSize();
            case "FILENAME":
            case "FILEPATH":
                return "";
            case "FULLNAME":
                return "Ace";
        }
    };
    this.variables = {};
    this.getVariableValue = function(editor, varName) {
        if (this.variables.hasOwnProperty(varName))
            return this.variables[varName](editor, varName) || "";
        return this.$getDefaultValue(editor, varName) || "";
    };
    this.tmStrFormat = function(str, ch, editor) {
        var flag = ch.flag || "";
        var re = ch.guard;
        re = new RegExp(re, flag.replace(/[^gi]/, ""));
        var fmtTokens = this.tokenizeTmSnippet(ch.fmt, "formatString");
        var _self = this;
        var formatted = str.replace(re, function() {
            _self.variables.__ = arguments;
            var fmtParts = _self.resolveVariables(fmtTokens, editor);
            var gChangeCase = "E";
            for (var i  = 0; i < fmtParts.length; i++) {
                var ch = fmtParts[i];
                if (typeof ch == "object") {
                    fmtParts[i] = "";
                    if (ch.changeCase && ch.local) {
                        var next = fmtParts[i + 1];
                        if (next && typeof next == "string") {
                            if (ch.changeCase == "u")
                                fmtParts[i] = next[0].toUpperCase();
                            else
                                fmtParts[i] = next[0].toLowerCase();
                            fmtParts[i + 1] = next.substr(1);
                        }
                    } else if (ch.changeCase) {
                        gChangeCase = ch.changeCase;
                    }
                } else if (gChangeCase == "U") {
                    fmtParts[i] = ch.toUpperCase();
                } else if (gChangeCase == "L") {
                    fmtParts[i] = ch.toLowerCase();
                }
            }
            return fmtParts.join("");
        });
        this.variables.__ = null;
        return formatted;
    };

    this.resolveVariables = function(snippet, editor) {
        var result = [];
        for (var i = 0; i < snippet.length; i++) {
            var ch = snippet[i];
            if (typeof ch == "string") {
                result.push(ch);
            } else if (typeof ch != "object") {
                continue;
            } else if (ch.skip) {
                gotoNext(ch);
            } else if (ch.processed < i) {
                continue;
            } else if (ch.text) {
                var value = this.getVariableValue(editor, ch.text);
                if (value && ch.fmtString)
                    value = this.tmStrFormat(value, ch);
                ch.processed = i;
                if (ch.expectIf == null) {
                    if (value) {
                        result.push(value);
                        gotoNext(ch);
                    }
                } else {
                    if (value) {
                        ch.skip = ch.elseBranch;
                    } else
                        gotoNext(ch);
                }
            } else if (ch.tabstopId != null) {
                result.push(ch);
            } else if (ch.changeCase != null) {
                result.push(ch);
            }
        }
        function gotoNext(ch) {
            var i1 = snippet.indexOf(ch, i + 1);
            if (i1 != -1)
                i = i1;
        }
        return result;
    };

    this.insertSnippetForSelection = function(editor, snippetText) {
        var cursor = editor.getCursorPosition();
        var line = editor.session.getLine(cursor.row);
        var tabString = editor.session.getTabString();
        var indentString = line.match(/^\s*/)[0];
        
        if (cursor.column < indentString.length)
            indentString = indentString.slice(0, cursor.column);

        snippetText = snippetText.replace(/\r/g, "");
        var tokens = this.tokenizeTmSnippet(snippetText);
        tokens = this.resolveVariables(tokens, editor);
        tokens = tokens.map(function(x) {
            if (x == "\n")
                return x + indentString;
            if (typeof x == "string")
                return x.replace(/\t/g, tabString);
            return x;
        });
        var tabstops = [];
        tokens.forEach(function(p, i) {
            if (typeof p != "object")
                return;
            var id = p.tabstopId;
            var ts = tabstops[id];
            if (!ts) {
                ts = tabstops[id] = [];
                ts.index = id;
                ts.value = "";
            }
            if (ts.indexOf(p) !== -1)
                return;
            ts.push(p);
            var i1 = tokens.indexOf(p, i + 1);
            if (i1 === -1)
                return;

            var value = tokens.slice(i + 1, i1);
            var isNested = value.some(function(t) {return typeof t === "object";});          
            if (isNested && !ts.value) {
                ts.value = value;
            } else if (value.length && (!ts.value || typeof ts.value !== "string")) {
                ts.value = value.join("");
            }
        });
        tabstops.forEach(function(ts) {ts.length = 0;});
        var expanding = {};
        function copyValue(val) {
            var copy = [];
            for (var i = 0; i < val.length; i++) {
                var p = val[i];
                if (typeof p == "object") {
                    if (expanding[p.tabstopId])
                        continue;
                    var j = val.lastIndexOf(p, i - 1);
                    p = copy[j] || {tabstopId: p.tabstopId};
                }
                copy[i] = p;
            }
            return copy;
        }
        for (var i = 0; i < tokens.length; i++) {
            var p = tokens[i];
            if (typeof p != "object")
                continue;
            var id = p.tabstopId;
            var i1 = tokens.indexOf(p, i + 1);
            if (expanding[id]) {
                if (expanding[id] === p)
                    expanding[id] = null;
                continue;
            }
            
            var ts = tabstops[id];
            var arg = typeof ts.value == "string" ? [ts.value] : copyValue(ts.value);
            arg.unshift(i + 1, Math.max(0, i1 - i));
            arg.push(p);
            expanding[id] = p;
            tokens.splice.apply(tokens, arg);

            if (ts.indexOf(p) === -1)
                ts.push(p);
        }
        var row = 0, column = 0;
        var text = "";
        tokens.forEach(function(t) {
            if (typeof t === "string") {
                var lines = t.split("\n");
                if (lines.length > 1){
                    column = lines[lines.length - 1].length;
                    row += lines.length - 1;
                } else
                    column += t.length;
                text += t;
            } else {
                if (!t.start)
                    t.start = {row: row, column: column};
                else
                    t.end = {row: row, column: column};
            }
        });
        var range = editor.getSelectionRange();
        var end = editor.session.replace(range, text);

        var tabstopManager = new TabstopManager(editor);
        var selectionId = editor.inVirtualSelectionMode && editor.selection.index;
        tabstopManager.addTabstops(tabstops, range.start, end, selectionId);
    };
    
    this.insertSnippet = function(editor, snippetText) {
        var self = this;
        if (editor.inVirtualSelectionMode)
            return self.insertSnippetForSelection(editor, snippetText);
        
        editor.forEachSelection(function() {
            self.insertSnippetForSelection(editor, snippetText);
        }, null, {keepOrder: true});
        
        if (editor.tabstopManager)
            editor.tabstopManager.tabNext();
    };

    this.$getScope = function(editor) {
        var scope = editor.session.$mode.$id || "";
        scope = scope.split("/").pop();
        if (scope === "html" || scope === "php") {
            if (scope === "php" && !editor.session.$mode.inlinePhp) 
                scope = "html";
            var c = editor.getCursorPosition();
            var state = editor.session.getState(c.row);
            if (typeof state === "object") {
                state = state[0];
            }
            if (state.substring) {
                if (state.substring(0, 3) == "js-")
                    scope = "javascript";
                else if (state.substring(0, 4) == "css-")
                    scope = "css";
                else if (state.substring(0, 4) == "php-")
                    scope = "php";
            }
        }
        
        return scope;
    };

    this.getActiveScopes = function(editor) {
        var scope = this.$getScope(editor);
        var scopes = [scope];
        var snippetMap = this.snippetMap;
        if (snippetMap[scope] && snippetMap[scope].includeScopes) {
            scopes.push.apply(scopes, snippetMap[scope].includeScopes);
        }
        scopes.push("_");
        return scopes;
    };

    this.expandWithTab = function(editor, options) {
        var self = this;
        var result = editor.forEachSelection(function() {
            return self.expandSnippetForSelection(editor, options);
        }, null, {keepOrder: true});
        if (result && editor.tabstopManager)
            editor.tabstopManager.tabNext();
        return result;
    };
    
    this.expandSnippetForSelection = function(editor, options) {
        var cursor = editor.getCursorPosition();
        var line = editor.session.getLine(cursor.row);
        var before = line.substring(0, cursor.column);
        var after = line.substr(cursor.column);

        var snippetMap = this.snippetMap;
        var snippet;
        this.getActiveScopes(editor).some(function(scope) {
            var snippets = snippetMap[scope];
            if (snippets)
                snippet = this.findMatchingSnippet(snippets, before, after);
            return !!snippet;
        }, this);
        if (!snippet)
            return false;
        if (options && options.dryRun)
            return true;
        editor.session.doc.removeInLine(cursor.row,
            cursor.column - snippet.replaceBefore.length,
            cursor.column + snippet.replaceAfter.length
        );

        this.variables.M__ = snippet.matchBefore;
        this.variables.T__ = snippet.matchAfter;
        this.insertSnippetForSelection(editor, snippet.content);

        this.variables.M__ = this.variables.T__ = null;
        return true;
    };

    this.findMatchingSnippet = function(snippetList, before, after) {
        for (var i = snippetList.length; i--;) {
            var s = snippetList[i];
            if (s.startRe && !s.startRe.test(before))
                continue;
            if (s.endRe && !s.endRe.test(after))
                continue;
            if (!s.startRe && !s.endRe)
                continue;

            s.matchBefore = s.startRe ? s.startRe.exec(before) : [""];
            s.matchAfter = s.endRe ? s.endRe.exec(after) : [""];
            s.replaceBefore = s.triggerRe ? s.triggerRe.exec(before)[0] : "";
            s.replaceAfter = s.endTriggerRe ? s.endTriggerRe.exec(after)[0] : "";
            return s;
        }
    };

    this.snippetMap = {};
    this.snippetNameMap = {};
    this.register = function(snippets, scope) {
        var snippetMap = this.snippetMap;
        var snippetNameMap = this.snippetNameMap;
        var self = this;
        
        if (!snippets) 
            snippets = [];
        
        function wrapRegexp(src) {
            if (src && !/^\^?\(.*\)\$?$|^\\b$/.test(src))
                src = "(?:" + src + ")";

            return src || "";
        }
        function guardedRegexp(re, guard, opening) {
            re = wrapRegexp(re);
            guard = wrapRegexp(guard);
            if (opening) {
                re = guard + re;
                if (re && re[re.length - 1] != "$")
                    re = re + "$";
            } else {
                re = re + guard;
                if (re && re[0] != "^")
                    re = "^" + re;
            }
            return new RegExp(re);
        }

        function addSnippet(s) {
            if (!s.scope)
                s.scope = scope || "_";
            scope = s.scope;
            if (!snippetMap[scope]) {
                snippetMap[scope] = [];
                snippetNameMap[scope] = {};
            }

            var map = snippetNameMap[scope];
            if (s.name) {
                var old = map[s.name];
                if (old)
                    self.unregister(old);
                map[s.name] = s;
            }
            snippetMap[scope].push(s);

            if (s.tabTrigger && !s.trigger) {
                if (!s.guard && /^\w/.test(s.tabTrigger))
                    s.guard = "\\b";
                s.trigger = lang.escapeRegExp(s.tabTrigger);
            }
            
            if (!s.trigger && !s.guard && !s.endTrigger && !s.endGuard)
                return;
            
            s.startRe = guardedRegexp(s.trigger, s.guard, true);
            s.triggerRe = new RegExp(s.trigger);

            s.endRe = guardedRegexp(s.endTrigger, s.endGuard, true);
            s.endTriggerRe = new RegExp(s.endTrigger);
        }

        if (snippets && snippets.content)
            addSnippet(snippets);
        else if (Array.isArray(snippets))
            snippets.forEach(addSnippet);
        
        this._signal("registerSnippets", {scope: scope});
    };
    this.unregister = function(snippets, scope) {
        var snippetMap = this.snippetMap;
        var snippetNameMap = this.snippetNameMap;

        function removeSnippet(s) {
            var nameMap = snippetNameMap[s.scope||scope];
            if (nameMap && nameMap[s.name]) {
                delete nameMap[s.name];
                var map = snippetMap[s.scope||scope];
                var i = map && map.indexOf(s);
                if (i >= 0)
                    map.splice(i, 1);
            }
        }
        if (snippets.content)
            removeSnippet(snippets);
        else if (Array.isArray(snippets))
            snippets.forEach(removeSnippet);
    };
    this.parseSnippetFile = function(str) {
        str = str.replace(/\r/g, "");
        var list = [], snippet = {};
        var re = /^#.*|^({[\s\S]*})\s*$|^(\S+) (.*)$|^((?:\n*\t.*)+)/gm;
        var m;
        while (m = re.exec(str)) {
            if (m[1]) {
                try {
                    snippet = JSON.parse(m[1]);
                    list.push(snippet);
                } catch (e) {}
            } if (m[4]) {
                snippet.content = m[4].replace(/^\t/gm, "");
                list.push(snippet);
                snippet = {};
            } else {
                var key = m[2], val = m[3];
                if (key == "regex") {
                    var guardRe = /\/((?:[^\/\\]|\\.)*)|$/g;
                    snippet.guard = guardRe.exec(val)[1];
                    snippet.trigger = guardRe.exec(val)[1];
                    snippet.endTrigger = guardRe.exec(val)[1];
                    snippet.endGuard = guardRe.exec(val)[1];
                } else if (key == "snippet") {
                    snippet.tabTrigger = val.match(/^\S*/)[0];
                    if (!snippet.name)
                        snippet.name = val;
                } else {
                    snippet[key] = val;
                }
            }
        }
        return list;
    };
    this.getSnippetByName = function(name, editor) {
        var snippetMap = this.snippetNameMap;
        var snippet;
        this.getActiveScopes(editor).some(function(scope) {
            var snippets = snippetMap[scope];
            if (snippets)
                snippet = snippets[name];
            return !!snippet;
        }, this);
        return snippet;
    };

}).call(SnippetManager.prototype);


var TabstopManager = function(editor) {
    if (editor.tabstopManager)
        return editor.tabstopManager;
    editor.tabstopManager = this;
    this.$onChange = this.onChange.bind(this);
    this.$onChangeSelection = lang.delayedCall(this.onChangeSelection.bind(this)).schedule;
    this.$onChangeSession = this.onChangeSession.bind(this);
    this.$onAfterExec = this.onAfterExec.bind(this);
    this.attach(editor);
};
(function() {
    this.attach = function(editor) {
        this.index = 0;
        this.ranges = [];
        this.tabstops = [];
        this.$openTabstops = null;
        this.selectedTabstop = null;

        this.editor = editor;
        this.editor.on("change", this.$onChange);
        this.editor.on("changeSelection", this.$onChangeSelection);
        this.editor.on("changeSession", this.$onChangeSession);
        this.editor.commands.on("afterExec", this.$onAfterExec);
        this.editor.keyBinding.addKeyboardHandler(this.keyboardHandler);
    };
    this.detach = function() {
        this.tabstops.forEach(this.removeTabstopMarkers, this);
        this.ranges = null;
        this.tabstops = null;
        this.selectedTabstop = null;
        this.editor.removeListener("change", this.$onChange);
        this.editor.removeListener("changeSelection", this.$onChangeSelection);
        this.editor.removeListener("changeSession", this.$onChangeSession);
        this.editor.commands.removeListener("afterExec", this.$onAfterExec);
        this.editor.keyBinding.removeKeyboardHandler(this.keyboardHandler);
        this.editor.tabstopManager = null;
        this.editor = null;
    };

    this.onChange = function(delta) {
        var changeRange = delta;
        var isRemove = delta.action[0] == "r";
        var start = delta.start;
        var end = delta.end;
        var startRow = start.row;
        var endRow = end.row;
        var lineDif = endRow - startRow;
        var colDiff = end.column - start.column;

        if (isRemove) {
            lineDif = -lineDif;
            colDiff = -colDiff;
        }
        if (!this.$inChange && isRemove) {
            var ts = this.selectedTabstop;
            var changedOutside = ts && !ts.some(function(r) {
                return comparePoints(r.start, start) <= 0 && comparePoints(r.end, end) >= 0;
            });
            if (changedOutside)
                return this.detach();
        }
        var ranges = this.ranges;
        for (var i = 0; i < ranges.length; i++) {
            var r = ranges[i];
            if (r.end.row < start.row)
                continue;

            if (isRemove && comparePoints(start, r.start) < 0 && comparePoints(end, r.end) > 0) {
                this.removeRange(r);
                i--;
                continue;
            }

            if (r.start.row == startRow && r.start.column > start.column)
                r.start.column += colDiff;
            if (r.end.row == startRow && r.end.column >= start.column)
                r.end.column += colDiff;
            if (r.start.row >= startRow)
                r.start.row += lineDif;
            if (r.end.row >= startRow)
                r.end.row += lineDif;

            if (comparePoints(r.start, r.end) > 0)
                this.removeRange(r);
        }
        if (!ranges.length)
            this.detach();
    };
    this.updateLinkedFields = function() {
        var ts = this.selectedTabstop;
        if (!ts || !ts.hasLinkedRanges)
            return;
        this.$inChange = true;
        var session = this.editor.session;
        var text = session.getTextRange(ts.firstNonLinked);
        for (var i = ts.length; i--;) {
            var range = ts[i];
            if (!range.linked)
                continue;
            var fmt = exports.snippetManager.tmStrFormat(text, range.original);
            session.replace(range, fmt);
        }
        this.$inChange = false;
    };
    this.onAfterExec = function(e) {
        if (e.command && !e.command.readOnly)
            this.updateLinkedFields();
    };
    this.onChangeSelection = function() {
        if (!this.editor)
            return;
        var lead = this.editor.selection.lead;
        var anchor = this.editor.selection.anchor;
        var isEmpty = this.editor.selection.isEmpty();
        for (var i = this.ranges.length; i--;) {
            if (this.ranges[i].linked)
                continue;
            var containsLead = this.ranges[i].contains(lead.row, lead.column);
            var containsAnchor = isEmpty || this.ranges[i].contains(anchor.row, anchor.column);
            if (containsLead && containsAnchor)
                return;
        }
        this.detach();
    };
    this.onChangeSession = function() {
        this.detach();
    };
    this.tabNext = function(dir) {
        var max = this.tabstops.length;
        var index = this.index + (dir || 1);
        index = Math.min(Math.max(index, 1), max);
        if (index == max)
            index = 0;
        this.selectTabstop(index);
        if (index === 0)
            this.detach();
    };
    this.selectTabstop = function(index) {
        this.$openTabstops = null;
        var ts = this.tabstops[this.index];
        if (ts)
            this.addTabstopMarkers(ts);
        this.index = index;
        ts = this.tabstops[this.index];
        if (!ts || !ts.length)
            return;
        
        this.selectedTabstop = ts;
        if (!this.editor.inVirtualSelectionMode) {        
            var sel = this.editor.multiSelect;
            sel.toSingleRange(ts.firstNonLinked.clone());
            for (var i = ts.length; i--;) {
                if (ts.hasLinkedRanges && ts[i].linked)
                    continue;
                sel.addRange(ts[i].clone(), true);
            }
            if (sel.ranges[0])
                sel.addRange(sel.ranges[0].clone());
        } else {
            this.editor.selection.setRange(ts.firstNonLinked);
        }
        
        this.editor.keyBinding.addKeyboardHandler(this.keyboardHandler);
    };
    this.addTabstops = function(tabstops, start, end) {
        if (!this.$openTabstops)
            this.$openTabstops = [];
        if (!tabstops[0]) {
            var p = Range.fromPoints(end, end);
            moveRelative(p.start, start);
            moveRelative(p.end, start);
            tabstops[0] = [p];
            tabstops[0].index = 0;
        }

        var i = this.index;
        var arg = [i + 1, 0];
        var ranges = this.ranges;
        tabstops.forEach(function(ts, index) {
            var dest = this.$openTabstops[index] || ts;
                
            for (var i = ts.length; i--;) {
                var p = ts[i];
                var range = Range.fromPoints(p.start, p.end || p.start);
                movePoint(range.start, start);
                movePoint(range.end, start);
                range.original = p;
                range.tabstop = dest;
                ranges.push(range);
                if (dest != ts)
                    dest.unshift(range);
                else
                    dest[i] = range;
                if (p.fmtString) {
                    range.linked = true;
                    dest.hasLinkedRanges = true;
                } else if (!dest.firstNonLinked)
                    dest.firstNonLinked = range;
            }
            if (!dest.firstNonLinked)
                dest.hasLinkedRanges = false;
            if (dest === ts) {
                arg.push(dest);
                this.$openTabstops[index] = dest;
            }
            this.addTabstopMarkers(dest);
        }, this);
        
        if (arg.length > 2) {
            if (this.tabstops.length)
                arg.push(arg.splice(2, 1)[0]);
            this.tabstops.splice.apply(this.tabstops, arg);
        }
    };

    this.addTabstopMarkers = function(ts) {
        var session = this.editor.session;
        ts.forEach(function(range) {
            if  (!range.markerId)
                range.markerId = session.addMarker(range, "ace_snippet-marker", "text");
        });
    };
    this.removeTabstopMarkers = function(ts) {
        var session = this.editor.session;
        ts.forEach(function(range) {
            session.removeMarker(range.markerId);
            range.markerId = null;
        });
    };
    this.removeRange = function(range) {
        var i = range.tabstop.indexOf(range);
        range.tabstop.splice(i, 1);
        i = this.ranges.indexOf(range);
        this.ranges.splice(i, 1);
        this.editor.session.removeMarker(range.markerId);
        if (!range.tabstop.length) {
            i = this.tabstops.indexOf(range.tabstop);
            if (i != -1)
                this.tabstops.splice(i, 1);
            if (!this.tabstops.length)
                this.detach();
        }
    };

    this.keyboardHandler = new HashHandler();
    this.keyboardHandler.bindKeys({
        "Tab": function(ed) {
            if (exports.snippetManager && exports.snippetManager.expandWithTab(ed)) {
                return;
            }

            ed.tabstopManager.tabNext(1);
        },
        "Shift-Tab": function(ed) {
            ed.tabstopManager.tabNext(-1);
        },
        "Esc": function(ed) {
            ed.tabstopManager.detach();
        },
        "Return": function(ed) {
            return false;
        }
    });
}).call(TabstopManager.prototype);



var changeTracker = {};
changeTracker.onChange = Anchor.prototype.onChange;
changeTracker.setPosition = function(row, column) {
    this.pos.row = row;
    this.pos.column = column;
};
changeTracker.update = function(pos, delta, $insertRight) {
    this.$insertRight = $insertRight;
    this.pos = pos; 
    this.onChange(delta);
};

var movePoint = function(point, diff) {
    if (point.row == 0)
        point.column += diff.column;
    point.row += diff.row;
};

var moveRelative = function(point, start) {
    if (point.row == start.row)
        point.column -= start.column;
    point.row -= start.row;
};


require("./lib/dom").importCssString("\
.ace_snippet-marker {\
    -moz-box-sizing: border-box;\
    box-sizing: border-box;\
    background: rgba(194, 193, 208, 0.09);\
    border: 1px dotted rgba(211, 208, 235, 0.62);\
    position: absolute;\
}");

exports.snippetManager = new SnippetManager();


var Editor = require("./editor").Editor;
(function() {
    this.insertSnippet = function(content, options) {
        return exports.snippetManager.insertSnippet(this, content, options);
    };
    this.expandSnippet = function(options) {
        return exports.snippetManager.expandWithTab(this, options);
    };
}).call(Editor.prototype);

});

define("ace/ext/emmet",["require","exports","module","ace/keyboard/hash_handler","ace/editor","ace/snippets","ace/range","resources","resources","tabStops","resources","utils","actions","ace/config","ace/config"], function(require, exports, module) {
"use strict";
var HashHandler = require("ace/keyboard/hash_handler").HashHandler;
var Editor = require("ace/editor").Editor;
var snippetManager = require("ace/snippets").snippetManager;
var Range = require("ace/range").Range;
var emmet, emmetPath;
function AceEmmetEditor() {}

AceEmmetEditor.prototype = {
    setupContext: function(editor) {
        this.ace = editor;
        this.indentation = editor.session.getTabString();
        if (!emmet)
            emmet = window.emmet;
        var resources = emmet.resources || emmet.require("resources");
        resources.setVariable("indentation", this.indentation);
        this.$syntax = null;
        this.$syntax = this.getSyntax();
    },
    getSelectionRange: function() {
        var range = this.ace.getSelectionRange();
        var doc = this.ace.session.doc;
        return {
            start: doc.positionToIndex(range.start),
            end: doc.positionToIndex(range.end)
        };
    },
    createSelection: function(start, end) {
        var doc = this.ace.session.doc;
        this.ace.selection.setRange({
            start: doc.indexToPosition(start),
            end: doc.indexToPosition(end)
        });
    },
    getCurrentLineRange: function() {
        var ace = this.ace;
        var row = ace.getCursorPosition().row;
        var lineLength = ace.session.getLine(row).length;
        var index = ace.session.doc.positionToIndex({row: row, column: 0});
        return {
            start: index,
            end: index + lineLength
        };
    },
    getCaretPos: function(){
        var pos = this.ace.getCursorPosition();
        return this.ace.session.doc.positionToIndex(pos);
    },
    setCaretPos: function(index){
        var pos = this.ace.session.doc.indexToPosition(index);
        this.ace.selection.moveToPosition(pos);
    },
    getCurrentLine: function() {
        var row = this.ace.getCursorPosition().row;
        return this.ace.session.getLine(row);
    },
    replaceContent: function(value, start, end, noIndent) {
        if (end == null)
            end = start == null ? this.getContent().length : start;
        if (start == null)
            start = 0;        
        
        var editor = this.ace;
        var doc = editor.session.doc;
        var range = Range.fromPoints(doc.indexToPosition(start), doc.indexToPosition(end));
        editor.session.remove(range);
        
        range.end = range.start;
        value = this.$updateTabstops(value);
        snippetManager.insertSnippet(editor, value);
    },
    getContent: function(){
        return this.ace.getValue();
    },
    getSyntax: function() {
        if (this.$syntax)
            return this.$syntax;
        var syntax = this.ace.session.$modeId.split("/").pop();
        if (syntax == "html" || syntax == "php") {
            var cursor = this.ace.getCursorPosition();
            var state = this.ace.session.getState(cursor.row);
            if (typeof state != "string")
                state = state[0];
            if (state) {
                state = state.split("-");
                if (state.length > 1)
                    syntax = state[0];
                else if (syntax == "php")
                    syntax = "html";
            }
        }
        return syntax;
    },
    getProfileName: function() {
        var resources = emmet.resources || emmet.require("resources");
        switch (this.getSyntax()) {
          case "css": return "css";
          case "xml":
          case "xsl":
            return "xml";
          case "html":
            var profile = resources.getVariable("profile");
            if (!profile)
                profile = this.ace.session.getLines(0,2).join("").search(/<!DOCTYPE[^>]+XHTML/i) != -1 ? "xhtml": "html";
            return profile;
          default:
            var mode = this.ace.session.$mode;
            return mode.emmetConfig && mode.emmetConfig.profile || "xhtml";
        }
    },
    prompt: function(title) {
        return prompt(title);
    },
    getSelection: function() {
        return this.ace.session.getTextRange();
    },
    getFilePath: function() {
        return "";
    },
    $updateTabstops: function(value) {
        var base = 1000;
        var zeroBase = 0;
        var lastZero = null;
        var ts = emmet.tabStops || emmet.require('tabStops');
        var resources = emmet.resources || emmet.require("resources");
        var settings = resources.getVocabulary("user");
        var tabstopOptions = {
            tabstop: function(data) {
                var group = parseInt(data.group, 10);
                var isZero = group === 0;
                if (isZero)
                    group = ++zeroBase;
                else
                    group += base;

                var placeholder = data.placeholder;
                if (placeholder) {
                    placeholder = ts.processText(placeholder, tabstopOptions);
                }

                var result = '${' + group + (placeholder ? ':' + placeholder : '') + '}';

                if (isZero) {
                    lastZero = [data.start, result];
                }

                return result;
            },
            escape: function(ch) {
                if (ch == '$') return '\\$';
                if (ch == '\\') return '\\\\';
                return ch;
            }
        };

        value = ts.processText(value, tabstopOptions);

        if (settings.variables['insert_final_tabstop'] && !/\$\{0\}$/.test(value)) {
            value += '${0}';
        } else if (lastZero) {
            var common = emmet.utils ? emmet.utils.common : emmet.require('utils');
            value = common.replaceSubstring(value, '${0}', lastZero[0], lastZero[1]);
        }
        
        return value;
    }
};


var keymap = {
    expand_abbreviation: {"mac": "ctrl+alt+e", "win": "alt+e"},
    match_pair_outward: {"mac": "ctrl+d", "win": "ctrl+,"},
    match_pair_inward: {"mac": "ctrl+j", "win": "ctrl+shift+0"},
    matching_pair: {"mac": "ctrl+alt+j", "win": "alt+j"},
    next_edit_point: "alt+right",
    prev_edit_point: "alt+left",
    toggle_comment: {"mac": "command+/", "win": "ctrl+/"},
    split_join_tag: {"mac": "shift+command+'", "win": "shift+ctrl+`"},
    remove_tag: {"mac": "command+'", "win": "shift+ctrl+;"},
    evaluate_math_expression: {"mac": "shift+command+y", "win": "shift+ctrl+y"},
    increment_number_by_1: "ctrl+up",
    decrement_number_by_1: "ctrl+down",
    increment_number_by_01: "alt+up",
    decrement_number_by_01: "alt+down",
    increment_number_by_10: {"mac": "alt+command+up", "win": "shift+alt+up"},
    decrement_number_by_10: {"mac": "alt+command+down", "win": "shift+alt+down"},
    select_next_item: {"mac": "shift+command+.", "win": "shift+ctrl+."},
    select_previous_item: {"mac": "shift+command+,", "win": "shift+ctrl+,"},
    reflect_css_value: {"mac": "shift+command+r", "win": "shift+ctrl+r"},

    encode_decode_data_url: {"mac": "shift+ctrl+d", "win": "ctrl+'"},
    expand_abbreviation_with_tab: "Tab",
    wrap_with_abbreviation: {"mac": "shift+ctrl+a", "win": "shift+ctrl+a"}
};

var editorProxy = new AceEmmetEditor();
exports.commands = new HashHandler();
exports.runEmmetCommand = function runEmmetCommand(editor) {
    try {
        editorProxy.setupContext(editor);
        var actions = emmet.actions || emmet.require("actions");
    
        if (this.action == "expand_abbreviation_with_tab") {
            if (!editor.selection.isEmpty())
                return false;
            var pos = editor.selection.lead;
            var token = editor.session.getTokenAt(pos.row, pos.column);
            if (token && /\btag\b/.test(token.type))
                return false;
        }
        
        if (this.action == "wrap_with_abbreviation") {
            return setTimeout(function() {
                actions.run("wrap_with_abbreviation", editorProxy);
            }, 0);
        }
        
        var result = actions.run(this.action, editorProxy);
    } catch(e) {
        if (!emmet) {
            exports.load(runEmmetCommand.bind(this, editor));
            return true;
        }
        editor._signal("changeStatus", typeof e == "string" ? e : e.message);
        console.log(e);
        result = false;
    }
    return result;
};

for (var command in keymap) {
    exports.commands.addCommand({
        name: "emmet:" + command,
        action: command,
        bindKey: keymap[command],
        exec: exports.runEmmetCommand,
        multiSelectAction: "forEach"
    });
}

exports.updateCommands = function(editor, enabled) {
    if (enabled) {
        editor.keyBinding.addKeyboardHandler(exports.commands);
    } else {
        editor.keyBinding.removeKeyboardHandler(exports.commands);
    }
};

exports.isSupportedMode = function(mode) {
    if (!mode) return false;
    if (mode.emmetConfig) return true;
    var id = mode.$id || mode;
    return /css|less|scss|sass|stylus|html|php|twig|ejs|handlebars/.test(id);
};

exports.isAvailable = function(editor, command) {
    if (/(evaluate_math_expression|expand_abbreviation)$/.test(command))
        return true;
    var mode = editor.session.$mode;
    var isSupported = exports.isSupportedMode(mode);
    if (isSupported && mode.$modes) {
        try {
            editorProxy.setupContext(editor);
            if (/js|php/.test(editorProxy.getSyntax()))
                isSupported = false;
        } catch(e) {}
    }
    return isSupported;
};

var onChangeMode = function(e, target) {
    var editor = target;
    if (!editor)
        return;
    var enabled = exports.isSupportedMode(editor.session.$mode);
    if (e.enableEmmet === false)
        enabled = false;
    if (enabled)
        exports.load();
    exports.updateCommands(editor, enabled);
};

exports.load = function(cb) {
    if (typeof emmetPath == "string") {
        require("ace/config").loadModule(emmetPath, function() {
            emmetPath = null;
            cb && cb();
        });
    }
};

exports.AceEmmetEditor = AceEmmetEditor;
require("ace/config").defineOptions(Editor.prototype, "editor", {
    enableEmmet: {
        set: function(val) {
            this[val ? "on" : "removeListener"]("changeMode", onChangeMode);
            onChangeMode({enableEmmet: !!val}, this);
        },
        value: true
    }
});

exports.setCore = function(e) {
    if (typeof e == "string")
       emmetPath = e;
    else
       emmet = e;
};
});

define("ace/autocomplete/popup",["require","exports","module","ace/virtual_renderer","ace/editor","ace/range","ace/lib/event","ace/lib/lang","ace/lib/dom"], function(require, exports, module) {
"use strict";

var Renderer = require("../virtual_renderer").VirtualRenderer;
var Editor = require("../editor").Editor;
var Range = require("../range").Range;
var event = require("../lib/event");
var lang = require("../lib/lang");
var dom = require("../lib/dom");

var $singleLineEditor = function(el) {
    var renderer = new Renderer(el);

    renderer.$maxLines = 4;

    var editor = new Editor(renderer);

    editor.setHighlightActiveLine(false);
    editor.setShowPrintMargin(false);
    editor.renderer.setShowGutter(false);
    editor.renderer.setHighlightGutterLine(false);

    editor.$mouseHandler.$focusTimeout = 0;
    editor.$highlightTagPending = true;

    return editor;
};

var AcePopup = function(parentNode) {
    var el = dom.createElement("div");
    var popup = new $singleLineEditor(el);

    if (parentNode)
        parentNode.appendChild(el);
    el.style.display = "none";
    popup.renderer.content.style.cursor = "default";
    popup.renderer.setStyle("ace_autocomplete");

    popup.setOption("displayIndentGuides", false);
    popup.setOption("dragDelay", 150);

    var noop = function(){};

    popup.focus = noop;
    popup.$isFocused = true;

    popup.renderer.$cursorLayer.restartTimer = noop;
    popup.renderer.$cursorLayer.element.style.opacity = 0;

    popup.renderer.$maxLines = 8;
    popup.renderer.$keepTextAreaAtCursor = false;

    popup.setHighlightActiveLine(false);
    popup.session.highlight("");
    popup.session.$searchHighlight.clazz = "ace_highlight-marker";

    popup.on("mousedown", function(e) {
        var pos = e.getDocumentPosition();
        popup.selection.moveToPosition(pos);
        selectionMarker.start.row = selectionMarker.end.row = pos.row;
        e.stop();
    });

    var lastMouseEvent;
    var hoverMarker = new Range(-1,0,-1,Infinity);
    var selectionMarker = new Range(-1,0,-1,Infinity);
    selectionMarker.id = popup.session.addMarker(selectionMarker, "ace_active-line", "fullLine");
    popup.setSelectOnHover = function(val) {
        if (!val) {
            hoverMarker.id = popup.session.addMarker(hoverMarker, "ace_line-hover", "fullLine");
        } else if (hoverMarker.id) {
            popup.session.removeMarker(hoverMarker.id);
            hoverMarker.id = null;
        }
    };
    popup.setSelectOnHover(false);
    popup.on("mousemove", function(e) {
        if (!lastMouseEvent) {
            lastMouseEvent = e;
            return;
        }
        if (lastMouseEvent.x == e.x && lastMouseEvent.y == e.y) {
            return;
        }
        lastMouseEvent = e;
        lastMouseEvent.scrollTop = popup.renderer.scrollTop;
        var row = lastMouseEvent.getDocumentPosition().row;
        if (hoverMarker.start.row != row) {
            if (!hoverMarker.id)
                popup.setRow(row);
            setHoverMarker(row);
        }
    });
    popup.renderer.on("beforeRender", function() {
        if (lastMouseEvent && hoverMarker.start.row != -1) {
            lastMouseEvent.$pos = null;
            var row = lastMouseEvent.getDocumentPosition().row;
            if (!hoverMarker.id)
                popup.setRow(row);
            setHoverMarker(row, true);
        }
    });
    popup.renderer.on("afterRender", function() {
        var row = popup.getRow();
        var t = popup.renderer.$textLayer;
        var selected = t.element.childNodes[row - t.config.firstRow];
        if (selected == t.selectedNode)
            return;
        if (t.selectedNode)
            dom.removeCssClass(t.selectedNode, "ace_selected");
        t.selectedNode = selected;
        if (selected)
            dom.addCssClass(selected, "ace_selected");
    });
    var hideHoverMarker = function() { setHoverMarker(-1); };
    var setHoverMarker = function(row, suppressRedraw) {
        if (row !== hoverMarker.start.row) {
            hoverMarker.start.row = hoverMarker.end.row = row;
            if (!suppressRedraw)
                popup.session._emit("changeBackMarker");
            popup._emit("changeHoverMarker");
        }
    };
    popup.getHoveredRow = function() {
        return hoverMarker.start.row;
    };

    event.addListener(popup.container, "mouseout", hideHoverMarker);
    popup.on("hide", hideHoverMarker);
    popup.on("changeSelection", hideHoverMarker);

    popup.session.doc.getLength = function() {
        return popup.data.length;
    };
    popup.session.doc.getLine = function(i) {
        var data = popup.data[i];
        if (typeof data == "string")
            return data;
        return (data && data.value) || "";
    };

    var bgTokenizer = popup.session.bgTokenizer;
    bgTokenizer.$tokenizeRow = function(row) {
        var data = popup.data[row];
        var tokens = [];
        if (!data)
            return tokens;
        if (typeof data == "string")
            data = {value: data};
        var caption = data.caption || data.value || data.name;

        function addToken(value, className) {
            value && tokens.push({
                type: (data.className || "") + (className || ""), 
                value: value
            });
        }
        
        var lower = caption.toLowerCase();
        var filterText = (popup.filterText || "").toLowerCase();
        var lastIndex = 0;
        var lastI = 0;
        for (var i = 0; i <= filterText.length; i++) {
            if (i != lastI && (data.matchMask & (1 << i) || i == filterText.length)) {
                var sub = filterText.slice(lastI, i);
                lastI = i;
                var index = lower.indexOf(sub);
                if (index == -1) continue;
                addToken(caption.slice(lastIndex, index), "");
                lastIndex = index + sub.length;
                addToken(caption.slice(index, lastIndex), "completion-highlight");
            }
        }
        addToken(caption.slice(lastIndex, caption.length), "");
        
        if (data.meta)
            tokens.push({type: "completion-meta", value: data.meta});

        return tokens;
    };
    bgTokenizer.$updateOnChange = noop;
    bgTokenizer.start = noop;

    popup.session.$computeWidth = function() {
        return this.screenWidth = 0;
    };
    popup.isOpen = false;
    popup.isTopdown = false;
    popup.autoSelect = true;
    popup.filterText = "";

    popup.data = [];
    popup.setData = function(list, filterText) {
        popup.filterText = filterText || "";
        popup.setValue(lang.stringRepeat("\n", list.length), -1);
        popup.data = list || [];
        popup.setRow(0);
    };
    popup.getData = function(row) {
        return popup.data[row];
    };

    popup.getRow = function() {
        return selectionMarker.start.row;
    };
    popup.setRow = function(line) {
        line = Math.max(this.autoSelect ? 0 : -1, Math.min(this.data.length, line));
        if (selectionMarker.start.row != line) {
            popup.selection.clearSelection();
            selectionMarker.start.row = selectionMarker.end.row = line || 0;
            popup.session._emit("changeBackMarker");
            popup.moveCursorTo(line || 0, 0);
            if (popup.isOpen)
                popup._signal("select");
        }
    };

    popup.on("changeSelection", function() {
        if (popup.isOpen)
            popup.setRow(popup.selection.lead.row);
        popup.renderer.scrollCursorIntoView();
    });

    popup.hide = function() {
        this.container.style.display = "none";
        this._signal("hide");
        popup.isOpen = false;
    };
    popup.show = function(pos, lineHeight, topdownOnly) {
        var el = this.container;
        var screenHeight = window.innerHeight;
        var screenWidth = window.innerWidth;
        var renderer = this.renderer;
        var maxH = renderer.$maxLines * lineHeight * 1.4;
        var top = pos.top + this.$borderSize;
        var allowTopdown = top > screenHeight / 2 && !topdownOnly;
        if (allowTopdown && top + lineHeight + maxH > screenHeight) {
            renderer.$maxPixelHeight = top - 2 * this.$borderSize;
            el.style.top = "";
            el.style.bottom = screenHeight - top + "px";
            popup.isTopdown = false;
        } else {
            top += lineHeight;
            renderer.$maxPixelHeight = screenHeight - top - 0.2 * lineHeight;
            el.style.top = top + "px";
            el.style.bottom = "";
            popup.isTopdown = true;
        }

        el.style.display = "";
        this.renderer.$textLayer.checkForSizeChanges();

        var left = pos.left;
        if (left + el.offsetWidth > screenWidth)
            left = screenWidth - el.offsetWidth;

        el.style.left = left + "px";

        this._signal("show");
        lastMouseEvent = null;
        popup.isOpen = true;
    };

    popup.getTextLeftOffset = function() {
        return this.$borderSize + this.renderer.$padding + this.$imageSize;
    };

    popup.$imageSize = 0;
    popup.$borderSize = 1;

    return popup;
};

dom.importCssString("\
.ace_editor.ace_autocomplete .ace_marker-layer .ace_active-line {\
    background-color: #CAD6FA;\
    z-index: 1;\
}\
.ace_dark.ace_editor.ace_autocomplete .ace_marker-layer .ace_active-line {\
    background-color: #3a674e;\
}\
.ace_editor.ace_autocomplete .ace_line-hover {\
    border: 1px solid #abbffe;\
    margin-top: -1px;\
    background: rgba(233,233,253,0.4);\
    position: absolute;\
    z-index: 2;\
}\
.ace_dark.ace_editor.ace_autocomplete .ace_line-hover {\
    border: 1px solid rgba(109, 150, 13, 0.8);\
    background: rgba(58, 103, 78, 0.62);\
}\
.ace_completion-meta {\
    opacity: 0.5;\
    margin: 0.9em;\
}\
.ace_editor.ace_autocomplete .ace_completion-highlight{\
    color: #2d69c7;\
}\
.ace_dark.ace_editor.ace_autocomplete .ace_completion-highlight{\
    color: #93ca12;\
}\
.ace_editor.ace_autocomplete {\
    width: 300px;\
    z-index: 200000;\
    border: 1px lightgray solid;\
    position: fixed;\
    box-shadow: 2px 3px 5px rgba(0,0,0,.2);\
    line-height: 1.4;\
    background: #fefefe;\
    color: #111;\
}\
.ace_dark.ace_editor.ace_autocomplete {\
    border: 1px #484747 solid;\
    box-shadow: 2px 3px 5px rgba(0, 0, 0, 0.51);\
    line-height: 1.4;\
    background: #25282c;\
    color: #c1c1c1;\
}", "autocompletion.css");

exports.AcePopup = AcePopup;

});

define("ace/autocomplete/util",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.parForEach = function(array, fn, callback) {
    var completed = 0;
    var arLength = array.length;
    if (arLength === 0)
        callback();
    for (var i = 0; i < arLength; i++) {
        fn(array[i], function(result, err) {
            completed++;
            if (completed === arLength)
                callback(result, err);
        });
    }
};

var ID_REGEX = /[a-zA-Z_0-9\$\-\u00A2-\uFFFF]/;

exports.retrievePrecedingIdentifier = function(text, pos, regex) {
    regex = regex || ID_REGEX;
    var buf = [];
    for (var i = pos-1; i >= 0; i--) {
        if (regex.test(text[i]))
            buf.push(text[i]);
        else
            break;
    }
    return buf.reverse().join("");
};

exports.retrieveFollowingIdentifier = function(text, pos, regex) {
    regex = regex || ID_REGEX;
    var buf = [];
    for (var i = pos; i < text.length; i++) {
        if (regex.test(text[i]))
            buf.push(text[i]);
        else
            break;
    }
    return buf;
};

exports.getCompletionPrefix = function (editor) {
    var pos = editor.getCursorPosition();
    var line = editor.session.getLine(pos.row);
    var prefix;
    editor.completers.forEach(function(completer) {
        if (completer.identifierRegexps) {
            completer.identifierRegexps.forEach(function(identifierRegex) {
                if (!prefix && identifierRegex)
                    prefix = this.retrievePrecedingIdentifier(line, pos.column, identifierRegex);
            }.bind(this));
        }
    }.bind(this));
    return prefix || this.retrievePrecedingIdentifier(line, pos.column);
};

});

define("ace/autocomplete",["require","exports","module","ace/keyboard/hash_handler","ace/autocomplete/popup","ace/autocomplete/util","ace/lib/event","ace/lib/lang","ace/lib/dom","ace/snippets"], function(require, exports, module) {
"use strict";

var HashHandler = require("./keyboard/hash_handler").HashHandler;
var AcePopup = require("./autocomplete/popup").AcePopup;
var util = require("./autocomplete/util");
var event = require("./lib/event");
var lang = require("./lib/lang");
var dom = require("./lib/dom");
var snippetManager = require("./snippets").snippetManager;

var Autocomplete = function() {
    this.autoInsert = false;
    this.autoSelect = true;
    this.exactMatch = false;
    this.gatherCompletionsId = 0;
    this.keyboardHandler = new HashHandler();
    this.keyboardHandler.bindKeys(this.commands);

    this.blurListener = this.blurListener.bind(this);
    this.changeListener = this.changeListener.bind(this);
    this.mousedownListener = this.mousedownListener.bind(this);
    this.mousewheelListener = this.mousewheelListener.bind(this);

    this.changeTimer = lang.delayedCall(function() {
        this.updateCompletions(true);
    }.bind(this));

    this.tooltipTimer = lang.delayedCall(this.updateDocTooltip.bind(this), 50);
};

(function() {

    this.$init = function() {
        this.popup = new AcePopup(document.body || document.documentElement);
        this.popup.on("click", function(e) {
            this.insertMatch();
            e.stop();
        }.bind(this));
        this.popup.focus = this.editor.focus.bind(this.editor);
        this.popup.on("show", this.tooltipTimer.bind(null, null));
        this.popup.on("select", this.tooltipTimer.bind(null, null));
        this.popup.on("changeHoverMarker", this.tooltipTimer.bind(null, null));
        return this.popup;
    };

    this.getPopup = function() {
        return this.popup || this.$init();
    };

    this.openPopup = function(editor, prefix, keepPopupPosition) {
        if (!this.popup)
            this.$init();

        this.popup.autoSelect = this.autoSelect;

        this.popup.setData(this.completions.filtered, this.completions.filterText);

        editor.keyBinding.addKeyboardHandler(this.keyboardHandler);
        
        var renderer = editor.renderer;
        this.popup.setRow(this.autoSelect ? 0 : -1);
        if (!keepPopupPosition) {
            this.popup.setTheme(editor.getTheme());
            this.popup.setFontSize(editor.getFontSize());

            var lineHeight = renderer.layerConfig.lineHeight;

            var pos = renderer.$cursorLayer.getPixelPosition(this.base, true);
            pos.left -= this.popup.getTextLeftOffset();

            var rect = editor.container.getBoundingClientRect();
            pos.top += rect.top - renderer.layerConfig.offset;
            pos.left += rect.left - editor.renderer.scrollLeft;
            pos.left += renderer.gutterWidth;

            this.popup.show(pos, lineHeight);
        } else if (keepPopupPosition && !prefix) {
            this.detach();
        }
    };

    this.detach = function() {
        this.editor.keyBinding.removeKeyboardHandler(this.keyboardHandler);
        this.editor.off("changeSelection", this.changeListener);
        this.editor.off("blur", this.blurListener);
        this.editor.off("mousedown", this.mousedownListener);
        this.editor.off("mousewheel", this.mousewheelListener);
        this.changeTimer.cancel();
        this.hideDocTooltip();

        this.gatherCompletionsId += 1;
        if (this.popup && this.popup.isOpen)
            this.popup.hide();

        if (this.base)
            this.base.detach();
        this.activated = false;
        this.completions = this.base = null;
    };

    this.changeListener = function(e) {
        var cursor = this.editor.selection.lead;
        if (cursor.row != this.base.row || cursor.column < this.base.column) {
            this.detach();
        }
        if (this.activated)
            this.changeTimer.schedule();
        else
            this.detach();
    };

    this.blurListener = function(e) {
        var el = document.activeElement;
        var text = this.editor.textInput.getElement();
        var fromTooltip = e.relatedTarget && this.tooltipNode && this.tooltipNode.contains(e.relatedTarget);
        var container = this.popup && this.popup.container;
        if (el != text && el.parentNode != container && !fromTooltip
            && el != this.tooltipNode && e.relatedTarget != text
        ) {
            this.detach();
        }
    };

    this.mousedownListener = function(e) {
        this.detach();
    };

    this.mousewheelListener = function(e) {
        this.detach();
    };

    this.goTo = function(where) {
        var row = this.popup.getRow();
        var max = this.popup.session.getLength() - 1;

        switch(where) {
            case "up": row = row <= 0 ? max : row - 1; break;
            case "down": row = row >= max ? -1 : row + 1; break;
            case "start": row = 0; break;
            case "end": row = max; break;
        }

        this.popup.setRow(row);
    };

    this.insertMatch = function(data, options) {
        if (!data)
            data = this.popup.getData(this.popup.getRow());
        if (!data)
            return false;

        if (data.completer && data.completer.insertMatch) {
            data.completer.insertMatch(this.editor, data);
        } else {
            if (this.completions.filterText) {
                var ranges = this.editor.selection.getAllRanges();
                for (var i = 0, range; range = ranges[i]; i++) {
                    range.start.column -= this.completions.filterText.length;
                    this.editor.session.remove(range);
                }
            }
            if (data.snippet)
                snippetManager.insertSnippet(this.editor, data.snippet);
            else
                this.editor.execCommand("insertstring", data.value || data);
        }
        this.detach();
    };


    this.commands = {
        "Up": function(editor) { editor.completer.goTo("up"); },
        "Down": function(editor) { editor.completer.goTo("down"); },
        "Ctrl-Up|Ctrl-Home": function(editor) { editor.completer.goTo("start"); },
        "Ctrl-Down|Ctrl-End": function(editor) { editor.completer.goTo("end"); },

        "Esc": function(editor) { editor.completer.detach(); },
        "Return": function(editor) { return editor.completer.insertMatch(); },
        "Shift-Return": function(editor) { editor.completer.insertMatch(null, {deleteSuffix: true}); },
        "Tab": function(editor) {
            var result = editor.completer.insertMatch();
            if (!result && !editor.tabstopManager)
                editor.completer.goTo("down");
            else
                return result;
        },

        "PageUp": function(editor) { editor.completer.popup.gotoPageUp(); },
        "PageDown": function(editor) { editor.completer.popup.gotoPageDown(); }
    };

    this.gatherCompletions = function(editor, callback) {
        var session = editor.getSession();
        var pos = editor.getCursorPosition();

        var prefix = util.getCompletionPrefix(editor);

        this.base = session.doc.createAnchor(pos.row, pos.column - prefix.length);
        this.base.$insertRight = true;

        var matches = [];
        var total = editor.completers.length;
        editor.completers.forEach(function(completer, i) {
            completer.getCompletions(editor, session, pos, prefix, function(err, results) {
                if (!err && results)
                    matches = matches.concat(results);
                callback(null, {
                    prefix: util.getCompletionPrefix(editor),
                    matches: matches,
                    finished: (--total === 0)
                });
            });
        });
        return true;
    };

    this.showPopup = function(editor) {
        if (this.editor)
            this.detach();

        this.activated = true;

        this.editor = editor;
        if (editor.completer != this) {
            if (editor.completer)
                editor.completer.detach();
            editor.completer = this;
        }

        editor.on("changeSelection", this.changeListener);
        editor.on("blur", this.blurListener);
        editor.on("mousedown", this.mousedownListener);
        editor.on("mousewheel", this.mousewheelListener);

        this.updateCompletions();
    };

    this.updateCompletions = function(keepPopupPosition) {
        if (keepPopupPosition && this.base && this.completions) {
            var pos = this.editor.getCursorPosition();
            var prefix = this.editor.session.getTextRange({start: this.base, end: pos});
            if (prefix == this.completions.filterText)
                return;
            this.completions.setFilter(prefix);
            if (!this.completions.filtered.length)
                return this.detach();
            if (this.completions.filtered.length == 1
            && this.completions.filtered[0].value == prefix
            && !this.completions.filtered[0].snippet)
                return this.detach();
            this.openPopup(this.editor, prefix, keepPopupPosition);
            return;
        }
        var _id = this.gatherCompletionsId;
        this.gatherCompletions(this.editor, function(err, results) {
            var detachIfFinished = function() {
                if (!results.finished) return;
                return this.detach();
            }.bind(this);

            var prefix = results.prefix;
            var matches = results && results.matches;

            if (!matches || !matches.length)
                return detachIfFinished();
            if (prefix.indexOf(results.prefix) !== 0 || _id != this.gatherCompletionsId)
                return;

            this.completions = new FilteredList(matches);

            if (this.exactMatch)
                this.completions.exactMatch = true;

            this.completions.setFilter(prefix);
            var filtered = this.completions.filtered;
            if (!filtered.length)
                return detachIfFinished();
            if (filtered.length == 1 && filtered[0].value == prefix && !filtered[0].snippet)
                return detachIfFinished();
            if (this.autoInsert && filtered.length == 1 && results.finished)
                return this.insertMatch(filtered[0]);

            this.openPopup(this.editor, prefix, keepPopupPosition);
        }.bind(this));
    };

    this.cancelContextMenu = function() {
        this.editor.$mouseHandler.cancelContextMenu();
    };

    this.updateDocTooltip = function() {
        var popup = this.popup;
        var all = popup.data;
        var selected = all && (all[popup.getHoveredRow()] || all[popup.getRow()]);
        var doc = null;
        if (!selected || !this.editor || !this.popup.isOpen)
            return this.hideDocTooltip();
        this.editor.completers.some(function(completer) {
            if (completer.getDocTooltip)
                doc = completer.getDocTooltip(selected);
            return doc;
        });
        if (!doc)
            doc = selected;

        if (typeof doc == "string")
            doc = {docText: doc};
        if (!doc || !(doc.docHTML || doc.docText))
            return this.hideDocTooltip();
        this.showDocTooltip(doc);
    };

    this.showDocTooltip = function(item) {
        if (!this.tooltipNode) {
            this.tooltipNode = dom.createElement("div");
            this.tooltipNode.className = "ace_tooltip ace_doc-tooltip";
            this.tooltipNode.style.margin = 0;
            this.tooltipNode.style.pointerEvents = "auto";
            this.tooltipNode.tabIndex = -1;
            this.tooltipNode.onblur = this.blurListener.bind(this);
            this.tooltipNode.onclick = this.onTooltipClick.bind(this);
        }

        var tooltipNode = this.tooltipNode;
        if (item.docHTML) {
            tooltipNode.innerHTML = item.docHTML;
        } else if (item.docText) {
            tooltipNode.textContent = item.docText;
        }

        if (!tooltipNode.parentNode)
            document.body.appendChild(tooltipNode);
        var popup = this.popup;
        var rect = popup.container.getBoundingClientRect();
        tooltipNode.style.top = popup.container.style.top;
        tooltipNode.style.bottom = popup.container.style.bottom;

        tooltipNode.style.display = "block";
        if (window.innerWidth - rect.right < 320) {
            if (rect.left < 320) {
                if(popup.isTopdown) {
                    tooltipNode.style.top = rect.bottom + "px";
                    tooltipNode.style.left = rect.left + "px";
                    tooltipNode.style.right = "";
                    tooltipNode.style.bottom = "";
                } else {
                    tooltipNode.style.top = popup.container.offsetTop - tooltipNode.offsetHeight + "px";
                    tooltipNode.style.left = rect.left + "px";
                    tooltipNode.style.right = "";
                    tooltipNode.style.bottom = "";
                }
            } else {
                tooltipNode.style.right = window.innerWidth - rect.left + "px";
                tooltipNode.style.left = "";
            }
        } else {
            tooltipNode.style.left = (rect.right + 1) + "px";
            tooltipNode.style.right = "";
        }
    };

    this.hideDocTooltip = function() {
        this.tooltipTimer.cancel();
        if (!this.tooltipNode) return;
        var el = this.tooltipNode;
        if (!this.editor.isFocused() && document.activeElement == el)
            this.editor.focus();
        this.tooltipNode = null;
        if (el.parentNode)
            el.parentNode.removeChild(el);
    };
    
    this.onTooltipClick = function(e) {
        var a = e.target;
        while (a && a != this.tooltipNode) {
            if (a.nodeName == "A" && a.href) {
                a.rel = "noreferrer";
                a.target = "_blank";
                break;
            }
            a = a.parentNode;
        }
    };

}).call(Autocomplete.prototype);

Autocomplete.startCommand = {
    name: "startAutocomplete",
    exec: function(editor) {
        if (!editor.completer)
            editor.completer = new Autocomplete();
        editor.completer.autoInsert = false;
        editor.completer.autoSelect = true;
        editor.completer.showPopup(editor);
        editor.completer.cancelContextMenu();
    },
    bindKey: "Ctrl-Space|Ctrl-Shift-Space|Alt-Space"
};

var FilteredList = function(array, filterText) {
    this.all = array;
    this.filtered = array;
    this.filterText = filterText || "";
    this.exactMatch = false;
};
(function(){
    this.setFilter = function(str) {
        if (str.length > this.filterText && str.lastIndexOf(this.filterText, 0) === 0)
            var matches = this.filtered;
        else
            var matches = this.all;

        this.filterText = str;
        matches = this.filterCompletions(matches, this.filterText);
        matches = matches.sort(function(a, b) {
            return b.exactMatch - a.exactMatch || b.$score - a.$score 
                || (a.caption || a.value) < (b.caption || b.value);
        });
        var prev = null;
        matches = matches.filter(function(item){
            var caption = item.snippet || item.caption || item.value;
            if (caption === prev) return false;
            prev = caption;
            return true;
        });

        this.filtered = matches;
    };
    this.filterCompletions = function(items, needle) {
        var results = [];
        var upper = needle.toUpperCase();
        var lower = needle.toLowerCase();
        loop: for (var i = 0, item; item = items[i]; i++) {
            var caption = item.caption || item.value || item.snippet;
            if (!caption) continue;
            var lastIndex = -1;
            var matchMask = 0;
            var penalty = 0;
            var index, distance;

            if (this.exactMatch) {
                if (needle !== caption.substr(0, needle.length))
                    continue loop;
            } else {
                var fullMatchIndex = caption.toLowerCase().indexOf(lower);
                if (fullMatchIndex > -1) {
                    penalty = fullMatchIndex;
                } else {
                    for (var j = 0; j < needle.length; j++) {
                        var i1 = caption.indexOf(lower[j], lastIndex + 1);
                        var i2 = caption.indexOf(upper[j], lastIndex + 1);
                        index = (i1 >= 0) ? ((i2 < 0 || i1 < i2) ? i1 : i2) : i2;
                        if (index < 0)
                            continue loop;
                        distance = index - lastIndex - 1;
                        if (distance > 0) {
                            if (lastIndex === -1)
                                penalty += 10;
                            penalty += distance;
                            matchMask = matchMask | (1 << j);
                        }
                        lastIndex = index;
                    }
                }
            }
            item.matchMask = matchMask;
            item.exactMatch = penalty ? 0 : 1;
            item.$score = (item.score || 0) - penalty;
            results.push(item);
        }
        return results;
    };
}).call(FilteredList.prototype);

exports.Autocomplete = Autocomplete;
exports.FilteredList = FilteredList;

});

define("ace/autocomplete/text_completer",["require","exports","module","ace/range"], function(require, exports, module) {
    var Range = require("../range").Range;
    
    var splitRegex = /[^a-zA-Z_0-9\$\-\u00C0-\u1FFF\u2C00-\uD7FF\w]+/;

    function getWordIndex(doc, pos) {
        var textBefore = doc.getTextRange(Range.fromPoints({row: 0, column:0}, pos));
        return textBefore.split(splitRegex).length - 1;
    }
    function wordDistance(doc, pos) {
        var prefixPos = getWordIndex(doc, pos);
        var words = doc.getValue().split(splitRegex);
        var wordScores = Object.create(null);
        
        var currentWord = words[prefixPos];

        words.forEach(function(word, idx) {
            if (!word || word === currentWord) return;

            var distance = Math.abs(prefixPos - idx);
            var score = words.length - distance;
            if (wordScores[word]) {
                wordScores[word] = Math.max(score, wordScores[word]);
            } else {
                wordScores[word] = score;
            }
        });
        return wordScores;
    }

    exports.getCompletions = function(editor, session, pos, prefix, callback) {
        var wordScore = wordDistance(session, pos);
        var wordList = Object.keys(wordScore);
        callback(null, wordList.map(function(word) {
            return {
                caption: word,
                value: word,
                score: wordScore[word],
                meta: "local"
            };
        }));
    };
});

define("ace/ext/language_tools",["require","exports","module","ace/snippets","ace/autocomplete","ace/config","ace/lib/lang","ace/autocomplete/util","ace/autocomplete/text_completer","ace/editor","ace/config"], function(require, exports, module) {
"use strict";

var snippetManager = require("../snippets").snippetManager;
var Autocomplete = require("../autocomplete").Autocomplete;
var config = require("../config");
var lang = require("../lib/lang");
var util = require("../autocomplete/util");

var textCompleter = require("../autocomplete/text_completer");
var keyWordCompleter = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        if (session.$mode.completer) {
            return session.$mode.completer.getCompletions(editor, session, pos, prefix, callback);
        }
        var state = editor.session.getState(pos.row);
        var completions = session.$mode.getCompletions(state, session, pos, prefix);
        callback(null, completions);
    }
};

var snippetCompleter = {
    getCompletions: function(editor, session, pos, prefix, callback) {
        var scopes = [];
        var token = session.getTokenAt(pos.row, pos.column);
        if (token && token.type.match(/(tag-name|tag-open|tag-whitespace|attribute-name|attribute-value)\.xml$/))
            scopes.push('html-tag');
        else
            scopes = snippetManager.getActiveScopes(editor);

        var snippetMap = snippetManager.snippetMap;
        var completions = [];
        scopes.forEach(function(scope) {
            var snippets = snippetMap[scope] || [];
            for (var i = snippets.length; i--;) {
                var s = snippets[i];
                var caption = s.name || s.tabTrigger;
                if (!caption)
                    continue;
                completions.push({
                    caption: caption,
                    snippet: s.content,
                    meta: s.tabTrigger && !s.name ? s.tabTrigger + "\u21E5 " : "snippet",
                    type: "snippet"
                });
            }
        }, this);
        callback(null, completions);
    },
    getDocTooltip: function(item) {
        if (item.type == "snippet" && !item.docHTML) {
            item.docHTML = [
                "<b>", lang.escapeHTML(item.caption), "</b>", "<hr></hr>",
                lang.escapeHTML(item.snippet)
            ].join("");
        }
    }
};

var completers = [snippetCompleter, textCompleter, keyWordCompleter];
exports.setCompleters = function(val) {
    completers.length = 0;
    if (val) completers.push.apply(completers, val);
};
exports.addCompleter = function(completer) {
    completers.push(completer);
};
exports.textCompleter = textCompleter;
exports.keyWordCompleter = keyWordCompleter;
exports.snippetCompleter = snippetCompleter;

var expandSnippet = {
    name: "expandSnippet",
    exec: function(editor) {
        return snippetManager.expandWithTab(editor);
    },
    bindKey: "Tab"
};

var onChangeMode = function(e, editor) {
    loadSnippetsForMode(editor.session.$mode);
};

var loadSnippetsForMode = function(mode) {
    var id = mode.$id;
    if (!snippetManager.files)
        snippetManager.files = {};
    loadSnippetFile(id);
    if (mode.modes)
        mode.modes.forEach(loadSnippetsForMode);
};

var loadSnippetFile = function(id) {
    if (!id || snippetManager.files[id])
        return;
    var snippetFilePath = id.replace("mode", "snippets");
    snippetManager.files[id] = {};
    config.loadModule(snippetFilePath, function(m) {
        if (m) {
            snippetManager.files[id] = m;
            if (!m.snippets && m.snippetText)
                m.snippets = snippetManager.parseSnippetFile(m.snippetText);
            snippetManager.register(m.snippets || [], m.scope);
            if (m.includeScopes) {
                snippetManager.snippetMap[m.scope].includeScopes = m.includeScopes;
                m.includeScopes.forEach(function(x) {
                    loadSnippetFile("ace/mode/" + x);
                });
            }
        }
    });
};

var doLiveAutocomplete = function(e) {
    var editor = e.editor;
    var hasCompleter = editor.completer && editor.completer.activated;
    if (e.command.name === "backspace") {
        if (hasCompleter && !util.getCompletionPrefix(editor))
            editor.completer.detach();
    }
    else if (e.command.name === "insertstring") {
        var prefix = util.getCompletionPrefix(editor);
        if (prefix && !hasCompleter) {
            if (!editor.completer) {
                editor.completer = new Autocomplete();
            }
            editor.completer.autoInsert = false;
            editor.completer.showPopup(editor);
        }
    }
};

var Editor = require("../editor").Editor;
require("../config").defineOptions(Editor.prototype, "editor", {
    enableBasicAutocompletion: {
        set: function(val) {
            if (val) {
                if (!this.completers)
                    this.completers = Array.isArray(val)? val: completers;
                this.commands.addCommand(Autocomplete.startCommand);
            } else {
                this.commands.removeCommand(Autocomplete.startCommand);
            }
        },
        value: false
    },
    enableLiveAutocompletion: {
        set: function(val) {
            if (val) {
                if (!this.completers)
                    this.completers = Array.isArray(val)? val: completers;
                this.commands.on('afterExec', doLiveAutocomplete);
            } else {
                this.commands.removeListener('afterExec', doLiveAutocomplete);
            }
        },
        value: false
    },
    enableSnippets: {
        set: function(val) {
            if (val) {
                this.commands.addCommand(expandSnippet);
                this.on("changeMode", onChangeMode);
                onChangeMode(null, this);
            } else {
                this.commands.removeCommand(expandSnippet);
                this.off("changeMode", onChangeMode);
            }
        },
        value: false
    }
});
});

define("ace/ext/beautify",["require","exports","module","ace/token_iterator"], function(require, exports, module) {
"use strict";
var TokenIterator = require("../token_iterator").TokenIterator;

function is(token, type) {
    return token.type.lastIndexOf(type + ".xml") > -1;
}
exports.singletonTags = ["area", "base", "br", "col", "command", "embed", "hr", "html", "img", "input", "keygen", "link", "meta", "param", "source", "track", "wbr"];
exports.blockTags = ["article", "aside", "blockquote", "body", "div", "dl", "fieldset", "footer", "form", "head", "header", "html", "nav", "ol", "p", "script", "section", "style", "table", "tbody", "tfoot", "thead", "ul"];

exports.beautify = function(session) {
    var iterator = new TokenIterator(session, 0, 0);
    var token = iterator.getCurrentToken();
    var tabString = session.getTabString();
    var singletonTags = exports.singletonTags;
    var blockTags = exports.blockTags;
    var nextToken;
    var breakBefore = false;
    var spaceBefore = false;
    var spaceAfter = false;
    var code = "";
    var value = "";
    var tagName = "";
    var depth = 0;
    var lastDepth = 0;
    var lastIndent = 0;
    var indent = 0;
    var unindent = 0;
    var roundDepth = 0;
    var onCaseLine = false;
    var row;
    var curRow = 0;
    var rowsToAdd = 0;
    var rowTokens = [];
    var abort = false;
    var i;
    var indentNextLine = false;
    var inTag = false;
    var inCSS = false;
    var inBlock = false;
    var levels = {0: 0};
    var parents = {};

    var trimNext = function() {
        if (nextToken && nextToken.value && nextToken.type !== 'string.regexp')
            nextToken.value = nextToken.value.trim();
    };

    var trimLine = function() {
        code = code.replace(/ +$/, "");
    };

    var trimCode = function() {
        code = code.trimRight();
        breakBefore = false;
    };

    while (token !== null) {
        curRow = iterator.getCurrentTokenRow();
        rowTokens = iterator.$rowTokens;
        nextToken = iterator.stepForward();

        if (typeof token !== "undefined") {
            value = token.value;
            unindent = 0;
            inCSS = (tagName === "style" || session.$modeId === "ace/mode/css");
            if (is(token, "tag-open")) {
                inTag = true;
                if (nextToken)
                    inBlock = (blockTags.indexOf(nextToken.value) !== -1);
                if (value === "</") {
                    if (inBlock && !breakBefore && rowsToAdd < 1)
                        rowsToAdd++;

                    if (inCSS)
                        rowsToAdd = 1;

                    unindent = 1;
                    inBlock = false;
                }
            } else if (is(token, "tag-close")) {
                inTag = false;
            } else if (is(token, "comment.start")) {
                inBlock = true;
            } else if (is(token, "comment.end")) {
                inBlock = false;
            }
            if (!inTag && !rowsToAdd && token.type === "paren.rparen" && token.value.substr(0, 1) === "}") {
                rowsToAdd++;
            }
            if (curRow !== row) {
                rowsToAdd = curRow;

                if (row)
                    rowsToAdd -= row;
            }

            if (rowsToAdd) {
                trimCode();
                for (; rowsToAdd > 0; rowsToAdd--)
                    code += "\n";

                breakBefore = true;
                if (!is(token, "comment") && !token.type.match(/^(comment|string)$/))
                   value = value.trimLeft();
            }

            if (value) {
                if (token.type === "keyword" && value.match(/^(if|else|elseif|for|foreach|while|switch)$/)) {
                    parents[depth] = value;

                    trimNext();
                    spaceAfter = true;
                    if (value.match(/^(else|elseif)$/)) {
                        if (code.match(/\}[\s]*$/)) {
                            trimCode();
                            spaceBefore = true;
                        }
                    }
                } else if (token.type === "paren.lparen") {
                    trimNext();
                    if (value.substr(-1) === "{") {
                        spaceAfter = true;
                        indentNextLine = false;

                        if(!inTag)
                            rowsToAdd = 1;
                    }
                    if (value.substr(0, 1) === "{") {
                        spaceBefore = true;
                        if (code.substr(-1) !== '[' && code.trimRight().substr(-1) === '[') {
                            trimCode();
                            spaceBefore = false;
                        } else if (code.trimRight().substr(-1) === ')') {
                            trimCode();
                        } else {
                            trimLine();
                        }
                    }
                } else if (token.type === "paren.rparen") {
                    unindent = 1;
                    if (value.substr(0, 1) === "}") {
                        if (parents[depth-1] === 'case')
                            unindent++;

                        if (code.trimRight().substr(-1) === '{') {
                            trimCode();
                        } else {
                            spaceBefore = true;

                            if (inCSS)
                                rowsToAdd+=2;
                        }
                    }
                    if (value.substr(0, 1) === "]") {
                        if (code.substr(-1) !== '}' && code.trimRight().substr(-1) === '}') {
                            spaceBefore = false;
                            indent++;
                            trimCode();
                        }
                    }
                    if (value.substr(0, 1) === ")") {
                        if (code.substr(-1) !== '(' && code.trimRight().substr(-1) === '(') {
                            spaceBefore = false;
                            indent++;
                            trimCode();
                        }
                    }

                    trimLine();
                } else if ((token.type === "keyword.operator" || token.type === "keyword") && value.match(/^(=|==|===|!=|!==|&&|\|\||and|or|xor|\+=|.=|>|>=|<|<=|=>)$/)) {
                    trimCode();
                    trimNext();
                    spaceBefore = true;
                    spaceAfter = true;
                } else if (token.type === "punctuation.operator" && value === ';') {
                    trimCode();
                    trimNext();
                    spaceAfter = true;

                    if (inCSS)
                        rowsToAdd++;
                } else if (token.type === "punctuation.operator" && value.match(/^(:|,)$/)) {
                    trimCode();
                    trimNext();
                    spaceAfter = true;
                    breakBefore = false;
                } else if (token.type === "support.php_tag" && value === "?>" && !breakBefore) {
                    trimCode();
                    spaceBefore = true;
                } else if (is(token, "attribute-name") && code.substr(-1).match(/^\s$/)) {
                    spaceBefore = true;
                } else if (is(token, "attribute-equals")) {
                    trimLine();
                    trimNext();
                } else if (is(token, "tag-close")) {
                    trimLine();
                    if(value === "/>")
                        spaceBefore = true;
                }
                if (breakBefore && !(token.type.match(/^(comment)$/) && !value.substr(0, 1).match(/^[/#]$/)) && !(token.type.match(/^(string)$/) && !value.substr(0, 1).match(/^['"]$/))) {

                    indent = lastIndent;

                    if(depth > lastDepth) {
                        indent++;

                        for (i=depth; i > lastDepth; i--)
                            levels[i] = indent;
                    } else if(depth < lastDepth)
                        indent = levels[depth];

                    lastDepth = depth;
                    lastIndent = indent;

                    if(unindent)
                        indent -= unindent;

                    if (indentNextLine && !roundDepth) {
                        indent++;
                        indentNextLine = false;
                    }

                    for (i = 0; i < indent; i++)
                        code += tabString;
                }


                if (token.type === "keyword" && value.match(/^(case|default)$/)) {
                    parents[depth] = value;
                    depth++;
                }


                if (token.type === "keyword" && value.match(/^(break)$/)) {
                    if(parents[depth-1] && parents[depth-1].match(/^(case|default)$/)) {
                        depth--;
                    }
                }
                if (token.type === "paren.lparen") {
                    roundDepth += (value.match(/\(/g) || []).length;
                    depth += value.length;
                }

                if (token.type === "keyword" && value.match(/^(if|else|elseif|for|while)$/)) {
                    indentNextLine = true;
                    roundDepth = 0;
                } else if (!roundDepth && value.trim() && token.type !== "comment")
                    indentNextLine = false;

                if (token.type === "paren.rparen") {
                    roundDepth -= (value.match(/\)/g) || []).length;

                    for (i = 0; i < value.length; i++) {
                        depth--;
                        if(value.substr(i, 1)==='}' && parents[depth]==='case') {
                            depth--;
                        }
                    }
                }
                if (spaceBefore && !breakBefore) {
                    trimLine();
                    if (code.substr(-1) !== "\n")
                        code += " ";
                }

                code += value;

                if (spaceAfter)
                    code += " ";

                breakBefore = false;
                spaceBefore = false;
                spaceAfter = false;
                if ((is(token, "tag-close") && (inBlock || blockTags.indexOf(tagName) !== -1)) || (is(token, "doctype") && value === ">")) {
                    if (inBlock && nextToken && nextToken.value === "</")
                        rowsToAdd = -1;
                    else
                        rowsToAdd = 1;
                }
                if (is(token, "tag-open") && value === "</") {
                    depth--;
                } else if (is(token, "tag-open") && value === "<" && singletonTags.indexOf(nextToken.value) === -1) {
                    depth++;
                } else if (is(token, "tag-name")) {
                    tagName = value;
                } else if (is(token, "tag-close") && value === "/>" && singletonTags.indexOf(tagName) === -1){
                    depth--;
                }

                row = curRow;
            }
        }

        token = nextToken;
    }

    code = code.trim();
    session.doc.setValue(code);
};

exports.commands = [{
    name: "beautify",
    exec: function(editor) {
        exports.beautify(editor.session);
    },
    bindKey: "Ctrl-Shift-B"
}];

});

define("kitchen-sink/demo",["require","exports","module","ace/lib/fixoldbrowsers","ace/ext/rtl","ace/multi_select","kitchen-sink/inline_editor","kitchen-sink/dev_util","kitchen-sink/file_drop","ace/config","ace/lib/dom","ace/lib/net","ace/lib/lang","ace/lib/useragent","ace/lib/event","ace/theme/textmate","ace/edit_session","ace/undomanager","ace/keyboard/hash_handler","ace/virtual_renderer","ace/editor","ace/ext/whitespace","kitchen-sink/doclist","kitchen-sink/layout","kitchen-sink/util","ace/ext/elastic_tabstops_lite","ace/incremental_search","kitchen-sink/token_tooltip","ace/config","ace/worker/worker_client","ace/split","ace/ext/options","ace/ext/statusbar","ace/ext/emmet","ace/placeholder","ace/snippets","ace/ext/language_tools","ace/ext/beautify","ace/keyboard/keybinding","ace/commands/command_manager"], function(require, exports, module) {
"use strict";

require("ace/lib/fixoldbrowsers");

require("ace/ext/rtl");

require("ace/multi_select");
require("./inline_editor");
var devUtil = require("./dev_util");
require("./file_drop");

var config = require("ace/config");
config.init();
var env = {};

var dom = require("ace/lib/dom");
var net = require("ace/lib/net");
var lang = require("ace/lib/lang");
var useragent = require("ace/lib/useragent");

var event = require("ace/lib/event");
var theme = require("ace/theme/textmate");
var EditSession = require("ace/edit_session").EditSession;
var UndoManager = require("ace/undomanager").UndoManager;

var HashHandler = require("ace/keyboard/hash_handler").HashHandler;

var Renderer = require("ace/virtual_renderer").VirtualRenderer;
var Editor = require("ace/editor").Editor;

var whitespace = require("ace/ext/whitespace");



var doclist = require("./doclist");
var layout = require("./layout");
var util = require("./util");
var saveOption = util.saveOption;


var ElasticTabstopsLite = require("ace/ext/elastic_tabstops_lite").ElasticTabstopsLite;

var IncrementalSearch = require("ace/incremental_search").IncrementalSearch;


var TokenTooltip = require("./token_tooltip").TokenTooltip;
require("ace/config").defineOptions(Editor.prototype, "editor", {
    showTokenInfo: {
        set: function(val) {
            if (val) {
                this.tokenTooltip = this.tokenTooltip || new TokenTooltip(this);
            }
            else if (this.tokenTooltip) {
                this.tokenTooltip.destroy();
                delete this.tokenTooltip;
            }
        },
        get: function() {
            return !!this.tokenTooltip;
        },
        handlesSet: true
    }
});


var workerModule = require("ace/worker/worker_client");
if (location.href.indexOf("noworker") !== -1) {
    workerModule.WorkerClient = workerModule.UIWorkerClient;
}
var container = document.getElementById("editor-container");
var Split = require("ace/split").Split;
var split = new Split(container, theme, 1);
env.editor = split.getEditor(0);
split.on("focus", function(editor) {
    env.editor = editor;
    updateUIEditorOptions();
});
env.split = split;
window.env = env;


var consoleEl = dom.createElement("div");
container.parentNode.appendChild(consoleEl);
consoleEl.style.cssText = "position:fixed; bottom:1px; right:0;\
border:1px solid #baf; z-index:100";

var cmdLine = new layout.singleLineEditor(consoleEl);
cmdLine.editor = env.editor;
env.editor.cmdLine = cmdLine;

env.editor.showCommandLine = function(val) {
    this.cmdLine.focus();
    if (typeof val == "string")
        this.cmdLine.setValue(val, 1);
};
env.editor.commands.addCommands([{
    name: "gotoline",
    bindKey: {win: "Ctrl-L", mac: "Command-L"},
    exec: function(editor, line) {
        if (typeof line == "object") {
            var arg = this.name + " " + editor.getCursorPosition().row;
            editor.cmdLine.setValue(arg, 1);
            editor.cmdLine.focus();
            return;
        }
        line = parseInt(line, 10);
        if (!isNaN(line))
            editor.gotoLine(line);
    },
    readOnly: true
}, {
    name: "snippet",
    bindKey: {win: "Alt-C", mac: "Command-Alt-C"},
    exec: function(editor, needle) {
        if (typeof needle == "object") {
            editor.cmdLine.setValue("snippet ", 1);
            editor.cmdLine.focus();
            return;
        }
        var s = snippetManager.getSnippetByName(needle, editor);
        if (s)
            snippetManager.insertSnippet(editor, s.content);
    },
    readOnly: true
}, {
    name: "focusCommandLine",
    bindKey: "shift-esc|ctrl-`",
    exec: function(editor, needle) { editor.cmdLine.focus(); },
    readOnly: true
}, {
    name: "nextFile",
    bindKey: "Ctrl-tab",
    exec: function(editor) { doclist.cycleOpen(editor, 1); },
    readOnly: true
}, {
    name: "previousFile",
    bindKey: "Ctrl-shift-tab",
    exec: function(editor) { doclist.cycleOpen(editor, -1); },
    readOnly: true
}, {
    name: "execute",
    bindKey: "ctrl+enter",
    exec: function(editor) {
        try {
            var r = window.eval(editor.getCopyText() || editor.getValue());
        } catch(e) {
            r = e;
        }
        editor.cmdLine.setValue(r + "");
    },
    readOnly: true
}, {
    name: "showKeyboardShortcuts",
    bindKey: {win: "Ctrl-Alt-h", mac: "Command-Alt-h"},
    exec: function(editor) {
        config.loadModule("ace/ext/keybinding_menu", function(module) {
            module.init(editor);
            editor.showKeyboardShortcuts();
        });
    }
}, {
    name: "increaseFontSize",
    bindKey: "Ctrl-=|Ctrl-+",
    exec: function(editor) {
        var size = parseInt(editor.getFontSize(), 10) || 12;
        editor.setFontSize(size + 1);
    }
}, {
    name: "decreaseFontSize",
    bindKey: "Ctrl+-|Ctrl-_",
    exec: function(editor) {
        var size = parseInt(editor.getFontSize(), 10) || 12;
        editor.setFontSize(Math.max(size - 1 || 1));
    }
}, {
    name: "resetFontSize",
    bindKey: "Ctrl+0|Ctrl-Numpad0",
    exec: function(editor) {
        editor.setFontSize(12);
    }
}]);


env.editor.commands.addCommands(whitespace.commands);

cmdLine.commands.bindKeys({
    "Shift-Return|Ctrl-Return|Alt-Return": function(cmdLine) { cmdLine.insert("\n"); },
    "Esc|Shift-Esc": function(cmdLine){ cmdLine.editor.focus(); },
    "Return": function(cmdLine){
        var command = cmdLine.getValue().split(/\s+/);
        var editor = cmdLine.editor;
        editor.commands.exec(command[0], editor, command[1]);
        editor.focus();
    }
});

cmdLine.commands.removeCommands(["find", "gotoline", "findall", "replace", "replaceall"]);

var commands = env.editor.commands;
commands.addCommand({
    name: "save",
    bindKey: {win: "Ctrl-S", mac: "Command-S"},
    exec: function(arg) {
        var session = env.editor.session;
        var name = session.name.match(/[^\/]+$/);
        localStorage.setItem(
            "saved_file:" + name,
            session.getValue()
        );
        env.editor.cmdLine.setValue("saved "+ name);
    }
});

commands.addCommand({
    name: "load",
    bindKey: {win: "Ctrl-O", mac: "Command-O"},
    exec: function(arg) {
        var session = env.editor.session;
        var name = session.name.match(/[^\/]+$/);
        var value = localStorage.getItem("saved_file:" + name);
        if (typeof value == "string") {
            session.setValue(value);
            env.editor.cmdLine.setValue("loaded "+ name);
        } else {
            env.editor.cmdLine.setValue("no previuos value saved for "+ name);
        }
    }
});
var consoleHeight = 20;
function onResize() {
    var left = env.split.$container.offsetLeft;
    var width = document.documentElement.clientWidth - left;
    container.style.width = width + "px";
    container.style.height = document.documentElement.clientHeight - consoleHeight + "px";
    env.split.resize();

    consoleEl.style.width = width + "px";
    cmdLine.resize();
}

window.onresize = onResize;
onResize();
doclist.history = doclist.docs.map(function(doc) {
    return doc.name;
});
doclist.history.index = 0;
doclist.cycleOpen = function(editor, dir) {
    var h = this.history;
    h.index += dir;
    if (h.index >= h.length)
        h.index = 0;
    else if (h.index <= 0)
        h.index = h.length - 1;
    var s = h[h.index];
    doclist.pickDocument(s);
};
doclist.addToHistory = function(name) {
    var h = this.history;
    var i = h.indexOf(name);
    if (i != h.index) {
        if (i != -1)
            h.splice(i, 1);
        h.index = h.push(name);
    }
};
doclist.pickDocument = function(name) {
    doclist.loadDoc(name, function(session) {
        if (!session)
            return;
        doclist.addToHistory(session.name);
        session = env.split.setSession(session);
        whitespace.detectIndentation(session);
        optionsPanel.render();
        env.editor.focus();
    });
};



var OptionPanel = require("ace/ext/options").OptionPanel;
var optionsPanel = new OptionPanel(env.editor);

optionsPanel.add({
    Main: {
        Document: {
            type: "select",
            path: "doc",
            items: doclist.all,
            position: -101,
            onchange: doclist.pickDocument,
            getValue: function() {
                return env.editor.session.name || "javascript";
            }
        },
        Split: {
            type: "buttonBar",
            path: "split",
            values: ["None", "Below", "Beside"],
            position: -100,
            onchange: function(value) {
                var sp = env.split;
                if (value == "Below" || value == "Beside") {
                    var newEditor = (sp.getSplits() == 1);
                    sp.setOrientation(value == "Below" ? sp.BELOW : sp.BESIDE);
                    sp.setSplits(2);

                    if (newEditor) {
                        var session = sp.getEditor(0).session;
                        var newSession = sp.setSession(session, 1);
                        newSession.name = session.name;
                    }
                } else {
                    sp.setSplits(1);
                }
            },
            getValue: function() {
                var sp = env.split;
                return sp.getSplits() == 1
                    ? "None"
                    : sp.getOrientation() == sp.BELOW
                    ? "Below"
                    : "Beside";
            }
        }
    },
    More: {
        "Rtl Text": {
            path: "rtlText",
            position: 900
        },
        "Show token info": {
            path: "showTokenInfo",
            position: 2000
        },
        "Text Input Debugger": devUtil.textInputDebugger
    }
});

var optionsPanelContainer = document.getElementById("optionsPanel");
optionsPanel.render();
optionsPanelContainer.insertBefore(optionsPanel.container, optionsPanelContainer.firstChild);
optionsPanel.on("setOption", function(e) {
    util.saveOption(e.name, e.value);
});

function updateUIEditorOptions() {
    optionsPanel.editor = env.editor;
    optionsPanel.render();
}

optionsPanel.setOption("doc", util.getOption("doc") || "JavaScript");
for (var i in optionsPanel.options) {
    var value = util.getOption(i);
    if (value != undefined) {
        if ((i == "mode" || i == "theme") && !/[/]/.test(value))
            value = "ace/" + i + "/" + value;
        optionsPanel.setOption(i, value);
    }
}


function synchroniseScrolling() {
    var s1 = env.split.$editors[0].session;
    var s2 = env.split.$editors[1].session;
    s1.on('changeScrollTop', function(pos) {s2.setScrollTop(pos)});
    s2.on('changeScrollTop', function(pos) {s1.setScrollTop(pos)});
    s1.on('changeScrollLeft', function(pos) {s2.setScrollLeft(pos)});
    s2.on('changeScrollLeft', function(pos) {s1.setScrollLeft(pos)});
}

var StatusBar = require("ace/ext/statusbar").StatusBar;
new StatusBar(env.editor, cmdLine.container);


var Emmet = require("ace/ext/emmet");
net.loadScript("https://cloud9ide.github.io/emmet-core/emmet.js", function() {
    Emmet.setCore(window.emmet);
    env.editor.setOption("enableEmmet", true);
});

require("ace/placeholder").PlaceHolder;

var snippetManager = require("ace/snippets").snippetManager;

env.editSnippets = function() {
    var sp = env.split;
    if (sp.getSplits() == 2) {
        sp.setSplits(1);
        return;
    }
    sp.setSplits(1);
    sp.setSplits(2);
    sp.setOrientation(sp.BESIDE);
    var editor = sp.$editors[1];
    var id = sp.$editors[0].session.$mode.$id || "";
    var m = snippetManager.files[id];
    if (!doclist["snippets/" + id]) {
        var text = m.snippetText;
        var s = doclist.initDoc(text, "", {});
        s.setMode("ace/mode/snippets");
        doclist["snippets/" + id] = s;
    }
    editor.on("blur", function() {
        m.snippetText = editor.getValue();
        snippetManager.unregister(m.snippets);
        m.snippets = snippetManager.parseSnippetFile(m.snippetText, m.scope);
        snippetManager.register(m.snippets);
    });
    sp.$editors[0].once("changeMode", function() {
        sp.setSplits(1);
    });
    editor.setSession(doclist["snippets/" + id], 1);
    editor.focus();
};

optionsPanelContainer.insertBefore(
    dom.buildDom(["div", {style: "text-align:right;margin-right: 60px"},
        ["div", {}, 
            ["button", {onclick: env.editSnippets}, "Edit Snippets"]],
        ["div", {}, 
            ["button", {onclick: function() {
                var info = navigator.platform + "\n" + navigator.userAgent;
                if (env.editor.getValue() == info)
                    return env.editor.undo();
                env.editor.setValue(info, -1);
                env.editor.setOption("wrap", 80);
            }}, "Show Browser Info"]],
        devUtil.getUI()
    ]),
    optionsPanelContainer.children[1]
);

require("ace/ext/language_tools");
env.editor.setOptions({
    enableBasicAutocompletion: true,
    enableSnippets: true
});

var beautify = require("ace/ext/beautify");
env.editor.commands.addCommands(beautify.commands);
var KeyBinding = require("ace/keyboard/keybinding").KeyBinding;
var CommandManager = require("ace/commands/command_manager").CommandManager;
var commandManager = new CommandManager();
var kb = new KeyBinding({
    commands: commandManager,
    fake: true
});
event.addCommandKeyListener(document.documentElement, kb.onCommandKey.bind(kb));
event.addListener(document.documentElement, "keyup", function(e) {
    if (e.keyCode === 18) // do not trigger browser menu on windows
        e.preventDefault();
});
commandManager.addCommands([{
    name: "window-left",
    bindKey: {win: "cmd-alt-left", mac: "ctrl-cmd-left"},
    exec: function() {
        moveFocus();
    }
}, {
    name: "window-right",
    bindKey: {win: "cmd-alt-right", mac: "ctrl-cmd-right"},
    exec: function() {
        moveFocus();
    }
}, {
    name: "window-up",
    bindKey: {win: "cmd-alt-up", mac: "ctrl-cmd-up"},
    exec: function() {
        moveFocus();
    }
}, {
    name: "window-down",
    bindKey: {win: "cmd-alt-down", mac: "ctrl-cmd-down"},
    exec: function() {
        moveFocus();
    }
}]);

function moveFocus() {
    var el = document.activeElement;
    if (el == env.editor.textInput.getElement())
        env.editor.cmdLine.focus();    
    else
        env.editor.focus();
}

});
                (function() {
                    window.require(["kitchen-sink/demo"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            