package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.common.Constants;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.utils.EnvUtils;

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
        log.debug("Start to analyse...");

        // 若指定了编译模式，则使用指定的策略进行分析
        String mode = EnvUtils.getStringProperty(Constants.COMPILE_MODE);
        if (mode != null) {
            AnalysisStrategy analysisStrategy = analysisStrategyMap.get(mode);
            if (analysisStrategy == null) {
                log.error("Unknown compile mode: {}.", mode);
                return null;
            }
            return analysisStrategy.analyse();
        }

        // 没有指定编译模式，或指定的策略分析失败，则编译顺序是必须指定的
        String[] order = EnvUtils.getStringArrayProperty(Constants.COMPILE_ORDER, Constants.SEPARATOR_COMMA);
        if (order == null) {
            log.error("Not found compile order.");
            return null;
        }

        return null;
    }

}
