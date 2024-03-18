define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.Model.extend({
		id: 'label-status',
		url: 'service/debug/labels.mvc',
		
		label: '',
		labels: {
			enable: 'Enable label debugging',
			disable: 'Disable label debugging'
		},
		
		initialize: function() {
			this.load();
		},
		
		fetch: function(options) {
            options || (options = {});
            var data = (options.data || {});
            if(this.enabled != undefined) {
            	options.data = { enabled: !this.enabled };
            }
            return Backbone.Collection.prototype.fetch.call(this, options);
		},
		
		load: function () {
			this.fetch().then(this.labelStatusLoaded.bind(this));
		},

		labelStatusLoaded: function(data) {
			this.enabled = data.enabled;
			this.label = this.enabled ? this.labels['disable'] : this.labels['enable'];
		}

	});
});
