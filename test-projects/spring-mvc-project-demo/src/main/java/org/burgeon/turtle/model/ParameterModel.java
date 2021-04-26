package org.burgeon.turtle.model;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Sam Lu
 * @createdOn 2021/4/9
 */
@Data
public class ParameterModel {

    private byte pByte1;

    private Byte pByte2;

    private short pShort1;

    private Short pShort2;

    private int pInt1;

    private Integer pInt2;

    private long pLong1;

    private Long pLong2;

    private float pFloat1;

    private Float pFloat2;

    private double pDouble1;

    private Double pDouble2;

    private boolean pBoolean1;

    private Boolean pBoolean2;

    private char pChar1;

    private Character pChar2;

    private ModelAttributeModelSub obj;

    private ModelAttributeModelSub[] arr;

    private Integer[] arr2;

    private ParameterModel child;

    private ParameterModel[] children;

    private BigInteger bigInteger;

    private BigDecimal bigDecimal;

    private Date date;

    private UUID uuid;

    private List list0;

    private List<Object> list1;

    private Map map;

}
