package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 分析策略
 *
 * @author Sam Lu
 * @createdOn 2021/3/6
 */
public interface AnalysisStrategy {

    /**
     * 策略名称
     *
     * @return
     */
    String name();

    /**
     * 经过分析，得到项目和源文件信息
     *
     * @return
     * @throws AnalysisException
     */
    SourceProject analyze() throws AnalysisException;

}
