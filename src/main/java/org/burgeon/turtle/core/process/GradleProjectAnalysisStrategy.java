package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * Gradle项目分析策略
 * TODO 待完善
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class GradleProjectAnalysisStrategy implements AnalysisStrategy {

    @Override
    public String name() {
        return "gradle";
    }

    @Override
    public SourceProject analyze() throws AnalysisException {
        log.debug("Analyze by gradle strategy.");

        throw new AnalysisException("Gradle is not supported yet.");
    }

}
