package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.event.BaseExportListener;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.core.utils.EnvUtils;

import java.io.*;
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
        log.debug("Export {} group.", apiGroups.size());
        int apiSize = 0;
        for (ApiGroup apiGroup : apiGroups) {
            List<HttpApi> httpApis = apiGroup.getHttpApis();
            apiSize += httpApis.size();
        }
        log.debug("Export {} api.", apiSize);

        try {
            export(apiProject);
        } catch (Exception e) {
            if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
                e.printStackTrace();
            }
            throw new RuntimeException("Export API Blueprint docs fail.");
        }
    }

    /**
     * 导出API文档
     *
     * @param apiProject
     */
    private void export(ApiProject apiProject) throws IOException {
        DocsBuilder docsBuilder = new DocsBuilder();
        docsBuilder.appendHost(apiProject.getHost());
        docsBuilder.appendTitle(apiProject.getName());
        docsBuilder.appendDescription(apiProject.getDescription());
        for (ApiGroup apiGroup : apiProject.getGroups()) {
            docsBuilder.appendGroupTitle(apiGroup.getName(), apiGroup.getVersion());
            docsBuilder.appendGroupDescription(apiGroup.getDescription());
            for (HttpApi httpApi : apiGroup.getHttpApis()) {
                docsBuilder.appendApi(httpApi.getName(), httpApi.getVersion(),
                        httpApi.getHttpMethod(), httpApi.getPath(), httpApi.getUriParameters());
                docsBuilder.appendApiDescription(httpApi.getDescription());
                docsBuilder.appendPathParameters(httpApi.getPathParameters());
                docsBuilder.appendUriParameters(httpApi.getUriParameters());
                docsBuilder.appendHttpRequest(httpApi.getHttpRequest());
                docsBuilder.appendHttpResponse(httpApi.getHttpResponse());
                docsBuilder.appendErrorCodes(httpApi.getErrorCodes());
            }
        }

        writeToFile(docsBuilder.build());
    }

    /**
     * 将API内容写入到文件
     *
     * @param content
     * @throws IOException
     */
    private void writeToFile(String content) throws IOException {
        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
        String targetFile = targetPath + Constants.SEPARATOR_FILE + "api-blueprint.apib";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(targetFile));
            writer.write(content);
            System.out.println(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

}
