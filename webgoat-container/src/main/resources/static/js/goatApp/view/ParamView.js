define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/ParamModel'],
function($,
	_,
	Backbone,
	ParamModel) {
	return Backbone.View.extend({
		el:'#params-view',

		initialize: function(options) {
			this.model = options.model;
			if (options.model)	{
				this.listenTo(this.model,'change',this.render);
			}
			this.render();
		},

		render: function() {
			this.$el.html('');
			var paramsTable = $('<table>',{'class':'param-table table-striped table-nonfluid'});
			var self = this;
			_.each(this.model.keys(), function(attribute) {
				var attributeLabel = attribute.replace(/Param/,'');
				var newRow = $('<tr>');
				newRow.append($('<th>',{text:_.escape(attributeLabel)}))
				newRow.append($('<td>',{text:_.escape(self.model.get(attribute))}));
				paramsTable.append(newRow);
			});
			
			this.$el.append($('<h4>',{text:'Parameters'}));
			this.$el.append(paramsTable);
		}
	});
});