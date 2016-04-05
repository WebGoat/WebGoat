define(['jquery',
	'underscore',
	'backbone'],
	function($,_,Backbone) {
	return Backbone.Model.extend({
		//url:'service/lessonplan.mvc',
		fetch: function (options) {
			options = options || {};
			return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
		},

		loadData: function() {
			var self=this;
			this.fetch().then(function(data) {
				self.setContent(data);
			});
		},

		setContent: function(content) {
			this.set('content',content);
			this.checkNullModel();
			this.trigger('loaded');
		}
	});
});