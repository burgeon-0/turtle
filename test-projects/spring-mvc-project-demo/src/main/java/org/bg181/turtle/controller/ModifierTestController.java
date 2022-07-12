package org.bg181.turtle.controller;

import org.bg181.turtle.model.ModifierModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sam Lu
 * @createdOn 2021/3/30
 */
@RestController
public class ModifierTestController {

    @GetMapping("/modifier")
    public ModifierModel modifier(@ModelAttribute ModifierModel modifierModel) {
        return modifierModel;
    }

}
