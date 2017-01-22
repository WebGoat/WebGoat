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
            Backbone.on('navigatedToPage', function(nav){
                self.selectHints(nav)
            });
		},

        isVisible: function() {
            return this.$el.is(':visible');
        },

        toggleLabel: function() {
            if (this.isVisible()) {
                $('show-hints-button').text('Hide hints');
            } else {
                $('show-hints-button').text('Show hints');
            }
        },

		render:function() {
			if (this.isVisible()) {
				this.$el.hide(350);
			} else if (this.hintsToShow.length > 0) {
				this.$el.show(350);
			}

            this.toggleLabel()

			if (this.hintsToShow.length > 0) {
				this.hideShowPrevNextButtons();
			}
			this.displayHint(this.curHint);
			
		},

        /**
		 * Select the hints, we get '/WebGoat/HttpBasics/attack1' in the json (nav) we need to select all the hints
		 * from the model where the assignment name is contained in the assignmentEndpoint. We do this not to mess
		 * with contextRoots etc and try to select the name from the url.
		 *
		 * @todo we can of course try to add the assigment name to the html form as attribute.
		 *
         * @param nav the json structure for navigating
         */
        selectHints: function(nav) {
        	this.curHint = 0;
        	var assignmentEndpoint = nav['assignmentEndpoint'];
			if (assignmentEndpoint != null) {
                this.hintsToShow = this.collection.getHintsForAssignment(assignmentEndpoint);
            } else {
				this.hintsToShow = new Array();
			}
        },

		onModelLoaded: function() {
			this.trigger('hints:loaded',{'helpElement':'hints','value':true})
		},

		hideHints: function() {
			if (this.$el.is(':visible')) {
				this.$el.hide(350);
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

		displayHint: function(curHint) {
            if(this.hintsToShow.length == 0) {
                this.hideHints();
            } else {
                this.$el.find('#lesson-hint-content').html(this.hintsToShow[curHint].get('hint'));
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
		}

	});
});