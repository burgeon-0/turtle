package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java注释
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaComment {

    private String text;

    private JavaCommentTag[] tags;

}
