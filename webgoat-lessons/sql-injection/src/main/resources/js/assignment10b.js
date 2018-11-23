$(document).ready( () => {

    var editor = ace.edit("editor");
    editor.setTheme("ace/theme/monokai");
    editor.session.setMode("ace/mode/java");

    editor.getSession().on("change", () => {
        setTimeout( () => {
            $("#codesubmit input[name='editor']").val(ace_collect());
        }, 20);
    });


});

function ace_collect() {
    let code = "";
    $(".ace_line").each(function(i, el) {
        var to_add = el.innerHTML;
        if(/\/\/.*/.test(to_add)) {
            to_add = to_add.replace(/\/\/.*/i, '');
        }
        code += to_add;
    });
    return code;
}