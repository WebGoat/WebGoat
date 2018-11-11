define("ace/snippets/asciidoc",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "asciidoc";

});
                (function() {
                    window.require(["ace/snippets/asciidoc"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            