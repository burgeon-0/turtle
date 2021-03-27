package org.burgeon.turtle.export.blueprint.model;

import org.burgeon.turtle.core.model.api.ApiGroup;
import org.burgeon.turtle.core.model.api.HttpApi;
import org.burgeon.turtle.export.blueprint.FilterHelper;

import java.util.List;

/**
 * API群组【装饰器】
 *
 * @author luxiaocong
 * @createdOn 2021/3/27
 */
public class ApiGroupDecorator extends ApiGroup {

    private ApiGroup apiGroup;
    private List<HttpApi> httpApis;

    public ApiGroupDecorator(ApiGroup apiGroup, List<HttpApi> httpApis) {
        this.apiGroup = apiGroup;
        this.httpApis = httpApis;
    }

    @Override
    public String getId() {
        return apiGroup.getId();
    }

    @Override
    public String getName() {
        return FilterHelper.filterName(apiGroup.getName());
    }

    @Override
    public String getDescription() {
        return FilterHelper.filterDescription(apiGroup.getDescription());
    }

    @Override
    public String getVersion() {
        return apiGroup.getVersion();
    }

    @Override
    public List<HttpApi> getHttpApis() {
        return httpApis;
    }

}
