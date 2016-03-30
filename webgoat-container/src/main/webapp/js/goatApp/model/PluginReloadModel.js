define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.Model.extend({
		url: 'service/reloadplugins.mvc',
		id: 'reload-plugins',
		label: 'Reload plugins',
		
		load: function () {
			this.fetch().then(this.pluginsLoaded.bind(this));
		},

		pluginsLoaded: function(data) {
			this.trigger('plugins:loaded', this, data);
		}

	});
});
