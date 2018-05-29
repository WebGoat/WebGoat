define(['jquery',
        'underscore',
        'backbone',
        'goatApp/model/LessonOverviewCollection',
        'text!templates/paging_controls.html'],
    function ($,
              _,
              Backbone,
              LessonOverviewCollection,
              PaginationTemplate) {
        return Backbone.View.extend({
            template: PaginationTemplate,
            el: '#lesson-page-controls',

            initialize: function ($contentPages,baseLessonUrl,initPageNum) {
                this.$contentPages = $contentPages;
                this.collection = new LessonOverviewCollection();
                this.listenTo(this.collection, 'reset', this.render);
                this.numPages = this.$contentPages.length;
                this.baseUrl = baseLessonUrl;
                this.collection.fetch({reset:true});
                this.initPagination(initPageNum);
                //this.render();
             },

            render: function (e) {
                this.parseLinks();
                var t = _.template(this.template);
                this.$el.html(t({'overview':this.lessonOverview}));
                this.bindNavButtons();
                this.hideShowNavButtons();
            },

            updateCollection: function() {
                this.collection.fetch({reset:true});
            },

            bindNavButtons: function() {
                this.$el.find('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-right.show-next-page').unbind().on('click',this.incrementPageView.bind(this));
                this.$el.find('span.glyphicon-class.glyphicon.glyphicon-circle-arrow-left.show-prev-page').unbind().on('click', this.decrementPageView.bind(this));
                this.navButtonsBound = true;
            },

            parseLinks: function() {
                var assignmentCount = this.$contentPages.find('.attack-container');
                var solvedMap = {};
                var pages = [];

                _.each(this.collection.models, function(model) {
                    //alert (model.get('solved'));
                     if (model.get('solved')) {
                        var key = model.get('assignment').path.replace(/\//g,'');
                        solvedMap[key] = model.get('assignment').name;
                     }

                });

                isAttackSolved = function (path) {
                    //strip
                    var newPath = path.replace(/^\/WebGoat/,'');
                    var newPath = newPath.replace(/\//g,'');
                    if (typeof solvedMap[newPath] !== 'undefined') {
                        return true;
                    }
                    return false;
                };

                var self = this;
                var pages, pageClass, solved;
                _.each(this.$contentPages,function(page,index) {
                    var curPageClass = (self.currentPage == index) ? ' cur-page' : '';

                    if ($(page).find('.attack-container').length < 1) { // no assignments [attacks]
                        pageClass = 'page-link';
                        pages.push({content:'content',pageClass:pageClass,curPageClass:curPageClass});
                    } else {
                        var $assignmentForms = $(page).find('.attack-container form.attack-form');
                        // use for loop to avoid anonymous function scope hell
                        //var pageAssignments = {content:'attack',attacks:[]}
                        pageClass = 'attack-link'
                        var solvedClass = 'solved-true'
                        for (var i=0; i< $assignmentForms.length; i++) {
                            //normalize path
                            var action = $assignmentForms.attr('action');//.replace(/\//g,'');
                            if (action && isAttackSolved(action)) {
                                //pageClass = 'fa fa-check-square-o assignment-solved';
                                //pageAssignments.attacks.push({solved:true});
                            } else {
                                solvedClass = 'solved-false';

                            }
                        }
                        pages.push({solvedClass:solvedClass,content:'assignment',curPageClass:curPageClass,pageClass:pageClass});
                    }
                });

                //assign to the view
                this.lessonOverview = {
                    baseUrl: this.baseUrl,
                    pages: pages
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

            initPagination: function(initPageNum) {
               //track pagination state in this view ... start at 0 .. unless a pageNum was provided
               this.currentPage = !initPageNum ? 0 : initPageNum;
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
                this.collection.fetch({reset:true});
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
                this.collection.fetch({reset:true});
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