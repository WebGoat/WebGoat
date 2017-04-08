$(document).ready(function () {
    //-- Click on detail
    $("ul.menu-items > li").on("click", function () {
        $("ul.menu-items > li").removeClass("active");
        $(this).addClass("active");
    })

    $(".attr,.attr2").on("click", function () {
        var clase = $(this).attr("class");

        $("." + clase).removeClass("active");
        $(this).addClass("active");
    })

    //-- Click on QUANTITY
    $(".btn-minus").on("click", function () {
        var now = $(".section > div > input").val();
        if ($.isNumeric(now)) {
            if (parseInt(now) - 1 > 0) {
                now--;
            }
            $(".quantity").val(now);
        } else {
            $(".quantity").val("1");
        }
    })
    $(".btn-plus").on("click", function () {
        var now = $(".section > div > input").val();
        if ($.isNumeric(now)) {
            $(".quantity").val(parseInt(now) + 1);
        } else {
            $(".quantity").val("1");
        }
    })
    $(".checkoutCode").on("blur", function () {
        var checkoutCode = $(".checkoutCode").val();
        $.get("challenge-store/coupons/" + checkoutCode, function (result, status) {
            var discount = result.discount;
            if (discount > 0) {
                var price = $('#price').val();
                $('#price').text((899 - (899 * discount / 100)).toFixed(2));
            } else {
                $('#price').text(899);
            }

        });
    })
})