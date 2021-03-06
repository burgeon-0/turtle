package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.BaseExportListener;

/**
 * 导出API Blueprint文档
 *
 * @author luxiaocong
 * @createdOn 2021/2/26
 */
@Slf4j
public class ApiBlueprintExporter extends BaseExportListener {

    private static final ApiBlueprintExporter INSTANCE = new ApiBlueprintExporter();

    private ApiBlueprintExporter() {}

    public static ApiBlueprintExporter getInstance() {
        return INSTANCE;
    }

    @Override
    public int targetCode() {
        return EventTarget.BLUEPRINT.getCode();
    }

    @Override
    public void action(ExportEvent exportEvent) {
        log.info("Export API Blueprint docs.");
    }

}
