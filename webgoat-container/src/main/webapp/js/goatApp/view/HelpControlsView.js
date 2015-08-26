define(['jquery',
	'underscore',
	'backbone'],
function($,_,Backbone) {
	return Backbone.View.extend({
		el:'#help-controls', //Check this
		helpButtons: {
			//TODO: move this into a template
			showSource:$('<button>',{id:'show-source-button','class':'btn btn-primary btn-xs help-button',type:'button',text:'Java Source'}),
			showSolution:$('<button>',{id:'show-solution-button','class':'btn btn-primary btn-xs help-button',type:'button',text:'Solution'}),
			showPlan:$('<button>',{id:'show-plan-button','class':'btn btn-primary btn-xs help-button',type:'button',text:'Lesson Plan'}),
			showHints:$('<button>',{id:'show-hints-button','class':'btn btn-primary btn-xs help-button',type:'button',text:'Hints'}),
			restartLesson:$('<button>',{id:'restart-lesson-button','class':'btn btn-xs help-button',type:'button',text:'Restart Lesson'})
		},
		initialize: function (options) {
			if (!options) {
				return;
			}
			this.hasPlan = options.hasPlan;
			this.hasSolution = options.hasSolution;
			this.hasSource = options.hasSource;
			this.hasHints = options.hasHints;
		},
		render:function(title) {
			this.$el.html();
			
			if (this.hasSource) {
				this.helpButtons.showSource.unbind().on('click',_.bind(this.showSource,this));
				this.$el.append(this.helpButtons.showSource);
			}
			if (this.hasSolution) {
				this.helpButtons.showSolution.unbind().on('click',_.bind(this.showSolution,this));
				this.$el.append(this.helpButtons.showSolution);
			}
			if (this.hasPlan) {
				this.helpButtons.showPlan.unbind().on('click',_.bind(this.showPlan,this));
				this.$el.append(this.helpButtons.showPlan);
			}
			if (this.hasHints) {
				this.helpButtons.showHints.unbind().on('click',_.bind(this.showHints,this));
				this.$el.append(this.helpButtons.showHints);
			}
			
			this.helpButtons.restartLesson.unbind().on('click',_.bind(this.restartLesson,this));
			this.$el.append(this.helpButtons.restartLesson);
		},

		showSource: function() {
			this.trigger('source:show','source');
		},

		showSolution: function() {
			this.trigger('solution:show','solution');
		},

		showPlan: function() {
			this.trigger('plan:show','plan');
		},

		showHints: function() {
			this.trigger('hints:show','hints');
		},
		restartLesson: function() {
			this.trigger('lesson:restart');
		}
	});
});