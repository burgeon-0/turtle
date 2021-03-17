package org.burgeon.turtle.core.process;

/**
 * 默认处理器
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class DefaultProcessor extends Processor {

    public DefaultProcessor() {
        JsonConverter jsonConverter = new DefaultJsonConverter();
        super.setJsonConverter(jsonConverter);
        Analyzer analyzer = new DefaultAnalyzer();
        super.setAnalyzer(analyzer);
        Collector collector = new DefaultCollector();
        super.addCollector(collector);
        Collector commentCollector = new DefaultCommentCollector();
        super.addCollector(commentCollector);
        Collector parameterCollector = new DefaultParameterCollector(this);
        super.addCollector(parameterCollector);
    }

}
