package com.jayqqaa12.jbase.spring.boot.base;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by 12 on 2017/11/20.
 */
@Data
@Accessors(chain = true)
public class JmsMsg {

    private Long msgId;
    private Object data;

}
