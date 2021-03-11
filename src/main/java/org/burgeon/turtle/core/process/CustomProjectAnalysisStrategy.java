package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 自定义项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class CustomProjectAnalysisStrategy implements AnalysisStrategy {

    @Override
    public String name() {
        return "custom";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by custom strategy.");
        return null;
    }

}
