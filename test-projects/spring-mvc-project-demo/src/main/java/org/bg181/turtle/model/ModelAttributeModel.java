package org.bg181.turtle.model;

import lombok.Data;

/**
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
@Data
public class ModelAttributeModel {

    private Integer mNum;

    private String mStr;

    private Boolean mBool;

    private ModelAttributeModelSub obj;

    private ModelAttributeModelSub[] arr;

    private String[] arr2;

    private ModelAttributeModel child;

    private ModelAttributeModel[] children;

}
