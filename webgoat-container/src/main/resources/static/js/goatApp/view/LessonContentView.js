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
            this.initPagination();
        },

        makeFormsAjax: function () {
            // will bind all forms with attack-form class
            var self = this;
            $("form.attack-form").each(function(form) {
                var options = {
                    success:self.onAttackExecution.bind(this),
                    url: this.action,
                    type:this.method,
                    // additional options
                };
                $(this).ajaxForm(options);
            });
        },

        initPagination: function() {
            //get basic pagination info
            this.currentPage = 0;
            this.$contentPages = this.$el.find('.lesson-page-wrapper');
            this.numPages = this.$contentPages.length;
            //
            if (this.numPages > 1) {
                //no animation on init
                this.$contentPages.hide();
                this.$el.find(this.$contentPages[this.currentPage]).show();
                this.addPaginationControls();
            }
         },

//        makeFormsAjax: function() {
//            var options = {
//                success:this.onAttackExecution.bind(this),
//                url: this.model.urlRoot.replace('\.lesson','.attack'),
//                type:'GET'
//                // $.ajax options can be used here too, for example:
//                //timeout:   3000
//            };
//            //hook forms //TODO: clarify form selectors later
//            $("form.attack-form").ajaxForm(options);
//        },

        makeFormsAjax: function () {
            this.$form = $('form.attack-form');
            // turn off standard submit

            //set standard options
            var contentType = (this.$form.attr('contentType')) ? this.$form.attr('contentType') : 'url-form-encoded';
            this.formOptions = {
                //success:this.reLoadView.bind(this),
                url: this.$form.attr('action'),
                method: this.$form.attr('method'),
                contentType: contentType,
                timeout: 3000, //usually running locally ... should be plenty faster than this

            };

//            if (typeof this.$form.attr('prepareData') === 'string') {
//                if (typeof this.$form.attr('prepareData') !== 'undefined' && typeof CustomGoat[this.$form.attr('prepareData')] === 'function') { // I'm sure this is dangerous ... but hey, it's WebGoat, right?
//                    this.formOptions.prepareData = CustomGoat[this.$form.attr('prepareData')];
//                }
//            }
//          set up submit to run via ajax and be handled by the success handler
            this.$form.submit(this.onFormSubmit.bind(this));

        },

         onFormSubmit: function () {
            var self = this;
            console.log(this.formOptions);
            var submitData = (typeof this.formOptions.prepareData === 'function') ? this.formOptions.prepareData() : this.$form.serialize();

            $.ajax({
                data:submitData,
                url:this.formOptions.url,
                method:this.formOptions.method,
                contentType:this.formOptions.contentType,
                data: submitData
            }).success(function(data) {
                //Log shows warning, see https://bugzilla.mozilla.org/show_bug.cgi?id=884693
                // Explicitly loading the lesson instead of triggering an
                // event in goatRouter.navigate().
                self.reLoadView(data);
            });
            return false;
         },

        ajaxifyAttackHref: function() {  // rewrite any links with hrefs point to relative attack URLs             
            var self = this;
            // The current LessonAdapter#getLink() generates a hash-mark link.  It will not match the mask below.
            // Besides, the new MVC code registers an event handler that will reload the lesson according to the route.

            $('form').submit(function(event){
                $.get(this.action, "json")
                    //.done(self.reLoadView.bind(self))
                    .fail(function() { alert("failed to GET " + url); });
            });

        },

        onAttackExecution: function(feedback) {
            console.log('attack executed')
            this.renderFeedback(feedback);
        },

        renderFeedback: function(feedback) {
            this.$el.find('feedback').html(feedback);
        },

        addPaginationControls: function() {
            this.$prevPageButton = $('<span>',{class:'glyphicon-class glyphicon glyphicon-circle-arrow-left show-prev-page'});
            this.$prevPageButton.unbind().on('click',this.decrementPageView.bind(this));

            this.$nextPageButton = $('<span>',{class:'glyphicon-class glyphicon glyphicon-circle-arrow-right show-next-page'});
            this.$nextPageButton.unbind().on('click',this.incrementPageView.bind(this));

            var pagingControlsDiv = $('<div>',{class:'panel-body', id:'lessong-page-controls'});
            pagingControlsDiv.append(this.$prevPageButton);
            pagingControlsDiv.append(this.$nextPageButton);
            this.$el.append(pagingControlsDiv);
            this.$prevPageButton.hide()
        },

        incrementPageView: function() {
            if (this.currentPage < this.numPages -1) {
               this.currentPage++;
               this.showCurContentPage(true);
            }

            if (this.currentPage >= this.numPages -1) {
                this.$nextPageButton.hide();
                this.$prevPageButton.show()
            }
        },

        decrementPageView: function() {
            if (this.currentPage > 0) {
                this.currentPage--;
                this.showCurContentPage(false);
            }

            if (this.currentPage == 0) {
                this.$prevPageButton.hide();
                this.$nextPageButton.show();
            }

        },

        showCurContentPage: function(isIncrement) {
            this.$contentPages.hide();
            this.$el.find(this.$contentPages[this.currentPage]).show();
        },

        hideNextPageButton: function() {

        }


    });

    
});
