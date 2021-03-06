package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 自定义项目分析策略
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class CustomProjectAnalysisStrategy implements AnalysisStrategy {

    @Override
    public String name() {
        return "custom";
    }

    @Override
    public SourceProject analyse() {
        return null;
    }

}
