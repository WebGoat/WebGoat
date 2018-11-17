$(function () {
    var json = "";
    var client = new XMLHttpRequest();
    var quiz_id = document.getElementById("quiz_id").getAttribute("data-quiz_id");
    client.open('GET', '/WebGoat/lesson_js/questions_' + quiz_id + '.json');
    client.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json += client.responseText;
            console.log("entry");
            let questionsJson = json;
            var questionsObj = JSON.parse(questionsJson);
            let html = "";
            jQuery.each(questionsObj, function(i, obj) {
                jQuery.each(obj, function(j, quest) {
                  html += "<div id='question_" + j + "' class='quiz_question' name='question' style='border: solid 1px; padding: 4px; margin: 5px 2px 5px 2px'><p>" + (j+1) + ".&nbsp;" + quest.text + "</p>";
                  html += "<fieldset>";
                  jQuery.each(quest.solutions, function(k, solution) {
                    solution = "Solution " + k + ": " + solution;
                    html += '<input type="checkbox" name="question_' + j +'_solution" value="' + solution + '">' + solution + '<br>';
                  });
                  html += "</fieldset></div>";
                });
            });
            document.getElementById("q_container").innerHTML = html;
        }
    }
    client.send();
});