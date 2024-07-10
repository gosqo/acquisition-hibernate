package org.flyinheron.hibernate.global;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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
