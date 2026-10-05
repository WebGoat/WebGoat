define(['jquery',
    'underscore',
    'backbone',
    'libs/jquery.form'
    ],
    function($,
      _,
      Backbone,
      JQueryForm) {
        var customGoat = {

                getFlights:function() {
                    var fromField = $('#travelFrom');
                    var toField = $('#travelTo');
                    var xml = '<?xml version="1.0"?>' +
                        '<searchForm>' +
                        '  <from>' + fromField.value() + '</from>' +
                        '</searchForm>';
                    return xml;
                },
            }

            return customGoat;
    });
