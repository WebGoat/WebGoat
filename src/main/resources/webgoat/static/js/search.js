function search(arg) {
      console.log(arg);
      var elementId = null;
      lessons = document.querySelectorAll('[class="lesson"]');
      lessons.forEach(function(lesson) {
        lessonLowerCase = lesson.textContent.toLowerCase();

        if (lessonLowerCase.includes(arg.toLowerCase())) {
            console.log(lessonLowerCase);
            console.log(lesson.childNodes[0].id);
             elementId = lesson.childNodes[0].id;
        }
      })


      if (elementId != null) {
            document.getElementById(elementId).click();
            categoryId = elementId.substring(0,elementId.indexOf("-"));
            document.querySelectorAll('[category="'+categoryId+'"]')[0].click();
      }


};
