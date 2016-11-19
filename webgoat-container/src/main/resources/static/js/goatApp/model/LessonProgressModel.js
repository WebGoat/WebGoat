define(['jquery',
        'underscore',
        'backbone'],
    function ($,
              _,
              Backbone) {
        return Backbone.Model.extend({
            url: 'service/lessonprogress.mvc',
            completed: function () {
                this.fetch();
            }
        });
    });