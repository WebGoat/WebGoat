define([
            'backbone',
            'goatApp/model/AssignmentStatusModel'
        ],
        function (
                Backbone,
                AssignmentStatusModel) {
            return Backbone.Collection.extend({
                url: function () {
                    return 'service/lessonoverview.mvc/' + this.lesson;
                },

                model: AssignmentStatusModel,

                initialize: function (options) {
                    //TODO: Not the best way to do this, but it works for now.
                    this.lesson = options.baseLessonUrl.substring(options.baseLessonUrl.lastIndexOf('/') + 1);
                },
            });
        });
