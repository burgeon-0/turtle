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
public class DefaultAnalyzer implements Analyzer {

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
    public SourceProject analyze() {
        log.debug("Start to analyze...");

        // 若指定了编译模式，则使用指定的编译模式对应的策略进行分析
        String mode = EnvUtils.getStringProperty(Constants.COMPILE_MODE);
        if (mode != null) {
            return analyzeByMode(mode);
        }

        // 没有指定编译模式，尝试按顺序使用不同的策略进行分析
        String[] order = EnvUtils.getStringArrayProperty(Constants.COMPILE_ORDER, Constants.SEPARATOR_COMMA);
        if (order == null) {
            order = Constants.DEFALUT_COMPILE_ORDER;
        }
        return analyzeByOrder(order);
    }

    /**
     * 按指定编译模式对应的策略进行分析
     *
     * @param mode
     * @return
     */
    private SourceProject analyzeByMode(String mode) {
        AnalysisStrategy analysisStrategy = analysisStrategyMap.get(mode);
        if (analysisStrategy == null) {
            log.error("Unknown compile mode: {}.", mode);
            return null;
        }
        try {
            return analysisStrategy.analyze();
        } catch (AnalysisException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 按顺序使用不同的策略进行分析，直到其中一个成功或全部失败为止
     *
     * @param order
     * @return
     */
    private SourceProject analyzeByOrder(String[] order) {
        for (String name : order) {
            AnalysisStrategy analysisStrategy = analysisStrategyMap.get(name);
            if (analysisStrategy == null) {
                log.error("Unknown compile name: {}.", name);
                continue;
            }
            try {
                return analysisStrategy.analyze();
            } catch (AnalysisException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

}
