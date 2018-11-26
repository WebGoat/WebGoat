function ace_collect() {
    let code = "";
    $(".ace_line").each(function(i, el) {
        code += el.innerHTML;
    });
    console.log(code);
    code = $(".ace_content")[0].innerHTML;
    $.ajax({
        type: "POST",
        url: "/WebGoat/CrossSiteScripting/attack3",
        dataType: "text",
        data: {
            editor: code
        }
    });
}