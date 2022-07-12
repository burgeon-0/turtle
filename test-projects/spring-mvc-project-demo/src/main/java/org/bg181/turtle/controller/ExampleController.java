package org.bg181.turtle.controller;

import org.bg181.turtle.model.Example;
import org.bg181.turtle.model.RestException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API群组名称
 * API群组描述
 *
 * @version 1
 * @order 1
 * @author Sam Lu
 * @createdOn 2021/4/21
 */
@RestController
public class ExampleController {

    /**
     * API名称
     * API描述
     *
     * @version 1
     * @order 1
     * @param str 111 URI参数描述
     * @return 222 Response参数描述
     */
    @GetMapping("/example")
    public String exampleApi(String str, @RequestBody Example example) {
        if (example.getParameter() == null) {
            throw new RestException(4001000, "The param `Parameter` must not be null!");
        }
        return str;
    }

    /**
     * @param str 111 URI参数描述
     */
    @GetMapping("/example1")
    public String exampleApi1(String str) {
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
