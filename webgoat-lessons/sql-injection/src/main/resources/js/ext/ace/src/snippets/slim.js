define("ace/snippets/slim",["require","exports","module"], function(require, exports, module) {
    "use strict";

    exports.snippetText = "";
    exports.scope = "slim";

});
                (function() {
                    window.require(["ace/snippets/slim"], function(m) {
                        if (typeof module == "object" && typeof exports == "object" && module) {
                            module.exports = m;
                        }
                    });
                })();
            