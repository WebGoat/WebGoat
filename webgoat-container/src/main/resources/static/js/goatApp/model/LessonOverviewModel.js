define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.Collection.extend({
	    tagName: 'ul',
		url: 'service/lessonoverview.mvc'
	});
});

