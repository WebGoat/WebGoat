define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.Model.extend({
		initialize: function(options) {
			for (var key in options) {
				this.set(key, options.key);
			}
		}
	});
});