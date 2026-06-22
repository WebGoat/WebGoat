define(['jquery',
	'underscore',
	'backbone'],
	 function($,
	 	_,
	 	Backbone){

	return Backbone.Model.extend({
		url: function() { return 'service/lessoninfo.mvc/' + this.lesson; },

		initialize: function (options) {
            this.lesson = options.lesson;
			this.fetch().then(this.infoLoaded.bind(this));
		},

		infoLoaded: function(data) {
			this.trigger('info:loaded',this,data);
		}
	});
});
