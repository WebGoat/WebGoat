define(['jquery',
        'underscore',
        'backbone'],
    function($,
             _,
             Backbone) {
        return Backbone.View.extend({
            el:'#error-notification-container',
            initialize: function() {
                Backbone.on("error:unhandled", this.showNotification.bind(this));
                this.hideNotification();
                this.currentTimeout = null;
            },

            showNotification: function() {
                var self = this;
                if (!this.$el.is(':visible')) {
                    this.$el.show(350);
                }
                if (this.currentTimeout != null) {
                    window.clearTimeout(this.currentTimeout);
                }
                this.currentTimeout = window.setTimeout(function() {
                    self.hideNotification();
                    self.currentTimeout = null;
                }, 3000);
            },

            hideNotification: function() {
                if (this.$el.is(':visible')) {
                    this.$el.hide(350);
                }
            }
        });
    });
