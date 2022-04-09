define([
	'backbone',
	'goatApp/model/AssignmentStatusModel'
	],
	function(
		Backbone,
		AssignmentStatusModel) {
	return Backbone.Collection.extend({
	    //tagName: 'ul',
		url: 'service/lessonoverview.mvc',
		model: AssignmentStatusModel
	});
});

