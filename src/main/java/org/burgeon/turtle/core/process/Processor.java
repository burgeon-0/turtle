package org.burgeon.turtle.core.process;

import lombok.Getter;
import lombok.Setter;
import org.burgeon.turtle.core.data.source.Group;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.ExportEventSupport;
import org.burgeon.turtle.core.data.api.Application;

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
        Application application = new Application();
        Group group = analyser.analyse();
        for (Collector collector : collectors) {
            collector.collect(application, group);
        }
        ExportEvent exportEvent = notifier.notice(application);
        ExportEventSupport.fireExportEvent(exportEvent);
    }

}
