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
			this.baseUrlRoot = 'attack?Screen=';//
		},
		loadData: function(options) {
			this.urlRoot = this.baseUrlRoot + +options.screen + '&menu=' + options.menu;
			var self=this;
			this.fetch().then(function(data) {
				self.setContent(data);
			});
			// 	success: function(content) {
			// 		console.log("content:" + content);
			// 	},
			// 	error: function(err) {
			// 		console.log("error:" + err);
			// 	},
			// 	done: function(a,b) {
			// 		console.log(a);
			// 		console.log(b);
			// 	}
			// });
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