package org.flyinheron.hibernate;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class ViewController {
    @GetMapping("")
    public String welcome() {
        return "welcome";
    }

//    @GetMapping("error")
//    public String error() {
//        return "error";
//    }
}
