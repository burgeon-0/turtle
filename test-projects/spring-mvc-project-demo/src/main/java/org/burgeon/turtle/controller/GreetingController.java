package org.burgeon.turtle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luxiaocong
 * @createdOn 2021/3/30
 */
@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(required = false, defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

}
