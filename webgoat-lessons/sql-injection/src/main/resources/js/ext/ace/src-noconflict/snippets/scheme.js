ace.define("ace/snippets/scheme",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "scheme";

});
                (function() {
                    ace.require(["ace/snippets/scheme"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            