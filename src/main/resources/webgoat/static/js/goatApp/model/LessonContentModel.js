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
            this.set('content',content);

            // Extract base lesson URL without using unbounded / complex regex
            var currentUrl = document.URL || '';
            var lessonIndex = currentUrl.indexOf('.lesson');
            if (lessonIndex !== -1) {
                this.set('lessonUrl', currentUrl.substring(0, lessonIndex) + '.lesson');
            } else {
                this.set('lessonUrl', currentUrl);
            }

            // Safely extract page number using simple parsing and bounded patterns
            var pageNum = 0;
            var lessonPathIndex = currentUrl.indexOf('.lesson/');
            if (lessonPathIndex !== -1) {
                var pagePart = currentUrl.substring(lessonPathIndex + '.lesson/'.length);
                // Only accept purely numeric page numbers with length 1–4
                if (/^[0-9]{1,4}$/.test(pagePart)) {
                    pageNum = parseInt(pagePart, 10);
                }
            }
            this.set('pageNum', pageNum);

            this.trigger('content:loaded',this,loadHelps);
        },

        fetch: function (options) {
            options = options || {};
            return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
        }
    });
});
