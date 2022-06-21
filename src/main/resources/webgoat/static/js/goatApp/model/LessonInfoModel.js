define(['jquery',
	'underscore',
	'backbone'],
	 function($,
	 	_,
	 	Backbone){

	return Backbone.Model.extend({
		url:'service/lessoninfo.mvc',

		initialize: function (options) {
			this.fetch().then(this.infoLoaded.bind(this));
		},

		infoLoaded: function(data) {
			this.trigger('info:loaded',this,data);
		}

	});
});