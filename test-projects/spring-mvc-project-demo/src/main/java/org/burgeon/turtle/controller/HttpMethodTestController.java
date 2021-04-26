package org.burgeon.turtle.controller;

import org.springframework.web.bind.annotation.*;

/**
 * @author Sam Lu
 * @createdOn 2021/3/31
 */
@RestController
@RequestMapping("/http_method")
public class HttpMethodTestController {

    @RequestMapping("/request_mapping/default")
    public String requestMappingDefault() {
        return "request mapping default";
    }

    @RequestMapping(value = "/request_mapping/get", method = RequestMethod.GET)
    public String requestMappingGet() {
        return "request mapping get";
    }

    /*@RequestMapping(value = "/request_mapping/head", method = RequestMethod.HEAD)
    public String requestMappingHead() {
        return "request mapping head";
    }*/

    @RequestMapping(value = "/request_mapping/post", method = RequestMethod.POST)
    public String requestMappingPost() {
        return "request mapping post";
    }

    @RequestMapping(value = "/request_mapping/put", method = RequestMethod.PUT)
    public String requestMappingPut() {
        return "request mapping put";
    }

    @RequestMapping(value = "/request_mapping/patch", method = RequestMethod.PATCH)
    public String requestMappingPatch() {
        return "request mapping patch";
    }

    @RequestMapping(value = "/request_mapping/delete", method = RequestMethod.DELETE)
    public String requestMappingDelete() {
        return "request mapping delete";
    }

    /*@RequestMapping(value = "/request_mapping/options", method = RequestMethod.OPTIONS)
    public String requestMappingOptions() {
        return "request mapping options";
    }*/

    /*@RequestMapping(value = "/request_mapping/trace", method = RequestMethod.TRACE)
    public String requestMappingTrace() {
        return "request mapping trace";
    }*/

    @DeleteMapping("/delete_mapping")
    public String deleteMapping() {
        return "delete mapping";
    }

    @GetMapping("/get_mapping")
    public String getMapping() {
        return "get mapping";
    }

    @PatchMapping("/patch_mapping")
    public String patchMapping() {
        return "patch mapping";
    }

    @PostMapping("/post_mapping")
    public String postMapping() {
        return "post mapping";
    }

    @PutMapping("/put_mapping")
    public String putMapping() {
        return "put mapping";
    }

}
