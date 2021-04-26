package org.burgeon.turtle;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 测试class.isInstance()
 *
 * @author Sam Lu
 * @createdOn 2021/3/16
 */
public class IsInstanceTest {

    private static byte b = 0;
    private static short s = 0;
    private static long l = 0;
    private static float f = 0;

    public static void main(String[] args) {
        System.out.println(java.lang.Byte.class.isInstance(b));
        System.out.println(java.lang.Short.class.isInstance(s));
        System.out.println(java.lang.Integer.class.isInstance(0));
        System.out.println(java.lang.Long.class.isInstance(l));
        System.out.println(java.lang.Float.class.isInstance(f));
        System.out.println(java.lang.Double.class.isInstance(0.0));
        System.out.println(java.lang.Boolean.class.isInstance(true));
        System.out.println(java.lang.Character.class.isInstance('a'));
        System.out.println(java.lang.String.class.isInstance("a"));
        System.out.println(java.lang.Number.class.isInstance(new BigInteger("0")));
        System.out.println(java.lang.Number.class.isInstance(new BigDecimal("0")));
        System.out.println(java.util.UUID.class.isInstance(UUID.randomUUID()));
        System.out.println(java.util.Date.class.isInstance(new java.util.Date()));
        System.out.println(java.util.Date.class.isInstance(new java.sql.Date(0)));
        System.out.println(java.util.Date.class.isInstance(new java.sql.Timestamp(0)));
        System.out.println(java.time.temporal.Temporal.class.isInstance(LocalDate.now()));
        System.out.println(java.time.temporal.Temporal.class.isInstance(LocalTime.now()));
        System.out.println(java.time.temporal.Temporal.class.isInstance(LocalDateTime.now()));
        System.out.println(java.util.Collection.class.isInstance(new ArrayList<>(0)));
        System.out.println(java.util.Collection.class.isInstance(new HashSet<>(0)));
        System.out.println(java.util.Collection.class.isInstance(new ArrayBlockingQueue(1)));
        System.out.println(java.util.Map.class.isInstance(new HashMap<>(0)));
        System.out.println(java.util.Map.class.isInstance(new Hashtable<>(0)));
        System.out.println(java.util.Map.class.isInstance(new Properties()));
    }

}
