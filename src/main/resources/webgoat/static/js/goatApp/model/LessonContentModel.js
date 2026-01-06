define(['jquery',
    'underscore',
    'backbone',
    'goatApp/model/HTMLContentModel'],
     function($,
        _,
        Backbone,
        HTMLContentModel){

    // Utility functions to avoid overly-broad regex on full URLs and reduce ReDoS risk
    function getLessonUrlFromLocation(loc) {
        if (!loc || !loc.href) {
            return '';
        }
        // Work on pathname only, not the entire URL string
        var pathname = loc.pathname || '';
        // Ensure we only ever match the final `.lesson` segment in the path
        return pathname.replace(/\.lesson(?:\/.*/)?$/, '.lesson');
    }

    function getLessonPageNumberFromLocation(loc) {
        if (!loc || !loc.href) {
            return 0;
        }
        var pathname = loc.pathname || '';
        // Match optional trailing page number segment (1–4 digits) after `.lesson/`
        var match = pathname.match(/\.lesson\/(\d{1,4})$/);
        if (match && match[1]) {
            return match[1];
        }
        return 0;
    }

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

            // Use safer helpers that operate on pathname only, avoiding complex patterns on full URL
            this.set('lessonUrl', getLessonUrlFromLocation(window.location));
            this.set('pageNum', getLessonPageNumberFromLocation(window.location));

            this.trigger('content:loaded',this,loadHelps);
        },

        fetch: function (options) {
            options = options || {};
            return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
        }
    });
});
