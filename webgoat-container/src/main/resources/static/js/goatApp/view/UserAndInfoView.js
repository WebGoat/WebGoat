//UserAndInfoView
define(['jquery',
	'underscore',
	'backbone'],
function($,
	_,
	Backbone) {
	return Backbone.View.extend({
		el:'#user-and-info-nav', //Check this,

		events: {
			'click #about-button': 'showAboutModal',
			'click #user-menu': 'showUserMenu'
		},

		initialize: function() {

		},
		
		render:function(title) {
		},

		showUserMenu: function() {
			var menu = this.$el.find('.dropdown-menu');
			if (menu.is(':visible')) {
				menu.hide(200);
			} else {
				menu.show(400);
				/*menu.on('mouseout', function() {
					$('#user-and-info-nav .dropdown-menu').hide(200);
				});*/
			}
		
		},

		showAboutModal: function() {
			$('#about-modal').show(400);
			$('#about-modal div.modal-header button.close, #about-modal div.modal-footer button').unbind('click').on('click', function() {
				$('#about-modal').hide(200);
			});
		}


	});
});