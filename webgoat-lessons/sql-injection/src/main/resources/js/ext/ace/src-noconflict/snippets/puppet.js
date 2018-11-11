ace.define("ace/snippets/puppet",["require","exports","module"], function(require, exports, module) {
"use strict";

exports.snippetText = "";
exports.scope = "puppet";

});
                (function() {
                    ace.require(["ace/snippets/puppet"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            