
package com.jayqqaa12.jbase.spring.dubbo;

import com.jayqqaa12.jbase.spring.exception.BusinessException;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;


/**
 *
 * RpcException 转为BusinessException
 * 
 */
@Activate(
        group = {"provider"}
)
public class DubboExceptionFilter extends ListenableFilter {
    public DubboExceptionFilter() {
        super.listener = new DubboExceptionFilter.ExceptionListener();
    }

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    static class ExceptionListener implements Listener {
        private Logger logger = LoggerFactory.getLogger(DubboExceptionFilter.ExceptionListener.class);

        ExceptionListener() {
        }

        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
                try {
                    Throwable exception = appResponse.getException();
                    if (exception instanceof RuntimeException || !(exception instanceof Exception)) {
                        try {
                            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                            Class<?>[] exceptionClassses = method.getExceptionTypes();
                            Class[] var7 = exceptionClassses;
                            int var8 = exceptionClassses.length;

                            for (int var9 = 0; var9 < var8; ++var9) {
                                Class<?> exceptionClass = var7[var9];
                                if (exception.getClass().equals(exceptionClass)) {
                                    return;
                                }
                            }
                        } catch (NoSuchMethodException var11) {
                            return;
                        }

                        this.logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
                        String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                        String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
                        if (serviceFile != null && exceptionFile != null && !serviceFile.equals(exceptionFile)) {
                            String className = exception.getClass().getName();
                            if (!className.startsWith("java.") && !className.startsWith("javax.")) {
                                if (!(exception instanceof RpcException)) {
                                    if (exception instanceof BusinessException) {
                                        appResponse.setException(exception);
                                    } else {
                                        appResponse.setException(new RuntimeException(StringUtils.toString(exception)));
                                    }

                                }
                            }
                        }
                    }
                } catch (Throwable var12) {
                    this.logger.warn("Fail to DubboExceptionFilter when called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + var12.getClass().getName() + ": " + var12.getMessage(), var12);
                }
            }
        }

        public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
            this.logger.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost() + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
        }

        public void setLogger(Logger logger) {
            this.logger = logger;
        }
    }
}
