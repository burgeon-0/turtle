package org.burgeon.turtle.export.blueprint.model;

import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.ApiProject;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.core.model.api.Parameter;
import org.burgeon.turtle.export.blueprint.FilterHelper;

import java.util.List;
import java.util.Map;

/**
 * API项目【装饰器】
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class ApiProjectDecorator extends ApiProject {

    private ApiProject apiProject;
    private List<ApiGroup> apiGroups;

    private Map<String, ApiGroup> apiGroupMap;
    private Map<String, HttpApi> httpApiMap;
    private Map<String, Parameter> parameterMap;

    public ApiProjectDecorator(ApiProject apiProject, List<ApiGroup> apiGroups, Map<String, ApiGroup> apiGroupMap,
                               Map<String, HttpApi> httpApiMap, Map<String, Parameter> parameterMap) {
        this.apiProject = apiProject;
        this.apiGroups = apiGroups;
        this.apiGroupMap = apiGroupMap;
        this.httpApiMap = httpApiMap;
        this.parameterMap = parameterMap;
    }

    @Override
    public String getName() {
        return FilterHelper.filterApiProjectName(apiProject.getName());
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterApiProjectDescription(apiProject.getDescription());
    }

    @Override
    public String getHost() {
        return apiProject.getHost();
    }

    @Override
    public List<ApiGroup> getGroups() {
        return apiGroups;
    }

    @Override
    public ApiGroup getApiGroup(String key) {
        return apiGroupMap.get(key);
    }

    @Override
    public HttpApi getHttpApi(String key) {
        return httpApiMap.get(key);
    }

    @Override
    public Parameter getParameter(String key) {
        return parameterMap.get(key);
    }

}
