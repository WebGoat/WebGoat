define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonCookieModel'],
	function($,
		_,
		Backbone,
		LessonCookieModel) {
	return Backbone.Collection.extend({
		url:'service/cookie.mvc',
		model:LessonCookieModel
	});
});