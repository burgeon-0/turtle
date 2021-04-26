package org.burgeon.turtle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sam Lu
 * @createdOn 2021/4/21
 */
@RestController
public class ExampleController {

    /**
     * @param str 111 URI参数描述
     */
    @GetMapping("/example")
    public String exampleApi(String str) {
        return str;
    }

    /**
     * @param str URI参数描述
     */
    @GetMapping("/example2")
    public String exampleApi2(String str) {
        return str;
    }

    /**
     * @return 111 Response参数描述
     */
    @GetMapping("/example3")
    public String exampleApi3(String str) {
        return str;
    }

    /**
     * @return Response参数描述
     */
    @GetMapping("/example4")
    public String exampleApi4(String str) {
        return str;
    }

}
