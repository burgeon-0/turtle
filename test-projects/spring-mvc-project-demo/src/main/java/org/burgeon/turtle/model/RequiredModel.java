package org.burgeon.turtle.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author luxiaocong
 * @createdOn 2021/4/2
 */
@Data
public class RequiredModel {

    private String notRequired;

    @NotNull
    private String notNull;

    @NotBlank
    private String notBlank;

    @NotEmpty
    private String notEmpty;

}
