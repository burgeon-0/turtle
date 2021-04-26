package org.burgeon.turtle.controller;

import org.burgeon.turtle.model.FileParameterModel;
import org.burgeon.turtle.model.FileParameterModel2;
import org.burgeon.turtle.model.FileParameterModel3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
@RestController
public class FileParameterTestController {

    @PostMapping("/upload")
    public String upload(@Valid FileParameterModel fileParameterModel, String str) {
        System.out.println("str: " + str);
        System.out.println("number: " + fileParameterModel.getNumber());
        System.out.println("file name: " + fileParameterModel.getFile().getName());
        System.out.println("original file name: " + fileParameterModel.getFile().getOriginalFilename());
        return "ok";
    }

    @PostMapping("/upload2")
    public String upload2(@Valid FileParameterModel2 fileParameterModel) {
        System.out.println("bool: " + fileParameterModel.getBool());
        System.out.println("number: " + fileParameterModel.getModel().getNumber());
        System.out.println("file name: " + fileParameterModel.getModel().getFile().getName());
        System.out.println("original file name: " + fileParameterModel.getModel().getFile().getOriginalFilename());
        return "ok";
    }

    @PostMapping("/upload3")
    public String upload3(@Valid FileParameterModel3 fileParameterModel) {
        System.out.println("arr0: " + fileParameterModel.getArr().get(0));
        System.out.println("arr1: " + fileParameterModel.getArr().get(1));
        System.out.println("bool0: " + fileParameterModel.getBool());
        System.out.println("bool1: " + fileParameterModel.getModel().getBool());
        System.out.println("str: " + fileParameterModel.getStr());
        System.out.println("number: " + fileParameterModel.getModel().getModel().getNumber());
        System.out.println("file name: " + fileParameterModel.getModel().getModel().getFile().getName());
        System.out.println("original file name: " + fileParameterModel.getModel().getModel().getFile().getOriginalFilename());
        return "ok";
    }

}
