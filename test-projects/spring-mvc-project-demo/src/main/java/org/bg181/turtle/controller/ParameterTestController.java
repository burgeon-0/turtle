package org.bg181.turtle.controller;

import org.bg181.turtle.model.ModelAttributeModel;
import org.bg181.turtle.model.ParameterModel;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
@RestController
public class ParameterTestController {

    @PostMapping(value = "/parameter/{path}")
    public ParameterModel parameter(@PathVariable String path,
                                    @RequestParam Integer rpi,
                                    @RequestParam String rps,
                                    @RequestParam Boolean rpb,
                                    @RequestParam String[] rpsArr,
                                    @ModelAttribute ModelAttributeModel modelAttributeModel,
                                    @RequestBody ParameterModel parameterModel) {
        System.out.println("===============================================================");
        System.out.println("path: " + path);
        System.out.println("requestParam num: " + rpi);
        System.out.println("requestParam str: " + rps);
        System.out.println("requestParam bool: " + rpb);
        System.out.println("requestParams[0]: " + (rpsArr == null ? null : rpsArr[0]));
        System.out.println("requestParams[1]: " + (rpsArr == null ? null : rpsArr[1]));
        System.out.println("modelAttributeModel.mNum: " + modelAttributeModel.getMNum());
        System.out.println("modelAttributeModel.mStr: " + modelAttributeModel.getMStr());
        System.out.println("modelAttributeModel.mBool: " + modelAttributeModel.getMBool());
        System.out.println("modelAttributeModel.arr2[0]: " + modelAttributeModel.getArr2()[0]);
        System.out.println("modelAttributeModel.arr2[1]: " + modelAttributeModel.getArr2()[1]);
        System.out.println("modelAttributeModel.obj.str: " + (modelAttributeModel.getObj() == null ? null : modelAttributeModel.getObj().getStr()));
        System.out.println("modelAttributeModel.obj.sub.str: " + (modelAttributeModel.getObj() == null ? null : modelAttributeModel.getObj().getSub().getStr()));
        // objectArray的length始终为0
        System.out.println("modelAttributeModel.arr: " + (modelAttributeModel.getArr() == null ? null : modelAttributeModel.getArr().length));
        System.out.println("modelAttributeModel.child.mNum: " + modelAttributeModel.getChild().getMNum());
        System.out.println("modelAttributeModel.child.mStr: " + modelAttributeModel.getChild().getMStr());
        System.out.println("modelAttributeModel.child.mBool: " + modelAttributeModel.getChild().getMBool());
        System.out.println("modelAttributeModel.child.obj.str: " + modelAttributeModel.getChild().getObj().getStr());
        return parameterModel;
    }

    @PostMapping(value = "/parameter2")
    public Object parameter2(@RequestBody Object object) {
        return object;
    }

    @PostMapping(value = "/parameter3")
    public List<ParameterModel> parameter3(@RequestBody List<ParameterModel> parameterModels) {
        return parameterModels;
    }

    @GetMapping(value = "/parameter/return1")
    public Integer return1() {
        return 255;
    }

    @GetMapping(value = "/parameter/return2")
    public String return2() {
        return "return2";
    }

    @GetMapping(value = "/parameter/return3")
    public Boolean return3() {
        return false;
    }

    @GetMapping(value = "/parameter/return4")
    public String[] return4() {
        return new String[1];
    }

    @GetMapping(value = "/parameter/return5")
    public List<ParameterModel> return5() {
        return Collections.EMPTY_LIST;
    }

    @GetMapping(value = "/parameter/return6")
    public Map<String, ParameterModel> return6() {
        return Collections.EMPTY_MAP;
    }

}
