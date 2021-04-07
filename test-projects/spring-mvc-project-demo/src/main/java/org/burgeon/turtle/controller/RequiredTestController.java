package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.RequiredModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author luxiaocong
 * @createdOn 2021/4/2
 */
@RestController
public class RequiredTestController {

    @GetMapping("/required")
    public RequiredModel required(@Valid @ModelAttribute RequiredModel requiredModel) {
        return requiredModel;
    }

    @GetMapping("/required/path_variable1/{str}")
    public String requiredPathVariable1(@PathVariable(required = false) String str) {
        return str;
    }

    @GetMapping("/required/path_variable2/{str}")
    public String requiredPathVariable2(@PathVariable String str) {
        return str;
    }

    @GetMapping("/required/request_param1")
    public String requiredRequestParam1(@RequestParam(required = false) String str) {
        return str;
    }

    @GetMapping("/required/request_param2")
    public String requiredRequestParam2(@RequestParam String str) {
        return str;
    }

    @PostMapping("/required/request_body1")
    public RequiredModel requiredRequestBody1(@RequestBody(required = false) RequiredModel requiredModel) {
        return requiredModel;
    }

    @PostMapping("/required/request_body2")
    public RequiredModel requiredRequestBody2(@RequestBody RequiredModel requiredModel) {
        return requiredModel;
    }

}
