package org.burgeon.turtle.core.process;

/**
 * 默认处理器
 *
 * @author luxiaocong
 * @createdOn 2021/3/4
 */
public class DefaultProcessor extends Processor {

    public DefaultProcessor() {
        Analyser analyser = new DefaultAnalyser();
        super.setAnalyser(analyser);
        Collector collector = new DefaultCollector();
        super.addCollector(collector);
    }

}
