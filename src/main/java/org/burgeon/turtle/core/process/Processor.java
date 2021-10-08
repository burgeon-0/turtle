package org.burgeon.turtle.core.process;

import org.burgeon.turtle.core.process.analysis.Analyzer;
import org.burgeon.turtle.core.process.collect.Collector;

/**
 * 处理器
 *
 * @author Sam Lu
 * @createdOn 2021/3/18
 */
public interface Processor {

    /**
     * 执行处理
     */
    void process();

    /**
     * 设置分析器
     *
     * @param analyzer
     */
    void setAnalyzer(Analyzer analyzer);

    /**
     * 获取分析器
     *
     * @return
     */
    Analyzer getAnalyzer();

    /**
     * 添加收集器
     *
     * @param collector
     */
    void addCollector(Collector collector);

    /**
     * 移除收集器
     *
     * @param collector
     */
    void removeCollector(Collector collector);

    /**
     * 设置通知器
     *
     * @param notifier
     */
    void setNotifier(Notifier notifier);

    /**
     * 获取通知器
     *
     * @return
     */
    Notifier getNotifier();

}
