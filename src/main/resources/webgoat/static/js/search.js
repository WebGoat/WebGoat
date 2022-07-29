function search(arg) {
      var elementId = null;
      lessons = document.querySelectorAll('[class="lesson"]');
      lessons.forEach(function(lesson) {
        lessonLowerCase = lesson.textContent.toLowerCase();
        if (arg.length>2 && lessonLowerCase.includes(arg.toLowerCase())) {
            elementId = lesson.childNodes[0].id;
            document.getElementById('search').value=lessonLowerCase;
        } else {
            return;
        }
      });

      if (elementId != null) {
        document.getElementById(elementId).click();
        categoryId = elementId.substring(0,elementId.indexOf("-"));
        document.querySelectorAll('[category="'+categoryId+'"]')[0].click();
      }

};
