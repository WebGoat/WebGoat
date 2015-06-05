//LessonContentView
define(['jquery',
	'underscore',
	'backbone',
	'libs/jquery.form',
	'goatApp/model/LessonContentData'], 
function($,_,Backbone,JQueryForm,LessonData) {
	return Backbone.View.extend({
		el:'#lessonContentWrapper', //TODO << get this fixed up in DOM
		initialize: function(options) {
			options = options || {};
		},
		render: function() {
			//alert('render');
			this.$el.html(this.model.get('content'));
			this.makeFormsAjax();
		},
		//TODO: reimplement this in custom fashion maybe?
		makeFormsAjax: function () {
			var options = {
			    //target: '#lesson_content', // target element(s) to be updated with server response                     
			    //beforeSubmit: GoatUtils.showRequest, // pre-submit callback, comment out after debugging 
			    //success: GoatUtils.showResponse  // post-submit callback, comment out after debugging 
			    success:this.reLoadView.bind(this),
			    url:'attack?Screen=' + this.model.get('screenParam') + '&menu=' + this.model.get('menuParam'),
			            // other available options: 
			            //url:       url         // override for form's 'action' attribute 
			            //type:      type        // 'get' or 'post', override for form's 'method' attribute 
			            //dataType:  null        // 'xml', 'script', or 'json' (expected server response type) 
			            //clearForm: true        // clear all form fields after successful submit 
			            //resetForm: true        // reset the form after successful submit 

			            // $.ajax options can be used here too, for example: 
			            //timeout:   3000 
			};
			//hook forms //TODO: clarify form selectors later
		    $("form").ajaxForm(options);
        },
        reLoadView: function(content) {
        	this.model.setContent(content);
        	this.render();
        }
	});

	
});