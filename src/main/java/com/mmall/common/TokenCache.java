package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author：wuhaoteng
 * @date：2020/3/23 11:12 上午
 * @desc：token本地缓存
 */
public class TokenCache {
    private static int CORE_SIZE = 1000; //缓存初始化容量
    private static int MAX_SIZE = 10000; //最大容量
    private static int INVALID_HOURS = 12; //失效时间为12小时
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    private static LoadingCache<String, String> localCache =
            CacheBuilder.newBuilder()
                    .initialCapacity(CORE_SIZE)
                    .maximumSize(MAX_SIZE)
                    .expireAfterAccess(INVALID_HOURS, TimeUnit.HOURS)
                    .build(new CacheLoader<String, String>() {
                        @Override
                        public String load(String s) throws Exception {
                            //如果key没有对应取值，将会调用这个方法
                            return "null";
                        }
                    });

    public static void put(String key, String value) {
        localCache.put(key, value);
    }

    public static String get(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
        } catch (Exception e) {
            logger.error("localCache get error", e);
        }
        return value;
    }
}
