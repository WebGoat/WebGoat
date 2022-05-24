define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/FlagModel'],
	function($,
		_,
		Backbone,
		FlagModel) {
	return Backbone.Collection.extend({
		url:'/WebGoat/scoreboard-data',
		model:FlagModel
	});
});