//
//UserAndInfoView
define(['jquery',
	'underscore',
	'backbone'],
function($,
	_,
	Backbone) {
	return Backbone.View.extend({
		el:'#toggle-menu',

        events: {
            "click": "toggleMenu"
        },

		toggleMenu: function(e) {
			//left
			if (!$('.sidebarRight').hasClass('.sidebar-toggle-right')) {
	            $('.sidebarRight').removeClass('sidebar-toggle-right');
                $('.main-content-wrapper').removeClass('main-content-toggle-right');
            }
            $('.sidebar').toggleClass('sidebar-toggle');
            $('.main-content-wrapper').toggleClass('main-content-toggle-left');
            e.stopImmediatePropagation();
        }
	});
});