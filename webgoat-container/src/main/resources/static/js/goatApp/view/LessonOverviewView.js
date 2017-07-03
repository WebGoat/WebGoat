define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/LessonOverviewCollection',
    'text!templates/lesson_overview.html'],
function($,
	_,
	Backbone,
	LessonOverviewCollection,
	LessonOverviewTemplate) {
	return Backbone.View.extend({
	    template: LessonOverviewTemplate,
		el:'#lesson-overview',
		 initialize: function (lessonOverviewCollection) {
			this.collection = lessonOverviewModel;
			this.listenTo(this.collection, 'change add remove update reset', this.render);
			this.hideLessonOverview();
		},

        events: {
            "click a": "clickedAssignment"
        },

        clickedAssignment: function(e){
            e.preventDefault();
            var id = $(e.currentTarget).data("id");
            Backbone.trigger('assignment:navTo',{'assignment': id});
        },

        showAssignments: function() {
            this.$el.html('');
            var t = _.template(this.template);
            this.$el.html(t({"assignments" : this.model.toJSON()}));
        },

		render: function() {
		    if (this.isVisible()) {
        	    this.$el.hide();
        	} else {
        		this.$el.show();
        	}
        	//this.showAssignments();

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