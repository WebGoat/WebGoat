//LessonContentView
define(['jquery',
    'underscore',
    'backbone',
    'libs/jquery.form',
    'goatApp/view/ErrorNotificationView',
    'goatApp/view/PaginationControlView'],
    function(
        $,
        _,
        Backbone,
        JQueryForm,
        ErrorNotificationView,
        PaginationControlView) {
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

        /* initial rendering */
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
            this.paginationControlView = new PaginationControlView(this.$contentPages,this.model.get('lessonUrl'));
            //this.listenTo(this.paginationControlView,'page:set',this.navToPage);
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
            //TODO: refactor back assignmentCompleted in Java
            if (data.lessonCompleted || data.assignmentCompleted) {

                this.markAssignmentComplete();
                this.trigger('assignment:complete');
            } else {
                this.markAssignmentIncomplete();
            }
            return false;
        },

        markAssignmentComplete: function () {
            this.curForm.reset();
            $(this.curForm).siblings('.assignment-success').find('i').removeClass('hidden');
        },

        markAssignmentIncomplete: function () {
            $(this.curForm).siblings('.assignment-success').find('i').addClass('hidden');
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
            this.$curFeedback.html(polyglot.t(feedback) || "");
            this.$curFeedback.show(400)

        },

        renderOutput: function(output) {
            this.$curOutput.html(polyglot.t(output) || "");
            this.$curOutput.show(400)
        },

        showCurContentPage: function(pageNum) {
            this.$contentPages.hide();
            this.$el.find(this.$contentPages[pageNum]).show();
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
            this.showCurContentPage(pageNum);
            this.paginationControlView.setCurrentPage(pageNum);//provides validation
            this.paginationControlView.hideShowNavButtons();
            var assignmentPath = this.findAssigmentEndpointOnPage(pageNum);
            Backbone.trigger('navigatedToPage',{'pageNumber':pageNum, 'assignmentPath' : assignmentPath});
        },

        /* for testing */
        showTestParam: function (param) {
            this.$el.find('.lesson-content').html('test:' + param);
        }

    });

    
});
