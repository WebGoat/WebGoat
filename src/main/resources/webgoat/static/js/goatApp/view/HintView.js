define(['jquery',
	'underscore',
	'backbone',
	'goatApp/model/HintCollection'],
function($,
	_,
	Backbone,
	HintCollection) {
	return Backbone.View.extend({
		el:'#lesson-hint-container',
		events: {
			"click #show-next-hint": "showNextHint",
			"click #show-prev-hint": "showPrevHint"
		},
		initialize: function() {
			this.curHint=0;
			this.collection = new HintCollection();
			this.hintsToShow = new Array();
			this.listenTo(this.collection,'loaded',this.onModelLoaded);
			this.hideHints();
            var self = this;
            // different way to do this?
            Backbone.on('navigatedToPage', function(nav){
                self.selectHints(nav);
                // end event delegation??
            });
		},

        isVisible: function() {
            return this.$el.is(':visible');
        },

        toggleLabel: function() {
            if (this.isVisible()) {
				$('#show-hints-button').text(polyglot.t("hide.hints"))
			} else {
				$('#show-hints-button').text(polyglot.t("show.hints"))
            }
        },

		render:function() {
			if (this.isVisible()) {
				this.$el.hide(350, this.toggleLabel.bind(this));
			} else if (this.hintsToShow.length > 0) {
				this.$el.show(350, this.toggleLabel.bind(this));
			}

			if (this.hintsToShow.length > 0) {
				this.hideShowPrevNextButtons();
			}
			this.displayHint(this.curHint);

		},

        /**
		 * Select the hints, we get 'HttpBasics/attack1' in the json (nav) we need to select all the hints
		 * from the model where the assignment name is contained in the assignmentPath. We do this not to mess
		 * with contextRoots etc and try to select the name from the url.
		 *
		 * @todo we can of course try to add the assignment name to the html form as attribute.
		 *
         * @param nav the json structure for navigating
         */
        filterHints: function(endpoints) {
            this.hintsToShow = [];
            _.each(endpoints, this.filterHint, this);

            if (this.hintsToShow.length > 0) {
                this.trigger('hints:showButton');
            } else {
                this.trigger('hints:hideButton');
            }
        },

        filterHint: function(endpoint) {
            var self = this;
            _.each(this.collection.models, function(hintModel) {
                if (endpoint.indexOf(hintModel.get('assignmentPath')) > -1 || decodeURIComponent(endpoint).indexOf(hintModel.get('assignmentPath')) > -1) {
                    self.hintsToShow.push(hintModel.get('hint'));
                }
            });
        },

		onModelLoaded: function() {
			this.trigger('hints:loaded',{'helpElement':'hints','value':true})
		},

		hideHints: function() {
			if (this.$el.is(':visible')) {
				this.$el.hide(350, this.toggleLabel.bind(this));
			}
        },

		showNextHint: function() {
			this.curHint = (this.curHint < this.hintsToShow.length -1) ? this.curHint+1 : this.curHint;
			this.hideShowPrevNextButtons();
			this.displayHint(this.curHint);
		},

		showPrevHint: function() {
			this.curHint = (this.curHint > 0) ? this.curHint-1 : this.curHint;
			this.hideShowPrevNextButtons();
			this.displayHint(this.curHint);
		},

        showFirstHint: function() {
            this.curHint = 0;
            this.hideShowPrevNextButtons();
            this.displayHint(this.curHint);
        },

		displayHint: function(curHint) {
            if(this.hintsToShow.length == 0) {
               // this.hideHints();
            } else {
                this.$el.find('#lesson-hint-content').html(polyglot.t(this.hintsToShow[curHint]));
            }
		},

		hideShowPrevNextButtons: function() {
			if (this.curHint === this.hintsToShow.length -1) {
				this.$el.find('#show-next-hint').css('visibility','hidden');
			} else {
				this.$el.find('#show-next-hint').css('visibility','visible');
			}

			if (this.curHint === 0) {
				this.$el.find('#show-prev-hint').css('visibility','hidden');
			} else {
				this.$el.find('#show-prev-hint').css('visibility','visible');
			}
		},

		getHintsCount: function () {
		    return this.collection.length;
		}

	});
});
