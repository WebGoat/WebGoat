ace.define("ace/snippets/luapage",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "luapage";

});
                (function() {
                    ace.require(["ace/snippets/luapage"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            