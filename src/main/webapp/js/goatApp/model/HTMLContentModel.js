define(['jquery',
	'underscore',
	'backbone'],
	function($,_,Backbone) {
	//TODO: make a base class to extend for items with 'traditional data' (e.g. LessonContentData, this ... others?)
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
				self.onModelLoaded();
			});
		},
		setContent: function(content) {
			this.set('content',content);
			this.trigger('loaded');
		},
		onModelLoaded: function() {
			this.checkNullModel();
		}
	});
});