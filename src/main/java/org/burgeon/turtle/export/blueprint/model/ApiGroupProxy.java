package org.burgeon.turtle.export.blueprint.model;

import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.export.blueprint.FilterHelper;

import java.util.List;

/**
 * API群组【代理】
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class ApiGroupProxy extends ApiGroup {

    private ApiGroup apiGroup;
    private List<HttpApi> httpApis;

    public ApiGroupProxy(ApiGroup apiGroup, List<HttpApi> httpApis) {
        this.apiGroup = apiGroup;
        this.httpApis = httpApis;
    }

    @Override
    public String getId() {
        return apiGroup.getId();
    }

    @Override
    public String getName() {
        return FilterHelper.filterApiGroupName(apiGroup.getName());
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterApiGroupDescription(apiGroup.getDescription());
    }

    @Override
    public String getVersion() {
        return FilterHelper.filterApiProjectVersion(apiGroup.getVersion());
    }

    @Override
    public List<HttpApi> getHttpApis() {
        return httpApis;
    }

}
