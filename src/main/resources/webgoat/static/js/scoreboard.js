//main.js
/*
/js
js/main.js << main file for require.js
--/libs/(jquery,backbone,etc.) << base libs
--/goatApp/ << base dir for goat application, js-wise
--/goatApp/model
--/goatApp/view
--/goatApp/support
--/goatApp/controller
*/

require.config({
  baseUrl: "js/",
  paths: {
    jquery: 'libs/jquery.min',
    jqueryvuln: 'libs/jquery-2.1.4.min',
    jqueryuivuln: 'libs/jquery-ui-1.10.4',
    jqueryui: 'libs/jquery-ui.min',
    underscore: 'libs/underscore-min',
    backbone: 'libs/backbone-min',
    text: 'libs/text',
    templates: 'goatApp/templates',
    polyglot: 'libs/polyglot.min'
  },

  shim: {
	"jqueryui": {
	  exports:"$",
	  deps: ['jquery']
	},
    underscore: {
      exports: "_"
    },
    backbone: {
      deps: ['underscore', 'jquery'],
      exports: 'Backbone'
    }
  }
});

require(['underscore','backbone','goatApp/scoreboardApp'], function(_,Backbone,ScoreboardApp){
    ScoreboardApp.initApp();
});
