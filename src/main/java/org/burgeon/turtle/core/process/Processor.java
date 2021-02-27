package org.burgeon.turtle.core.process;

import lombok.Getter;
import lombok.Setter;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.ExportEventSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理器
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
public class Processor {

    @Setter
    @Getter
    private Analyser analyser;

    @Getter
    private List<Collector> collectors = new ArrayList<>();

    @Setter
    @Getter
    private Notifier notifier;

    /**
     * 添加收集器
     *
     * @param collector
     */
    public void addCollector(Collector collector) {
        collectors.add(collector);
    }

    /**
     * 移除收集器
     *
     * @param collector
     */
    public void removeCollector(Collector collector) {
        collectors.remove(collector);
    }

    /**
     * 处理
     */
    public void process() {
        SourceProject sourceProject = analyser.analyse();
        ApiProject apiProject = new ApiProject();
        for (Collector collector : collectors) {
            collector.collect(apiProject, sourceProject);
        }
        ExportEvent exportEvent = notifier.notice(apiProject);
        ExportEventSupport.fireExportEvent(exportEvent);
    }

}
