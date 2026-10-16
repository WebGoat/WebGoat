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
    var editor = ace.edit("editor");
    var code = editor.getValue();
    return code;
}
