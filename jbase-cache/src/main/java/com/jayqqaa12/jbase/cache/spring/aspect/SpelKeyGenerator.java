package com.jayqqaa12.jbase.cache.spring.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class SpelKeyGenerator {

  private static final ExpressionParser parser = new SpelExpressionParser();
  private static final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<>();

  /**
   *
   */
  public static String buildKey(String key, JoinPoint invocation) throws NoSuchMethodException {

    if (key.indexOf("#") == -1) {// 如果不是表达式，直接返回字符串
      return key;
    }

    String keySpEL = "";
    String pre = "";
    String str[] = key.split("\\#");
    if (str.length > 1) {
      pre = str[0];
      for (int i = 1; i < str.length; i++) {
        keySpEL = keySpEL + "#" + str[i];
      }
    } else {
      keySpEL = key;
    }

    MethodSignature signature = (MethodSignature) invocation.getSignature();
    Method method = signature.getMethod();
    Class<?>[] parameterTypes = method.getParameterTypes();
    ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    String[] parameterNames = parameterNameDiscoverer.getParameterNames(
        invocation.getTarget().getClass().getDeclaredMethod(method.getName(), parameterTypes));
    StandardEvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], invocation.getArgs()[i]);
    }

    Expression expression = expCache.get(keySpEL);
    if (null == expression) {
      expression = parser.parseExpression(keySpEL);
      expCache.put(keySpEL, expression);
    }

    String value = expression.getValue(context, String.class);

    if (!StringUtils.isEmpty(pre)) {
      return pre + value;
    } else {
      return value;
    }

  }
}