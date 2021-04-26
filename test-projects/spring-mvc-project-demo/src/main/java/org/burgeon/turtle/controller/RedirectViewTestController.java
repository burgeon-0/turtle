package org.burgeon.turtle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Sam Lu
 * @createdOn 2021/4/7
 */
@RestController
public class RedirectViewTestController {

    @GetMapping("/redirect")
    public RedirectView redirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("https://www.baidu.com");
        return redirectView;
    }

}
