package org.burgeon.turtle.core.process;

/**
 * 默认处理器
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class DefaultProcessor extends Processor {

    public DefaultProcessor() {
        ParameterTypeHandlerChain parameterTypeHandlerChain = new ParameterTypeHandlerChain();
        parameterTypeHandlerChain.addHandler(new PrimitiveParameterTypeHandler());
        parameterTypeHandlerChain.addHandler(new DefaultParameterTypeHandler());
        super.setParameterTypeHandlerChain(parameterTypeHandlerChain);

        Analyzer analyzer = new DefaultAnalyzer();
        super.setAnalyzer(analyzer);
        Collector collector = new DefaultCollector();
        super.addCollector(collector);
        Collector parameterCollector = new DefaultParameterCollector(this);
        super.addCollector(parameterCollector);
        Collector commentCollector = new DefaultCommentCollector();
        super.addCollector(commentCollector);
    }

}
