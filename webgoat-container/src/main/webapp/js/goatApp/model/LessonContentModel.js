define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HTMLContentModel'],
	 function($,
	 	_,
	 	Backbone,
	 	HTMLContentModel){

	return HTMLContentModel.extend({
		urlRoot:null,
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function (options) {
			this.screenParam = null;
			this.menuParam = null;
			this.stageParam = null;
			this.baseUrlRoot = 'attack?Screen=';
		},
		loadData: function(options) {
			this.urlRoot = this.baseUrlRoot  +options.screen + '&menu=' + options.menu + '&stage=' + options.stage;
			this.set('menuParam',options.menu);
			this.set('screenParam',options.screen);
			this.set('stageParam',options.stage)
			var self=this;
			this.fetch().then(function(data) {
				self.setContent(data);
			});
		},
		setContent: function(content) {
			this.set('content',content);
			this.trigger('contentLoaded');
		},
		fetch: function (options) {
			options = options || {};
			return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
		}
	});
});