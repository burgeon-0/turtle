package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.event.BaseExportListener;
import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;

import java.util.List;

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

        ApiProject apiProject = exportEvent.getApiProject();
        List<ApiGroup> apiGroups = apiProject.getGroups();
        for (ApiGroup apiGroup : apiGroups) {
            System.out.println(apiGroup);
            System.out.println("=========================");
            List<HttpApi> httpApis = apiGroup.getHttpApis();
            for (HttpApi httpApi : httpApis) {
                System.out.println(httpApi);
            }
        }
    }

}
