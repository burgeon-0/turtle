package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.RequiredModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author luxiaocong
 * @createdOn 2021/4/2
 */
@RestController
public class RequiredTestController {

    // TODO @RequestParam->required...

    @GetMapping("/required")
    public RequiredModel required(@Valid @ModelAttribute RequiredModel requiredModel) {
        return requiredModel;
    }

}
