define(['jquery',
	'underscore',
	'backbone'],
function($,_,Backbone) {
	return Backbone.View.extend({
		el:'#help-controls', //Check this
		helpButtons: {
			//TODO: move this into a template
			showSource:$('<button>',{id:'showSourceBtn','class':'btn btn-primary btn-xs help-button',type:'button',text:'Java [Source]'}),
			showSolution:$('<button>',{id:'showSolutionBtn','class':'btn btn-primary btn-xs help-button',type:'button',text:'Solution'}),
			showPlan:$('<button>',{id:'showPlanBtn','class':'btn btn-primary btn-xs help-button',type:'button',text:'Lesson Plan]'}),
			showHints:$('<button>',{id:'showHintsBtn','class':'btn btn-primary btn-xs help-button',type:'button',text:'Hints'}),
			restartLesson:$('<button>',{id:'restartLessonBtn','class':'btn btn-xs help-button',type:'button',text:'Restart Lesson'})
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
			if (this.hasSource) {
				this.$el.append(this.helpButtons.showSource);
			}
			if (this.hasSolution) {
				this.$el.append(this.helpButtons.showSolution);
			}
			if (this.hasPlan) {
				this.$el.append(this.helpButtons.showPlan);
			}
			if (this.hasHints) {
				this.$el.append(this.helpButtons.showHints);
			}
			//
			this.$el.append(this.helpButtons.restartLesson);
		}
	});
});