//LessonContentView
define(['jquery',
    'underscore',
    'backbone',
    'libs/jquery.form',
    'goatApp/view/ErrorNotificationView'],
    function(
        $,
        _,
        Backbone,
        JQueryForm,
        ErrorNotificationView) {
    return Backbone.View.extend({
        el:'#lesson-content-wrapper', //TODO << get this fixed up in DOM

        initialize: function(options) {
            options = options || {};
            new ErrorNotificationView();
            var self = this;
            Backbone.on('assignment:navTo', function(assignment){
              var page = self.findPage(assignment);
              if (page != -1) {
                self.navToPage(page);
              }
            });
        },

        findPage: function(assignment) {
          for (var i = 0; i < this.$contentPages.length; i++) {
             var contentPage = this.$contentPages[i];
             var form = $('form.attack-form', contentPage);
             var action = form.attr('action')
             if (action !== undefined && action.includes(assignment.assignment)) {
               return i;
             }
          }
          return -1;
        },

        /* initial renering */
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
            this.addPaginationControls();
            if (this.numPages > 1) {
                //no animation on init
                this.$contentPages.hide();
                this.$el.find(this.$contentPages[this.currentPage]).show();
                this.showNextPageButton();
                this.hidePrevPageButton();
            } else if (this.numPages === 1) {
                this.hideNextPageButton();
                this.hidePrevPageButton();
            }
         },

         setCurrentPage: function (pageNum) {
            this.currentPage = (_.isNumber(pageNum) && pageNum < this.numPages) ? pageNum : 0;
         },

         getCurrentPage: function () {
            return this.currentPage;
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

        /* form submission handling */
        onFormSubmit: function (e) {
            var curForm = e.currentTarget; // the form from which the
            var self = this;
            // TODO custom Data prep for submission
            var prepareDataFunctionName = $(curForm).attr('prepareData');
            var submitData = (typeof webgoat.customjs[prepareDataFunctionName] === 'function') ? webgoat.customjs[prepareDataFunctionName]() : $(curForm).serialize();
            // var submitData = this.$form.serialize();
            this.curForm = curForm;
            this.$curFeedback = $(curForm).closest('.attack-container').find('.attack-feedback');
            this.$curOutput = $(curForm).closest('.attack-container').find('.attack-output');
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
            this.renderFeedback(data.feedback);

            this.renderOutput(data.output || "");
            if (data.lessonCompleted) {
                this.curForm.reset();
                this.trigger('lesson:complete');
            }
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

        /* create, show & hide pagination controls */

        addPaginationControls: function() {
            var pagingControlsDiv;
            //this.$el.html();
            //prev
            var prevPageButton = $('<span>',{class:'glyphicon-class glyphicon glyphicon-circle-arrow-left show-prev-page'});
            prevPageButton.unbind().on('click',this.decrementPageView.bind(this));
            //next
            var nextPageButton = $('<span>',{class:'glyphicon-class glyphicon glyphicon-circle-arrow-right show-next-page'});
            nextPageButton.unbind().on('click',this.incrementPageView.bind(this));
            //add to DOM
            if (this.$el.find('#lesson-page-controls').length < 1) {
                pagingControlsDiv = $('<div>',{class:'panel-body', id:'lesson-page-controls'});
                pagingControlsDiv.append(prevPageButton);
                pagingControlsDiv.append(nextPageButton);
                this.$el.append(pagingControlsDiv);
            }

        },

        showPrevPageButton: function() {
            $('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-left.show-prev-page').show();
        },

        hidePrevPageButton: function() {
            $('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-left.show-prev-page').hide();
        },

        showNextPageButton: function() {
            $('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-right.show-next-page').show();
        },

        hideNextPageButton: function() {
            $('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-right.show-next-page').hide();
        },

        /* increment, decrement & display handlers */
        incrementPageView: function() {
            if (this.currentPage < this.numPages -1) {
               this.currentPage++;
               window.location.href = this.model.get('lessonUrl') + '/' + this.currentPage;
               //this.showCurContentPage(true);Con
            }

            if (this.currentPage > 0) {
                this.showPrevPageButton();
            }

            if (this.currentPage >= this.numPages -1) {
                this.hideNextPageButton();
                this.showPrevPageButton;
            }
        },

        decrementPageView: function() {
            if (this.currentPage > 0) {
                this.currentPage--;
                window.location.href = this.model.get('lessonUrl') + '/' + this.currentPage;
                //this.showCurContentPage(false);
            }

            if (this.currentPage < this.numPages -1) {
                this.showNextPageButton();
            }

            if (this.currentPage == 0) {
                this.hidePrevPageButton();
                this.showNextPageButton()
            }

        },

        showCurContentPage: function(isIncrement) {
            this.$contentPages.hide();
            this.$el.find(this.$contentPages[this.currentPage]).show();
        },

        findAssigmentEndpointOnPage: function(pageNumber) {
            var contentPage = this.$contentPages[this.currentPage];
            var form = $('form.attack-form', contentPage);
            var action = form.attr('action')
            if (action !== undefined) {
                return action;
            }
        },

        navToPage: function (pageNum) {
            this.setCurrentPage(pageNum);//provides validation
            this.showCurContentPage(this.currentPage);
            this.hideShowNavButtons();
            var assignmentPath = this.findAssigmentEndpointOnPage(pageNum);
            Backbone.trigger('navigatedToPage',{'pageNumber':pageNum, 'assignmentPath' : assignmentPath});
        },

        hideShowNavButtons: function () {
            //one page only
            if (this.numPages === 1) {
                this.hidePrevPageButton();
                this.hideNextPageButton();
            }
            //first page
            if (this.currentPage === 0) {
                this.hidePrevPageButton();
                if (this.numPages > 1) {
                    this.showNextPageButton();
                }
                return;
            }
            // > first page, but not last
            if (this.currentPage > 0 && this.currentPage < this.numPages -1) {
                this.showNextPageButton();
                this.showPrevPageButton();
                return;
            }
            // last page and more than one page
            if (this.currentPage === this.numPages -1 && this.numPages > 1) {
                this.hideNextPageButton();
                this.showPrevPageButton();
                return;
            }

        },

        /* for testing */
        showTestParam: function (param) {
            this.$el.find('.lesson-content').html('test:' + param);
        }

    });

    
});
