function submit_secret_credentials() {
    var xhttp = new XMLHttpRequest();
    xhttp['open']('POST', 'InsecureLogin/login', true);
	//sending the request is obfuscated, to descourage js reading
	var _0xb7f9=["\x43\x61\x70\x74\x61\x69\x6E\x4A\x61\x63\x6B","\x42\x6C\x61\x63\x6B\x50\x65\x61\x72\x6C","\x73\x74\x72\x69\x6E\x67\x69\x66\x79","\x73\x65\x6E\x64"];xhttp[_0xb7f9[3]](JSON[_0xb7f9[2]]({username:_0xb7f9[0],password:_0xb7f9[1]}))
}