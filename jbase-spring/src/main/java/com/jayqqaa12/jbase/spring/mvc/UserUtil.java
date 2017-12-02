//package com.jayqqaa12.jbase.spring.mvc;
//
//import org.jboss.netty.util.internal.StringUtil;
//import org.springframework.beans.BeansException;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.concurrent.TimeUnit;
//
///**
// */
//public class UserUtil implements ApplicationContextAware {
//    public static final String SESSION_USER_ID_PREFIX = "_session_user:";
//    public static final String SESSION_USER_BIND = "_session_user_bind";
//
//
//    private static ThreadLocal<Integer> userIds = new ThreadLocal<>();
//    private static RedisTemplate<String, Object> _redisTemplate;
//
//
//    static int expiredSeconds = 7 * 24 * 3600;
//
//    public static void setCache(RedisTemplate redisTemplate) {
//
//        _redisTemplate = redisTemplate;
//    }
//
//
//    public static Integer getUserId() {
//        return userIds.get();
//    }
//
//
//    static void recordUser(int userId, String token) {
//        putToken(token, userId, false);
//    }
//
//    public static void injectToken(String token) {
//        Integer userId = parseInteger(_redisTemplate.opsForValue().get(SESSION_USER_ID_PREFIX + token));
//        if (userId != null) {
//            putToken(token, userId, true);
//        }
//    }
//
//
//    private static void putToken(String token, int userId, boolean flush) {
//        String key = SESSION_USER_ID_PREFIX + token;
//
//        userIds.set(userId);
//
//        if (!flush) {
//            _redisTemplate.opsForValue().set(key, userId, expiredSeconds, TimeUnit.SECONDS);
//
//            String oldToken = parseString(_redisTemplate.opsForHash().get(SESSION_USER_BIND, userId));
//
//            if (StringUtil.isNotEmpty(oldToken)) {
//                _redisTemplate.delete(SESSION_USER_ID_PREFIX + oldToken);
//            }
//            _redisTemplate.opsForHash().put(SESSION_USER_BIND, userId, token);
//
//        } else {
//            _redisTemplate.expire(key, expiredSeconds, TimeUnit.SECONDS);
//
//            String oldToken = parseString(_redisTemplate.opsForHash().get(SESSION_USER_BIND, userId));
//
//            if (StringUtil.isEmpty(oldToken) || !oldToken.equals(token)) {
//                if(StringUtil.isNotEmpty(oldToken)) {
//                    _redisTemplate.delete(SESSION_USER_ID_PREFIX + oldToken);
//                }
//                _redisTemplate.opsForHash().put(SESSION_USER_BIND, userId, token);
//            }
//
//        }
//
//
//    }
//
//    private static Integer parseInteger(Object object) {
//        if (object == null) {
//            return null;
//        }
//        if (object instanceof Integer) {
//            return (Integer) object;
//        } else {
//            return Integer.parseInt(object.toString());
//        }
//    }
//
//    private static String parseString(Object object) {
//        if (object == null) {
//            return null;
//        }
//        if (object instanceof String) {
//            return (String) object;
//        } else {
//            return object.toString();
//        }
//    }
//
//
//    public static void flush() {
//        userIds.remove();
//    }
//
//    @Override
//    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        //Ignore
//    }
//}