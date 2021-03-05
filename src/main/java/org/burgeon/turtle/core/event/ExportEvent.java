package org.burgeon.turtle.core.event;

import lombok.Data;
import org.burgeon.turtle.core.model.api.ApiProject;

/**
 * 导出事件
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Data
public class ExportEvent {

    /**
     * 事件来源编码
     */
    private int sourceCode;

    /**
     * 事件目标编码，导出器（exporter）通过targetCode判断是否需要进行处理
     */
    private int[] targetCodes;

    /**
     * 导出项目
     */
    private ApiProject apiProject;

    /**
     * 是否包含事件目标编码
     *
     * @param targetCode
     * @return
     */
    public boolean containsTargetCode(int targetCode) {
        for (int code : targetCodes) {
            if (code == targetCode) {
                return true;
            }
        }
        return false;
    }

}
