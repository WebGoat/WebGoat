function ace_collect() {
    let code = "";
    code = editor.getSession().getValue();
    $.ajax({
        type: "POST",
        url: "/WebGoat/CrossSiteScripting/attack3",
        dataType: "text",
        data: {
            editor: code
        }
    });
}