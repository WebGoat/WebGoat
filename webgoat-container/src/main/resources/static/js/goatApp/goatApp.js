define(['jquery',
        'underscore',
        'backbone',
        'polyglot',
        'goatApp/view/GoatRouter',
        'goatApp/support/goatAsyncErrorHandler'],
    function ($,
         _,
         Backbone,
         Polyglot,
         Router,
         asyncErrorHandler) {
        'use strict'
        return {
            initApp: function () {
                var locale = localStorage.getItem('locale') || 'en';
                $.getJSON('service/labels.mvc', function(data) {
                    window.polyglot = new Polyglot({phrases: data});
                    asyncErrorHandler.init();
                    var goatRouter = new Router();
                });

            }
        };
    });