package org.burgeon.turtle.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * @author Sam Lu
 * @createdOn 2021/4/12
 */
@Data
public class FileParameterModel {

    private Long number;

    @NotNull
    private MultipartFile file;

}
