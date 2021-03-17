package org.burgeon.turtle.core.process;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 处理器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Slf4j
public class Processor {

    @Setter
    @Getter
    private Analyzer analyzer;

    private CollectorPipeline collectorPipeline = new CollectorPipeline();

    @Setter
    @Getter
    private Notifier notifier;

    @Setter
    @Getter
    private JsonConverter jsonConverter;

    /**
     * 处理
     */
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

    /**
     * 添加收集器
     *
     * @param collector
     */
    public void addCollector(Collector collector) {
        collectorPipeline.addCollector(collector);
    }

    /**
     * 移除收集器
     *
     * @param collector
     */
    public void removeCollector(Collector collector) {
        collectorPipeline.removeCollector(collector);
    }

}
