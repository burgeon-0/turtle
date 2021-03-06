package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.source.SourceProject;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认分析器：先分析是什么类型的项目，再使用具体的项目分析策略进行分析
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class DefaultAnalyser implements Analyser {

    private Map<String, AnalysisStrategy> analysisStrategyMap = new HashMap<>();
    private MavenProjectAnalysisStrategy mavenProjectAnalysisStrategy = new MavenProjectAnalysisStrategy();
    private GradleProjectAnalysisStrategy gradleProjectAnalysisStrategy = new GradleProjectAnalysisStrategy();
    private CustomProjectAnalysisStrategy customProjectAnalysisStrategy = new CustomProjectAnalysisStrategy();

    {
        analysisStrategyMap.put(mavenProjectAnalysisStrategy.name(), mavenProjectAnalysisStrategy);
        analysisStrategyMap.put(gradleProjectAnalysisStrategy.name(), gradleProjectAnalysisStrategy);
        analysisStrategyMap.put(customProjectAnalysisStrategy.name(), customProjectAnalysisStrategy);
    }

    @Override
    public SourceProject analyse() {
        log.info("Start to analyse...");
        return null;
    }

}
