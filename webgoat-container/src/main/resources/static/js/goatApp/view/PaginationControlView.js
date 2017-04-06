define(['jquery',
        'underscore',
        'backbone',
        'text!templates/paging_controls.html'],
//        'css!css/paging-controls.css'],
    function ($,
              _,
              Backbone,
              PaginationTemplate) {
        return Backbone.View.extend({
            template: PaginationTemplate,
            el: '#lesson-page-controls',

            initialize: function ($contentPages,baseLessonUrl) {
                this.numPages = $contentPages.length;
                this.baseUrl = baseLessonUrl;
                this.parseLinks($contentPages);
                this.initPagination();
                this.render();
                this.bindNavButtons();
             },

            render: function () {
                var t = _.template(this.template);
                this.$el.html(t());
                this.hideShowNavButtons();
            },

            bindNavButtons: function() {
                this.$el.find('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-right.show-next-page').unbind().on('click',this.incrementPageView.bind(this));
                this.$el.find('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-left.show-prev-page').unbind().on('click', this.decrementPageView.bind(this));
            },

            parseLinks: function($contentPages) {

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

            initPagination: function() {
               //track pagination state in this view ... start at 0
               this.currentPage = 0;
            },

            setCurrentPage: function (pageNum) {
                this.currentPage = (_.isNumber(pageNum) && pageNum < this.numPages) ? pageNum : 0;
            },

            /* increment, decrement & display handlers */
            incrementPageView: function() {
                if (this.currentPage < this.numPages -1) {
                   this.currentPage++;
                   window.location.href = this.baseUrl + '/' + this.currentPage;
                }

                if (this.currentPage > 0) {
                    this.showPrevPageButton();
                }

                if (this.currentPage >= this.numPages -1) {
                    this.hideNextPageButton();
                    this.showPrevPageButton;
                }
                this.trigger('page:set',this,this.currentPage);
            },

            decrementPageView: function() {
                if (this.currentPage > 0) {
                    this.currentPage--;
                    window.location.href = this.baseUrl + '/' + this.currentPage;
                }

                if (this.currentPage < this.numPages -1) {
                    this.showNextPageButton();
                }

                if (this.currentPage == 0) {
                    this.hidePrevPageButton();
                    this.showNextPageButton()
                }
                this.trigger('page:set',this,this.currentPage);

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
        });
    });