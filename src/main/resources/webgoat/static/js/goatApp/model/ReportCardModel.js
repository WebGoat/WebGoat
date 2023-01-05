define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.Model.extend({
	    url: 'service/reportcard.mvc'
	});
});