package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.event.DefaultCodes;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.ExportListener;

/**
 * 导出API Blueprint文档
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Slf4j
public class ApiBlueprintExporter extends ExportListener {

    private static final ApiBlueprintExporter instance = new ApiBlueprintExporter();

    private ApiBlueprintExporter() {}

    public static ApiBlueprintExporter getInstance() {
        return instance;
    }

    @Override
    public void action(ExportEvent exportEvent) {
        log.debug("收到导出事件: {} to {}", exportEvent.getSourceCode(), exportEvent.getTargetCodes());
        if (exportEvent.containTargetCode(DefaultCodes.TARGET_CODE_BLUEPRINT)) {
            log.debug("导出API Blueprint文档");
        }
    }

}
