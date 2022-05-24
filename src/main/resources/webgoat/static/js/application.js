var app = function() {

    var init = function() {

        tooltips();
        // menu is handled by angular
        //menu();
        togglePanel();
        sideBarLeftInit();
        window.onresize = function(){
            sideBarLeftInit();    
        }
        closePanel();        
    };

    var tooltips = function() {
        $('#toggle-left').tooltip();
        $('.right_nav_button').tooltip({'placement': 'bottom'});
    };

    var togglePanel = function() {
        $('.actions > .fa-chevron-down').click(function() {
            $(this).parent().parent().next().slideToggle('fast');
            $(this).toggleClass('fa-chevron-down fa-chevron-up');
        });

    };

    var closePanel = function() {
        $('.actions > .fa-times').click(function() {
            $(this).parent().parent().parent().fadeOut();
        });

    }

    //End functions

    //Dashboard functions
    var timer = function() {
        $('.timer').countTo();
    };


    //Vector Maps 
    var map = function() {
        $('#map').vectorMap({
            map: 'world_mill_en',
            backgroundColor: 'transparent',
            regionStyle: {
                initial: {
                    fill: '#1ABC9C',
                },
                hover: {
                    "fill-opacity": 0.8
                }
            },
            markerStyle: {
                initial: {
                    r: 10
                },
                hover: {
                    r: 12,
                    stroke: 'rgba(255,255,255,0.8)',
                    "stroke-width": 3
                }
            },
            markers: [{
                latLng: [27.9881, 86.9253],
                name: '36 Employees',
                style: {
                    fill: '#E84C3D',
                    stroke: 'rgba(255,255,255,0.7)',
                    "stroke-width": 3
                }
            }, {
                latLng: [48.8582, 2.2945],
                name: '58 Employees',
                style: {
                    fill: '#E84C3D',
                    stroke: 'rgba(255,255,255,0.7)',
                    "stroke-width": 3
                }
            }, {
                latLng: [-40.6892, -74.0444],
                name: '109 Employees',
                style: {
                    fill: '#E84C3D',
                    stroke: 'rgba(255,255,255,0.7)',
                    "stroke-width": 3
                }
            }, {
                latLng: [34.05, -118.25],
                name: '85 Employees ',
                style: {
                    fill: '#E84C3D',
                    stroke: 'rgba(255,255,255,0.7)',
                    "stroke-width": 3
                }
            }]
        });

    };

    var weather = function() {
        var icons = new Skycons({
            "color": "white"
        });

        icons.set("clear-day", Skycons.CLEAR_DAY);
        icons.set("clear-night", Skycons.CLEAR_NIGHT);
        icons.set("partly-cloudy-day", Skycons.PARTLY_CLOUDY_DAY);
        icons.set("partly-cloudy-night", Skycons.PARTLY_CLOUDY_NIGHT);
        icons.set("cloudy", Skycons.CLOUDY);
        icons.set("rain", Skycons.RAIN);
        icons.set("sleet", Skycons.SLEET);
        icons.set("snow", Skycons.SNOW);
        icons.set("wind", Skycons.WIND);
        icons.set("fog", Skycons.FOG);

        icons.play();
    }

    //morris pie chart
    var morrisPie = function() {

        Morris.Donut({
            element: 'donut-example',
            data: [{
                    label: "Chrome",
                    value: 73
                }, {
                    label: "Firefox",
                    value: 71
                }, {
                    label: "Safari",
                    value: 69
                }, {
                    label: "Internet Explorer",
                    value: 40
                }, {
                    label: "Opera",
                    value: 20
                }, {
                    label: "Android Browser",
                    value: 10
                }

            ],
            colors: [
                '#1abc9c',
                '#293949',
                '#e84c3d',
                '#3598db',
                '#2dcc70',
                '#f1c40f'
            ]
        });
    }

    //Sliders
    var sliders = function() {
        $('.slider-span').slider()
    };

    var sideBarLeftInit = function(){
        $("#leftside-navigation").css("height", (window.innerHeight-80)+"px");
    };

    //return functions
    return {
        init: init,
        timer: timer,
        map: map,
        sliders: sliders,
        weather: weather,
        morrisPie: morrisPie,
        sideBarLeftInit:sideBarLeftInit
    };
}();


