define("ace/snippets/mel",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "mel";

});
                (function() {
                    window.require(["ace/snippets/mel"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            