package com.jayqqaa12.jbase.spring.mvc.resp.page;

/**
 * @author wangshuai
 */
public interface IPageData<T> {

  IPageData<T> create(Pages pages, T data);


}
