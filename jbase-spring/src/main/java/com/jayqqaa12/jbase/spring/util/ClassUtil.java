package com.jayqqaa12.jbase.spring.util;


import org.springframework.core.GenericTypeResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 
 * @author Heavenick
 * @date 2013年12月29日 下午11:15:51
 */
public class ClassUtil extends ClassUtils {
    private static CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    public static  Method[] getMethods(Class<?> clazz , String methodName){

        Method[] methods =  clazz.getMethods() ;

        ArrayList<Method> arrayList = new ArrayList<Method>() ;

        for(Method method : methods){
            if( method.getName().equals(methodName)){
                arrayList.add(method);
            }
        }
        return arrayList.toArray(new Method[arrayList.size()]);
    }

    
    public static boolean  isSuperClass(Class<?> clazz , Class<?> superClass){

        if(superClass.isInterface()){
            for (Class c : clazz.getInterfaces()) {
                if(superClass == c){
                    return true ;
                }
            }
        }else{
            Class<?> s = clazz.getSuperclass() ;
            if(s != null && clazz.getSuperclass() == superClass){
                return true ;
            }else if(s!= null && clazz != Object.class){
              return  isSuperClass(s , superClass);
            }
        }
        return false ;
    }


    public static Method getSingleMethod(Class<?> clazz, String methodName){

        Method[] methods = getMethods(clazz,methodName) ;

        if(methods.length == 1 ){
            return methods[0] ;
        }
        throw  new RuntimeException("class:"+clazz.getName() + " contain more method:" + methodName);
    }

    public static Class<?> getClass(String clazz) throws ClassNotFoundException {

        return forName(clazz , null);
    }

    public static Class<?> loadClass(String className) {
        if (className == null || className.length() == 0) {
            return null;
        }

        Class<?> clazz = mappings.get(className);

        if (clazz != null) {
            return clazz;
        }

        if (className.charAt(0) == '[') {
            Class<?> componentType = loadClass(className.substring(1));
            return Array.newInstance(componentType, 0).getClass();
        }

        if (className.startsWith("L") && className.endsWith(";")) {
            String newClassName = className.substring(1, className.length() - 1);
            return loadClass(newClassName);
        }

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            if (classLoader != null) {
                clazz = classLoader.loadClass(className);

                addClassMapping(className, clazz);

                return clazz;
            }
        } catch (Throwable e) {
            // skip
        }

        try {
            clazz = Class.forName(className);

            addClassMapping(className, clazz);

            return clazz;
        } catch (Throwable e) {
            // skip
        }

        return clazz;
    }


    public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes) {
        Assert.notNull(clazz, "class must not be null");
        try {
            return clazz.getConstructor(paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> clazz){
        T t = null ;
        try {
            if(clazz.isInterface()){
                if(Map.class == clazz){
                    return (T) new HashMap();
                }else if(List.class == clazz){
                    return (T) new ArrayList();
                }

            }else{
                t = clazz.newInstance();
            }
        } catch (Exception ignore) {
            // ignore
        }
        return t;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String clazzName) throws ClassNotFoundException {
        Class<?> class1 = ClassUtil.getClass(clazzName) ;
        return newInstance((Class<T>)class1) ;
    }

    public static <T> T convertObject(Object origin, Class<T> t){
        return t.cast(origin) ;
    }

    public static Object convertObject(Object origin, String clazz) throws ClassNotFoundException {
        Class<?> cl = ClassUtil.getClass(clazz) ;
        if(cl == null)
            return null ;

        return cl.cast(origin) ;
    }


   
    public static Constructor[] getConstructor(Class<?> clazz , Integer parameterLength ) {
        Constructor<?>[] constructors = clazz.getConstructors() ;

        List<Constructor> result = new ArrayList<Constructor>();

        for (Constructor<?> constructor : constructors) {
            if(constructor.getParameterTypes().length == parameterLength){
                result.add(constructor);
            }
        }
        return (Constructor[]) result.toArray();
    }



    public static Class<?> getResourceClass(Resource resource){
        try {
            String className = getClassName(resource) ;
            if(StringUtil.isEmpty(className))
                return null ;
            Class<?> clazz = getClass(className);
            return clazz ;
        } catch (ClassNotFoundException e) {
            //Ignore ;
        }
        return null ;
    }

    public static String getClassName(Resource resource){

        try {
            MetadataReader metadataReader =  metadataReaderFactory.getMetadataReader(resource);
            return metadataReader.getClassMetadata().getClassName();
        } catch (IOException e) {
            //Ignore ;
        }
        return null ;
    }

    public static String determineClassName(Class<?> clazz, String suffix){
        String name = clazz.getSimpleName() ;

        if (!StringUtil.isEmpty(suffix) && name.endsWith(suffix)) {
            name =  name.substring(0,name.lastIndexOf(suffix));
        }

        return StringUtil.firstCharToLowerCase(name);
    }


    public  static Class<?> getGenericType(Class clazz,Class...faceTypes){
        for (Class type : faceTypes) {
            if(clazz == type){
                continue;
            }

            Class<?> result  = getGenericClass(clazz,type);
            if(result != Object.class){
                return  result;
            }
        }
        return null;
    }

    /**
     *  获取声明的泛型
     * @param clazz
     * @param faceType
     * @return
     */
    public static Class<?> getGenericClass(Class clazz,Class faceType){
        return GenericTypeResolver.resolveTypeArgument(clazz, faceType);
    }
    /**
     * 基础类型
     * @param clazz
     * @return
     */
    public static boolean isBaseType(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz) ||  clazz == String.class;
    }

    public static boolean isArrayType(Class<?> clazz){
        return clazz.isArray() || Iterable.class.isAssignableFrom(clazz);
    }

    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap<String, Class<?>>();
    static {
        addBaseClassMappings();
    }

    public static void addClassMapping(String className, Class<?> clazz) {
        if (className == null) {
            className = clazz.getName();
        }

        mappings.put(className, clazz);
    }

    public static void addBaseClassMappings() {
        mappings.put("byte", byte.class);
        mappings.put("short", short.class);
        mappings.put("int", int.class);
        mappings.put("long", long.class);
        mappings.put("float", float.class);
        mappings.put("double", double.class);
        mappings.put("boolean", boolean.class);
        mappings.put("char", char.class);

        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);

        mappings.put(HashMap.class.getName(), HashMap.class);
    }

}
