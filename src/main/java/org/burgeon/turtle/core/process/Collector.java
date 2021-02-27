package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 收集器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public interface Collector {

    /**
     * 收集器负责从源文件中收集API信息
     *
     * @param apiProject
     * @param sourceProject
     */
    void collect(ApiProject apiProject, SourceProject sourceProject);

}
