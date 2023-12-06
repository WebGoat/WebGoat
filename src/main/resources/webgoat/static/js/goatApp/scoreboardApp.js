define(['underscore',
        'goatApp/support/goatAsyncErrorHandler',
        'goatApp/view/ScoreboardView'],
    function (
         _,
         asyncErrorHandler,
         ScoreboardView) {
        'use strict'
        class ScoreboardApp {
            initApp() {
                asyncErrorHandler.init();
                this.scoreboard = new ScoreboardView();
            }
        }
        return new ScoreboardApp();
    });
