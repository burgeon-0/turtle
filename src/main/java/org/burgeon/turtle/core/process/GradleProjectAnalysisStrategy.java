package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * Gradle项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class GradleProjectAnalysisStrategy implements AnalysisStrategy {

    @Override
    public String name() {
        return "gradle";
    }

    @Override
    public SourceProject analyse() {
        return null;
    }

}
