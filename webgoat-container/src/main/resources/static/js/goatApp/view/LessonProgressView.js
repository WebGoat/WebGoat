define(['jquery',
        'underscore',
        'backbone',
        'goatApp/model/LessonProgressModel'],
    function ($,
              _,
              Backbone,
              LessonProgressModel) {
        return Backbone.View.extend({
            el: '#lesson-progress',
            initialize: function (lessonProgressModel) {
                this.model = lessonProgressModel;

                if (this.model) {
                    this.listenTo(this.model, 'change', this.render);
                }
            },
            render: function () {
                if (this.model.get("lessonCompleted")) {
                    this.$el.html(this.model.get('successMessage'));
                } else {
                    this.$el.html("");
                }
            }
        });
    });