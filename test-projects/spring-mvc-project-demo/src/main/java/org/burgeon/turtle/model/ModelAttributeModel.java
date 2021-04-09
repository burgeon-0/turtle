package org.burgeon.turtle.model;

import lombok.Data;

/**
 * @author luxiaocong
 * @createdOn 2021/4/9
 */
@Data
public class ModelAttributeModel {

    private Integer mNum;

    private String mStr;

    private Boolean mBool;

    private ModelAttributeModelSub[] arr;

    private ModelAttributeModelSub obj;

}
