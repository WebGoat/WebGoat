define(['jquery',
        'underscore',
        'backbone',
        'goatApp/support/goatAsyncErrorHandler',
        'goatApp/view/ScoreboardView'],
    function ($,
         _,
         Backbone,
         asyncErrorHandler,
         ScoreboardView) {
        'use strict'
        return {
            initApp: function () {
                scoreboard = new ScoreboardView();
            }
        };
    });