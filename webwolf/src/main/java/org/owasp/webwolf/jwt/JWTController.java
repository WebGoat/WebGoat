package org.owasp.webwolf.jwt;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class JWTController {

    @GetMapping("/WebWolf/jwt")
    public ModelAndView jwt() {
        return new ModelAndView("jwt");
    }

    @PostMapping(value = "/WebWolf/jwt/decode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JWTToken decode(@RequestBody JWTToken token) {
        token.decode();
        return token;
    }

    @PostMapping(value = "/WebWolf/jwt/encode", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public JWTToken encode(@RequestBody JWTToken token) {
       token.encode();
       return token;
    }

}
