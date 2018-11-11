ace.define("ace/snippets/yaml",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "yaml";

});
                (function() {
                    ace.require(["ace/snippets/yaml"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            