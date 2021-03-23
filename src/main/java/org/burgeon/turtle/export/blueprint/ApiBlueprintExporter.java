package org.burgeon.turtle.export.blueprint;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.burgeon.turtle.core.common.Constants;
import org.burgeon.turtle.core.event.BaseExportListener;
import org.burgeon.turtle.core.event.EventTarget;
import org.burgeon.turtle.core.event.ExportEvent;
import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.core.utils.EnvUtils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
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
        } catch (FileNotFoundException e) {
            if (EnvUtils.getBooleanProperty(Constants.DEBUG, false)) {
                e.printStackTrace();
            }
            throw new RuntimeException("Export API Blueprint docs fail.");
        }
    }

    /**
     * 导出文档
     *
     * @param apiProject
     */
    private void export(ApiProject apiProject) throws FileNotFoundException {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class",
                ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        VelocityContext context = new VelocityContext();
        context.put("hashtag", "#");
        context.put("apiProject", apiProject);

        Template t = velocityEngine.getTemplate("template/api-blueprint.vm");
        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        String targetPath = EnvUtils.getStringProperty(Constants.TARGET_PATH);
        String targetFile = targetPath + Constants.SEPARATOR_FILE + "api-blueprint.apib";
        PrintWriter out = new PrintWriter(targetFile);
        out.println(writer.toString());
    }

}
