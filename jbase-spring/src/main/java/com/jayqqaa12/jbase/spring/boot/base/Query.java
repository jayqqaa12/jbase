

package com.jayqqaa12.jbase.spring.boot.base;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author 12
 * @date 2017/12/10
 */
public class Query<T> extends Page<T> {
    private static final String PAGE = "page";
    private static final String LIMIT = "limit";
    private static final String ORDER_BY_FIELD = "orderByField";
    private static final String IS_ASC = "isAsc";

    public Query(Map<String, Object> params) {
        super(Integer.parseInt(params.getOrDefault(PAGE, 1).toString())
                , Integer.parseInt(params.getOrDefault(LIMIT, 10).toString()));

        String orderByField = params.getOrDefault(ORDER_BY_FIELD, "").toString();
        if (StringUtils.isNotEmpty(orderByField)) {
            this.setOrderByField(orderByField);
        }

        Boolean isAsc = Boolean.parseBoolean(params.getOrDefault(IS_ASC, Boolean.TRUE).toString());
        this.setAsc(isAsc);

        params.remove(PAGE);
        params.remove(LIMIT);
        params.remove(ORDER_BY_FIELD);
        params.remove(IS_ASC);
        this.setCondition(params);
    }
}
