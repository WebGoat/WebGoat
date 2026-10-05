# Multi language support in WebGoat

WebGoat is mainly written in English, but it does support multiple languages.

## Default language selection

1. Current supported languages are: en, fr, de, nl
2. The primary language is based on the language setting of the browser.
3. If the language is not in the list of supported language, the language is English
4. Once logged in, you can switch between the supported languages using a language dropdown menu on the main page
   1. After switching a language you are back at the Introduction page

## Adding a new language

The following steps are required when you want to add a new language

1. Update [main_new.html](src/main/resources/webgoat/static/main_new.html)
   1. Add the parts for showing the flag and providing the correct value for the flag= parameter
      2.
2. Add a flag image to src/main/resources/webgoat/static/css/img
   1. See the main_new.html for a link to download flag resources
3. Add a welcome page to the introduction lesson
   1. Copy Introduction_.adoc to Introduction_es.adoc (if in this case you want to add Spanish)
   2. Add a highlighted section that explains that most parts of WebGoat will still be in English and invite people to translate parts where it would be valuable
4. Translate the main labels
   1. Copy messages.properties to messages_es.properties (if in this case you want to add Spanish)
   2. Translate the label values
5. Optionally translate lessons by
   1. Adding lang specifc adoc files in documentation folder of the lesson
   2. Adding WebGoatLabels.properties of a specific language if you want to
6. Run mvn clean to see if the LabelAndHintIntegration test passes
7. Run WebGoat and verify that your own language and the other languages work as expected

If you only want to translate more for a certain language, you only need to do step 4-8
