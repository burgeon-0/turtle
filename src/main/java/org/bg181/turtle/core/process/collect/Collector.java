package org.bg181.turtle.core.process.collect;

import org.bg181.turtle.core.model.api.ApiProject;
import org.bg181.turtle.core.model.source.SourceProject;

/**
 * 收集器
 *
 * @author Sam Lu
 * @createdOn 2021/2/27
 */
public interface Collector {

    /**
     * 收集器负责从源文件中收集API信息
     *
     * @param apiProject
     * @param sourceProject
     * @param context
     */
    void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context);

}
