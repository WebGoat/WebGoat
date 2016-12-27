define([
	'backbone'],
	function(
		Backbone) {
	return Backbone.View.extend({
	    tagName: 'li',
	    template: _.template($('#assignmentTemplate').html() ),

        events: {
          "click a": "clicked"
        },

        clicked: function(e){
          e.preventDefault();
          var id = $(e.currentTarget).data("id");
          Backbone.trigger('assignment:navTo',{'assignment': id});
        },

        render: function() {
            this.$el.html(this.template(this.model.toJSON()));
        	return this;
        }
	});
});
