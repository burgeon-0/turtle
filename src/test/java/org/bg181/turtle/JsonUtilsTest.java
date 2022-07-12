package org.bg181.turtle;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 测试Json工具类
 *
 * @author Sam Lu
 * @createdOn 2021/3/16
 */
public class JsonUtilsTest {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(0));
        System.out.println(objectMapper.writeValueAsString(0.0));
        System.out.println(objectMapper.writeValueAsString(true));
        System.out.println(objectMapper.writeValueAsString('a'));
        System.out.println(objectMapper.writeValueAsString("a"));
        System.out.println(objectMapper.writeValueAsString(new BigInteger("0")));
        System.out.println(objectMapper.writeValueAsString(new BigDecimal("0")));
        System.out.println(objectMapper.writeValueAsString(UUID.randomUUID()));
        System.out.println(objectMapper.writeValueAsString(new java.util.Date()));
        System.out.println(objectMapper.writeValueAsString(new java.sql.Date(0)));
        System.out.println(objectMapper.writeValueAsString(new java.sql.Timestamp(0)));
        System.out.println(objectMapper.writeValueAsString(LocalDate.now()));
        System.out.println(objectMapper.writeValueAsString(LocalTime.now()));
        System.out.println(objectMapper.writeValueAsString(LocalDateTime.now()));
        System.out.println(objectMapper.writeValueAsString(new ArrayList<>(0)));
        System.out.println(objectMapper.writeValueAsString(new HashSet<>(0)));
        System.out.println(objectMapper.writeValueAsString(new ArrayBlockingQueue(1)));
        System.out.println(objectMapper.writeValueAsString(new HashMap<>(0)));
        System.out.println(objectMapper.writeValueAsString(new Hashtable<>(0)));
        System.out.println(objectMapper.writeValueAsString(new Properties()));

        String json = "{\"num1\":1," +
                "\"num2\":90000000000000000000000000000000000000000.00000000000000000000000000000000000000009," +
                "\"num3\":9999999999999999," +
                "\"num4\":90000000000000000000000000000000000000000," +
                "\"bool\":true," +
                "\"str\":\"str1\"," +
                "\"arr\":[]}";
        Object obj = objectMapper.readValue(json, Object.class);
        System.out.println(obj.getClass());
        Map map = (Map) obj;
        for (Object key : map.keySet()) {
            System.out.println(key + ": " + map.get(key).getClass());
        }
    }

}
