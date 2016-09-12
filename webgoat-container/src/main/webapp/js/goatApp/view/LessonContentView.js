//LessonContentView
define(['jquery',
    'underscore',
    'backbone',
    'libs/jquery.form'], 
    function(
        $,
        _,
        Backbone,
        JQueryForm) {
    return Backbone.View.extend({
        el:'#lesson-content-wrapper', //TODO << get this fixed up in DOM

        initialize: function(options) {
            options = options || {};
        },

        render: function() {
            this.$el.html(this.model.get('content'));
            this.makeFormsAjax();
            this.ajaxifyAttackHref();
            $(window).scrollTop(0); //work-around til we get the scroll down sorted out
        },
        
        //TODO: reimplement this in custom fashion maybe?
        makeFormsAjax: function () {
            var $form = $('form');
            var options = {
                success:this.reLoadView.bind(this),
                url: this.model.urlRoot,
                type: $form.attr('method')
                // $.ajax options can be used here too, for example: 
                //timeout:   3000 
            };
            //hook forms //TODO: clarify form selectors later
            $form.ajaxForm(options);
        },

        ajaxifyAttackHref: function() {  // rewrite any links with hrefs point to relative attack URLs             
            var self = this;
            // The current LessonAdapter#getLink() generates a hash-mark link.  It will not match the mask below.
            // Besides, the new MVC code registers an event handler that will reload the lesson according to the route.
            $.each($('a[href^="attack?"]'),function(i,el) {
                var url = $(el).attr('href');
                $(el).unbind('click').attr('href','#').attr('link',url);
                //TODO pull currentMenuId
                $(el).click(function(event) {
                    event.preventDefault();
                    var _url = $(el).attr('link');
                    console.log("About to POST " + _url);
                    $.post(_url)
                        .done(self.reLoadView.bind(self))
                        .fail(function() { alert("failed to POST " + _url); });
                });
            });
        },

        reLoadView: function(content) {
            this.model.setContent(content);
            this.render();
        }
    });

    
});
