define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/PluginReloadModel',
	'goatApp/model/LabelDebugModel'],
function(
	$,
	_,
	Backbone,
	PluginReloadModel,
	LabelDebugModel) {
	return Backbone.View.extend({
		el: '#developer-controls',
	    
	    onControlClick: function(model) {
	    	$('#' + model.id).find('td').text('Loading...');
	    	model.load();
	    },
	    
	    onPluginsLoaded: function(model) {
	    	window.location.href = 'welcome.mvc';
	    },
	    
	    onLabelsLoaded: function(model) {
	    	$('#' + model.id).find('td').text('Enabled: ' + model.enabled);
	    	this.models[1] = model;
	    	this.render();
	    },

		initialize: function(options) {
			this.models = [new PluginReloadModel(), new LabelDebugModel()];
			this.listenTo(this.models[0], 'plugins:loaded', this.onPluginsLoaded);
			this.listenTo(this.models[1], 'plugins:loaded', this.onLabelsLoaded);
			this.render();
		},

		render: function() {
			this.$el.html('');
			var table = $('<table>',{'class':'developer-controls-table table-nonfluid'});
			var self = this;
			_.each(this.models, function(model) {
				var newRow = $('<tr>', { id: model.id });
				var headerCell = $('<th>')
				var statusCell = $('<td>')
				
				var link = $('<a>', {
					'text': model.label,
					'title': model.label
				});
				link.click(_.bind(self.onControlClick, self, model));
				
				newRow.append(headerCell.append(link));
				newRow.append(statusCell);
				table.append(newRow);
			});
			
			this.$el.append(table);
		}
	});
});
