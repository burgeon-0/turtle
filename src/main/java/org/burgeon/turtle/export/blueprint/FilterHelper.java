package org.burgeon.turtle.export.blueprint;

import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.utils.StringUtils;

/**
 * 过滤工具
 * TODO 通过边界值测试，验证Filter是否将需要过滤的字符都过滤了
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class FilterHelper {

    /**
     * 过滤API项目名称
     *
     * @param name
     * @return
     */
    public static String filterApiProjectName(String name) {
        return name;
    }

    /**
     * 过滤API群组名称
     *
     * @param name
     * @return
     */
    public static String filterApiGroupName(String name) {
        return filterName(name);
    }

    /**
     * 过滤API名称
     *
     * @param name
     * @return
     */
    public static String filterHttpApiName(String name) {
        return filterName(name);
    }

    /**
     * 过滤HTTP Header名称
     *
     * @param name
     * @return
     */
    public static String filterHttpHeaderName(String name) {
        return name;
    }

    /**
     * 过滤HTTP参数名称
     *
     * @param name
     * @return
     */
    public static String filterParameterName(String name) {
        return name;
    }

    /**
     * 过滤API项目描述
     *
     * @param description
     * @return
     */
    public static String filterApiProjectDescription(String description) {
        return filterDescription(description);
    }

    /**
     * 过滤API群组描述
     *
     * @param description
     * @return
     */
    public static String filterApiGroupDescription(String description) {
        return filterDescription(description);
    }

    /**
     * 过滤API描述
     * <ol>
     * <li>
     * API Blueprint的API描述中不能有"/"，如果存在"/"会解析出错，因此，将"/"过滤掉
     * </li>
     * </ol>
     *
     * @param description
     * @return
     */
    public static String filterHttpApiDescription(String description) {
        if (StringUtils.isBlank(description)) {
            return description;
        }
        description = filterDescription(description);
        description = description.replaceAll("/", "");
        return description;
    }

    /**
     * 过滤HTTP Header描述
     *
     * @param description
     * @return
     */
    public static String filterHttpHeaderDescription(String description) {
        return filterDescription(description);
    }

    /**
     * 过滤HTTP参数描述
     * <ol>
     * <li>
     * API Blueprint的参数描述中不能有"_"，如果存在"_"会解析出错，因此，将"_"转换为"-"
     * </li>
     * </ol>
     *
     * @param description
     * @return
     */
    public static String filterParameterDescription(String description) {
        if (StringUtils.isBlank(description)) {
            return description;
        }
        description = filterDescription(description);
        description = description.replaceAll("_", "-");
        return description;
    }

    /**
     * 过滤名称
     * <ol>
     * <li>
     * API Blueprint的API名称和API群组名称不允许出现"()"
     * </li>
     * </ol>
     *
     * @param name
     * @return
     */
    private static String filterName(String name) {
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
     * <li>过滤描述中的换行符</li>
     * <li>"\t"转换成4个空格</li>
     * </ol>
     *
     * @param description
     * @return
     */
    private static String filterDescription(String description) {
        if (StringUtils.isBlank(description)) {
            return description;
        }
        description = StringUtils.strip(description, Constants.SEPARATOR_LINE_BREAK);
        description = description.replaceAll("\t", "    ");
        return description;
    }

}
