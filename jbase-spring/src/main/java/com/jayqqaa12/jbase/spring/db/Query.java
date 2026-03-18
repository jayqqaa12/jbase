

package com.jayqqaa12.jbase.spring.db;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
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

    private Map map =Maps.newHashMap();

    public Query(Map<String, Object> params) {
        super(Integer.parseInt(params.getOrDefault(PAGE, 1).toString())
                , Integer.parseInt(params.getOrDefault(LIMIT, 10).toString()));

        String orderByField = params.getOrDefault(ORDER_BY_FIELD, "").toString();

        Boolean isAsc = Boolean.parseBoolean(params.getOrDefault(IS_ASC, Boolean.TRUE).toString());

        if(!StringUtils.isEmpty(orderByField)) {
            List<OrderItem> orders = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setColumn(orderByField);
            orderItem.setAsc(isAsc);
            orders.add(orderItem);
            this.setOrders(orders);
        }
        

        params.remove(PAGE);
        params.remove(LIMIT);
        params.remove(ORDER_BY_FIELD);
        params.remove(IS_ASC);
        this.condition().putAll(params);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> condition() {
        return map;
    }
}
