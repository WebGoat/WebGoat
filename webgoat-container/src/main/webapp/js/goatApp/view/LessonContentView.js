//LessonContentView
define(['jquery',
	'underscore',
	'backbone',
	'libs/jquery.form'], 
	function(
		$,
		_,
		Backbone,
		JQueryForm) {
	return Backbone.View.extend({
		el:'#lesson-content-wrapper', //TODO << get this fixed up in DOM
		initialize: function(options) {
			options = options || {};
		},
		render: function() {
			this.$el.html(this.model.get('content'));
			this.makeFormsAjax();
			this.ajaxifyAttackHref();
		},
		//TODO: reimplement this in custom fashion maybe?
		makeFormsAjax: function () {
			var options = {
			    success:this.reLoadView.bind(this),
			    url:'attack?Screen=' + this.model.get('screenParam') + '&menu=' + this.model.get('menuParam'),
			    type:'GET'
	            // $.ajax options can be used here too, for example: 
	            //timeout:   3000 
			};
			//hook forms //TODO: clarify form selectors later
		    $("form").ajaxForm(options);
        },

        ajaxifyAttackHref: function() {  // rewrite any links with hrefs point to relative attack URLs             
        var self = this;
        	$.each($('a[href^="attack?"]'),function(i,el) {
        		var url = $(el).attr('href');
        		$(el).unbind('click').attr('href','#').attr('link',url);
        		//TODO pull currentMenuId
        		$(el).click(function() {
        			event.preventDefault();
        			var _url = $(el).attr('link');
        			$.get(_url, {success:self.reloadView.bind(self)});
        		});
        	});
		},

        reLoadView: function(content) {
        	this.model.setContent(content);
        	this.render();
        }
	});

	
});