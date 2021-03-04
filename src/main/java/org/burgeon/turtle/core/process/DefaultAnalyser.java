package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 默认分析器：先分析是什么类型的项目，再由具体的项目分析器进行分析
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
@Slf4j
public class DefaultAnalyser implements Analyser {

    private MavenProjectAnalyser mavenProjectAnalyser = new MavenProjectAnalyser();

    private GradleProjectAnalyser gradleProjectAnalyser = new GradleProjectAnalyser();

    private CustomProjectAnalyser customProjectAnalyser = new CustomProjectAnalyser();

    @Override
    public SourceProject analyse() {
        log.info("Start to analyse...");
        return null;
    }

}
