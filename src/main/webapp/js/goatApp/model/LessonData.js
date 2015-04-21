define(['jquery', 'underscore','backbone'], function($,_,Backbone){

	return Backbone.Model.extend({
		urlRoot:null,
		defaults: {
			items:null,
			selectedItem:null
		},
		initialize: function (options) {
			var self = this;
			this.urlRoot = 'attack.jsp?Screen='+options.screen + '&menu=' + options.menu;
			this.fetch().then(function(content){
				self.lessonContent = content
			});
		},
		getHint: function (i) {

		}
	});
});