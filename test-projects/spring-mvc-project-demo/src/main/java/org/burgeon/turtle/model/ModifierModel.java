package org.burgeon.turtle.model;

import lombok.Data;

/**
 * @author Sam Lu
 * @createdOn 2021/3/30
 */
@Data
public class ModifierModel {

    String modifierModelField1;

    private String modifierModelField2;

    protected String modifierModelField3;

    public String modifierModelField4;

    // private abstract String modifierModelField5;

    private static String modifierModelField6;

    private final String modifierModelField7 = "1";

    private transient String modifierModelField8;

    private volatile String modifierModelField9;

    // private synchronized String modifierModelField10;

    // private native String modifierModelField11;

    // strictfp 精确浮点，可用于类、接口或方法
    // private strictfp float modifierModelField12;

    // private interface String modifierModelField13;

}
