package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.ModifierModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试属性修饰符
 *
 * @author luxiaocong
 * @createdOn 2021/3/30
 */
@RestController
public class ModifierTestController {

    @GetMapping("/modifier")
    public ModifierModel modifier(@ModelAttribute ModifierModel modifierModel) {
        return modifierModel;
    }

}
