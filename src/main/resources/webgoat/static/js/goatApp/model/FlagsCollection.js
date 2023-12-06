define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/FlagModel'],
	function($,
		_,
		Backbone,
		FlagModel) {
	return Backbone.Collection.extend({
		url:'scoreboard-data',
		model:FlagModel
	});
});
