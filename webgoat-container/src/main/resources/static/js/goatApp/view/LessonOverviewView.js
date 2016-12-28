define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonOverviewModel',
	'goatApp/view/AssignmentOverview'],
function($,
	_,
	Backbone,
	LessonOverviewModel,
	AssignmentOverview) {
	return Backbone.View.extend({
		el:'#lesson-overview',
		 initialize: function (lessonOverviewModel) {
			this.model = lessonOverviewModel;
			this.listenTo(this.model, 'change add remove update', this.render);
			this.hideLessonOverview();
		},

        showAssignments: function() {
            this.$el.html('');
      		this.model.each(function(assignment) {
      	        var assignmentView = new AssignmentOverview({ model: assignment });
                this.$el.append(assignmentView.render().el);
           	}, this);
        },

		render: function() {
		    if (this.isVisible()) {
        	    this.$el.hide();
        	} else {
        		this.$el.show();
        	}
        	this.showAssignments();

       		return this;
       	},

        isVisible: function() {
            return this.$el.is(':visible');
        },

		hideLessonOverview: function() {
			if (this.$el.is(':visible')) {
				this.$el.hide();
			}
		}
	});
});