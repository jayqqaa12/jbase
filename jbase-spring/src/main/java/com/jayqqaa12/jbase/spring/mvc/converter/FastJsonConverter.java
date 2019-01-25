package com.jayqqaa12.jbase.spring.mvc.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.jayqqaa12.jbase.spring.mvc.inteceptor.EffectInterceptor;
import com.jayqqaa12.jbase.spring.serialize.json.PageDeserializer;
import com.jayqqaa12.jbase.spring.serialize.json.PageableDeserializer;
import com.jayqqaa12.jbase.spring.serialize.json.SortDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class FastJsonConverter extends FastJsonHttpMessageConverter {

    public static FastJsonConfig FAST_JSON_CONFIG = new FastJsonConfig();

    static {
        ParserConfig.getGlobalInstance().putDeserializer(Pageable.class, new PageableDeserializer());
        ParserConfig.getGlobalInstance().putDeserializer(Page.class, new PageDeserializer());
        ParserConfig.getGlobalInstance().putDeserializer(Sort.class, new SortDeserializer());

//        ParserConfig.getGlobalInstance().putDeserializer(BaseModel.Deleted.class,
//                new EnumDeserializer());
//        ParserConfig.getGlobalInstance().putDeserializer(BaseModel.Status.class,
//                new EnumDeserializer());


        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);

        JSON.DEFAULT_GENERATE_FEATURE = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.WriteEnumUsingName, false);
        FAST_JSON_CONFIG.setSerializerFeatures(
                SerializerFeature.IgnoreErrorGetter,
                SerializerFeature.BrowserSecure,
                SerializerFeature.BrowserCompatible);
    }


    public FastJsonConverter() {

        setFastJsonConfig(FAST_JSON_CONFIG);
        setSupportedMediaTypes(Arrays.asList(
                new MediaType[]{MediaType.APPLICATION_JSON,
                        new MediaType("application", "*+json")}
        ));
    }

    @Override
    public void writeInternal(Object obj, HttpOutputMessage outputMessage)
            throws IOException {
        String text = "";
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            text += obj;
        } else {
            text = JSON.toJSONString(obj, getFeatures());
        }

        String call = EffectInterceptor.callBack.get();
        if (StringUtils.isNotBlank(call)) {

            if (obj instanceof String) {
                text = call + "(\"" + text + "\")";
            } else {
                text = call + "(" + text + ")";
            }
        }
        OutputStream out = outputMessage.getBody();
        byte[] bytes = text.getBytes(getCharset());
        out.write(bytes);
    }
}
