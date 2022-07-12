package org.bg181.turtle.core.process.analysis;

import org.bg181.turtle.core.model.source.SourceProject;

/**
 * 分析器
 *
 * @author Sam Lu
 * @createdOn 2021/2/27
 */
public interface Analyzer {

    /**
     * 分析器经过分析，得到项目和源文件信息
     *
     * @return
     */
    SourceProject analyze();

}
