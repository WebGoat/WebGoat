define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonCookieCollection'],
function($,
	_,
	Backbone,
	LessonCookieCollection) {
	return Backbone.View.extend({
		el:'#cookies-view',

		initialize: function() {
			this.collection = new LessonCookieCollection();
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
			this.$el.append(cookieTable);
		}
	});
});