package org.bg181.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.bg181.turtle.core.event.ExportEvent;
import org.bg181.turtle.core.event.ExportEventSupport;
import org.bg181.turtle.core.model.api.ApiProject;
import org.bg181.turtle.core.model.source.SourceProject;
import org.bg181.turtle.core.process.analysis.Analyzer;
import org.bg181.turtle.core.process.collect.Collector;
import org.bg181.turtle.core.process.collect.CollectorPipeline;

/**
 * 处理器抽象类
 *
 * @author Sam Lu
 * @createdOn 2021/2/26
 */
@Slf4j
public class AbstractProcessor implements Processor {

    private Analyzer analyzer;

    private CollectorPipeline collectorPipeline = new CollectorPipeline();

    private Notifier notifier;

    @Override
    public void process() {
        long time = System.currentTimeMillis();
        SourceProject sourceProject = analyzer.analyze();
        if (sourceProject == null) {
            log.error("Analyze fail!");
            return;
        }
        log.debug("Analyze cost: " + (System.currentTimeMillis() - time) + "ms.");

        time = System.currentTimeMillis();
        ApiProject apiProject = new ApiProject();
        collectorPipeline.collect(apiProject, sourceProject);
        log.debug("Collect cost: " + (System.currentTimeMillis() - time) + "ms.");

        time = System.currentTimeMillis();
        ExportEvent exportEvent = notifier.notice(apiProject);
        ExportEventSupport.fireExportEvent(exportEvent);
        log.debug("Export cost: " + (System.currentTimeMillis() - time) + "ms.");
    }

    @Override
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    @Override
    public Analyzer getAnalyzer() {
        return analyzer;
    }

    @Override
    public void addCollector(Collector collector) {
        collectorPipeline.addCollector(collector);
    }

    @Override
    public void removeCollector(Collector collector) {
        collectorPipeline.removeCollector(collector);
    }

    @Override
    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    @Override
    public Notifier getNotifier() {
        return notifier;
    }

}
