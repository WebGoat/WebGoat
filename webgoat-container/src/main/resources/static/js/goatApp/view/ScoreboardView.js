define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/FlagsCollection'],
function($,
	_,
	Backbone,
	FlagsCollection) {
	return Backbone.View.extend({
		el:'#scoreboard',

		initialize: function() {
			this.collection = new FlagsCollection();
			this.listenTo(this.collection,'reset',this.render)
			this.collection.fetch({reset:true});
		},

		render: function() {
			this.$el.html('test')
			//TODO: add template (table) to iterate over ...
			//this.collection.toJSON(); << put that in the template data
			//TODO: set up next poll here with listenToOnce
		}
	});
});