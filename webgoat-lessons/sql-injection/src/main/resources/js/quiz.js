$(function () {
    console.log("entry");
    let questionsJson = '{"questions": [ { "text": "What is the difference between a prepared statement and a statement?", "solutions": { "1": "Prepared statements are statements with hard-coded parameters.", "2": "Prepared statements are not stored in the database.", "3": "A statement is faster.", "4": "A statement has got values instead of a prepared statement" } }, { "text": "Which one of the following characters is a placeholder for variables?", "solutions": { "1": "\'", "2": "=", "3": "?", "4": "!" } }, { "text": "How can prepared statements be faster than statements?", "solutions": { "1": "They are not static so they can compile better written code than statements.", "2": "Prepared statements are compiled once by the database management system waiting for input and are pre-compiled this way.", "3": "Prepared statements are stored and wait for input it raises performance considerably.", "4": "Oracle optimized prepared statements. Because of the minimal use of the database\'s resources it is faster." } }, { "text": "How can a prepared statement prevent SQL-Injection?", "solutions": { "1": "Prepared statements have got an inner check to distinguish between input and logical errors.", "2": "Prepared statements use the placeholders to make rules what input is allowed to use.", "3": "Placeholders can prevent that the user\'s input gets attached to the SQL query resulting in a seperation of code and data.", "4": "Prepared statements always read inputs literally and never mixes it with its SQL commands." } }, { "text": "What happens if a person with malicious intent writes into a register form :Robert\'); DROP TABLE Students;-- that has a prepared statement?", "solutions": { "1": "The table Students and all of its content will be deleted.", "2": "The input deletes all students with the name Robert.", "3": "The database registers: \'Robert\' and deletes the table afterwards.", "4": "The database registers: \'Robert\' ); DROP TABLE Students;--\'." } } ] }';
    var questionsObj = JSON.parse(questionsJson);
    let html = "";
    jQuery.each(questionsObj, function(i, obj) {
        jQuery.each(obj, function(j, quest) {
          html += "<div id='question_" + j + "' class='quiz_question attack-container' name='question'><p>" + (j+1) + ".&nbsp;" + quest.text + "</p>";
          html += "<fieldset>";
          jQuery.each(quest.solutions, function(k, solution) {
          //question_' + j + '_solution_' + k + '" value="' + solution + '
            solution = "Solution " + k + ": " + solution;
            html += '<input type="checkbox" name="question_' + j +'_solution" value="' + solution + '">' + solution + '<br>';
          });
          html += "</fieldset></div>";
        });
    });
    document.getElementById("q_container").innerHTML = html;
});