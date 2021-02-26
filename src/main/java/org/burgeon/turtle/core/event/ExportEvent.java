package org.burgeon.turtle.core.event;

import lombok.Data;
import org.burgeon.turtle.core.model.Project;

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
    private Project project;

    /**
     * 是否包含目标编码
     *
     * @param targetCode
     * @return
     */
    public boolean containTargetCode(int targetCode) {
        for (int code : targetCodes) {
            if (code == targetCode) {
                return true;
            }
        }
        return false;
    }

}
