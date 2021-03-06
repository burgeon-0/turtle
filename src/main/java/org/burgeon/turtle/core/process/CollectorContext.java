package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;

import java.util.ArrayList;
import java.util.List;

/**
 * 收集器上下文
 *
 * @author luxiaocong
 * @createdOn 2021/3/6
 */
public class CollectorContext {

    private List<Collector> collectors = new ArrayList<>();
    private int index;

    /**
     * 执行下一个收集器的收集操作
     *
     * @param apiProject
     * @param sourceProject
     */
    public void collectNext(ApiProject apiProject, SourceProject sourceProject) {
        if (index < collectors.size()) {
            Collector collector = collectors.get(index++);
            collector.collect(apiProject, sourceProject, this);
        }
    }

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

}
