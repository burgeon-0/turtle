package org.burgeon.turtle.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * @author Sam Lu
 * @createdOn 2021/4/2
 */
@Data
public class ValidatorModel {

    @Digits(integer = 1, fraction = 1)
    private String digits;

    @Email
    private String email;

    @Min(1)
    @Max(10)
    private Integer maxAndMin;

    @Pattern(regexp = "^debug: .*")
    private String pattern;

    @Size(min = 2, max = 8)
    private String size;

    @Length(min = 3, max = 7)
    private String length;

    @Range(min = 4, max = 6)
    private Integer range;

    @Range(min = 4, max = 6)
    private String sRange;

    @URL
    private String url;

}
