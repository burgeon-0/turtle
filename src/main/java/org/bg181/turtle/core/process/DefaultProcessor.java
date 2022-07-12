package org.bg181.turtle.core.process;

import org.bg181.turtle.core.process.analysis.DefaultAnalyzer;
import org.bg181.turtle.core.process.collect.*;

/**
 * 默认处理器
 *
 * @author Sam Lu
 * @createdOn 2021/3/4
 */
public class DefaultProcessor extends AbstractProcessor {

    public DefaultProcessor() {
        super.setAnalyzer(new DefaultAnalyzer());
        super.addCollector(new DefaultCollector());
        DefaultParameterCollector defaultParameterCollector = new DefaultParameterCollector();
        defaultParameterCollector.addParameterTypeHandler(new PrimitiveParameterTypeHandler());
        defaultParameterCollector.addParameterTypeHandler(new DefaultParameterTypeHandler());
        super.addCollector(defaultParameterCollector);
        super.addCollector(new DefaultCommentCollector());
    }

}
