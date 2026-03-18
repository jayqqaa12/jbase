// Copyright 2021 Leyantech Ltd. All Rights Reserved.

package com.jayqqaa12.jbase.spring.mvc.resp.page;

/**
 * @author 12
 */
public class PageData<T> implements IPageData<T> {

  private Pages pages;

  private T data;

  public PageData() {
  }

  public PageData(Pages pages, T data) {
    this.pages = pages;
    this.data = data;
  }

  public Pages getPages() {
    return pages;
  }

  public void setPages(Pages pages) {
    this.pages = pages;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  @Override
  public IPageData<T> create(Pages pages, T data) {
    return new PageData(pages, data);

  }


}
