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
            this.$el.find('.lesson-content').html(this.model.get('content'));
            this.$el.find('.attack-feedback').hide();
            this.$el.find('.attack-output').hide();
            this.makeFormsAjax();
            //this.ajaxifyAttackHref();
            $(window).scrollTop(0); //work-around til we get the scroll down sorted out
            this.initPagination();
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


        makeFormsAjax: function () {
            this.$form = $('form.attack-form');
            // turn off standard submit
            var self = this;
            // each submit handled per form
            this.$form.each( function() {
                $(this).submit(self.onFormSubmit.bind(self));
            });
        },

         onFormSubmit: function (e) {
            var curForm = e.currentTarget; // the form from which the
            var self = this;
            // TODO custom Data prep for submission
            var prepareDataFunctionName = $(curForm).attr('prepareData');
            var submitData = (typeof webgoat.customjs[prepareDataFunctionName] === 'function') ? webgoat.customjs[prepareDataFunctionName]() : this.$form.serialize();
            // var submitData = this.$form.serialize();
            this.$curFeedback = $(curForm).closest('.lesson-page-wrapper').find('.attack-feedback');
            this.$curOutput = $(curForm).closest('.lesson-page-wrapper').find('.attack-output');
            var formUrl = $(curForm).attr('action');
            var formMethod = $(curForm).attr('method');
            var contentType = ($(curForm).attr('contentType')) ? $(curForm).attr('contentType') : 'application/x-www-form-urlencoded; charset=UTF-8';
            $.ajax({
                //data:submitData,
                url:formUrl,
                method:formMethod,
                contentType:contentType,
                data: submitData
            }).then(self.onSuccessResponse.bind(self), self.onErrorResponse.bind(self));
            return false;
         },

         onSuccessResponse: function(data) {
            console.log(data);
            this.renderFeedback(data.feedback);
            // update menu if lessonCompleted is true
            this.renderOutput(data.output || "");
            return false;
         },

         onErrorResponse: function (a,b,c) {
            console.error(a);
            console.error(b);
            console.error(c);
            return false;
         },

        ajaxifyAttackHref: function() {  // rewrite any links with hrefs point to relative attack URLs
            var self = this;
            // instruct in template to have links returned with the attack-link class
            $('a.attack-link').submit(function(event){
                $.get(this.action, "json").then(self.onSuccessResponse, self.onErrorResponse);
             });
        },

        renderFeedback: function(feedback) {
            this.$curFeedback.html(feedback || "");
            this.$curFeedback.show(400)

        },

        renderOutput: function(output) {
            this.$curOutput.html(output || "");
            this.$curOutput.show(400)
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
