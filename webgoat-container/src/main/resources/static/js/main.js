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
    jquery: 'libs/jquery-2.2.4.min',
    underscore: 'libs/underscore-min',
    backbone: 'libs/backbone-min',
    text: 'libs/text',
    templates: 'goatApp/templates',
    polyglot: 'libs/polyglot.min'
  },

  map: {
    'libs/jquery-base' : {'jquery':'libs/jquery-2.2.4.min'},
    'libs/jquery-vuln' : {'jquery':'libs/jquery-2.1.4.min'}
  },

  shim: {
    underscore: {
      exports: "_"
    },
    backbone: {
      deps: ['underscore', 'jquery'],
      exports: 'Backbone'
    }
  }
});

require(['jquery','libs/jquery-base','libs/jquery-vuln','underscore','backbone','goatApp/goatApp'], function($,jqueryBase,jqueryVuln,_,Backbone,Goat){
    Goat.initApp();
});