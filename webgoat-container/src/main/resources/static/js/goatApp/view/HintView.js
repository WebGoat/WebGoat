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
			this.listenTo(this.collection,'loaded',this.onModelLoaded);
			this.hideHints();
		},

		render:function() {
			if (this.$el.is(':visible')) {
				this.$el.hide(350);
			} else {
				this.$el.show(350);
			}
			
			if (this.collection.length > 0) {
				this.hideShowPrevNextButtons();
			}
			this.displayHint(this.curHint);
			
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
			this.curHint = (this.curHint < this.collection.length -1) ? this.curHint+1 : this.curHint;
			this.hideShowPrevNextButtons();
			this.displayHint(this.curHint);
		},

		showPrevHint: function() {
			this.curHint = (this.curHint > 0) ? this.curHint-1 : this.curHint;
			this.hideShowPrevNextButtons();
			this.displayHint(this.curHint);
		},

		displayHint: function(curHint) {
			this.$el.find('#lesson-hint-content').html(this.collection.models[curHint].get('hint'));
		},

		hideShowPrevNextButtons: function() {
			if (this.curHint === this.collection.length -1) {
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