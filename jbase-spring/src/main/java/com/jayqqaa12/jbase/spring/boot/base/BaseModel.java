package com.jayqqaa12.jbase.spring.boot.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 数据库表的基本类
 * Created by 12 on 2018/1/23.
 */
@Data
@Accessors(chain = true)
@ToString(callSuper = true)
public abstract class BaseModel implements Serializable {

    @JSONField(serializeUsing = ToStringSerializer.class)
    @Id
    @TableId(type = IdType.ID_WORKER)
    protected Long id;



    public enum Status {
        CLOSE, OPEN;
    }

    public enum Deleted {
        FALSE, TRUE
    }

}
