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
		    this.template = ScoreboardTemplate,
		    this.collection = new FlagsCollection();
		    this.listenTo(this.collection,'reset',this.render)
		    this.collection.fetch({reset:true});
		},

		render: function() {
			//this.$el.html('test');
			var t = _.template(this.template);
            this.$el.html(t({'rankings':this.collection.toJSON()}));
            setTimeout(this.pollData.bind(this), 5000);
		},

		pollData: function() {
		    this.collection.fetch({reset:true});
		}
	});
});
