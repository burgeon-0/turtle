package org.bg181.turtle.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Sam Lu
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
