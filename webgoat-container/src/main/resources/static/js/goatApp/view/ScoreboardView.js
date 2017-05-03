define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/FlagsCollection',
	'text!templates/scoreboard.html'],
function($,
	_,
	Backbone,
	FlagsCollection,
	ScoreboardTemplate) {
	return Backbone.View.extend({
		el:'#scoreboard',

		initialize: function() {
		    template:ScoreboardTemplate,
			this.collection = new FlagsCollection();
			this.listenTo(this.collection,'reset',this.render)
			this.collection.fetch({reset:true});
		},

		render: function() {
			//this.$el.html('test');
			var t = _.template(this.template);
            this.$el.html(t({'flags':this.collection.toJSON()}));
			//TODO: add template (table) to iterate over ...
			//this.collection.toJSON(); << put that in the template data
			//TODO: set up next poll here with listenToOnce
		},

		pollData: function() {
		    this.collection.fetch({reset:true});
		}
	});
});