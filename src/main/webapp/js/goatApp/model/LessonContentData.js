define(['jquery',
	'underscore',
	'backbone'],
	 function($,_,Backbone){

	return Backbone.Model.extend({
		urlRoot:null,
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function (options) {
			this.screenParam = null;
			this.menuParam = null;
			this.baseUrlRoot = 'attack?Screen=';//
		},
		loadData: function(options) {
			this.urlRoot = this.baseUrlRoot + +options.screen + '&menu=' + options.menu;
			this.set('menuParam',options.menu);
			this.set('screenParam',options.screen);

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
			// var self=this;
			// Backbone.Model.prototype.fetch.apply(this, arguments).then(
			// 	function(content){
			// 		self.setContent(content);
			// 	});

			// //override with prototype
		}
	});
});