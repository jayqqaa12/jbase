package com.jayqqaa12.jbase.web.spring.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.SpringProperties;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Boyce 下午4:00:21
 *         本来考虑是否为UserUtil实现存放用户attr的功能，后来考虑分布式情况下，很容易userId和userAttr的失效时间不一致。
 *         且在分布式情况下，很少需要session来工作，故而放弃
 */
public class UserUtil implements ApplicationContextAware {
	public static final String SESSION_USER_ID_PREFIX = "_session_user:";
	public static final String REDIS_HOST_CHANNEL_KEY = "uhost_channel";
	static ThreadLocal<Integer> userIds = new ThreadLocal<>();
	static ThreadLocal<String> hosts = new ThreadLocal<>();
	static ValueOperations<String, Integer> _userIds;
	public static int expiredSeconds = 24 * 3600;
	static ApplicationContext ctx;
	static SpringProperties prop = null;
	static final String LOAD_HOST_CHANNEL_URL = "sys.host_channel.url";

	public void setExpiredSeconds(int seconds) {
		if (seconds > 0)
			expiredSeconds = seconds;
	}

	public void setCache(RedisTemplate redisTemplate) {
		_userIds = redisTemplate.opsForValue();
		host_channels = redisTemplate.opsForHash();
	}


	public static Integer getUserId() {
		return userIds.get();
	}

	public static void recordHost(String host) {
		hosts.set(host);
	}

	static void recordUser(int userId, String token) {
		userIds.set(userId);
		_userIds.set(SESSION_USER_ID_PREFIX + token, userId, expiredSeconds, TimeUnit.SECONDS);
	}

	public static void injectToken(String token) {
		Integer userId = _userIds.get(SESSION_USER_ID_PREFIX + token);
		if (userId != null) {
			userIds.set(userId);
			_userIds.set(SESSION_USER_ID_PREFIX + token, userId, expiredSeconds, TimeUnit.SECONDS);// 刷新
		}
	}

	public static void flush() {
		userIds.remove();
		hosts.remove();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
		prop = ctx.getBean(SpringProperties.class);
	}
}
