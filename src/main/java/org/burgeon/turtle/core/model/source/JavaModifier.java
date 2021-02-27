package org.burgeon.turtle.core.model.source;

import lombok.Data;

/**
 * Java修饰符
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Data
public class JavaModifier {

    private boolean isPublic;

    private boolean isPrivate;

    private boolean isProtected;

    private boolean isStatic;

    private boolean isFinal;

    private boolean isSynchronized;

    private boolean isVolatile;

    private boolean isTransient;

    private boolean isNative;

    private boolean isAbstract;

    private boolean isStrict;

}
