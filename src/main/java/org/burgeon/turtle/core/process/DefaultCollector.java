package org.burgeon.turtle.core.process;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.source.SourceProject;

/**
 * 默认收集器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
@Slf4j
public class DefaultCollector implements Collector {

    @Override
    public void collect(ApiProject apiProject, SourceProject sourceProject, CollectorContext context) {
        log.info("Collect by default collector.");
        context.collectNext(apiProject, sourceProject);
    }

}
