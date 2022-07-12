package org.bg181.turtle.controller;

import org.bg181.turtle.model.ExclusionModel;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sam Lu
 * @createdOn 2021/4/2
 */
@RestController
public class ExclusionParameterTestController {

    @GetMapping("/exclusion")
    public ExclusionModel exclusion(@ModelAttribute ExclusionModel exclusionModel,
                                    HttpServletRequest request, HttpServletResponse response,
                                    BindingResult bindingResult,
                                    Model model) {
        return exclusionModel;
    }

}
