define(['jquery',
	'underscore',
	'backbone'],
function($,_,Backbone) {
	return Backbone.View.extend({
		el:'#help-controls', //Check this

		initialize: function (options) {
			if (!options) {
				return;
			}
			this.hasPlan = options.hasPlan;
			this.hasSolution = options.hasSolution;
			this.hasSource = options.hasSource;
		},

        showHintsButton: function(nav) {
            this.$el.find('#show-hints-button').unbind().on('click',this.showHints.bind(this)).show();
        },

        hideHintsButton: function(){
           $('#show-hints-button').hide();
        },

		render:function() {
			this.$el.find('#restart-lesson-button').unbind().on('click',_.bind(this.restartLesson,this)).show();
		},

		showHints: function() {
		    this.trigger('hints:show','hint');
		},

		restartLesson: function() {
			this.trigger('lesson:restart');
		}
	});
});