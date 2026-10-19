{
  "$schema": "https://raw.githubusercontent.com/oasis-tcs/sarif-spec/master/Schemata/sarif-schema-2.1.0.json",
  "version": "2.1.0",
  "runs": [
    {
      "tool": {
        "driver": {
          "name": "Snyk Open Source",
          "rules": [
            {
              "id": "SNYK-JS-ADMZIP-1065796",
              "shortDescription": {
                "text": "High severity - Directory Traversal vulnerability in adm-zip"
              },
              "fullDescription": {
                "text": "adm-zip@0.4.7"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: adm-zip\n* Introduced through: goof@1.0.1 and adm-zip@0.4.7\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › adm-zip@0.4.7\n# Overview\n[adm-zip](https://www.npmjs.com/package/adm-zip) is a JavaScript implementation for zip data compression for NodeJS.\n\nAffected versions of this package are vulnerable to Directory Traversal. It could extract files outside the target folder.\n\n# Details\n\nA Directory Traversal attack (also known as path traversal) aims to access files and directories that are stored outside the intended folder. By manipulating files with \"dot-dot-slash (../)\" sequences and its variations, or by using absolute file paths, it may be possible to access arbitrary files and directories stored on file system, including application source code, configuration, and other critical system files.\n\nDirectory Traversal vulnerabilities can be generally divided into two types:\n\n- **Information Disclosure**: Allows the attacker to gain information about the folder structure or read the contents of sensitive files on the system.\n\n`st` is a module for serving static files on web pages, and contains a [vulnerability of this type](https://snyk.io/vuln/npm:st:20140206). In our example, we will serve files from the `public` route.\n\nIf an attacker requests the following URL from our server, it will in turn leak the sensitive private key of the root user.\n\n```\ncurl http://localhost:8080/public/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/root/.ssh/id_rsa\n```\n**Note** `%2e` is the URL encoded version of `.` (dot).\n\n- **Writing arbitrary files**: Allows the attacker to create or replace existing files. This type of vulnerability is also known as `Zip-Slip`. \n\nOne way to achieve this is by using a malicious `zip` archive that holds path traversal filenames. When each filename in the zip archive gets concatenated to the target extraction folder, without validation, the final path ends up outside of the target folder. If an executable or a configuration file is overwritten with a file containing malicious code, the problem can turn into an arbitrary code execution issue quite easily.\n\nThe following is an example of a `zip` archive with one benign file and one malicious file. Extracting the malicious file will result in traversing out of the target folder, ending up in `/root/.ssh/` overwriting the `authorized_keys` file:\n\n```\n2018-04-15 22:04:29 .....           19           19  good.txt\n2018-04-15 22:04:42 .....           20           20  ../../../../../../root/.ssh/authorized_keys\n```\n\n# Remediation\nUpgrade `adm-zip` to version 0.5.2 or higher.\n# References\n- [GitHub Commit](https://github.com/cthackers/adm-zip/commit/119dcad6599adccc77982feb14a0c7440fa63013)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-22",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:adm-zip:20180415",
              "shortDescription": {
                "text": "High severity - Arbitrary File Write via Archive Extraction (Zip Slip) vulnerability in adm-zip"
              },
              "fullDescription": {
                "text": "(CVE-2018-1002204) adm-zip@0.4.7"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: adm-zip\n* Introduced through: goof@1.0.1 and adm-zip@0.4.7\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › adm-zip@0.4.7\n# Overview\n\n[adm-zip](https://www.npmjs.com/package/adm-zip) is a JavaScript implementation for zip data compression for NodeJS.\n\n\nAffected versions of this package are vulnerable to Arbitrary File Write via Archive Extraction (Zip Slip).\n\n# Details\nIt is exploited using a specially crafted zip archive, that holds path traversal filenames. When exploited, a filename in a malicious archive is concatenated to the target extraction directory, which results in the final path ending up outside of the target folder. For instance, a zip may hold a file with a \"../../file.exe\" location and thus break out of the target folder. If an executable or a configuration file is overwritten with a file containing malicious code, the problem can turn into an arbitrary code execution issue quite easily.\r\n\r\nThe following is an example of a zip archive with one benign file and one malicious file. Extracting the malicous file will result in traversing out of the target folder, ending up in `/root/.ssh/` overwriting the `authorized_keys` file:\r\n\r\n```\r\n\r\n+2018-04-15 22:04:29 ..... 19 19 good.txt\r\n\r\n+2018-04-15 22:04:42 ..... 20 20 ../../../../../../root/.ssh/authorized_keys\r\n\r\n```\n\n# Remediation\n\nUpgrade `adm-zip` to version 0.4.11 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/cthackers/adm-zip/commit/d01fa8c80c3a5fcf5ce1eda82d96600c62910d3f)\n\n- [GitHub Commit](https://github.com/cthackers/adm-zip/pull/212/commits/6f4dfeb9a2166e93207443879988f97d88a37cde)\n\n- [Hackerone Report](https://hackerone.com/reports/362118)\n\n- [Zip Slip Advisory](https://github.com/snyk/zip-slip-vulnerability)\n\n- [Zip Slip Advisory](https://snyk.io/research/zip-slip-vulnerability)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-29",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-AJV-584908",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in ajv"
              },
              "fullDescription": {
                "text": "(CVE-2020-15366) ajv@6.10.2"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ajv\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › coveralls@3.0.9 › request@2.88.0 › har-validator@5.1.3 › ajv@6.10.2\n# Overview\n[ajv](https://www.npmjs.com/package/ajv) is an Another JSON Schema Validator\n\nAffected versions of this package are vulnerable to Prototype Pollution. A carefully crafted JSON schema could be provided that allows execution of other code by prototype pollution. (While untrusted schemas are recommended against, the worst case of an untrusted schema should be a denial of service, not execution of code.)\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `ajv` to version 6.12.3 or higher.\n# References\n- [HackerOne Report](https://hackerone.com/bugs?subject=user&report_id=894259)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-BL-608877",
              "shortDescription": {
                "text": "High severity - Remote Memory Exposure vulnerability in bl"
              },
              "fullDescription": {
                "text": "(CVE-2020-8244) bl@2.2.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: bl\n* Introduced through: goof@1.0.1, mongodb@3.5.9 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongodb@3.5.9 › bl@2.2.0\n# Overview\n[bl](https://github.com/rvagg/bl) is a library that allows you to collect buffers and access with a standard readable buffer interface.\n\nAffected versions of this package are vulnerable to Remote Memory Exposure. If user input ends up in `consume()` argument and can become negative, BufferList state can be corrupted, tricking it into exposing uninitialized memory via regular `.slice()` calls.\r\n\r\n## PoC by chalker\r\n```\r\nconst { BufferList } = require('bl')\r\nconst secret = require('crypto').randomBytes(256)\r\nfor (let i = 0; i < 1e6; i++) {\r\n  const clone = Buffer.from(secret)\r\n  const bl = new BufferList()\r\n  bl.append(Buffer.from('a'))\r\n  bl.consume(-1024)\r\n  const buf = bl.slice(1)\r\n  if (buf.indexOf(clone) !== -1) {\r\n    console.error(`Match (at ${i})`, buf)\r\n  }\r\n}\r\n```\n# Remediation\nUpgrade `bl` to version 2.2.1, 3.0.1, 4.0.3, 1.2.3 or higher.\n# References\n- [Github Commit](https://github.com/rvagg/bl/commit/8a8c13c880e2bef519133ea43e0e9b78b5d0c91e)\n- [Github Commit](https://github.com/rvagg/bl/commit/d3e240e3b8ba4048d3c76ef5fb9dd1f8872d3190)\n- [Github Commit](https://github.com/rvagg/bl/commit/dacc4ac7d5fcd6201bcf26fbd886951be9537466)\n- [GitHub Commit](https://github.com/rvagg/bl/commit/0bd87ec97be399b129fc62feff2943ffa21bcc00)\n- [HackerOne Report](https://hackerone.com/reports/966347)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-9",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-DUSTJSLINKEDIN-1089257",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in dustjs-linkedin"
              },
              "fullDescription": {
                "text": "dustjs-linkedin@2.5.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: dustjs-linkedin\n* Introduced through: goof@1.0.1 and dustjs-linkedin@2.5.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › dustjs-linkedin@2.5.0\n# Overview\n[dustjs-linkedin](https://www.npmjs.com/package/dustjs-linkedin) is a Javascript templating engine designed to run asynchronously on both the server and the browser.\n\nAffected versions of this package are vulnerable to Prototype Pollution. It is possible to pollute the `blocks` Array attribute of the object `context` within the `compileBlocks` function. This vulnerability can be leveraged for code execution since  this property is added to the `compiled` function which is then execute by the `vm` module.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nThere is no fixed version for `dustjs-linkedin`.\n# References\n- [GitHub Issue](https://github.com/linkedin/dustjs/issues/804)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-1321",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:dustjs-linkedin:20160819",
              "shortDescription": {
                "text": "High severity - Code Injection vulnerability in dustjs-linkedin"
              },
              "fullDescription": {
                "text": "dustjs-linkedin@2.5.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: dustjs-linkedin\n* Introduced through: goof@1.0.1 and dustjs-linkedin@2.5.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › dustjs-linkedin@2.5.0\n# Overview\n[dustjs-linkedin](https://www.npmjs.com/package/dustjs-linkedin) is a Javascript templating engine designed to run asynchronously on both the server and the browser.\n\nAffected versions of this package are vulnerable to Code Injection. Dust.js uses Javascript's `eval()` function to evaluate the \"if\" statement conditions. The input to the function is sanitized by escaping all potentially dangerous characters.\r\n\r\nHowever, if the variable passed in is an array, no escaping is applied, exposing an easy path to code injection. The risk of exploit is especially high given the fact `express`, `koa` and many other Node.js servers allow users to force a query parameter to be an array using the `param[]=value` notation.\n# Remediation\nUpgrade `dustjs-linkedin` to version 2.6.0 or higher.\n# References\n- [Artsploit Blog](https://artsploit.blogspot.co.il/2016/08/pprce2.html)\n- [GitHub Commit](https://github.com/linkedin/dustjs/pull/534/commits/884be3bb3a34a843e6fb411100088e9b02326bd4)\n- [GitHub Issue](https://github.com/linkedin/dustjs/issues/741)\n- [GitHub PR](https://github.com/linkedin/dustjs/pull/534)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-95",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-EJS-1049328",
              "shortDescription": {
                "text": "Medium severity - Arbitrary Code Injection vulnerability in ejs"
              },
              "fullDescription": {
                "text": "ejs@1.0.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ejs\n* Introduced through: goof@1.0.1 and ejs@1.0.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › ejs@1.0.0\n* _Introduced through_: goof@1.0.1 › ejs-locals@1.0.2 › ejs@0.8.8\n# Overview\n[ejs](https://www.npmjs.com/package/ejs) is a popular JavaScript templating engine.\n\nAffected versions of this package are vulnerable to Arbitrary Code Injection via the `render` and `renderFile`. If external input is flowing into the `options` parameter, an attacker is able run arbitrary code. This include the `filename`, `compileDebug`, and `client` option.\r\n\r\n# POC\r\n```\r\nlet ejs = require('ejs')\r\nejs.render('./views/test.ejs',{\r\n    filename:'/etc/passwd\\nfinally { this.global.process.mainModule.require(\\'child_process\\').execSync(\\'touch EJS_HACKED\\') }',\r\n    compileDebug: true,\r\n    message: 'test',\r\n    client: true\r\n})\r\n```\n# Remediation\nUpgrade `ejs` to version 3.1.6 or higher.\n# References\n- [GitHub Commit](https://github.com/mde/ejs/commit/abaee2be937236b1b8da9a1f55096c17dda905fd)\n- [GitHub Issue](https://github.com/mde/ejs/issues/571)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-94",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:ejs:20161128",
              "shortDescription": {
                "text": "High severity - Arbitrary Code Execution vulnerability in ejs"
              },
              "fullDescription": {
                "text": "(CVE-2017-1000228) ejs@1.0.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ejs\n* Introduced through: goof@1.0.1 and ejs@1.0.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › ejs@1.0.0\n* _Introduced through_: goof@1.0.1 › ejs-locals@1.0.2 › ejs@0.8.8\n# Overview\r\n[`ejs`](https://www.npmjs.com/package/ejs) is a popular JavaScript templating engine.\r\nAffected versions of the package are vulnerable to _Remote Code Execution_ by letting the attacker under certain conditions control the source folder from which the engine renders include files.\r\nYou can read more about this vulnerability on the [Snyk blog](https://snyk.io/blog/fixing-ejs-rce-vuln).\r\n\r\nThere's also a [Cross-site Scripting](https://snyk.io/vuln/npm:ejs:20161130) & [Denial of Service](https://snyk.io/vuln/npm:ejs:20161130-1) vulnerabilities caused by the same behaviour. \r\n\r\n# Details\r\n`ejs` provides a few different options for you to render a template, two being very similar: `ejs.render()` and `ejs.renderFile()`. The only difference being that `render` expects a string to be used for the template and `renderFile` expects a path to a template file.\r\n\r\nBoth functions can be invoked in two ways. The first is calling them with `template`, `data`, and `options`:\r\n```js\r\nejs.render(str, data, options);\r\n\r\nejs.renderFile(filename, data, options, callback)\r\n```\r\nThe second way would be by calling only the `template` and `data`, while `ejs` lets the `options` be passed as part of the `data`:\r\n```js\r\nejs.render(str, dataAndOptions);\r\n\r\nejs.renderFile(filename, dataAndOptions, callback)\r\n```\r\n\r\nIf used with a variable list supplied by the user (e.g. by reading it from the URI with `qs` or equivalent), an attacker can control `ejs` options. This includes the `root` option, which allows changing the project root for includes with an absolute path.  \r\n\r\n```js\r\nejs.renderFile('my-template', {root:'/bad/root/'}, callback);\r\n```\r\n\r\nBy passing along the root directive in the line above, any includes would now be pulled from `/bad/root` instead of the path intended. This allows the attacker to take control of the root directory for included scripts and divert it to a library under his control, thus leading to remote code execution.\r\n\r\nThe [fix](https://github.com/mde/ejs/commit/3d447c5a335844b25faec04b1132dbc721f9c8f6) introduced in version `2.5.3` blacklisted `root` options from options passed via the `data` object.\r\n\r\n# Disclosure Timeline\r\n- November 27th, 2016 - Reported the issue to package owner.\r\n- November 27th, 2016 - Issue acknowledged by package owner.\r\n- November 28th, 2016 - Issue fixed and version `2.5.3` released.\r\n\r\n# Remediation\r\nThe vulnerability can be resolved by either using the GitHub integration to [generate a pull-request](https://snyk.io/org/projects) from your dashboard or by running `snyk wizard` from the command-line interface.\r\nOtherwise, Upgrade `ejs` to version `2.5.3` or higher.\r\n\r\n# References\r\n- [Snyk Blog](https://snyk.io/blog/fixing-ejs-rce-vuln)\r\n- [Fix commit](https://github.com/mde/ejs/commit/3d447c5a335844b25faec04b1132dbc721f9c8f6)"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-94",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:ejs:20161130",
              "shortDescription": {
                "text": "Medium severity - Cross-site Scripting (XSS) vulnerability in ejs"
              },
              "fullDescription": {
                "text": "(CVE-2017-1000188) ejs@1.0.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ejs\n* Introduced through: goof@1.0.1 and ejs@1.0.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › ejs@1.0.0\n* _Introduced through_: goof@1.0.1 › ejs-locals@1.0.2 › ejs@0.8.8\n# Overview\n[`ejs`](https://www.npmjs.com/package/ejs) is a popular JavaScript templating engine.\nAffected versions of the package are vulnerable to _Cross-site Scripting_ by letting the attacker under certain conditions control and override the `filename` option causing it to render the value as is, without escaping it.\nYou can read more about this vulnerability on the [Snyk blog](https://snyk.io/blog/fixing-ejs-rce-vuln).\n\nThere's also a [Remote Code Execution](https://snyk.io/vuln/npm:ejs:20161128) & [Denial of Service](https://snyk.io/vuln/npm:ejs:20161130-1) vulnerabilities caused by the same behaviour.\n\n# Details\n`ejs` provides a few different options for you to render a template, two being very similar: `ejs.render()` and `ejs.renderFile()`. The only difference being that `render` expects a string to be used for the template and `renderFile` expects a path to a template file.\n\nBoth functions can be invoked in two ways. The first is calling them with `template`, `data`, and `options`:\n```js\nejs.render(str, data, options);\n\nejs.renderFile(filename, data, options, callback)\n```\nThe second way would be by calling only the `template` and `data`, while `ejs` lets the `options` be passed as part of the `data`:\n```js\nejs.render(str, dataAndOptions);\n\nejs.renderFile(filename, dataAndOptions, callback)\n```\n\nIf used with a variable list supplied by the user (e.g. by reading it from the URI with `qs` or equivalent), an attacker can control `ejs` options. This includes the `filename` option, which will be rendered as is when an error occurs during rendering. \n\n```js\nejs.renderFile('my-template', {filename:'<script>alert(1)</script>'}, callback);\n```\n\nThe [fix](https://github.com/mde/ejs/commit/49264e0037e313a0a3e033450b5c184112516d8f) introduced in version `2.5.3` blacklisted `root` options from options passed via the `data` object.\n\n# Disclosure Timeline\n- November 28th, 2016 - Reported the issue to package owner.\n- November 28th, 2016 - Issue acknowledged by package owner.\n- December 06th, 2016 - Issue fixed and version `2.5.5` released.\n\n# Remediation\nThe vulnerability can be resolved by either using the GitHub integration to [generate a pull-request](https://snyk.io/org/projects) from your dashboard or by running `snyk wizard` from the command-line interface.\nOtherwise, Upgrade `ejs` to version `2.5.5` or higher.\n\n# References\n- [Snyk Blog](https://snyk.io/blog/fixing-ejs-rce-vuln)\n- [Fix commit](https://github.com/mde/ejs/commit/49264e0037e313a0a3e033450b5c184112516d8f)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:ejs:20161130-1",
              "shortDescription": {
                "text": "Medium severity - Denial of Service (DoS) vulnerability in ejs"
              },
              "fullDescription": {
                "text": "(CVE-2017-1000189) ejs@1.0.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ejs\n* Introduced through: goof@1.0.1 and ejs@1.0.0\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › ejs@1.0.0\n* _Introduced through_: goof@1.0.1 › ejs-locals@1.0.2 › ejs@0.8.8\n# Overview\n[`ejs`](https://www.npmjs.com/package/ejs) is a popular JavaScript templating engine.\nAffected versions of the package are vulnerable to _Denial of Service_ by letting the attacker under certain conditions control and override the `localNames` option causing it to crash.\nYou can read more about this vulnerability on the [Snyk blog](https://snyk.io/blog/fixing-ejs-rce-vuln).\n\nThere's also a [Remote Code Execution](https://snyk.io/vuln/npm:ejs:20161128) & [Cross-site Scripting](https://snyk.io/vuln/npm:ejs:20161130) vulnerabilities caused by the same behaviour.\n\n# Details\n`ejs` provides a few different options for you to render a template, two being very similar: `ejs.render()` and `ejs.renderFile()`. The only difference being that `render` expects a string to be used for the template and `renderFile` expects a path to a template file.\n\nBoth functions can be invoked in two ways. The first is calling them with `template`, `data`, and `options`:\n```js\nejs.render(str, data, options);\n\nejs.renderFile(filename, data, options, callback)\n```\nThe second way would be by calling only the `template` and `data`, while `ejs` lets the `options` be passed as part of the `data`:\n```js\nejs.render(str, dataAndOptions);\n\nejs.renderFile(filename, dataAndOptions, callback)\n```\n\nIf used with a variable list supplied by the user (e.g. by reading it from the URI with `qs` or equivalent), an attacker can control `ejs` options. This includes the `localNames` option, which will cause the renderer to crash.\n\n```js\nejs.renderFile('my-template', {localNames:'try'}, callback);\n```\n\nThe [fix](https://github.com/mde/ejs/commit/49264e0037e313a0a3e033450b5c184112516d8f) introduced in version `2.5.3` blacklisted `root` options from options passed via the `data` object.\n\n# Disclosure Timeline\n- November 28th, 2016 - Reported the issue to package owner.\n- November 28th, 2016 - Issue acknowledged by package owner.\n- December 06th, 2016 - Issue fixed and version `2.5.5` released.\n\n# Remediation\nThe vulnerability can be resolved by either using the GitHub integration to [generate a pull-request](https://snyk.io/org/projects) from your dashboard or by running `snyk wizard` from the command-line interface.\nOtherwise, Upgrade `ejs` to version `2.5.5` or higher.\n\n# References\n- [Snyk Blog](https://snyk.io/blog/fixing-ejs-rce-vuln)\n- [Fix commit](https://github.com/mde/ejs/commit/49264e0037e313a0a3e033450b5c184112516d8f)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-EXPRESSFILEUPLOAD-473997",
              "shortDescription": {
                "text": "High severity - Denial of Service (DoS) vulnerability in express-fileupload"
              },
              "fullDescription": {
                "text": "express-fileupload@0.0.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: express-fileupload\n* Introduced through: goof@1.0.1 and express-fileupload@0.0.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › express-fileupload@0.0.5\n# Overview\n[express-fileupload](https://github.com/richardgirges/express-fileupload) is a file upload middleware for express that wraps around busboy.\n\nAffected versions of this package are vulnerable to Denial of Service (DoS). The package does not limit file name length.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its intended and legitimate users.\n\nUnlike other vulnerabilities, DoS attacks usually do not aim at breaching security. Rather, they are focused on making websites and services unavailable to genuine users resulting in downtime.\n\nOne popular Denial of Service vulnerability is DDoS (a Distributed Denial of Service), an attack that attempts to clog network pipes to the system by generating a large volume of traffic from many machines.\n\nWhen it comes to open source libraries, DoS vulnerabilities allow attackers to trigger such a crash or crippling of the service by using a flaw either in the application code or from the use of open source libraries.\n\nTwo common types of DoS vulnerabilities:\n\n* High CPU/Memory Consumption- An attacker sending crafted requests that could cause the system to take a disproportionate amount of time to process. For example, [commons-fileupload:commons-fileupload](SNYK-JAVA-COMMONSFILEUPLOAD-30082).\n\n* Crash - An attacker sending crafted requests that could cause the system to crash. For Example,  [npm `ws` package](https://snyk.io/vuln/npm:ws:20171108)\n\n# Remediation\nUpgrade `express-fileupload` to version 1.1.6-alpha.6 or higher.\n# References\n- [GitHub PR](https://github.com/richardgirges/express-fileupload/pull/171)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-EXPRESSFILEUPLOAD-595969",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in express-fileupload"
              },
              "fullDescription": {
                "text": "(CVE-2020-7699) express-fileupload@0.0.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: express-fileupload\n* Introduced through: goof@1.0.1 and express-fileupload@0.0.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › express-fileupload@0.0.5\n# Overview\n[express-fileupload](https://github.com/richardgirges/express-fileupload) is a file upload middleware for express that wraps around busboy.\n\nAffected versions of this package are vulnerable to Prototype Pollution. If the `parseNested` option is enabled, sending a corrupt HTTP request can lead to denial of service or arbitrary code execution.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `express-fileupload` to version 1.1.10 or higher.\n# References\n- [GitHub Issue](https://github.com/richardgirges/express-fileupload/issues/236)\n- [GitHub PR](https://github.com/richardgirges/express-fileupload/commit/9fca550f08a9dc07cc3500921f4fa7879cf88b8f)\n- [POSIX Vulnerability Blog](https://blog.p6.is/Real-World-JS-1/)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-1056767",
              "shortDescription": {
                "text": "Medium severity - Remote Code Execution (RCE) vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "(CVE-2021-23369) handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Remote Code Execution (RCE) when selecting certain compiling options to compile templates coming from an untrusted source.\r\n\r\n## POC\r\n```\r\n<script src=\"https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js\"></script> \r\n<script> \r\n// compile the template \r\nvar s = ` \r\n{{#with (__lookupGetter__ \"__proto__\")}} \r\n{{#with (./constructor.getOwnPropertyDescriptor . \"valueOf\")}} \r\n{{#with ../constructor.prototype}} \r\n{{../../constructor.defineProperty . \"hasOwnProperty\" ..}} \r\n{{/with}} \r\n{{/with}} \r\n{{/with}} \r\n{{#with \"constructor\"}} \r\n{{#with split}} \r\n{{pop (push \"alert('Vulnerable Handlebars JS when compiling in strict mode');\")}} \r\n{{#with .}} \r\n{{#with (concat (lookup join (slice 0 1)))}} \r\n{{#each (slice 2 3)}} \r\n{{#with (apply 0 ../..)}} \r\n{{.}} \r\n{{/with}} \r\n{{/each}} \r\n{{/with}} \r\n{{/with}} \r\n{{/with}} \r\n{{/with}} \r\n`;\r\nvar template = Handlebars.compile(s, { \r\nstrict: true \r\n}); \r\n// execute the compiled template and print the output to the console console.log(template({})); \r\n</script>\r\n```\n# Remediation\nUpgrade `handlebars` to version 4.7.7 or higher.\n# References\n- [GitHub Commit](https://github.com/handlebars-lang/handlebars.js/commit/b6d3de7123eebba603e321f04afdbae608e8fea8)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-94",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-1279029",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "(CVE-2021-23383) handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Prototype Pollution when selecting certain compiling options to compile templates coming from an untrusted source.\r\n\r\n## POC\r\n```\r\n<script src=\"https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js\"></script> \r\n<script> \r\n// compile the template \r\n\r\nvar s2 = `{{'a/.\") || alert(\"Vulnerable Handlebars JS when compiling in compat mode'}}`; \r\nvar template = Handlebars.compile(s2, { \r\ncompat: true \r\n}); \r\n// execute the compiled template and print the output to the console console.log(template({})); \r\n</script>\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `handlebars` to version 4.7.7 or higher.\n# References\n- [GitHub Commit](https://github.com/handlebars-lang/handlebars.js/commit/f0589701698268578199be25285b2ebea1c1e427)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-1321",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-173692",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Prototype Pollution. Templates may alter an Objects' prototype, thus allowing an attacker to execute arbitrary code on the server.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `handlebars` to version 4.0.14, 4.1.2 or higher.\n# References\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/7372d4e9dffc9d70c09671aa28b9392a1577fd86)\n- [GitHub Issue](https://github.com/wycats/handlebars.js/issues/1495)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/755)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-471",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-174183",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Prototype Pollution. A Prototype Pollution allowing Remote Code Execution can be exploited using the constructor, via the 'lookup' helper.\r\nThis vulnerability is due to an incomplete fix for: `SNYK-JS-HANDLEBARS-173692`\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `handlebars` to version 3.0.7, 4.1.2, 4.0.14 or higher.\n# References\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/cd38583216dce3252831916323202749431c773e)\n- [GitHub Issue](https://github.com/wycats/handlebars.js/issues/1495)\n- [SNYK-JS-HANDLEBARS-173692](https://snyk.io/vuln/SNYK-JS-HANDLEBARS-173692)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-471",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-469063",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "(CVE-2019-19919) handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n\n[handlebars](https://www.npmjs.com/package/handlebars) is a extension to the Mustache templating language.\n\n\nAffected versions of this package are vulnerable to Prototype Pollution.\nTemplates may alter an Object's `__proto__` and `__defineGetter__` properties, which may allow an attacker to execute arbitrary code on the server through crafted payloads.\n\n# Details\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\r\n\r\nThere are two main ways in which the pollution of prototypes occurs:\r\n\r\n-   Unsafe `Object` recursive merge\r\n    \r\n-   Property definition by path\r\n    \r\n\r\n## Unsafe Object recursive merge\r\n\r\nThe logic of a vulnerable recursive merge function follows the following high-level model:\r\n```\r\nmerge (target, source)\r\n\r\n  foreach property of source\r\n\r\n    if property exists and is an object on both the target and the source\r\n\r\n      merge(target[property], source[property])\r\n\r\n    else\r\n\r\n      target[property] = source[property]\r\n```\r\n<br>  \r\n\r\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\r\n\r\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\r\n\r\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\r\n\r\n## Property definition by path\r\n\r\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\r\n\r\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\r\n\r\n# Types of attacks\r\n\r\nThere are a few methods by which Prototype Pollution can be manipulated:\r\n\r\n| Type |Origin  |Short description |\r\n|--|--|--|\r\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\r\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\r\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\r\n\r\n# Affected environments\r\n\r\nThe following environments are susceptible to a Prototype Pollution attack:\r\n\r\n-   Application server\r\n    \r\n-   Web server\r\n    \r\n\r\n# How to prevent\r\n\r\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\r\n    \r\n2.  Require schema validation of JSON input.\r\n    \r\n3.  Avoid using unsafe recursive merge functions.\r\n    \r\n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\r\n    \r\n5.  As a best practice use `Map` instead of `Object`.\r\n\r\n## For more information on this vulnerability type:\r\n\r\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\n\nUpgrade `handlebars` to version 4.3.0, 3.8.0 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/213c0bbe3c4bd83a534d67384e5afa0000347ff6)\n\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/7b67a29a8c926b38af265c727ff6551fbb277111)\n\n- [GitHub Issue](https://github.com/wycats/handlebars.js/issues/1558)\n\n- [Reference](https://www.npmjs.com/advisories/1164)\n\n- [Release Notes](https://github.com/wycats/handlebars.js/blob/master/release-notes.md#v430---september-24th-2019)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-471",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-480388",
              "shortDescription": {
                "text": "High severity - Denial of Service (DoS) vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Denial of Service (DoS). The package's parser may be forced into an endless loop while processing specially-crafted templates, which may allow attackers to exhaust system resources leading to Denial of Service.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `handlebars` to version 4.4.5 or higher.\n# References\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/8d5530ee2c3ea9f0aee3fde310b9f36887d00b8b)\n- [GitHub Issue](https://github.com/wycats/handlebars.js/issues/1579)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1300)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-534478",
              "shortDescription": {
                "text": "High severity - Arbitrary Code Execution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Arbitrary Code Execution. The package's lookup helper doesn't validate templates correctly, allowing attackers to submit templates that execute arbitrary JavaScript in the system.\n# Remediation\nUpgrade `handlebars` to version 4.5.3, 3.0.8 or higher.\n# References\n- [NPM Security Advisory #1](https://www.npmjs.com/advisories/1316)\n- [NPM Security Advisory #2](https://www.npmjs.com/advisories/1324)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-94",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-534988",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\n\nAffected versions of this package are vulnerable to Prototype Pollution.\nIt is possible to add or modify properties to the Object prototype through a malicious template. This may allow attackers to crash the application or execute Arbitrary Code in specific conditions.\n\n# Details\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\r\n\r\nThere are two main ways in which the pollution of prototypes occurs:\r\n\r\n-   Unsafe `Object` recursive merge\r\n    \r\n-   Property definition by path\r\n    \r\n\r\n## Unsafe Object recursive merge\r\n\r\nThe logic of a vulnerable recursive merge function follows the following high-level model:\r\n```\r\nmerge (target, source)\r\n\r\n  foreach property of source\r\n\r\n    if property exists and is an object on both the target and the source\r\n\r\n      merge(target[property], source[property])\r\n\r\n    else\r\n\r\n      target[property] = source[property]\r\n```\r\n<br>  \r\n\r\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\r\n\r\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\r\n\r\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\r\n\r\n## Property definition by path\r\n\r\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\r\n\r\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\r\n\r\n# Types of attacks\r\n\r\nThere are a few methods by which Prototype Pollution can be manipulated:\r\n\r\n| Type |Origin  |Short description |\r\n|--|--|--|\r\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\r\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\r\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\r\n\r\n# Affected environments\r\n\r\nThe following environments are susceptible to a Prototype Pollution attack:\r\n\r\n-   Application server\r\n    \r\n-   Web server\r\n    \r\n\r\n# How to prevent\r\n\r\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\r\n    \r\n2.  Require schema validation of JSON input.\r\n    \r\n3.  Avoid using unsafe recursive merge functions.\r\n    \r\n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\r\n    \r\n5.  As a best practice use `Map` instead of `Object`.\r\n\r\n## For more information on this vulnerability type:\r\n\r\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\n\nUpgrade `handlebars` to version 4.5.3, 3.0.8 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/wycats/handlebars.js/commit/198887808780bbef9dba67a8af68ece091d5baa7)\n\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1325)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HANDLEBARS-567742",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in handlebars"
              },
              "fullDescription": {
                "text": "handlebars@4.0.11"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: handlebars\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11\n# Overview\n[handlebars](https://www.npmjs.com/package/handlebars) is an extension to the Mustache templating language.\n\nAffected versions of this package are vulnerable to Prototype Pollution. Prototype access to the template engine allows for potential code execution.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `handlebars` to version 4.6.0 or higher.\n# References\n- [GitHub PR](https://github.com/handlebars-lang/handlebars.js/pull/1633)\n- [HackerOne Report](https://hackerone.com/reports/726364)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HIGHLIGHTJS-1045326",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in highlight.js"
              },
              "fullDescription": {
                "text": "(CVE-2020-26237) highlight.js@9.18.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: highlight.js\n* Introduced through: goof@1.0.1, typeorm@0.2.24 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › typeorm@0.2.24 › cli-highlight@2.1.4 › highlight.js@9.18.1\n# Overview\n[highlight.js](https://www.npmjs.com/package/highlight.js) is a syntax highlighter written in JavaScript. It works in the browser as well as on the server. It works with pretty much any markup, doesn’t depend on any framework, and has automatic language detection.\n\nAffected versions of this package are vulnerable to Prototype Pollution. A malicious HTML code block can be crafted that will result in prototype pollution of the base object's prototype during highlighting. If you allow users to insert custom HTML code blocks into your page/app via parsing Markdown code blocks (or similar) and do not filter the language names the user can provide you may be vulnerable.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `highlight.js` to version 9.18.2, 10.1.2 or higher.\n# References\n- [GitHub Commit](https://github.com/highlightjs/highlight.js/commit/7241013ae011a585983e176ddc0489a7a52f6bb0)\n- [GitHub PR](https://github.com/highlightjs/highlight.js/pull/2636)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HIGHLIGHTJS-1048676",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in highlight.js"
              },
              "fullDescription": {
                "text": "highlight.js@9.18.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: highlight.js\n* Introduced through: goof@1.0.1, typeorm@0.2.24 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › typeorm@0.2.24 › cli-highlight@2.1.4 › highlight.js@9.18.1\n# Overview\n[highlight.js](https://www.npmjs.com/package/highlight.js) is a syntax highlighter written in JavaScript. It works in the browser as well as on the server. It works with pretty much any markup, doesn’t depend on any framework, and has automatic language detection.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS) via Exponential and Polynomial catastrophic backtracking in multiple language highlighting.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `highlight.js` to version 10.4.1 or higher.\n# References\n- [GitHub Commit](https://github.com/highlightjs/highlight.js/commit/373b9d862401162e832ce77305e49b859e110f9c)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-HOSTEDGITINFO-1088355",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in hosted-git-info"
              },
              "fullDescription": {
                "text": "(CVE-2021-23362) hosted-git-info@2.6.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: hosted-git-info\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › read-pkg-up@1.0.1 › read-pkg@1.1.0 › normalize-package-data@2.4.0 › hosted-git-info@2.6.0\n# Overview\n[hosted-git-info](https://www.npmjs.org/package/hosted-git-info) is a Provides metadata and conversions from repository urls for Github, Bitbucket and Gitlab\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS) via regular expression `shortcutMatch ` in the `fromUrl` function in index.js. The affected regular expression exhibits polynomial worst-case time complexity.\r\n\r\n## PoC by Yeting Li\r\n```\r\nvar hostedGitInfo = require(\"hosted-git-info\")\r\nfunction build_attack(n) {\r\n    var ret = \"a:\"\r\n    for (var i = 0; i < n; i++) {\r\n        ret += \"a\"\r\n    }\r\n    return ret + \"!\";\r\n}\r\n\r\nfor(var i = 1; i <= 5000000; i++) {\r\n   if (i % 1000 == 0) {\r\n        var time = Date.now();\r\n        var attack_str = build_attack(i)\r\n       var parsedInfo = hostedGitInfo.fromUrl(attack_str)\r\n        var time_cost = Date.now() - time;\r\n        console.log(\"attack_str.length: \" + attack_str.length + \": \" + time_cost+\" ms\")\r\n}\r\n```\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `hosted-git-info` to version 3.0.8, 2.8.9 or higher.\n# References\n- [GitHub Commit](https://github.com/npm/hosted-git-info/commit/bede0dc38e1785e732bf0a48ba6f81a4a908eba3)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-INI-1048974",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in ini"
              },
              "fullDescription": {
                "text": "(CVE-2020-7788) ini@1.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ini\n* Introduced through: goof@1.0.1, npmconf@0.0.24 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › npmconf@0.0.24 › config-chain@1.1.12 › ini@1.3.5\n* _Introduced through_: goof@1.0.1 › npmconf@0.0.24 › ini@1.1.0\n# Overview\n[ini](https://www.npmjs.org/package/ini) is an An ini encoder/decoder for node\n\nAffected versions of this package are vulnerable to Prototype Pollution. If an attacker submits a malicious INI file to an application that parses it with `ini.parse`, they will pollute the prototype on the application. This can be exploited further depending on the context.\r\n\r\n# PoC by Eugene Lim\r\n\r\npayload.ini\r\n```\r\n[__proto__]\r\npolluted = \"polluted\"\r\n```\r\n\r\npoc.js:\r\n```\r\nvar fs = require('fs')\r\nvar ini = require('ini')\r\n\r\nvar parsed = ini.parse(fs.readFileSync('./payload.ini', 'utf-8'))\r\nconsole.log(parsed)\r\nconsole.log(parsed.__proto__)\r\nconsole.log(polluted)\r\n```\r\n\r\n```\r\n> node poc.js\r\n{}\r\n{ polluted: 'polluted' }\r\n{ polluted: 'polluted' }\r\npolluted\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `ini` to version 1.3.6 or higher.\n# References\n- [Eugene Lim - Research Blog Post](https://medium.com/csg-govtech/supply-chain-pollution-discovering-a-16-million-download-week-node-js-2fa4d2c27cf7)\n- [GitHub Commit](https://github.com/npm/ini/commit/56d2805e07ccd94e2ba0984ac9240ff02d44b6f1)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-JQUERY-174006",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in jquery"
              },
              "fullDescription": {
                "text": "(CVE-2019-11358,CVE-2019-5428) jquery@2.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: jquery\n* Introduced through: goof@1.0.1 and jquery@2.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › jquery@2.2.4\n# Overview\n[jquery](https://www.npmjs.com/package/jquery) is a package that makes things like HTML document traversal and manipulation, event handling, animation, and Ajax much simpler with an easy-to-use API that works across a multitude of browsers.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The `extend` function can be tricked into modifying the prototype of `Object` when the attacker controls part of the structure passed to this function. This can let an attacker add or modify an existing property that will then exist on all objects.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `jquery` to version 3.4.0 or higher.\n# References\n- [GitHub Commit](https://github.com/jquery/jquery/commit/753d591aea698e57d6db58c9f722cd0808619b1b)\n- [GitHub PR](https://github.com/jquery/jquery/pull/4333)\n- [Hackerone Report](https://hackerone.com/reports/454365)\n- [Snyk Blog](https://snyk.io/blog/after-three-years-of-silence-a-new-jquery-prototype-pollution-vulnerability-emerges-once-again/)\n- [Third-Party Backported Patches Repo](https://github.com/DanielRuf/snyk-js-jquery-174006)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-JQUERY-565129",
              "shortDescription": {
                "text": "Medium severity - Cross-site Scripting (XSS) vulnerability in jquery"
              },
              "fullDescription": {
                "text": "(CVE-2020-11023) jquery@2.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: jquery\n* Introduced through: goof@1.0.1 and jquery@2.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › jquery@2.2.4\n# Overview\n\n[jquery](https://www.npmjs.com/package/jquery) is a package that makes things like HTML document traversal and manipulation, event handling, animation, and Ajax much simpler with an easy-to-use API that works across a multitude of browsers.\n\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS)\nPassing HTML containing `<option>` elements from untrusted sources - even after sanitizing it - to one of jQuery's DOM manipulation methods (i.e. `.html()`, `.append()`, and others) may execute untrusted code.\n\n# Details\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\r\n\r\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\r\n\r\nֿInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\r\n\r\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\r\n \r\nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \r\n\r\n## Types of attacks\r\nThere are a few methods by which XSS can be manipulated:\r\n\r\n|Type|Origin|Description|\r\n|--|--|--|\r\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\r\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \r\n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\r\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\r\n\r\n## Affected environments\r\nThe following environments are susceptible to an XSS attack:\r\n\r\n* Web servers\r\n* Application servers\r\n* Web application environments\r\n\r\n## How to prevent\r\nThis section describes the top best practices designed to specifically protect your code: \r\n\r\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \r\n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \r\n* Give users the option to disable client-side scripts.\r\n* Redirect invalid requests.\r\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\r\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\r\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n# Remediation\n\nUpgrade `jquery` to version 3.5.0 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/jquery/jquery/commit/1d61fd9407e6fbe82fe55cb0b938307aa0791f77)\n\n- [PoC](https://vulnerabledoma.in/jquery_htmlPrefilter_xss.html)\n\n- [Release Notes](https://blog.jquery.com/2020/04/10/jquery-3-5-0-released/)\n\n- [Security Blog](https://masatokinugawa.l0.cm/2020/05/jquery3.5.0-xss.html?spref=tw)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-JQUERY-567880",
              "shortDescription": {
                "text": "Medium severity - Cross-site Scripting (XSS) vulnerability in jquery"
              },
              "fullDescription": {
                "text": "(CVE-2020-11022) jquery@2.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: jquery\n* Introduced through: goof@1.0.1 and jquery@2.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › jquery@2.2.4\n# Overview\n\n[jquery](https://www.npmjs.com/package/jquery) is a package that makes things like HTML document traversal and manipulation, event handling, animation, and Ajax much simpler with an easy-to-use API that works across a multitude of browsers.\n\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS).\nPassing HTML from untrusted sources - even after sanitizing it - to one of jQuery's DOM manipulation methods (i.e. `.html(), .append()`, and others) may execute untrusted code.\n\n\n# Details:\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\r\n\r\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\r\n\r\nֿInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\r\n\r\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\r\n \r\nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \r\n\r\n## Types of attacks\r\nThere are a few methods by which XSS can be manipulated:\r\n\r\n|Type|Origin|Description|\r\n|--|--|--|\r\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\r\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \r\n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\r\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\r\n\r\n## Affected environments\r\nThe following environments are susceptible to an XSS attack:\r\n\r\n* Web servers\r\n* Application servers\r\n* Web application environments\r\n\r\n## How to prevent\r\nThis section describes the top best practices designed to specifically protect your code: \r\n\r\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \r\n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \r\n* Give users the option to disable client-side scripts.\r\n* Redirect invalid requests.\r\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\r\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\r\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n\n# Remediation\n\nUpgrade `jquery` to version 3.5.0 or higher.\n\n\n# References\n\n- [GHSA](https://github.com/jquery/jquery/security/advisories/GHSA-gxr4-xjj5-5px2)\n\n- [GitHub Commit](https://github.com/jquery/jquery/commit/1d61fd9407e6fbe82fe55cb0b938307aa0791f77)\n\n- [JQuery 3.5.0 Release](https://blog.jquery.com/2020/04/10/jquery-3-5-0-released/)\n\n- [JQuery Upgrade Guide](https://jquery.com/upgrade-guide/3.5/)\n\n- [PoC](https://vulnerabledoma.in/jquery_htmlPrefilter_xss.html)\n\n- [Security Blog](https://mksben.l0.cm/2020/05/jquery3.5.0-xss.html)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:jquery:20150627",
              "shortDescription": {
                "text": "Medium severity - Cross-site Scripting (XSS) vulnerability in jquery"
              },
              "fullDescription": {
                "text": "(CVE-2015-9251,CVE-2017-16012) jquery@2.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: jquery\n* Introduced through: goof@1.0.1 and jquery@2.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › jquery@2.2.4\n# Overview\n\n[jquery](https://www.npmjs.com/package/jquery) is a package that makes things like HTML document traversal and manipulation, event handling, animation, and Ajax much simpler with an easy-to-use API that works across a multitude of browsers.\n\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS)\nattacks when a cross-domain ajax request is performed without the `dataType` option causing `text/javascript` responses to be executed.\n\n# Details\n A cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\r\n\r\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\r\n\r\nֿInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\r\n\r\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\r\n \r\nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \r\n\r\n## Types of attacks\r\nThere are a few methods by which XSS can be manipulated:\r\n\r\n|Type|Origin|Description|\r\n|--|--|--|\r\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\r\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \r\n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\r\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\r\n\r\n## Affected environments\r\nThe following environments are susceptible to an XSS attack:\r\n\r\n* Web servers\r\n* Application servers\r\n* Web application environments\r\n\r\n## How to prevent\r\nThis section describes the top best practices designed to specifically protect your code: \r\n\r\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \r\n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \r\n* Give users the option to disable client-side scripts.\r\n* Redirect invalid requests.\r\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\r\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\r\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n\n# Remediation\n\nUpgrade `jquery` to version 1.12.2, 2.2.2, 3.0.0 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/jquery/jquery/commit/f60729f3903d17917dc351f3ac87794de379b0cc)\n\n- [GitHub Commit](https://github.com/jquery/jquery/pull/2588/commits/c254d308a7d3f1eac4d0b42837804cfffcba4bb2)\n\n- [GitHub Issue](https://github.com/jquery/jquery/issues/2432)\n\n- [GitHub PR](https://github.com/jquery/jquery/pull/2588)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-KERBEROS-568900",
              "shortDescription": {
                "text": "High severity - DLL Injection vulnerability in kerberos"
              },
              "fullDescription": {
                "text": "(CVE-2020-13110) kerberos@0.0.24"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: kerberos\n* Introduced through: goof@1.0.1, mongoose@4.2.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mongodb@2.0.46 › mongodb-core@1.2.19 › kerberos@0.0.24\n# Overview\n\nAffected versions of this package are vulnerable to DLL Injection. An attacker can execute arbitrary code by creating a file with the same name in a folder that precedes the intended file in the DLL path search.\n# Remediation\nUpgrade `kerberos` to version 1.0.0 or higher.\n# References\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1514)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-114",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-KINDOF-537849",
              "shortDescription": {
                "text": "Low severity - Validation Bypass vulnerability in kind-of"
              },
              "fullDescription": {
                "text": "(CVE-2019-20149) kind-of@6.0.2"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: kind-of\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › use@3.1.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon-node@2.1.1 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › to-regex@3.0.2 › define-property@2.0.2 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-accessor-descriptor@1.0.0 › kind-of@6.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › define-property@1.0.0 › is-descriptor@1.0.2 › is-data-descriptor@1.0.0 › kind-of@6.0.2\n# Overview\n[kind-of](https://github.com/jonschlinkert/kind-of) is a package that gets the native type of a value.\n\nAffected versions of this package are vulnerable to Validation Bypass. It leverages the built-in constructor of unsafe user-input to detect type information. However, a crafted payload can overwrite this built in attribute to manipulate the type detection result.\r\n\r\n# PoC by Feng Xiao\r\n```\r\nvar kindOf = require('kind-of');\r\n\r\n\r\nvar user_input = {\r\n  user: 'barney',\r\n  age: 36,\r\n  active: true,\r\n  \"constructor\":{\"name\":\"Symbol\"}\r\n};\r\nconsole.log(kindOf(user_input));\r\n```\n# Remediation\nUpgrade `kind-of` to version 6.0.3 or higher.\n# References\n- [GitHub Issue](https://github.com/jonschlinkert/kind-of/issues/30)\n- [GitHub PR](https://github.com/jonschlinkert/kind-of/pull/31)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-20",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-1018905",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2020-28500) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS) via the `toNumber`, `trim` and `trimEnd` functions.\r\n\r\n## POC\r\n```\r\nvar lo = require('lodash');\r\n\r\nfunction build_blank (n) {\r\nvar ret = \"1\"\r\nfor (var i = 0; i < n; i++) {\r\nret += \" \"\r\n}\r\n\r\nreturn ret + \"1\";\r\n}\r\n\r\nvar s = build_blank(50000)\r\nvar time0 = Date.now();\r\nlo.trim(s)\r\nvar time_cost0 = Date.now() - time0;\r\nconsole.log(\"time_cost0: \" + time_cost0)\r\n\r\nvar time1 = Date.now();\r\nlo.toNumber(s)\r\nvar time_cost1 = Date.now() - time1;\r\nconsole.log(\"time_cost1: \" + time_cost1)\r\n\r\nvar time2 = Date.now();\r\nlo.trimEnd(s)\r\nvar time_cost2 = Date.now() - time2;\r\nconsole.log(\"time_cost2: \" + time_cost2)\r\n```\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `lodash` to version 4.17.21 or higher.\n# References\n- [GitHub Commit](https://github.com/lodash/lodash/commit/c4847ebe7d14540bb28a8b932a9ce1b9ecbfee1a)\n- [GitHub Fix PR](https://github.com/lodash/lodash/pull/5065)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-1040724",
              "shortDescription": {
                "text": "High severity - Command Injection vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2021-23337) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Command Injection via `template`.\r\n\r\n## PoC\r\n```\r\nvar _ = require('lodash');\r\n\r\n_.template('', { variable: '){console.log(process.env)}; with(obj' })()\r\n```\n# Remediation\nUpgrade `lodash` to version 4.17.21 or higher.\n# References\n- [GitHub Commit](https://github.com/lodash/lodash/commit/3469357cff396a26c363f8c1b5a91dde28ba4b1c)\n- [Vulnerable Code](https://github.com/lodash/lodash/blob/ddfd9b11a0126db2302cb70ec9973b66baec0975/lodash.js#L14851)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-78",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-450202",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2019-10744) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The function `defaultsDeep` could be tricked into adding or modifying properties of `Object.prototype` using a `constructor` payload.\r\n\r\n# PoC by Snyk\r\n```\r\nconst mergeFn = require('lodash').defaultsDeep;\r\nconst payload = '{\"constructor\": {\"prototype\": {\"a0\": true}}}'\r\n\r\nfunction check() {\r\n    mergeFn({}, JSON.parse(payload));\r\n    if (({})[`a0`] === true) {\r\n        console.log(`Vulnerable to Prototype Pollution via ${payload}`);\r\n    }\r\n  }\r\n\r\ncheck();\r\n```\r\n\r\nFor more information, check out our [blog post](https://snyk.io/blog/snyk-research-team-discovers-severe-prototype-pollution-security-vulnerabilities-affecting-all-versions-of-lodash/)\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.12 or higher.\n# References\n- [GitHub Issue](https://github.com/lodash/lodash/issues/4348)\n- [GitHub PR](https://github.com/lodash/lodash/pull/4336)\n- [GitHub PR](https://github.com/lodash/lodash/pull/4355)\n- [GitHub PR](https://github.com/sailshq/lodash/pull/1)\n- [Node Security Advisory](https://www.npmjs.com/advisories/1065)\n- [Snyk Blog](https://snyk.io/blog/snyk-research-team-discovers-severe-prototype-pollution-security-vulnerabilities-affecting-all-versions-of-lodash/)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-567746",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2020-8203) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The function `zipObjectDeep` can be tricked into adding or modifying properties of the Object prototype. These properties will be present on all objects.\r\n\r\n# PoC\r\n```\r\nconst _ = require('lodash');\r\n_.zipObjectDeep(['__proto__.z'],[123])\r\nconsole.log(z) // 123\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.16 or higher.\n# References\n- [GitHub PR](https://github.com/lodash/lodash/pull/4759)\n- [HackerOne Report](https://hackerone.com/reports/712065)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-590103",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution in `zipObjectDeep` due to an incomplete fix for [CVE-2020-8203](https://snyk.io/vuln/SNYK-JS-LODASH-567746).\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.20 or higher.\n# References\n- [GitHub Issue](https://github.com/lodash/lodash/issues/4874)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-608086",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution via the `setWith` and `set` functions.\r\n\r\n## PoC by awarau\r\n* Create a JS file with this contents:\r\n```\r\nlod = require('lodash')\r\nlod.setWith({}, \"__proto__[test]\", \"123\")\r\nlod.set({}, \"__proto__[test2]\", \"456\")\r\nconsole.log(Object.prototype)\r\n```\r\n* Execute it with `node`\r\n* Observe that `test` and `test2` is now in the `Object.prototype`.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.17 or higher.\n# References\n- [HackerOne Report](https://hackerone.com/reports/864701)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-73638",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2018-16487) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The functions `merge`, `mergeWith`, and `defaultsDeep` could be tricked into adding or modifying properties of `Object.prototype`. This is due to an incomplete fix to `CVE-2018-3721`.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.11 or higher.\n# References\n- [GitHub Commit](https://github.com/lodash/lodash/commit/90e6199a161b6445b01454517b40ef65ebecd2ad)\n- [GitHub PR](https://github.com/lodash/lodash/pull/4337)\n- [HackerOne Report](https://hackerone.com/reports/380873)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1066)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1068)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1071)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/782)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-LODASH-73639",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2019-1010266) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-generator@6.26.1 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › lodash@4.17.10\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-instrument@1.10.1 › babel-template@6.26.0 › babel-traverse@6.26.0 › babel-types@6.26.0 › lodash@4.17.10\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). It parses dates using regex strings, which may cause a slowdown of 2 seconds per 50k characters.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `lodash` to version 4.17.11 or higher.\n# References\n- [GitHub Commit](https://github.com/lodash/lodash/commit/5c08f18d365b64063bfbfa686cbb97cdd6267347)\n- [GitHub Issue](https://github.com/lodash/lodash/issues/3359)\n- [GitHub PR](https://github.com/lodash/lodash/pull/4450)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-185",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:lodash:20180130",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in lodash"
              },
              "fullDescription": {
                "text": "(CVE-2018-3721) lodash@4.17.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: lodash\n* Introduced through: goof@1.0.1 and lodash@4.17.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › lodash@4.17.4\n# Overview\n[lodash](https://www.npmjs.com/package/lodash) is a modern JavaScript utility library delivering modularity, performance, & extras.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The utilities function allow modification of the `Object` prototype. If an attacker can control part of the structure passed to this function, they could add or modify an existing property.  \r\n\r\n# PoC by Olivier Arteau (HoLyVieR)\r\n```js\r\nvar _= require('lodash');\r\nvar malicious_payload = '{\"__proto__\":{\"oops\":\"It works !\"}}';\r\n\r\nvar a = {};\r\nconsole.log(\"Before : \" + a.oops);\r\n_.merge({}, JSON.parse(malicious_payload));\r\nconsole.log(\"After : \" + a.oops);\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `lodash` to version 4.17.5 or higher.\n# References\n- [GitHub Commit](https://github.com/lodash/lodash/commit/d8e069cc3410082e44eb18fcf8e7f3d08ebe1d4a)\n- [GitHub PR](https://github.com/lodash/lodash/pull/4337)\n- [HackerOne Report](https://hackerone.com/reports/310443)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1067)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1069)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1070)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MARKED-174116",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). The `inline.text regex` may take quadratic time to scan for potential email addresses starting at every point.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `marked` to version 0.6.2 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/commit/00f1f7a23916ef27186d0904635aa3509af63d47)\n- [GitHub Commit](https://github.com/markedjs/marked/pull/1460/commits/be27472a8169dda7875330939f8115ab677cdc07)\n- [GitHub PR](https://github.com/markedjs/marked/pull/1460)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/812)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MARKED-451540",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). A  Denial of Service condition could be triggered through exploitation of the `heading` regex.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `marked` to version 0.4.0 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/commit/09afabf69c6d0c919c03443f47bdfe476566105d)\n- [GitHub PR](https://github.com/markedjs/marked/pull/1224)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MARKED-584281",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS  ) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS  ). The `em` regex within `src/rules.js` file have multiple unused capture groups which could lead to a denial of service attack if user input is reachable.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `marked` to version 1.1.1 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/commit/bd4f8c464befad2b304d51e33e89e567326e62e0)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20150520",
              "shortDescription": {
                "text": "High severity - Cross-site Scripting (XSS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "(CVE-2016-10531) marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS). An attacker could bypass its output sanitization (`sanitize: true`) protection. Using the [HTML Coded Character Set](https://www.w3.org/MarkUp/html-spec/html-spec_13.html#SEC13), attackers can inject `javascript:` code snippets into the output. For example, the following input `javascript&#x58document;alert&#40;1&#41;`  will result in `alert(1)` being executed when the user clicks on the link.\n# Details\n\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\n\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\n\nInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\n\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\n \nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \n\n## Types of attacks\nThere are a few methods by which XSS can be manipulated:\n\n|Type|Origin|Description|\n|--|--|--|\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\n\n## Affected environments\nThe following environments are susceptible to an XSS attack:\n\n* Web servers\n* Application servers\n* Web application environments\n\n## How to prevent\nThis section describes the top best practices designed to specifically protect your code: \n\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \n* Give users the option to disable client-side scripts.\n* Redirect invalid requests.\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n# Remediation\nUpgrade `marked` to version 0.3.6 or higher.\n# References\n- [GitHub Commit](https://github.com/chjj/marked/pull/592/commits/2cff85979be8e7a026a9aca35542c470cf5da523)\n- [GitHub PR](https://github.com/chjj/marked/pull/592)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20170112",
              "shortDescription": {
                "text": "High severity - Cross-site Scripting (XSS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "(CVE-2017-1000427) marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS). Data URIs enable embedding small files in line in HTML documents, provided in the URL itself.\r\nAttackers can craft malicious web pages containing either HTML or script code that utilizes the data URI scheme, allowing them to bypass access controls or steal sensitive information.\r\n\r\nAn example of data URI used to deliver javascript code. The data holds `<script>alert('XSS')</script>` tag in base64 encoded format.\r\n```html\r\n[xss link](data:text/html;base64,PHNjcmlwdD5hbGVydCgnWFNTJyk8L3NjcmlwdD4K)\r\n```\n# Details\n\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\n\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\n\nInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\n\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\n \nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \n\n## Types of attacks\nThere are a few methods by which XSS can be manipulated:\n\n|Type|Origin|Description|\n|--|--|--|\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\n\n## Affected environments\nThe following environments are susceptible to an XSS attack:\n\n* Web servers\n* Application servers\n* Web application environments\n\n## How to prevent\nThis section describes the top best practices designed to specifically protect your code: \n\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \n* Give users the option to disable client-side scripts.\n* Redirect invalid requests.\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n# Remediation\nUpgrade `marked` to version 0.3.7 or higher.\n# References\n- [GitHub Commit](https://github.com/chjj/marked/commit/cd2f6f5b7091154c5526e79b5f3bfb4d15995a51)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20170815",
              "shortDescription": {
                "text": "High severity - Cross-site Scripting (XSS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS). Browsers support both lowercase and uppercase x in hexadecimal form of HTML character entity, but marked [unescaped only lowercase](https://github.com/chjj/marked/blob/v0.3.7/lib/marked.js#L1096-L1108).\r\n\r\nThis may allow an attacker to create a link with javascript code.\r\n\r\nFor example:\r\n```js\r\nvar marked = require('marked');\r\nmarked.setOptions({\r\n  renderer: new marked.Renderer(),\r\n  sanitize: true\r\n});\r\n\r\ntext = `\r\nlower[click me](javascript&#x3a;...)lower\r\nupper[click me](javascript&#X3a;...)upper\r\n`;\r\n\r\nconsole.log(marked(text));\r\n```\r\n\r\nwill render the following:\r\n\r\n```html\r\n<p>lowerlower\r\nupper<a href=\"javascript&#X3a;...\">click me</a>upper</p>\r\n\r\n```\n# Details\n\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\n\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\n\nInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\n\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\n \nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \n\n## Types of attacks\nThere are a few methods by which XSS can be manipulated:\n\n|Type|Origin|Description|\n|--|--|--|\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\n\n## Affected environments\nThe following environments are susceptible to an XSS attack:\n\n* Web servers\n* Application servers\n* Web application environments\n\n## How to prevent\nThis section describes the top best practices designed to specifically protect your code: \n\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \n* Give users the option to disable client-side scripts.\n* Redirect invalid requests.\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n# Remediation\nUpgrade `marked` to version 0.3.9 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/pull/976/commits/6d1901ff71abb83aa32ca9a5ce47471382ea42a9)\n- [GitHub Issue](https://github.com/chjj/marked/issues/925)\n- [GitHub PR](https://github.com/chjj/marked/pull/958)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20170815-1",
              "shortDescription": {
                "text": "Medium severity - Cross-site Scripting (XSS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Cross-site Scripting (XSS). When mangling is disabled via option `mangle`, marked doesn't escape target `href`. This may allow an attacker to inject arbitrary `html-event` into resulting a tag.\r\n\r\nFor example:\r\n```js\r\nvar marked = require('marked');\r\nmarked.setOptions({\r\n  renderer: new marked.Renderer(),\r\n  sanitize: true,\r\n  mangle: false\r\n});\r\n\r\ntext = `\r\n<bar\"onclick=\"alert('XSS')\"@foo>\r\n`;\r\n\r\nconsole.log(marked(text));\r\n```\r\n\r\nwill render:\r\n\r\n```html\r\n<p><a href=\"mailto:bar\"onclick=\"alert('XSS')\"@foo\">bar\"onclick=\"alert('XSS')\"@foo</a></p>\r\n```\n# Details\n\nA cross-site scripting attack occurs when the attacker tricks a legitimate web-based application or site to accept a request as originating from a trusted source.\n\nThis is done by escaping the context of the web application; the web application then delivers that data to its users along with other trusted dynamic content, without validating it. The browser unknowingly executes malicious script on the client side (through client-side languages; usually JavaScript or HTML)  in order to perform actions that are otherwise typically blocked by the browser’s Same Origin Policy.\n\nInjecting malicious code is the most prevalent manner by which XSS is exploited; for this reason, escaping characters in order to prevent this manipulation is the top method for securing code against this vulnerability.\n\nEscaping means that the application is coded to mark key characters, and particularly key characters included in user input, to prevent those characters from being interpreted in a dangerous context. For example, in HTML, `<` can be coded as  `&lt`; and `>` can be coded as `&gt`; in order to be interpreted and displayed as themselves in text, while within the code itself, they are used for HTML tags. If malicious content is injected into an application that escapes special characters and that malicious content uses `<` and `>` as HTML tags, those characters are nonetheless not interpreted as HTML tags by the browser if they’ve been correctly escaped in the application code and in this way the attempted attack is diverted.\n \nThe most prominent use of XSS is to steal cookies (source: OWASP HttpOnly) and hijack user sessions, but XSS exploits have been used to expose sensitive information, enable access to privileged services and functionality and deliver malware. \n\n## Types of attacks\nThere are a few methods by which XSS can be manipulated:\n\n|Type|Origin|Description|\n|--|--|--|\n|**Stored**|Server|The malicious code is inserted in the application (usually as a link) by the attacker. The code is activated every time a user clicks the link.|\n|**Reflected**|Server|The attacker delivers a malicious link externally from the vulnerable web site application to a user. When clicked, malicious code is sent to the vulnerable web site, which reflects the attack back to the user’s browser.| \n|**DOM-based**|Client|The attacker forces the user’s browser to render a malicious page. The data in the page itself delivers the cross-site scripting data.|\n|**Mutated**| |The attacker injects code that appears safe, but is then rewritten and modified by the browser, while parsing the markup. An example is rebalancing unclosed quotation marks or even adding quotation marks to unquoted parameters.|\n\n## Affected environments\nThe following environments are susceptible to an XSS attack:\n\n* Web servers\n* Application servers\n* Web application environments\n\n## How to prevent\nThis section describes the top best practices designed to specifically protect your code: \n\n* Sanitize data input in an HTTP request before reflecting it back, ensuring all data is validated, filtered or escaped before echoing anything back to the user, such as the values of query parameters during searches. \n* Convert special characters such as `?`, `&`, `/`, `<`, `>` and spaces to their respective HTML or URL encoded equivalents. \n* Give users the option to disable client-side scripts.\n* Redirect invalid requests.\n* Detect simultaneous logins, including those from two separate IP addresses, and invalidate those sessions.\n* Use and enforce a Content Security Policy (source: Wikipedia) to disable any features that might be manipulated for an XSS attack.\n* Read the documentation for any of the libraries referenced in your code to understand which elements allow for embedded HTML.\n\n# Remediation\nUpgrade `marked` to version 0.3.9 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/pull/976/commits/cb72584c5d9d32ebfdbb99e35fb9b81af2b79686)\n- [GitHub Issue](https://github.com/chjj/marked/issues/926)\n- [GitHub PR](https://github.com/chjj/marked/pull/958)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-79",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20170907",
              "shortDescription": {
                "text": "High severity - Regular Expression Denial of Service (ReDoS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "(CVE-2017-16114) marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS)\nwhen parsing the input markdown content (1,000 characters costs around 6 seconds matching time).\n\n# Details\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\n\nUpgrade `marked` to version 0.3.9 or higher.\n\n\n# References\n\n- [GitHub Issue](https://github.com/chjj/marked/issues/937)\n\n- [GitHub PR](https://github.com/chjj/marked/pull/958)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:marked:20180225",
              "shortDescription": {
                "text": "High severity - Regular Expression Denial of Service (ReDoS) vulnerability in marked"
              },
              "fullDescription": {
                "text": "marked@0.3.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: marked\n* Introduced through: goof@1.0.1 and marked@0.3.5\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › marked@0.3.5\n# Overview\n[marked](https://marked.js.org/) is a low-level compiler for parsing markdown without caching or blocking for long periods of time.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). This can cause an impact of about 10 seconds matching time for data 150 characters long.\r\n\r\n# Disclosure Timeline\r\n* Feb 21th, 2018 - Initial Disclosure to package owner\r\n* Feb 21th, 2018 - Initial Response from package owner\r\n* Feb 26th, 2018 - Fix issued\r\n* Feb 27th, 2018 - Vulnerability published\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `marked` to version 0.3.18 or higher.\n# References\n- [GitHub Commit](https://github.com/markedjs/marked/pull/1083/commits/b15e42b67cec9ded8505e9d68bb8741ad7a9590d)\n- [GitHub PR](https://github.com/markedjs/marked/pull/1083)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-185",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MINIMIST-559764",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in minimist"
              },
              "fullDescription": {
                "text": "(CVE-2020-7598) minimist@1.2.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: minimist\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › coveralls@3.0.9 › minimist@1.2.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › caching-transform@1.0.1 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › find-cache-dir@0.1.1 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-report@1.1.3 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-source-maps@1.2.3 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › spawn-wrap@1.4.2 › mkdirp@0.5.1 › minimist@0.0.8\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-reports@1.4.0 › handlebars@4.0.11 › optimist@0.6.1 › minimist@0.0.8\n# Overview\n[minimist](https://www.npmjs.com/package/minimist) is a parse argument options module.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The library could be tricked into adding or modifying properties of `Object.prototype` using a `constructor` or `__proto__` payload.\r\n\r\n# PoC by Snyk\r\n```\r\nrequire('minimist')('--__proto__.injected0 value0'.split(' '));\r\nconsole.log(({}).injected0 === 'value0'); // true\r\n\r\nrequire('minimist')('--constructor.prototype.injected1 value1'.split(' '));\r\nconsole.log(({}).injected1 === 'value1'); // true\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `minimist` to version 0.2.1, 1.2.3 or higher.\n# References\n- [Command Injection PoC](https://gist.github.com/Kirill89/47feb345b09bf081317f08dd43403a8a)\n- [GitHub Fix Commit #1](https://github.com/substack/minimist/commit/63e7ed05aa4b1889ec2f3b196426db4500cbda94)\n- [GitHub Fix Commit #2](https://github.com/substack/minimist/commit/38a4d1caead72ef99e824bb420a2528eec03d9ab)\n- [Snyk Research Blog](https://snyk.io/blog/prototype-pollution-minimist/)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MIXINDEEP-450212",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in mixin-deep"
              },
              "fullDescription": {
                "text": "(CVE-2019-10746) mixin-deep@1.3.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mixin-deep\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › mixin-deep@1.3.1\n# Overview\n[mixin-deep](https://www.npmjs.com/package/mixin-deep) is a package that deeply mixes the properties of objects into the first object.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The function `mixin-deep` could be tricked into adding or modifying properties of `Object.prototype` using a `constructor` payload.\r\n\r\n# PoC by Snyk\r\n```\r\nconst mixin = require('mixin-deep');\r\nconst payload = '{\"constructor\": {\"prototype\": {\"a0\": true}}}'\r\n\r\nfunction check() {\r\n    mixin({}, JSON.parse(payload));\r\n    if (({})[`a0`] === true) {\r\n          console.log(`Vulnerable to Prototype Pollution via ${payload}`)\r\n  }\r\n}\r\n\r\ncheck();\r\n```\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `mixin-deep` to version 2.0.1, 1.3.2 or higher.\n# References\n- [GitHub Commit](https://github.com/jonschlinkert/mixin-deep/commit/8f464c8ce9761a8c9c2b3457eaeee9d404fa7af9)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MONGODB-473855",
              "shortDescription": {
                "text": "High severity - Denial of Service (DoS) vulnerability in mongodb"
              },
              "fullDescription": {
                "text": "mongodb@2.0.46"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mongodb\n* Introduced through: goof@1.0.1, mongoose@4.2.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mongodb@2.0.46\n# Overview\n[mongodb](https://www.npmjs.com/package/mongodb) is an official MongoDB driver for Node.js.\n\nAffected versions of this package are vulnerable to Denial of Service (DoS). The package fails to properly catch an exception when a collection name is invalid and the DB does not exist, crashing the application.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `mongodb` to version 3.1.13 or higher.\n# References\n- [NPM Security Advisory](https://www.npmjs.com/advisories/1203)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MONGOOSE-1086688",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in mongoose"
              },
              "fullDescription": {
                "text": "mongoose@4.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mongoose\n* Introduced through: goof@1.0.1 and mongoose@4.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4\n# Overview\n[mongoose](https://www.npmjs.com/package/mongoose) is a Mongoose is a MongoDB object modeling tool designed to work in an asynchronous environment.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The `mongoose.Schema()` function is subject to prototype pollution due to the recursively calling of `Schema.prototype.add()` function to add new items into the schema object. This vulnerability allows modification of the Object prototype.\r\n\r\n\r\n## PoC\r\n```\r\nmongoose = require('mongoose');\r\nmongoose.version; //'5.12.0'\r\nvar malicious_payload = '{\"__proto__\":{\"polluted\":\"HACKED\"}}';\r\nconsole.log('Before:', {}.polluted); // undefined\r\nmongoose.Schema(JSON.parse(malicious_payload));\r\nconsole.log('After:', {}.polluted); // HACKED\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `mongoose` to version 5.12.2 or higher.\n# References\n- [GitHub Commit](https://github.com/Automattic/mongoose/commit/3ed44ffa13737be9fc0d709980da9c3c552d54e7)\n- [GitHub Issue](https://github.com/Automattic/mongoose/issues/10035)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-1321",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MONGOOSE-472486",
              "shortDescription": {
                "text": "Medium severity - Information Exposure vulnerability in mongoose"
              },
              "fullDescription": {
                "text": "(CVE-2019-17426) mongoose@4.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mongoose\n* Introduced through: goof@1.0.1 and mongoose@4.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4\n# Overview\n[mongoose](https://www.npmjs.com/package/mongoose) is a Mongoose is a MongoDB object modeling tool designed to work in an asynchronous environment.\n\nAffected versions of this package are vulnerable to Information Exposure. Any query object with a `_bsontype` attribute is ignored, allowing attackers to bypass access control.\n# Remediation\nUpgrade `mongoose` to version 4.13.21, 5.7.5 or higher.\n# References\n- [GitHub Commit](https://github.com/Automattic/mongoose/commit/f3eca5b94d822225c04e96cbeed9f095afb3c31c)\n- [GitHub Issue](https://github.com/Automattic/mongoose/issues/8222)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-200",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:mongoose:20160116",
              "shortDescription": {
                "text": "Medium severity - Remote Memory Exposure vulnerability in mongoose"
              },
              "fullDescription": {
                "text": "mongoose@4.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mongoose\n* Introduced through: goof@1.0.1 and mongoose@4.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4\n# Overview\r\nA potential memory disclosure vulnerability exists in mongoose.\r\nA `Buffer` field in a MongoDB document can be used to expose sensitive\r\ninformation such as code, runtime memory and user data into MongoDB.\r\n\r\n## Details\r\nInitializing a `Buffer` field in a document with integer `N` creates a `Buffer`\r\nof length `N` with non zero-ed out memory.\r\n**Example:**\r\n```\r\nvar x = new Buffer(100); // uninitialized Buffer of length 100\r\n// vs\r\nvar x = new Buffer('100'); // initialized Buffer with value of '100'\r\n```\r\nInitializing a MongoDB document field in such manner will dump uninitialized\r\nmemory into MongoDB.\r\nThe patch wraps `Buffer` field initialization in mongoose by converting a\r\n`number` value `N` to array `[N]`, initializing the `Buffer` with `N` in its\r\nbinary form.\r\n\r\n### Proof of concept\r\n```javascript\r\nvar mongoose = require('mongoose');\r\nmongoose.connect('mongodb://localhost/bufftest');\r\n\r\n// data: Buffer is not uncommon, taken straight from the docs: http://mongoosejs.com/docs/schematypes.html\r\nmongoose.model('Item', new mongoose.Schema({id: String, data: Buffer}));\r\n\r\nvar Item = mongoose.model('Item');\r\n\r\nvar sample = new Item();\r\nsample.id = 'item1';\r\n\r\n// This will create an uninitialized buffer of size 100\r\nsample.data = 100;\r\nsample.save(function () {\r\n    Item.findOne(function (err, result) {\r\n        // Print out the data (exposed memory)\r\n        console.log(result.data.toString('ascii'))\r\n        mongoose.connection.db.dropDatabase(); // Clean up everything\r\n        process.exit();\r\n    });\r\n});\r\n```\r\n\r\n# Remediation\r\nUpgrade `mongoose` to version >= 3.8.39 or >= 4.3.6.\r\n\r\nIf a direct dependency update is not possible, use [`snyk wizard`](https://snyk.io/docs/using-snyk#wizard) to patch this vulnerability.\r\n\r\n# References\r\n- [GitHub Issue](https://github.com/Automattic/mongoose/issues/3764)\r\n- [Blog: Node Buffer API fix](https://github.com/ChALkeR/notes/blob/master/Lets-fix-Buffer-API.md#previous-materials)\r\n- [Blog: Information about Buffer](https://github.com/ChALkeR/notes/blob/master/Buffer-knows-everything.md)"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-201",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MQUERY-1050858",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in mquery"
              },
              "fullDescription": {
                "text": "(CVE-2020-35149) mquery@1.6.3"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mquery\n* Introduced through: goof@1.0.1, mongoose@4.2.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mquery@1.6.3\n# Overview\n[mquery](https://www.npmjs.org/package/mquery) is an Expressive query building for MongoDB\n\nAffected versions of this package are vulnerable to Prototype Pollution via the `merge` function within `lib/utils.js`.  Depending on if user input is provided, an attacker can overwrite and pollute the object prototype of a program. \r\n\r\n\r\n## PoC\r\n```\r\n   require('./env').getCollection(function(err, collection) {\r\n      assert.ifError(err);\r\n      col = collection;\r\n      done();\r\n    });\r\n    var payload = JSON.parse('{\"__proto__\": {\"polluted\": \"vulnerable\"}}');\r\n    var m = mquery(payload);\r\n    console.log({}.polluted);\r\n// The empty object {} will have a property called polluted which will print vulnerable\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `mquery` to version 3.2.3 or higher.\n# References\n- [mquery GitHub Commit](https://github.com/aheckmann/mquery/commit/792e69fd0a7281a0300be5cade5a6d7c1d468ad4)\n- [rxdb GitHub Commit](https://github.com/pubkey/rxdb/commit/b72ea7789ed61649ed05926bd6bac6d2bb6662d6)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-MQUERY-1089718",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in mquery"
              },
              "fullDescription": {
                "text": "mquery@1.6.3"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mquery\n* Introduced through: goof@1.0.1, mongoose@4.2.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mquery@1.6.3\n# Overview\n[mquery](https://www.npmjs.org/package/mquery) is an Expressive query building for MongoDB\n\nAffected versions of this package are vulnerable to Prototype Pollution via the `mergeClone()` function.\r\n\r\n## PoC by zhou, peng\r\n```\r\nmquery = require('mquery');\r\nvar malicious_payload = '{\"__proto__\":{\"polluted\":\"HACKED\"}}';\r\nconsole.log('Before:', {}.polluted); // undefined\r\nmquery.utils.mergeClone({}, JSON.parse(malicious_payload));\r\nconsole.log('After:', {}.polluted); // HACKED\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `mquery` to version 3.2.5 or higher.\n# References\n- [GitHub Commit](https://github.com/aheckmann/mquery/commit/158f059e058579d2d08c2f1380689f5f69336778)\n- [GitHub PR](https://github.com/aheckmann/mquery/pull/121)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-1321",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-PATHPARSE-1077067",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in path-parse"
              },
              "fullDescription": {
                "text": "(CVE-2021-23343) path-parse@1.0.5"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: path-parse\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › istanbul-lib-report@1.1.3 › path-parse@1.0.5\n# Overview\n[path-parse](https://www.npmjs.org/package/path-parse) is a Node.js path.parse() ponyfill\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS) via `splitDeviceRe`, `splitTailRe`, and `splitPathRe` regular expressions. ReDoS exhibits polynomial worst-case time complexity.\r\n\r\n## PoC\r\n```\r\nvar pathParse = require('path-parse');\r\nfunction build_attack(n) {\r\n    var ret = \"\"\r\n    for (var i = 0; i < n; i++) {\r\n        ret += \"/\"\r\n    }\r\n    return ret + \"◎\";\r\n}\r\n\r\nfor(var i = 1; i <= 5000000; i++) {\r\n    if (i % 10000 == 0) {\r\n        var time = Date.now();\r\n        var attack_str = build_attack(i)\r\n        pathParse(attack_str);\r\n        var time_cost = Date.now() - time;\r\n        console.log(\"attack_str.length: \" + attack_str.length + \": \" + time_cost+\" ms\")\r\n }\r\n}\r\n```\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `path-parse` to version 1.0.7 or higher.\n# References\n- [GitHub Issue 1](https://github.com/jbgutierrez/path-parse/issues/8)\n- [GitHub PR](https://github.com/jbgutierrez/path-parse/pull/10)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-SETVALUE-450213",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in set-value"
              },
              "fullDescription": {
                "text": "(CVE-2019-10747) set-value@2.0.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: set-value\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › set-value@2.0.0\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › braces@2.3.2 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › nanomatch@1.2.9 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › test-exclude@4.2.1 › micromatch@3.1.10 › extglob@2.0.4 › expand-brackets@2.1.4 › snapdragon@0.8.2 › base@0.11.2 › cache-base@1.0.1 › union-value@1.0.0 › set-value@0.4.3\n# Overview\n[set-value](https://www.npmjs.com/package/set-value) is a package that creates nested values and any intermediaries using dot notation ('a.b.c') paths.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The function `set-value` could be tricked into adding or modifying properties of `Object.prototype` using any of the `constructor`, `prototype` and `_proto_` payloads.\r\n\r\n# PoC by Snyk\r\n```\r\nconst setFn = require('set-value');\r\nconst paths = [\r\n  'constructor.prototype.a0',\r\n  '__proto__.a1',\r\n];\r\n\r\nfunction check() {\r\n  for (const p of paths) {\r\n      setFn({}, p, true);\r\n  }\r\n  for (let i = 0; i < paths.length; i++) {\r\n      if (({})[`a${i}`] === true) {\r\n          console.log(`Yes with ${paths[i]}`);\r\n      }\r\n  }\r\n}\r\n\r\ncheck();\r\n\r\n```\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `set-value` to version 2.0.1, 3.0.1 or higher.\n# References\n- [GitHub Commit](https://github.com/jonschlinkert/set-value/commit/95e9d9923f8a8b4a01da1ea138fcc39ec7b6b15f)\n- [NPM Security Advisory](https://nodesecurity.io/advisories/1012)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-TYPEORM-590152",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in typeorm"
              },
              "fullDescription": {
                "text": "(CVE-2020-8158) typeorm@0.2.24"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: typeorm\n* Introduced through: goof@1.0.1 and typeorm@0.2.24\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › typeorm@0.2.24\n# Overview\n[typeorm](https://www.npmjs.com/package/typeorm) is an ORM that can run in NodeJS, Browser, Cordova, PhoneGap, Ionic, React Native, NativeScript, Expo, and Electron platforms and can be used with TypeScript and JavaScript (ES5, ES6, ES7, ES8).\n\nAffected versions of this package are vulnerable to Prototype Pollution. It allows an attacker that is able to save a specially crafted object to pollute the `Object` prototype and cause side effects on the library/application logic, such as Denial of Service attacks and/or SQL injections.\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `typeorm` to version 0.2.25 or higher.\n# References\n- [GitHub PR](https://github.com/typeorm/typeorm/pull/6096)\n- [HackerOne Report](https://hackerone.com/reports/869574)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-UNDERSCORE-1080984",
              "shortDescription": {
                "text": "Medium severity - Arbitrary Code Injection vulnerability in underscore"
              },
              "fullDescription": {
                "text": "(CVE-2021-23358) underscore@1.9.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: underscore\n* Introduced through: goof@1.0.1, cfenv@1.2.2 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › cfenv@1.2.2 › underscore@1.9.1\n# Overview\n[underscore](https://www.npmjs.org/package/underscore) is a JavaScript's functional programming helper library.\n\nAffected versions of this package are vulnerable to Arbitrary Code Injection via the `template` function, particularly when the `variable` option is taken from `_.templateSettings` as it is not sanitized.\r\n\r\n## PoC\r\n```\r\nconst _ = require('underscore');\r\n_.templateSettings.variable = \"a = this.process.mainModule.require('child_process').execSync('touch HELLO')\";\r\nconst t = _.template(\"\")();\r\n```\n# Remediation\nUpgrade `underscore` to version 1.13.0-2, 1.12.1 or higher.\n# References\n- [GitHub Additional Information](https://github.com/jashkenas/underscore/blob/cb5f6fc6c2400649d942f1e36f9e5191fb7a1bf1/modules/template.js#L71)\n- [GitHub Commit](https://github.com/jashkenas/underscore/commit/4c73526d43838ad6ab43a6134728776632adeb66)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-94",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-Y18N-1021887",
              "shortDescription": {
                "text": "High severity - Prototype Pollution vulnerability in y18n"
              },
              "fullDescription": {
                "text": "(CVE-2020-7774) y18n@3.2.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: y18n\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › yargs@11.1.0 › y18n@3.2.1\n* _Introduced through_: goof@1.0.1 › typeorm@0.2.24 › yargs@13.3.2 › y18n@4.0.0\n* _Introduced through_: goof@1.0.1 › typeorm@0.2.24 › cli-highlight@2.1.4 › yargs@15.4.1 › y18n@4.0.0\n# Overview\n[y18n](https://www.npmjs.com/package/y18n) is a the bare-bones internationalization library used by yargs\n\nAffected versions of this package are vulnerable to Prototype Pollution. PoC by po6ix:\r\n```\r\nconst y18n = require('y18n')();\r\n \r\ny18n.setLocale('__proto__');\r\ny18n.updateLocale({polluted: true});\r\n\r\nconsole.log(polluted); // true\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `y18n` to version 3.2.2, 4.0.1, 5.0.5 or higher.\n# References\n- [GitHub Issue](https://github.com/yargs/y18n/issues/96)\n- [GitHub PR](https://github.com/yargs/y18n/pull/108)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "SNYK-JS-YARGSPARSER-560381",
              "shortDescription": {
                "text": "Medium severity - Prototype Pollution vulnerability in yargs-parser"
              },
              "fullDescription": {
                "text": "(CVE-2020-7608) yargs-parser@9.0.2"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: yargs-parser\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › yargs@11.1.0 › yargs-parser@9.0.2\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › yargs-parser@8.1.0\n# Overview\n[yargs-parser](https://www.npmjs.com/package/yargs-parser) is a mighty option parser used by yargs.\n\nAffected versions of this package are vulnerable to Prototype Pollution. The library could be tricked into adding or modifying properties of `Object.prototype` using a `__proto__` payload.\r\n\r\nOur research team checked several attack vectors to verify this vulnerability:\r\n\r\n1. It could be used for [privilege escalation](https://gist.github.com/Kirill89/dcd8100d010896157a36624119439832).\r\n2. The library could be used to parse user input received from different sources:\r\n    - terminal emulators\r\n    - system calls from other code bases\r\n    - CLI RPC servers\r\n\r\n# PoC by Snyk\r\n```\r\nconst parser = require(\"yargs-parser\");\r\nconsole.log(parser('--foo.__proto__.bar baz'));\r\nconsole.log(({}).bar);\r\n```\n\n# Details\n\nPrototype Pollution is a vulnerability affecting JavaScript. Prototype Pollution refers to the ability to inject properties into existing JavaScript language construct prototypes, such as objects. JavaScript allows all Object attributes to be altered, including their magical attributes such as `_proto_`, `constructor` and `prototype`. An attacker manipulates these attributes to overwrite, or pollute, a JavaScript application object prototype of the base object by injecting other values.  Properties on the `Object.prototype` are then inherited by all the JavaScript objects through the prototype chain. When that happens, this leads to either denial of service by triggering JavaScript exceptions, or it tampers with the application source code to force the code path that the attacker injects, thereby leading to remote code execution.\n\nThere are two main ways in which the pollution of prototypes occurs:\n\n-   Unsafe `Object` recursive merge\n    \n-   Property definition by path\n    \n\n## Unsafe Object recursive merge\n\nThe logic of a vulnerable recursive merge function follows the following high-level model:\n```\nmerge (target, source)\n\n  foreach property of source\n\n    if property exists and is an object on both the target and the source\n\n      merge(target[property], source[property])\n\n    else\n\n      target[property] = source[property]\n```\n<br>  \n\nWhen the source object contains a property named `_proto_` defined with `Object.defineProperty()` , the condition that checks if the property exists and is an object on both the target and the source passes and the merge recurses with the target, being the prototype of `Object` and the source of `Object` as defined by the attacker. Properties are then copied on the `Object` prototype.\n\nClone operations are a special sub-class of unsafe recursive merges, which occur when a recursive merge is conducted on an empty object: `merge({},source)`.\n\n`lodash` and `Hoek` are examples of libraries susceptible to recursive merge attacks.\n\n## Property definition by path\n\nThere are a few JavaScript libraries that use an API to define property values on an object based on a given path. The function that is generally affected contains this signature: `theFunction(object, path, value)`\n\nIf the attacker can control the value of “path”, they can set this value to `_proto_.myValue`. `myValue` is then assigned to the prototype of the class of the object.\n\n# Types of attacks\n\nThere are a few methods by which Prototype Pollution can be manipulated:\n\n| Type |Origin  |Short description |\n|--|--|--|\n| **Denial of service (DoS)**|Client  |This is the most likely attack. <br>DoS occurs when `Object` holds generic functions that are implicitly called for various operations (for example, `toString` and `valueOf`). <br> The attacker pollutes `Object.prototype.someattr` and alters its state to an unexpected value such as `Int` or `Object`. In this case, the code fails and is likely to cause a denial of service.  <br>**For example:** if an attacker pollutes `Object.prototype.toString` by defining it as an integer, if the codebase at any point was reliant on `someobject.toString()` it would fail. |\n |**Remote Code Execution**|Client|Remote code execution is generally only possible in cases where the codebase evaluates a specific attribute of an object, and then executes that evaluation.<br>**For example:** `eval(someobject.someattr)`. In this case, if the attacker pollutes `Object.prototype.someattr` they are likely to be able to leverage this in order to execute code.|\n|**Property Injection**|Client|The attacker pollutes properties that the codebase relies on for their informative value, including security properties such as cookies or tokens.<br>  **For example:** if a codebase checks privileges for `someuser.isAdmin`, then when the attacker pollutes `Object.prototype.isAdmin` and sets it to equal `true`, they can then achieve admin privileges.|\n\n# Affected environments\n\nThe following environments are susceptible to a Prototype Pollution attack:\n\n-   Application server\n    \n-   Web server\n    \n\n# How to prevent\n\n1.  Freeze the prototype— use `Object.freeze (Object.prototype)`.\n    \n2.  Require schema validation of JSON input.\n    \n3.  Avoid using unsafe recursive merge functions.\n    \n4.  Consider using objects without prototypes (for example, `Object.create(null)`), breaking the prototype chain and preventing pollution.\n    \n5.  As a best practice use `Map` instead of `Object`.\n\n## For more information on this vulnerability type:\n\n[Arteau, Oliver. “JavaScript prototype pollution attack in NodeJS application.” GitHub, 26 May 2018](https://github.com/HoLyVieR/prototype-pollution-nsec18/blob/master/paper/JavaScript_prototype_pollution_attack_in_NodeJS.pdf)\n\n# Remediation\nUpgrade `yargs-parser` to version 5.0.1, 13.1.2, 15.0.1, 18.1.1 or higher.\n# References\n- [Command Injection PoC](https://gist.github.com/Kirill89/dcd8100d010896157a36624119439832)\n- [GitHub Fix Commit](https://github.com/yargs/yargs-parser/commit/63810ca1ae1a24b08293a4d971e70e058c7a41e2)\n- [Snyk Research Blog](https://snyk.io/blog/prototype-pollution-minimist/)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:debug:20170905",
              "shortDescription": {
                "text": "Low severity - Regular Expression Denial of Service (ReDoS) vulnerability in debug"
              },
              "fullDescription": {
                "text": "(CVE-2017-16137) debug@2.2.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: debug\n* Introduced through: goof@1.0.1, express@4.12.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › debug@2.2.0\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › finalhandler@0.3.6 › debug@2.2.0\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › send@0.12.3 › debug@2.2.0\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mquery@1.6.3 › debug@2.2.0\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › serve-static@1.9.3 › send@0.12.3 › debug@2.2.0\n# Overview\r\n[`debug`](https://www.npmjs.com/package/debug) is a JavaScript debugging utility modelled after Node.js core's debugging technique..\r\n\r\n`debug` uses [printf-style](https://wikipedia.org/wiki/Printf_format_string) formatting. Affected versions of this package are vulnerable to Regular expression Denial of Service (ReDoS) attacks via the the `%o` formatter (Pretty-print an Object all on a single line). It used a regular expression (`/\\s*\\n\\s*/g`) in order to strip whitespaces and replace newlines with spaces, in order to join the data into a single line. This can cause a very low impact of about 2 seconds matching time for data 50k characters long.\r\n\r\n# Details\r\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\r\n\r\n\r\n# Remediation\r\nUpgrade `debug` to version 2.6.9, 3.1.0 or higher.\r\n\r\n# References\r\n- [GitHub Issue](https://github.com/visionmedia/debug/issues/501)\r\n- [GitHub PR](https://github.com/visionmedia/debug/pull/504)"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:fresh:20170908",
              "shortDescription": {
                "text": "High severity - Regular Expression Denial of Service (ReDoS) vulnerability in fresh"
              },
              "fullDescription": {
                "text": "(CVE-2017-16119) fresh@0.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: fresh\n* Introduced through: goof@1.0.1, express@4.12.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › fresh@0.2.4\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › send@0.12.3 › fresh@0.2.4\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › serve-static@1.9.3 › send@0.12.3 › fresh@0.2.4\n# Overview\r\n[`fresh`](https://www.npmjs.com/package/fresh) is HTTP response freshness testing.\r\n\r\nAffected versions of this package are vulnerable to Regular expression Denial of Service (ReDoS) attacks. A Regular Expression (`/ *, */`) was used for parsing HTTP headers and take about 2 seconds matching time for 50k characters.\r\n\r\n# Details\r\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\r\n\r\n\r\n# Remediation\r\nUpgrade `fresh` to version 0.5.2 or higher.\n\n# References\n- [GitHub Commit](https://github.com/jshttp/fresh/commit/21a0f0c2a5f447e0d40bc16be0c23fa98a7b46ec)\n- [GitHub Issue](https://github.com/jshttp/fresh/issues/24)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:mem:20180117",
              "shortDescription": {
                "text": "Medium severity - Denial of Service (DoS) vulnerability in mem"
              },
              "fullDescription": {
                "text": "mem@1.1.0"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mem\n* Introduced through: goof@1.0.1, tap@11.1.5 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › tap@11.1.5 › nyc@11.9.0 › yargs@11.1.0 › os-locale@2.1.0 › mem@1.1.0\n# Overview\n \n[mem](https://www.npmjs.com/package/mem) is an optimization used to speed up consecutive function calls by caching the result of calls with identical input.\n\n\nAffected versions of this package are vulnerable to Denial of Service (DoS).\nOld results were deleted from the cache and could cause a memory leak.\n\n# details\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its intended and legitimate users.\r\n\r\nUnlike other vulnerabilities, DoS attacks usually do not aim at breaching security. Rather, they are focused on making websites and services unavailable to genuine users resulting in downtime.\r\n\r\nOne popular Denial of Service vulnerability is DDoS (a Distributed Denial of Service), an attack that attempts to clog network pipes to the system by generating a large volume of traffic from many machines.\r\n\r\nWhen it comes to open source libraries, DoS vulnerabilities allow attackers to trigger such a crash or crippling of the service by using a flaw either in the application code or from the use of open source libraries.\r\n\r\nTwo common types of DoS vulnerabilities:\r\n\r\n* High CPU/Memory Consumption- An attacker sending crafted requests that could cause the system to take a disproportionate amount of time to process. For example, [commons-fileupload:commons-fileupload](SNYK-JAVA-COMMONSFILEUPLOAD-30082).\r\n\r\n* Crash - An attacker sending crafted requests that could cause the system to crash. For Example,  [npm `ws` package](npm:ws:20171108)\n\n# Remediation\n\nUpgrade mem to version 4.0.0 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/sindresorhus/mem/commit/da4e4398cb27b602de3bd55f746efa9b4a31702b)\n\n- [GitHub Issue](https://github.com/sindresorhus/mem/issues/14)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:mime:20170907",
              "shortDescription": {
                "text": "Low severity - Regular Expression Denial of Service (ReDoS) vulnerability in mime"
              },
              "fullDescription": {
                "text": "(CVE-2017-16138) mime@1.3.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: mime\n* Introduced through: goof@1.0.1, express@4.12.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › send@0.12.3 › mime@1.3.4\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › serve-static@1.9.3 › send@0.12.3 › mime@1.3.4\n* _Introduced through_: goof@1.0.1 › st@0.2.4 › mime@1.2.11\n# Overview\n[mime](https://www.npmjs.com/package/mime) is a comprehensive, compact MIME type module.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). It uses regex the following regex `/.*[\\.\\/\\\\]/` in its lookup, which can cause a slowdown of 2 seconds for 50k characters.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `mime` to version 1.4.1, 2.0.3 or higher.\n# References\n- [GitHub Commit](https://github.com/broofa/node-mime/commit/1df903fdeb9ae7eaa048795b8d580ce2c98f40b0)\n- [GitHub Commit](https://github.com/broofa/node-mime/commit/855d0c4b8b22e4a80b9401a81f2872058eae274d)\n- [GitHub Issue](https://github.com/broofa/node-mime/issues/167)\n- [NPM Security Advisory](https://www.npmjs.com/advisories/535)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:moment:20161019",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in moment"
              },
              "fullDescription": {
                "text": "moment@2.15.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: moment\n* Introduced through: goof@1.0.1 and moment@2.15.1\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › moment@2.15.1\n# Overview\r\n[`moment`](https://www.npmjs.com/package/moment) is a lightweight JavaScript date library for parsing, validating, manipulating, and formatting dates.\r\n\r\nAffected versions of the package are vulnerable to Regular Expression Denial of Service (ReDoS) attacks for any locale that has separate format and standalone options and `format` input can be controlled by the user.\r\n\r\nAn attacker can provide a specially crafted input to the `format` function, which nearly matches the pattern being matched. This will cause the regular expression matching to take a long time, all the while occupying the event loop and preventing it from processing other requests and making the server unavailable (a Denial of Service attack).\r\n\r\n# Disclosure Timeline\r\n- October 19th, 2016 - Reported the issue to package owner.\r\n- October 19th, 2016 - Issue acknowledged by package owner.\r\n- October 24th, 2016 - Issue fixed and version `2.15.2` released.\r\n\r\n# Details\r\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\r\n\r\n\r\n# References\r\n- [Proof of concept](https://gist.github.com/grnd/50192ce22681848a7de812d95241b7fc)\r\n- [Fix commit](https://github.com/moment/moment/commit/663f33e333212b3800b63592cd8e237ac8fabdb9)"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:moment:20170905",
              "shortDescription": {
                "text": "Low severity - Regular Expression Denial of Service (ReDoS) vulnerability in moment"
              },
              "fullDescription": {
                "text": "(CVE-2017-18214) moment@2.15.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: moment\n* Introduced through: goof@1.0.1 and moment@2.15.1\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › moment@2.15.1\n# Overview\n[moment](https://www.npmjs.com/package/moment) is a lightweight JavaScript date library for parsing, validating, manipulating, and formatting dates.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS). It used a regular expression (`/[0-9]*['a-z\\u00A0-\\u05FF\\u0700-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]+|[\\u0600-\\u06FF\\/]+(\\s*?[\\u0600-\\u06FF]+){1,2}/i`) in order to parse dates specified as strings. This can cause a very low impact of about 2 seconds matching time for data 50k characters long.\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `moment` to version 2.19.3 or higher.\n# References\n- [GitHub Issue](https://github.com/moment/moment/issues/4163)\n- [GitHub PR](https://github.com/moment/moment/pull/4326)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:ms:20151024",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in ms"
              },
              "fullDescription": {
                "text": "(CVE-2015-8315) ms@0.6.2"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ms\n* Introduced through: goof@1.0.1, humanize-ms@1.0.1 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › humanize-ms@1.0.1 › ms@0.6.2\n# Overview\n\n[ms](https://www.npmjs.com/package/ms) is a tiny milisecond conversion utility.\n\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS)\nattack when converting a time period string (i.e. `\"2 days\"`, `\"1h\"`) into a milliseconds integer. A malicious user could pass extremely long strings to `ms()`, causing the server to take a long time to process, subsequently blocking the event loop for that extended period.\n\n# Details\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\n\nUpgrade `ms` to version 0.7.1 or higher.\n\n\n# References\n\n- [OSS security Advisory](https://www.openwall.com/lists/oss-security/2016/04/20/11)\n\n- [OWASP - ReDoS](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)\n\n- [Security Focus](https://www.securityfocus.com/bid/96389)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:ms:20170412",
              "shortDescription": {
                "text": "Low severity - Regular Expression Denial of Service (ReDoS) vulnerability in ms"
              },
              "fullDescription": {
                "text": "ms@0.7.1"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: ms\n* Introduced through: goof@1.0.1, mongoose@4.2.4 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › debug@2.2.0 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › send@0.12.3 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › finalhandler@0.3.6 › debug@2.2.0 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › send@0.12.3 › debug@2.2.0 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › mongoose@4.2.4 › mquery@1.6.3 › debug@2.2.0 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › serve-static@1.9.3 › send@0.12.3 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › serve-static@1.9.3 › send@0.12.3 › debug@2.2.0 › ms@0.7.1\n* _Introduced through_: goof@1.0.1 › ms@0.7.3\n# Overview\r\n[`ms`](https://www.npmjs.com/package/ms) is a tiny millisecond conversion utility.\r\n\r\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS) due to an incomplete fix for previously reported vulnerability [npm:ms:20151024](https://snyk.io/vuln/npm:ms:20151024). The fix limited the length of accepted input string to 10,000 characters, and turned to be insufficient making it possible to block the event loop for 0.3 seconds (on a typical laptop) with a specially crafted string passed to `ms()` function.\r\n\r\n*Proof of concept*\r\n```js\r\nms = require('ms');\r\nms('1'.repeat(9998) + 'Q') // Takes about ~0.3s\r\n```\r\n\r\n**Note:** Snyk's patch for this vulnerability limits input length to 100 characters. This new limit was deemed to be a breaking change by the author.\r\nBased on user feedback, we believe the risk of breakage is _very_ low, while the value to your security is much greater, and therefore opted to still capture this change in a patch for earlier versions as well.  Whenever patching security issues, we always suggest to run tests on your code to validate that nothing has been broken.\r\n\r\nFor more information on `Regular Expression Denial of Service (ReDoS)` attacks, go to our [blog](https://snyk.io/blog/redos-and-catastrophic-backtracking/).\r\n\r\n# Disclosure Timeline\r\n- Feb 9th, 2017 - Reported the issue to package owner.\r\n- Feb 11th, 2017 - Issue acknowledged by package owner.\r\n- April 12th, 2017 - Fix PR opened by Snyk Security Team.\r\n- May 15th, 2017 - Vulnerability published.\r\n- May 16th, 2017 - Issue fixed and version `2.0.0` released.\r\n- May 21th, 2017 - Patches released for versions `>=0.7.1, <=1.0.0`.\r\n\r\n# Details\r\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\r\n\r\n\r\n# Remediation\r\nUpgrade `ms` to version 2.0.0 or higher.\r\n\r\n# References\r\n- [GitHub PR](https://github.com/zeit/ms/pull/89)\r\n- [GitHub Commit](https://github.com/zeit/ms/pull/89/commits/305f2ddcd4eff7cc7c518aca6bb2b2d2daad8fef)"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:negotiator:20160616",
              "shortDescription": {
                "text": "High severity - Regular Expression Denial of Service (ReDoS) vulnerability in negotiator"
              },
              "fullDescription": {
                "text": "(CVE-2016-10539) negotiator@0.4.9"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: negotiator\n* Introduced through: goof@1.0.1, errorhandler@1.2.0 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › errorhandler@1.2.0 › accepts@1.1.4 › negotiator@0.4.9\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › accepts@1.2.13 › negotiator@0.5.3\n* _Introduced through_: goof@1.0.1 › st@0.2.4 › negotiator@0.2.8\n# Overview\n\n[negotiator](https://npmjs.org/package/negotiator) is an HTTP content negotiator for Node.js.\n\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS)\nwhen parsing `Accept-Language` http header.\n\n# Details\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\r\n\r\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\r\n\r\nLet’s take the following regular expression as an example:\r\n```js\r\nregex = /A(B|C+)+D/\r\n```\r\n\r\nThis regular expression accomplishes the following:\r\n- `A` The string must start with the letter 'A'\r\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\r\n- `D` Finally, we ensure this section of the string ends with a 'D'\r\n\r\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\r\n\r\nIt most cases, it doesn't take very long for a regex engine to find a match:\r\n\r\n```bash\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\r\n0.04s user 0.01s system 95% cpu 0.052 total\r\n\r\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\r\n1.79s user 0.02s system 99% cpu 1.812 total\r\n```\r\n\r\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\r\n\r\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\r\n\r\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\r\n1. CCC\r\n2. CC+C\r\n3. C+CC\r\n4. C+C+C.\r\n\r\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\r\n\r\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\r\n\r\n| String | Number of C's | Number of steps |\r\n| -------|-------------:| -----:|\r\n| ACCCX | 3 | 38\r\n| ACCCCX | 4 | 71\r\n| ACCCCCX | 5 | 136\r\n| ACCCCCCCCCCCCCCX | 14 | 65,553\r\n\r\n\r\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\n\nUpgrade `negotiator` to version 0.6.1 or higher.\n\n\n# References\n\n- [GitHub Commit](https://github.com/jshttp/negotiator/commit/26a05ec15cf7d1fa56000d66ebe9c9a1a62cb75c)\n\n- [OSWAP Advisory](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:npmconf:20180512",
              "shortDescription": {
                "text": "High severity - Uninitialized Memory Exposure vulnerability in npmconf"
              },
              "fullDescription": {
                "text": "npmconf@0.0.24"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: npmconf\n* Introduced through: goof@1.0.1 and npmconf@0.0.24\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › npmconf@0.0.24\n# Overview\n\n[npmconf](https://www.npmjs.com/package/npmconf) is a package to reintegrate directly into npm.\n\n\nAffected versions of this package are vulnerable to Uninitialized Memory Exposure.\nIt allocates and writes to disk uninitialized memory content when a typed number is passed as input.\r\n\r\n**Note** `npmconf` is deprecated and should not be used.\r\n**Note** This is vulnerable only for Node <=4\n\n# Details\nThe Buffer class on Node.js is a mutable array of binary data, and can be initialized with a string, array or number.\r\n```js\r\nconst buf1 = new Buffer([1,2,3]);\r\n// creates a buffer containing [01, 02, 03]\r\nconst buf2 = new Buffer('test');\r\n// creates a buffer containing ASCII bytes [74, 65, 73, 74]\r\nconst buf3 = new Buffer(10);\r\n// creates a buffer of length 10\r\n```\r\n\r\nThe first two variants simply create a binary representation of the value it received. The last one, however, pre-allocates a buffer of the specified size, making it a useful buffer, especially when reading data from a stream.\r\nWhen using the number constructor of Buffer, it will allocate the memory, but will not fill it with zeros. Instead, the allocated buffer will hold whatever was in memory at the time. If the buffer is not `zeroed` by using `buf.fill(0)`, it may leak sensitive information like keys, source code, and system info.\n\n# Remediation\n\nUpgrade `npmconf` to version 2.1.3 or higher.\n\n\n# References\n\n- [HAckerOne Report](https://hackerone.com/reports/320269)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-201",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:qs:20170213",
              "shortDescription": {
                "text": "High severity - Prototype Override Protection Bypass vulnerability in qs"
              },
              "fullDescription": {
                "text": "(CVE-2017-1000048) qs@2.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: qs\n* Introduced through: goof@1.0.1, body-parser@1.9.0 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › body-parser@1.9.0 › qs@2.2.4\n* _Introduced through_: goof@1.0.1 › express@4.12.4 › qs@2.4.2\n# Overview\n[qs](https://www.npmjs.com/package/qs) is a querystring parser that supports nesting and arrays, with a depth limit.\n\nAffected versions of this package are vulnerable to Prototype Override Protection Bypass. By default `qs` protects against attacks that attempt to overwrite an object's existing prototype properties, such as `toString()`, `hasOwnProperty()`,etc.\r\n\r\nFrom [`qs` documentation](https://github.com/ljharb/qs):\r\n> By default parameters that would overwrite properties on the object prototype are ignored, if you wish to keep the data from those fields either use plainObjects as mentioned above, or set allowPrototypes to true which will allow user input to overwrite those properties. WARNING It is generally a bad idea to enable this option as it can cause problems when attempting to use the properties that have been overwritten. Always be careful with this option.\r\n\r\nOverwriting these properties can impact application logic, potentially allowing attackers to work around security controls, modify data, make the application unstable and more.\r\n\r\nIn versions of the package affected by this vulnerability, it is possible to circumvent this protection and overwrite prototype properties and functions by prefixing the name of the parameter with `[` or `]`. e.g. `qs.parse(\"]=toString\")` will return `{toString = true}`, as a result, calling `toString()` on the object will throw an exception.\r\n\r\n**Example:**\r\n```js\r\nqs.parse('toString=foo', { allowPrototypes: false })\r\n// {}\r\n\r\nqs.parse(\"]=toString\", { allowPrototypes: false })\r\n// {toString = true} <== prototype overwritten\r\n```\r\n\r\nFor more information, you can check out our [blog](https://snyk.io/blog/high-severity-vulnerability-qs/).\r\n\r\n# Disclosure Timeline\r\n- February 13th, 2017 - Reported the issue to package owner.\r\n- February 13th, 2017 - Issue acknowledged by package owner.\r\n- February 16th, 2017 - Partial fix released in versions `6.0.3`, `6.1.1`, `6.2.2`, `6.3.1`.\r\n- March 6th, 2017     - Final fix released in versions `6.4.0`,`6.3.2`, `6.2.3`, `6.1.2` and `6.0.4`\n# Remediation\nUpgrade `qs` to version 6.0.4, 6.1.2, 6.2.3, 6.3.2 or higher.\n# References\n- [GitHub Commit](https://github.com/ljharb/qs/commit/beade029171b8cef9cee0d03ebe577e2dd84976d)\n- [GitHub Issue](https://github.com/ljharb/qs/issues/200)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-20",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:semver:20150403",
              "shortDescription": {
                "text": "Medium severity - Regular Expression Denial of Service (ReDoS) vulnerability in semver"
              },
              "fullDescription": {
                "text": "(CVE-2015-8855) semver@1.1.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: semver\n* Introduced through: goof@1.0.1, npmconf@0.0.24 and others\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › npmconf@0.0.24 › semver@1.1.4\n# Overview\n[semver](https://github.com/npm/node-semver) is a semantic version parser used by npm.\n\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS).\n\n# Overview\r\n[npm](https://github.com/npm/npm) is a package manager for javascript.\r\n\r\nAffected versions of this package are vulnerable to Regular Expression Denial of Service (ReDoS).\r\nThe semver module uses regular expressions when parsing a version string. For a carefully crafted input, the time it takes to process these regular expressions is not linear to the length of the input. Since the semver module did not enforce a limit on the version string length, an attacker could provide a long string that would take up a large amount of resources, potentially taking a server down. This issue therefore enables a potential Denial of Service attack. This is a slightly differnt variant of a typical Regular Expression Denial of Service ([ReDoS](https://www.owasp.org/index.php/Regular_expression_Denial_of_Service_-_ReDoS)) vulnerability.\r\n\r\n# Details\r\n<<ReDoS>>\r\n\r\n\r\n# Remediation\r\nUpdate to a version 4.3.2 or greater. From the issue description [2]: \"Package version can no longer be more than 256 characters long. This prevents a situation in which parsing the version number can use exponentially more time and memory to parse, leading to a potential denial of service.\"\r\n\r\n# References\r\n- [GitHub Release](https://github.com/npm/npm/releases/tag/v2.7.5)\n\n# Details\n\nDenial of Service (DoS) describes a family of attacks, all aimed at making a system inaccessible to its original and legitimate users. There are many types of DoS attacks, ranging from trying to clog the network pipes to the system by generating a large volume of traffic from many machines (a Distributed Denial of Service - DDoS - attack) to sending crafted requests that cause a system to crash or take a disproportional amount of time to process.\n\nThe Regular expression Denial of Service (ReDoS) is a type of Denial of Service attack. Regular expressions are incredibly powerful, but they aren't very intuitive and can ultimately end up making it easy for attackers to take your site down.\n\nLet’s take the following regular expression as an example:\n```js\nregex = /A(B|C+)+D/\n```\n\nThis regular expression accomplishes the following:\n- `A` The string must start with the letter 'A'\n- `(B|C+)+` The string must then follow the letter A with either the letter 'B' or some number of occurrences of the letter 'C' (the `+` matches one or more times). The `+` at the end of this section states that we can look for one or more matches of this section.\n- `D` Finally, we ensure this section of the string ends with a 'D'\n\nThe expression would match inputs such as `ABBD`, `ABCCCCD`, `ABCBCCCD` and `ACCCCCD`\n\nIt most cases, it doesn't take very long for a regex engine to find a match:\n\n```bash\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCD\")'\n0.04s user 0.01s system 95% cpu 0.052 total\n\n$ time node -e '/A(B|C+)+D/.test(\"ACCCCCCCCCCCCCCCCCCCCCCCCCCCCX\")'\n1.79s user 0.02s system 99% cpu 1.812 total\n```\n\nThe entire process of testing it against a 30 characters long string takes around ~52ms. But when given an invalid string, it takes nearly two seconds to complete the test, over ten times as long as it took to test a valid string. The dramatic difference is due to the way regular expressions get evaluated.\n\nMost Regex engines will work very similarly (with minor differences). The engine will match the first possible way to accept the current character and proceed to the next one. If it then fails to match the next one, it will backtrack and see if there was another way to digest the previous character. If it goes too far down the rabbit hole only to find out the string doesn’t match in the end, and if many characters have multiple valid regex paths, the number of backtracking steps can become very large, resulting in what is known as _catastrophic backtracking_.\n\nLet's look at how our expression runs into this problem, using a shorter string: \"ACCCX\". While it seems fairly straightforward, there are still four different ways that the engine could match those three C's:\n1. CCC\n2. CC+C\n3. C+CC\n4. C+C+C.\n\nThe engine has to try each of those combinations to see if any of them potentially match against the expression. When you combine that with the other steps the engine must take, we can use [RegEx 101 debugger](https://regex101.com/debugger) to see the engine has to take a total of 38 steps before it can determine the string doesn't match.\n\nFrom there, the number of steps the engine must use to validate a string just continues to grow.\n\n| String | Number of C's | Number of steps |\n| -------|-------------:| -----:|\n| ACCCX | 3 | 38\n| ACCCCX | 4 | 71\n| ACCCCCX | 5 | 136\n| ACCCCCCCCCCCCCCX | 14 | 65,553\n\n\nBy the time the string includes 14 C's, the engine has to take over 65,000 steps just to see if the string is valid. These extreme situations can cause them to work very slowly (exponentially related to input size, as shown above), allowing an attacker to exploit this and can cause the service to excessively consume CPU, resulting in a Denial of Service.\n\n# Remediation\nUpgrade `semver` to version 4.3.2 or higher.\n# References\n- [GitHub Release](https://github.com/npm/npm/releases/tag/v2.7.5)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-400",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:st:20140206",
              "shortDescription": {
                "text": "Medium severity - Directory Traversal vulnerability in st"
              },
              "fullDescription": {
                "text": "(CVE-2014-3744) st@0.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: st\n* Introduced through: goof@1.0.1 and st@0.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › st@0.2.4\n# Overview\r\nVersions prior to 0.2.5 did not properly prevent path traversal. Literal dots in a path were resolved out, but url encoded dots were not. Thus, a request like ``` /%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/etc/passwd ``` would leak sensitive files and data from the server.\r\n\r\nAs of version 0.2.5, any ```'/../'``` in the request path, urlencoded or not, will be replaced with ```'/'```. If your application depends on url traversal, then you are encouraged to please refactor so that you do not depend on having ```..``` in url paths, as this tends to expose data that you may be surprised to be exposing.\r\n\r\n# Details\r\nA Directory Traversal attack (also known as path traversal) aims to access files and directories that are stored outside the intended folder. By manipulating files with \"dot-dot-slash (../)\" sequences and its variations, or by using absolute file paths, it may be possible to access arbitrary files and directories stored on file system, including application source code, configuration, and other critical system files.\r\n\r\nDirectory Traversal vulnerabilities can be generally divided into two types:\r\n\r\n- **Information Disclosure**: Allows the attacker to gain information about the folder structure or read the contents of sensitive files on the system.\r\n\r\n`st` is a module for serving static files on web pages, and contains a [vulnerability of this type](https://snyk.io/vuln/npm:st:20140206). In our example, we will serve files from the `public` route.\r\n\r\nIf an attacker requests the following URL from our server, it will in turn leak the sensitive private key of the root user.\r\n\r\n```\r\ncurl http://localhost:8080/public/%2e%2e/%2e%2e/%2e%2e/%2e%2e/%2e%2e/root/.ssh/id_rsa\r\n```\r\n**Note** `%2e` is the URL encoded version of `.` (dot).\r\n\r\n- **Writing arbitrary files**: Allows the attacker to create or replace existing files. This type of vulnerability is also known as `Zip-Slip`. \r\n\r\nOne way to achieve this is by using a malicious `zip` archive that holds path traversal filenames. When each filename in the zip archive gets concatenated to the target extraction folder, without validation, the final path ends up outside of the target folder. If an executable or a configuration file is overwritten with a file containing malicious code, the problem can turn into an arbitrary code execution issue quite easily.\r\n\r\nThe following is an example of a `zip` archive with one benign file and one malicious file. Extracting the malicious file will result in traversing out of the target folder, ending up in `/root/.ssh/` overwriting the `authorized_keys` file:\r\n\r\n```\r\n2018-04-15 22:04:29 .....           19           19  good.txt\r\n2018-04-15 22:04:42 .....           20           20  ../../../../../../root/.ssh/authorized_keys\r\n```\r\n\r\n\r\n# Remediation\r\nUpgrade to version 0.2.5 or greater.\r\n\r\n# References\r\n- https://github.com/isaacs/st#security-status\r\n- http://blog.npmjs.org/post/80277229932/newly-paranoid-maintainers"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-22",
                  "npm"
                ]
              }
            },
            {
              "id": "npm:st:20171013",
              "shortDescription": {
                "text": "Medium severity - Open Redirect vulnerability in st"
              },
              "fullDescription": {
                "text": "(CVE-2017-16224) st@0.2.4"
              },
              "help": {
                "text": "",
                "markdown": "* Package Manager: npm\n* Vulnerable module: st\n* Introduced through: goof@1.0.1 and st@0.2.4\n### Detailed paths\n* _Introduced through_: goof@1.0.1 › st@0.2.4\n# Overview\n[`st`](https://www.npmjs.com/package/st) is a module for serving static files.\n\nAffected versions of this package are vulnerable to Open Redirect. A malicious user could send a specially crafted request, which would automatically redirect the request to another domain, controlled by the attacker.\n\n**Note:**  `st` will only redirect if requests are served from the root(`/`) and not from a subdirectory\n\n# References\n- [GitHub Commit](https://github.com/isaacs/st/commit/579960c629f12a27428e2da84c54f517e37b0a16)\n"
              },
              "properties": {
                "tags": [
                  "security",
                  "CWE-601",
                  "npm"
                ]
              }
            }
          ]
        }
      },
      "results": [
        {
          "ruleId": "SNYK-JS-ADMZIP-1065796",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable adm-zip package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:adm-zip:20180415",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable adm-zip package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-AJV-584908",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable ajv package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-BL-608877",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable bl package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-DUSTJSLINKEDIN-1089257",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable dustjs-linkedin package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:dustjs-linkedin:20160819",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable dustjs-linkedin package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-EJS-1049328",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable ejs package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:ejs:20161128",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable ejs package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:ejs:20161130",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable ejs package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:ejs:20161130-1",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable ejs package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-EXPRESSFILEUPLOAD-473997",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable express-fileupload package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-EXPRESSFILEUPLOAD-595969",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable express-fileupload package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-1056767",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-1279029",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-173692",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-174183",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-469063",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-480388",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-534478",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-534988",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HANDLEBARS-567742",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable handlebars package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HIGHLIGHTJS-1045326",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable highlight.js package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HIGHLIGHTJS-1048676",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable highlight.js package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-HOSTEDGITINFO-1088355",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable hosted-git-info package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-INI-1048974",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable ini package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-JQUERY-174006",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable jquery package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-JQUERY-565129",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable jquery package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-JQUERY-567880",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable jquery package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:jquery:20150627",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable jquery package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-KERBEROS-568900",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable kerberos package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-KINDOF-537849",
          "level": "note",
          "message": {
            "text": "This file introduces a vulnerable kind-of package with a low severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-1018905",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-1040724",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-450202",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-567746",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-590103",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-608086",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-73638",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-LODASH-73639",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:lodash:20180130",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable lodash package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MARKED-174116",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable marked package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MARKED-451540",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable marked package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MARKED-584281",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable marked package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20150520",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable marked package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20170112",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable marked package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20170815",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable marked package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20170815-1",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable marked package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20170907",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable marked package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:marked:20180225",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable marked package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MINIMIST-559764",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable minimist package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MIXINDEEP-450212",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable mixin-deep package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MONGODB-473855",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable mongodb package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MONGOOSE-1086688",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable mongoose package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MONGOOSE-472486",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable mongoose package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:mongoose:20160116",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable mongoose package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MQUERY-1050858",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable mquery package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-MQUERY-1089718",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable mquery package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-PATHPARSE-1077067",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable path-parse package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-SETVALUE-450213",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable set-value package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-TYPEORM-590152",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable typeorm package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-UNDERSCORE-1080984",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable underscore package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-Y18N-1021887",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable y18n package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "SNYK-JS-YARGSPARSER-560381",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable yargs-parser package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:debug:20170905",
          "level": "note",
          "message": {
            "text": "This file introduces a vulnerable debug package with a low severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:fresh:20170908",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable fresh package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:mem:20180117",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable mem package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:mime:20170907",
          "level": "note",
          "message": {
            "text": "This file introduces a vulnerable mime package with a low severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:moment:20161019",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable moment package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:moment:20170905",
          "level": "note",
          "message": {
            "text": "This file introduces a vulnerable moment package with a low severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:ms:20151024",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable ms package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:ms:20170412",
          "level": "note",
          "message": {
            "text": "This file introduces a vulnerable ms package with a low severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:negotiator:20160616",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable negotiator package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:npmconf:20180512",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable npmconf package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:qs:20170213",
          "level": "error",
          "message": {
            "text": "This file introduces a vulnerable qs package with a high severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:semver:20150403",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable semver package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:st:20140206",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable st package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        },
        {
          "ruleId": "npm:st:20171013",
          "level": "warning",
          "message": {
            "text": "This file introduces a vulnerable st package with a medium severity vulnerability."
          },
          "locations": [
            {
              "physicalLocation": {
                "artifactLocation": {
                  "uri": "package.json"
                },
                "region": {
                  "startLine": 1
                }
              }
            }
          ]
        }
      ]
    }
  ]
}
