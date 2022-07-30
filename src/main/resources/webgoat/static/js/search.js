let input = document.getElementById('search');
let timeout = null;

input.addEventListener('keyup', function (e) {
    clearTimeout(timeout);
    timeout = setTimeout(function () {
        //console.log('Value:', input.value);
        search(input.value);
    }, 1000);
});

function search(arg) {
      var elementId = null;
      lessons = document.querySelectorAll('[class="lesson"]');
      lessons.forEach(function(lesson) {
        lessonLowerCase = lesson.textContent.toLowerCase();
        if (arg.length>2 && lessonLowerCase.includes(arg.toLowerCase())) {
            if (arg.length<7 && arg.toLowerCase().includes('sql')) {
                elementId = 'A3Injection-SQLInjectionintro';
                document.getElementById('search').value='sql injection';
            } else if (arg.length<9 && arg.toLowerCase().includes('pass')) {
                elementId = 'A7IdentityAuthFailure-Passwordreset';
                document.getElementById('search').value='password';
            } else {
                elementId = lesson.childNodes[0].id;
                document.getElementById('search').value=lessonLowerCase;
            }
        } else {
            return;
        }
      });

      if (elementId != null) {
        document.getElementById(elementId).click();
        categoryId = elementId.substring(0,elementId.indexOf("-"));
        //extra click to make sure menu does not disappear on same category search
        if (categoryId == 'Challenges') {
            document.querySelectorAll('[category="Introduction"]')[0].click();
        } else {
            document.querySelectorAll('[category="Challenges"]')[0].click();
        }
        document.querySelectorAll('[category="'+categoryId+'"]')[0].click();
      }

};
