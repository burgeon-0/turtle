package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * Maven项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class MavenProjectAnalysisStrategy implements AnalysisStrategy {

    @Override
    public String name() {
        return "maven";
    }

    @Override
    public SourceProject analyse() throws AnalysisException {
        return null;
    }

}
