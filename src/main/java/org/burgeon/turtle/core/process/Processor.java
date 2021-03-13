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

    /**
     * 处理
     */
    public void process() {
        SourceProject sourceProject = analyzer.analyze();
        if (sourceProject == null) {
            log.error("Analyze fail!");
        }
        ApiProject apiProject = new ApiProject();
        collectorPipeline.collect(apiProject, sourceProject);
        ExportEvent exportEvent = notifier.notice(apiProject);
        ExportEventSupport.fireExportEvent(exportEvent);
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
