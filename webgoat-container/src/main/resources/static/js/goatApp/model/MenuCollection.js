define(['jquery',
        'underscore',
        'backbone',
        'goatApp/model/MenuModel'],
    function ($, _, Backbone, MenuModel) {

        return Backbone.Collection.extend({
            model: MenuModel,
            url: 'service/lessonmenu.mvc',

            initialize: function () {
                var self = this;
                this.fetch();
                //RZ not completely removed, but for now changed to once every minute. after each assignment the menu and content are always updated anyway
                setInterval(function () {
                    this.fetch()
                }.bind(this), 60000);
            },

            onDataLoaded: function () {
                this.trigger('menuData:loaded');
            },

            fetch: function () {
                var self = this;
                Backbone.Collection.prototype.fetch.apply(this, arguments).then(
                    function (data) {
                        this.models = data;
                        self.onDataLoaded();
                    }
                );
            }
        });
    });