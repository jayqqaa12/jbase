package com.jayqqaa12.jbase.spring.db;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jayqqaa12.jbase.spring.serialize.json.JacksonToStringSerializer;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 数据库表的基本类
 * Created by 12 on 2018/1/23.
 */
@Data
@ToString(callSuper = true)
public abstract class BaseModel implements Serializable {

    @JSONField(serializeUsing = ToStringSerializer.class)
    @Id
    @JsonSerialize(using = JacksonToStringSerializer.class)
    @TableId(type = IdType.ID_WORKER)
    protected Long id;

    /**
     * 加个方法方便链式调用
     *
     * @param id
     * @return
     */
    public <T> T settId(Long id) {
        this.id = id;

        return (T) this;
    }


    public enum Status {
        CLOSE, OPEN;

        public static Status valueOf(int v) {
            for (Status status : values()) {
                if (status.ordinal() == v) return status;
            }
            throw new IllegalStateException(" error param enum for status");
        }
    }

    public enum Deleted {
        FALSE, TRUE;

        public static Deleted valueOf(int v) {
            for (Deleted deleted : values()) {
                if (deleted.ordinal() == v) return deleted;
            }
            throw new IllegalStateException(" error param enum for deleted");
        }
    }

}
