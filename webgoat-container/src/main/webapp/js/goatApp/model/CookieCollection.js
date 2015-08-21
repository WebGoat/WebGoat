define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/CookieModel'],
	function($,
		_,
		Backbone,
		CookieModel) {
	return Backbone.Collection.extend({
		url:'service/cookie.mvc',
		model:CookieModel
	});
});