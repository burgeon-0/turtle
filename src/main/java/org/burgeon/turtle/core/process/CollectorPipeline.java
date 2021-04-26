package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 收集器管道
 *
 * @author Sam Lu
 * @createdOn 2021/3/6
 */
public class CollectorPipeline {

    private CollectorContext context = new CollectorContext();

    /**
     * 执行收集操作
     *
     * @param apiProject
     * @param sourceProject
     */
    public void collect(ApiProject apiProject, SourceProject sourceProject) {
        context.collectNext(apiProject, sourceProject);
    }

    /**
     * 添加收集器
     *
     * @param collector
     */
    public void addCollector(Collector collector) {
        context.addCollector(collector);
    }

    /**
     * 移除收集器
     *
     * @param collector
     */
    public void removeCollector(Collector collector) {
        context.removeCollector(collector);
    }

}
