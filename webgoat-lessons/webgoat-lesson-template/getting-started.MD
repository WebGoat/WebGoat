### To include lesson template in build ###
1. Edit the webgoat-server/pom.xml file and uncomment the section under  
  ```xml  
   <!--uncommment below to run/include lesson template in WebGoat Build-->  
  ```

2. Also uncomment in webgoat-lessons/pom.xml where it says    
  ```xml  
   <!-- uncomment below to include lesson template in build, also uncomment the dependency in webgoat-server/pom.xml-->
  ```  
  
### To add a lesson to WebGoat ###

There are a number of moving parts and this sample lesson will help you navigate those parts. Most of your work will be done in two directories. To start though, you can copy this directory with the name of your-lesson in the webgoat-lessons directory.

0. The POM file  

    *  Change the line to give your lesson its own artifactId.   
        That should be all you need to do there:  
	```xml  
	 <artifactId>webgoat-lesson-template</artifactId>  
	```  
1. The Base Class  

    *  The name of the class (file and class name) to better match your lesson. (e.g. `sql-injection` >> `SqlInjection`)  
    *  The category in which you want your lesson to be in. You can create a new category if you want, or put in an issue to have one added.  
    * The `defaultRanking` will move your lesson up or down in the categories list.  
    * Implement a new key name pair `lesson-template.title` (the key) and update the same key/value pair `your.key=your value` in src/main/resources/i18n/WebGoatLabels.properties.  
    * Implement a new value for the `getId` method, which leads us to...  

2. The HTML content framing  

    * Rename the provided file in src/main/resources/html using your value from the `getId` method in your lesson's base class:  
	e.g.   
	`public String getId() { return "your-lesson";  }` >> `your-lesson.html`   
    * Modify that file following the commented instructions in there.   
    * In conjunction with this file you.  

3. Assignment Endpoints    
    * In the above html file, you will see an example of an 'attack form'. You can create endpoints to handle these attacks and provide the user feedback and simulated output. See the example file here as well as other existing lessons for ways to extend these.  You will extend the `AssignmentEndpoint` as the example will show:  
    * You can also create supporting (non-assignment) endpoints, that are not evaluated/graded.  
    * See other lesson examples for creating unit/integration tests for your project as well.


4. Getting your lesson to show up  

    * Modify the webgoat-lessons/pom.xml to include your project in the `<modules>` section:  
        ```xml
	  <modules>
	             <!-- ... -->
	     <module>webgoat-lesson-template</module>
		     <!-- ... -->
	  </modules>
        ```
	
    * Modify the webgoat-server/pom.xml to add your project as a dependency in the `<dependencies>` section:   
       ```xml
         <dependencies>
	             <!-- .... >
           <dependency>
              <groupId>org.owasp.webgoat.lesson</groupId>
              <artifactId>your-artfifact-id-here</artifactId>
              <version>${project.version}</version>
           </dependency>
                     <!-- .... >
        </dependencies>
    ```
        

5. You should be ready to run and test your project. Please create issues at https://github.com/WebGoat/WebGoat if there errors or confusion with this documentation/template
