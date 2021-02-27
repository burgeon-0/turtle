package org.burgeon.turtle.collect.idea.config;

import com.intellij.openapi.application.ApplicationListener;
import org.jetbrains.annotations.NotNull;

/**
 * IDEA应用监听器
 *
 * @author luxiaocong
 * @createdOn 2021/2/27
 */
public class IdeaApplicationListener implements ApplicationListener {

    @Override
    public boolean canExitApplication() {
        return true;
    }

    @Override
    public void applicationExiting() {

    }

    @Override
    public void beforeWriteActionStart(@NotNull Object action) {

    }

    @Override
    public void writeActionStarted(@NotNull Object action) {

    }

    @Override
    public void writeActionFinished(@NotNull Object action) {

    }

    @Override
    public void afterWriteActionFinished(@NotNull Object action) {
        ExporterConfig exporterConfig = new ExporterConfig();
        exporterConfig.init();
    }

}
