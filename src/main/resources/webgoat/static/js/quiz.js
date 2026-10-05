/**
This is the basic javascript that can be used for a quiz assignment. It is made for single choice quizzes (tho a multiple choice extension should be easy to make).
Basic steps for implementing a quiz:
1. HTML: include this js script file for the assignment, build a basic form, where you include a #q_container div element, create a submit button with "Quiz_solutions" as name attribute
2. JSON: Create a JSON-file with the name questions_lesson_name.json, include a span element #quiz_id with lesson_name as the data-quiz_id attribute. Build a JSON file like the one in sql-injection -> resources -> js
3. Java: Create a normal assignment that has a String[] where the correct solutions are contained in the form of "Solution [i]", replace [i] with the position of the solution beginning at 1.
         The request parameters will contain the answer in full text with "Solution [i]" in front of the text. Use them to check the answers validity.
4. CSS:  include the css/quiz.css file for styling.
**/

$(function () {
    var json = "";
    var client = new XMLHttpRequest();
    var quiz_id = document.getElementById("quiz_id").getAttribute("data-quiz_id");
    client.open('GET', 'lesson_js/questions_' + quiz_id + '.json');
    client.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            json += client.responseText;
            console.log("entry");
            let questionsJson = json;
            var questionsObj = JSON.parse(questionsJson);
            let html = "";
            $.each(questionsObj, function(i, obj) {
                $.each(obj, function(j, quest) {
                  html += "<div id='question_" + j + "' class='quiz_question' name='question'><p>" + (j+1) + ".&nbsp;" + quest.text + "</p>";
                  html += "<fieldset>";
                  $.each(quest.solutions, function(k, solution) {
                    solution = "Solution " + k + ": " + solution;
                    html += '<input id="question_' + j + '_' + k + '_input" type="radio" name="question_' + j +'_solution" value="' + solution + '" required><label for="question_' + j + '_' + k + '_input">' + solution + '</label><br>';
                  });
                  html += "</fieldset></div>";
                });
            });
            document.getElementById("q_container").innerHTML = html;
        }
    }
    client.send();
});

$(document).ready( () => {
    $("#q_container").closest(".attack-container").addClass("quiz");
    $("#q_container").closest("form").on("submit", function(e) {
        setTimeout(getFeedback, 200, this);
    }); // end listener
}); // end ready

function getFeedback(context) {
    $.ajax({
        url: $(context).attr("action")
    }).done( (result) => {
        if (!result) return;
        for(let i=0; i<result.length; i++) {
            if (result[i] === true)
                $("#q_container .quiz_question:nth-of-type(" + (i+1) + ")").removeClass("incorrect").addClass("correct");
            else if (result[i] === false)
                $("#q_container .quiz_question:nth-of-type(" + (i+1) + ")").removeClass("correct").addClass("incorrect");
        }
    }); // end ajax-done
} // end getFeedback
