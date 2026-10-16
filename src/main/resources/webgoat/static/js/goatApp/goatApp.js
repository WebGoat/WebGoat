/*
 * This JavaScript is used to load a Backbone router which is defined by GoatRouter.js
 */

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
                    window.polyglot = new Polyglot({phrases: data});//i18n polyglot labels
                    asyncErrorHandler.init();
                    console.log('about to create app router');//default js
                    var goatRouter = new Router();//backbone router
                });//jquery

            }
        };
    });
