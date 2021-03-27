package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.utils.StringUtils;

/**
 * 过滤工具
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class FilterHelper {

    /**
     * 过滤名称
     * <ol>
     * <li>
     * API Blueprint的API名称不允许出现"()"
     * </li>
     * </ol>
     *
     * @param name
     * @return
     */
    public static String filterName(String name) {
        if (StringUtils.isBlank(name)) {
            return name;
        }
        name = StringUtils.strip(name, Constants.SEPARATOR_LINE_BREAK);
        name = name.replaceAll("\\(", " ");
        name = name.replaceAll("\\)", " ");
        return name;
    }

    /**
     * 过滤描述
     * <ol>
     * <li>
     * API Blueprint的描述中不能有"_"，如果存在"_"会抛出警告，因此，将"_"转换为"-"
     * </li>
     * </ol>
     *
     * @param description
     * @return
     */
    public static String filterDescription(String description) {
        if (StringUtils.isBlank(description)) {
            return description;
        }
        description = StringUtils.strip(description, Constants.SEPARATOR_LINE_BREAK);
        description = description.replaceAll("_", "-");
        return description;
    }

}
