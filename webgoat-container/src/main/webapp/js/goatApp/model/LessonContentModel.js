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
            this.scrParam = null;
            this.menuParam = null;
            this.stageParam = null;
            this.numParam = null;
            this.baseUrlRoot = 'attack';
        },
        loadData: function(options) {
            this.urlRoot = this.baseUrlRoot + "?Screen=" + options.scr + '&menu=' + options.menu;
            if (options.stage != null) {
               this.urlRoot += '&stage=' + options.stage;
            }
            if (options.num != null) {
               this.urlRoot += '&Num=' + options.num;
            }
            this.set('menuParam', options.menu);
            this.set('scrParam', options.scr);
            this.set('stageParam', options.stage)
            this.set('numParam', options.num)
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
            this.trigger('content:loaded',this,loadHelps);
        },

        fetch: function (options) {
            options = options || {};
            return Backbone.Model.prototype.fetch.call(this, _.extend({ dataType: "html"}, options));
        }
    });
});
