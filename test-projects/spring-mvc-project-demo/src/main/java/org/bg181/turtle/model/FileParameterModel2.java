package org.bg181.turtle.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Sam Lu
 * @createdOn 2021/4/12
 */
@Data
public class FileParameterModel2 {

    private Boolean bool;

    @NotNull
    private FileParameterModel model;

}
