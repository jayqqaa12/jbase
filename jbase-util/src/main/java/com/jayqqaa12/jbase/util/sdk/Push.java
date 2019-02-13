package com.jayqqaa12.jbase.util.sdk;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: 12
 * @create: 2018-09-30 09:53
 **/
@Data
public class Push {


    private String  title;

    private String content;

    /**
     * 自定义参数
     * 会自动转为json格式
     */
    private Map<String ,Object > data ;


    private List<String > devices = new ArrayList<>();

    /**
     * 是否全部发送
     */
    private boolean sendAll;

    /**
     * 是否发生产
     */
    private boolean prod;


}
