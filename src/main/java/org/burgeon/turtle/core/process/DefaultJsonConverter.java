package org.burgeon.turtle.core.process;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 默认的JSON转换器
 *
 * @author luxiaocong
 * @createdOn 2021/3/17
 */
public class DefaultJsonConverter extends AbstractJsonConverter {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object convert(String json) {
        try {
            objectMapper.readValue(json, Object.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
