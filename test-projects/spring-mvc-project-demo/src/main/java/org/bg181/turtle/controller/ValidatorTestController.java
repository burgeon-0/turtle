package org.bg181.turtle.controller;

import org.bg181.turtle.model.ValidatorModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Sam Lu
 * @createdOn 2021/4/2
 */
@RestController
public class ValidatorTestController {

    @GetMapping("/validator")
    public ValidatorModel validator(@Valid @ModelAttribute ValidatorModel validatorModel) {
        return validatorModel;
    }

}
