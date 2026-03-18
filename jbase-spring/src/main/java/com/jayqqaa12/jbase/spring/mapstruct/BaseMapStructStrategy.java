package com.jayqqaa12.jbase.spring.mapstruct;


import java.util.Date;
import java.util.Objects;

/**
 * @author wangshuai
 */
public class BaseMapStructStrategy {

  public Date longToDate(Long value) {
    if (Objects.isNull(value)) {
      return null;
    }

    return new Date(value);
  }

  public Long dateToLong(Date value) {
    if (Objects.isNull(value)) {
      return null;
    }
    return value.getTime();
  }




}
