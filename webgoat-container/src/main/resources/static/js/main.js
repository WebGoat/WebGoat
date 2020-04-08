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

/**
 * Main configuration for RequireJS. Referred to from Spring MVC /start.mvc using main_new.html template.
 * baseURL is the base path of all JavaScript libraries.
 * paths refers to the JavaScript Libraries that we want to use. A name and relative path is used. Extension .js is not required.
 * 
 * jquery is a library that can easily access all objects on the HTML page.
 * jquery-ui is an UI extension on jquery and adds stuff like dialog boxes.
 * underscore contains a library of helper methods.
 * backbone contains models, events, key value bindings. Depends on jQuery and underscore
 * polyglot is a tiny i18n helper library
 */
require.config({
  baseUrl: "js/",
  paths: {
    jquery: 'libs/jquery.min',
    jqueryuivuln: 'libs/jquery-ui-1.10.4',
    jqueryui: 'libs/jquery-ui.min',
    underscore: 'libs/underscore-min',
    backbone: 'libs/backbone-min',
    text: 'libs/text',
    templates: 'goatApp/templates',
    polyglot: 'libs/polyglot.min'
  },

  map: {
	    'libs/jquery-base' : {'jquery':'libs/jquery.min'},
	    'libs/jquery-vuln' : {'jquery':'libs/jquery-2.1.4.min'}
  },


  shim: {
	"jqueryui": {
	  exports:"$",
	  deps: ['libs/jquery-base']
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

/*
 * Load and init the GoatApp. which is added here as a ADM asynchronous module definition.
 */
require([
	'jquery',
	'libs/jquery-base',
	'libs/jquery-vuln',
	'jqueryui', 
	'underscore',
	'backbone',
	'goatApp/goatApp'], function($,jqueryBase,jqueryVuln,jqueryui,_,Backbone,Goat){
    Goat.initApp();
});