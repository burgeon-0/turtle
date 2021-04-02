package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.HttpParameterTypeModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author luxiaocong
 * @createdOn 2021/3/31
 */
@RestController
@RequestMapping("/http_parameter_type")
public class HttpParameterTypeTestController {

    @GetMapping("/get_default")
    public String getDefault(Integer num, String str, Boolean bool, HttpParameterTypeModel model) {
        return String.format("get default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    @DeleteMapping("/delete_default")
    public String deleteDefault(Integer num, String str, Boolean bool, HttpParameterTypeModel model) {
        return String.format("delete default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    @PostMapping("/post_default")
    public String postDefault(Integer num, String str, Boolean bool, HttpParameterTypeModel model) {
        return String.format("post default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    @PutMapping("/put_default")
    public String putDefault(Integer num, String str, Boolean bool, HttpParameterTypeModel model) {
        return String.format("put default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    @PatchMapping("/patch_default")
    public String patchDefault(Integer num, String str, Boolean bool, HttpParameterTypeModel model) {
        return String.format("patch default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    /**
     * 无PathVariable标识的参数为uri参数，此处的path参数无对应参数接收
     *
     * @param str
     * @return
     */
    @GetMapping("/path_variable1/{str}")
    public String pathVariable1(String str) {
        return String.format("path variable1, str: %s", str);
    }

    @GetMapping("/path_variable2/{str}")
    public String pathVariable2(@PathVariable String str) {
        return String.format("path variable2, str: %s", str);
    }

    /**
     * 存在PathVariable，但路径上无对应参数，调用接口时会抛出异常
     *
     * @param str
     * @return
     */
    /*@GetMapping("/path_variable3/null")
    public String pathVariable3(@PathVariable String str) {
        return String.format("get path default, str: %s", str);
    }*/

    @GetMapping("/path_variable4/{str}/{num}")
    public String pathVariable4(@PathVariable Integer num, @PathVariable String str) {
        return String.format("path variable4, str: %s, num: %d", str, num);
    }

    @GetMapping("/path_variable5/{str}/{num}")
    public String pathVariable5(@PathVariable("str") Integer num, @PathVariable("num") String str) {
        return String.format("path variable5, str: %s, num: %d", str, num);
    }

    @PostMapping("/request_param")
    public String requestParam(@RequestParam Integer num, @RequestParam String str, @RequestParam Boolean bool) {
        return String.format("request param, num: %d, str: %s, bool: %b", num, str, bool);
    }

    @PostMapping("/model_attribute")
    public String modelAttribute(@ModelAttribute String str, @ModelAttribute HttpParameterTypeModel model) {
        return String.format("model attribute, str: %s, model: %s", str, model);
    }

    @PostMapping("/request_body")
    public String requestBody(@RequestBody HttpParameterTypeModel model) {
        return String.format("request body, model: %s", model);
    }

    @GetMapping("/valid/get_default")
    public String validGetDefault(@Valid Integer num, @Valid String str, Boolean bool,
                                  @Valid HttpParameterTypeModel model) {
        return String.format("valid get default, num: %d, str: %s, bool: %b, model: %s", num, str, bool, model);
    }

    @GetMapping("/valid/path_variable/{str}")
    public String validPathVariable(@Valid @PathVariable("str") String str) {
        return String.format("valid path variable, str: %s", str);
    }

    @PostMapping("/valid/request_param")
    public String validRequestParam(@Valid @RequestParam Integer num, @Valid @RequestParam String str,
                                    @Valid @RequestParam Boolean bool) {
        return String.format("valid request param, num: %d, str: %s, bool: %b", num, str, bool);
    }

    @PostMapping("/valid/model_attribute")
    public String validModelAttribute(@Valid @ModelAttribute String str,
                                      @Valid @ModelAttribute HttpParameterTypeModel model) {
        return String.format("valid model attribute, str: %s, model: %s", str, model);
    }

    @PostMapping("/valid/request_body")
    public String validRequestBody(@Valid @RequestBody HttpParameterTypeModel model) {
        return String.format("valid request body, model: %s", model);
    }

}
