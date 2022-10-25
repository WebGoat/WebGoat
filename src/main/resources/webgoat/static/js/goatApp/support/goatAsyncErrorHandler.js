define(
    ['backbone', 'underscore'],
    function(Backbone, _) {
        return {
            init: function() {
                var backboneSync = Backbone.sync;

                var asyncErrorHandler = function(error) {
                    return function(jqXHR) {
                        var statusCode = jqXHR.status;
                        var errorCodes = {
                            404: true,
                            500: true,
                            503: true,
                            504: true
                        };

                        if (statusCode === 401 || statusCode === 403) {
                            window.top.location.href = "login";
                        } else if(errorCodes[statusCode]) {
                            Backbone.trigger("error:unhandled");
                        }
                    };
                };

                Backbone.sync = function(method, model, options) {
                    // override error handler
                    options.error = asyncErrorHandler(options.error);
                    return backboneSync(method, model, options);
                }
            }
        };
    }
);
