//
//UserAndInfoView
define(['jquery',
	'underscore',
	'backbone'],
function($,
	_,
	Backbone) {
	return Backbone.View.extend({
		el:'#toggle-menu', //Check this,

		initialize: function() {
			this.$el.on('click',this.toggleMenu);
		},
		
		toggleMenu: function() {
			//left
			if (!$('.sidebarRight').hasClass('.sidebar-toggle-right')) {
	            $('.sidebarRight').removeClass('sidebar-toggle-right');
                $('.main-content-wrapper').removeClass('main-content-toggle-right');
            }
            $('.sidebar').toggleClass('sidebar-toggle');
            $('.main-content-wrapper').toggleClass('main-content-toggle-left');
            //e.stopPropagation();
        }
	});
});