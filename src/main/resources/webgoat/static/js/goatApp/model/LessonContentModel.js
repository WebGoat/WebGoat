define(['jquery',
    'underscore',
    'backbone',
    'goatApp/model/HTMLContentModel'],
     function($,
        _,
        Backbone,
        HTMLContentModel){

    return HTMLContentModel.extend({
        urlRoot:null,
        defaults: {
            items:null,
            selectedItem:null
        },

        initialize: function (options) {

        },

        loadData: function(options) {
            this.urlRoot = _.escape(encodeURIComponent(options.name)) + '.lesson'
            var self = this;
            this.fetch().done(function(data) {
                self.setContent(data);
            });
        },

        setContent: function(content, loadHelps) {
            if (typeof loadHelps === 'undefined') {
                loadHelps = true;
            }

            // Cache URL locally to avoid repeated property access
            var currentUrl = String(document.URL || '');

            this.set('content',content);
            this.set('lessonUrl', currentUrl.replace(/\.lesson.*/,'\.lesson'));

            // Use a more efficient, anchored pattern without backtracking-prone constructs
            var pageNumMatch = currentUrl.match(/\.lesson\/(\d{1,4})$/);
            if (pageNumMatch) {
                this.set('pageNum', pageNumMatch[1]);
            } else {
                this.set('pageNum',0);
            }

            this.trigger('content:loaded',this,loadHelps);
        },

        fetch: function (options) {
            options = options || {};
            return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
        }
    });
});
