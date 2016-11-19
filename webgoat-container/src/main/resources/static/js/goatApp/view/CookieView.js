define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/CookieCollection'],
function($,
	_,
	Backbone,
	CookieCollection) {
	return Backbone.View.extend({
		el:'#cookies-view',

		initialize: function() {
			this.collection = new CookieCollection();
			this.listenTo(this.collection,'reset',this.render)
			this.collection.fetch({reset:true});
		},

		render: function() {
			this.$el.html('')
			var cookieTable;
			this.collection.each(function(model) {
				cookieTable = $('<table>',{'class':'cookie-table table-striped table-nonfluid'});
    			_.each(model.keys(), function(attribute) {
    				var newRow = $('<tr>');
    				newRow.append($('<th>',{text:_.escape(attribute)}))
    				newRow.append($('<td>',{text:_.escape(model.get(attribute))}));
    				cookieTable.append(newRow);
    			});
			});
			this.$el.append($('<h4>',{text:'Cookie/s'}));
			this.$el.append(cookieTable);
		}
	});
});