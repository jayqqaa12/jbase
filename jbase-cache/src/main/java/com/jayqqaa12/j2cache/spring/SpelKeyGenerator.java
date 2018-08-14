package com.jayqqaa12.j2cache.spring;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
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

@Service
public class SpelKeyGenerator {

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ConcurrentHashMap<String, Expression> expCache = new ConcurrentHashMap<>();

    /**
     * @param invocation
     * @return
     * @throws NoSuchMethodException
     */
    public Object buildKey(String key, ProceedingJoinPoint invocation) throws NoSuchMethodException {

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
        } else keySpEL = key;


        MethodSignature signature = (MethodSignature) invocation.getSignature();
        Method method = signature.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(invocation.getTarget().getClass().getDeclaredMethod(method.getName(), parameterTypes));
        StandardEvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], invocation.getArgs()[i]);
        }

        Expression expression = expCache.get(keySpEL);
        if (null == expression) {
            expression = parser.parseExpression(keySpEL);
            expCache.put(keySpEL, expression);
        }

        Object value = expression.getValue(context, Object.class);

        if (!StringUtils.isEmpty(pre)) return pre + value;
        else return value;

    }
}
