package org.burgeon.turtle.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author luxiaocong
 * @createdOn 2021/4/12
 */
@Data
public class FileParameterModel3 {

    private Boolean bool;

    private String str;

    private List<String> arr;

    @NotNull
    private FileParameterModel2 model;

}
