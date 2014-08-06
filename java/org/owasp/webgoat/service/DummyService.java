/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.owasp.webgoat.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author rlawson
 */
@Controller
public class DummyService extends BaseService{

    @RequestMapping(value = "/first.mvc", produces = "application/json")
    public @ResponseBody
    List<String> firstNames() {
        List<String> test = new ArrayList<String>();
        test.add("one");
        test.add("two)");
        return test;
    }
}
