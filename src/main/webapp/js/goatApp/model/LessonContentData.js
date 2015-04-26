define(['jquery', 'underscore','backbone'], function($,_,Backbone){

	return Backbone.Model.extend({
		urlRoot:null,
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function (options) {
			this.baseUrlRoot = 'attack?Screen=';//
		},
		loadData: function(options) {
			this.urlRoot = this.baseUrlRoot + +options.screen + '&menu=' + options.menu;
			var self = this;
			this.fetch().then(function(content){
				alert('content loaded');
				self.content = content;
				self.trigger('contentLoaded');
			});
		}
	});
});