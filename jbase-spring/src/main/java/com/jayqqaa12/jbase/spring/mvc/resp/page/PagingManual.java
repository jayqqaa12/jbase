package com.jayqqaa12.jbase.spring.mvc.resp.page;


import com.jayqqaa12.jbase.spring.exception.BusinessException;
import com.jayqqaa12.jbase.spring.util.ThreadKit;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.MDC;

import java.util.Collections;
import java.util.List;

/**
 * @author donggang
 */
public class PagingManual {

  public static final String REQUEST_PAGE_NUM = "pageNum";
  public static final String REQUEST_PAGE_SIZE = "pageSize";

  private PagingManual() {
  }

  /**
   * 结合 List.subList(fromIndex, toIndex) 手动分页.
   */
  public static Pair<Integer, Integer> getPagingIndexPair(Integer listSize) {

    Pair<Integer, Integer> startPageNunAndSizePair = getStartPageNumAndSize();
    int pageNum = startPageNunAndSizePair.getLeft();
    int pageSize = startPageNunAndSizePair.getRight();

    int total = listSize;
    // pageSize <= 0 不分页.
    if (pageSize <= 0) {
      return Pair.of(0, total);
    }

    int fromIndex = (pageNum - 1) * pageSize;
    int toIndex = (pageNum) * pageSize;
    if (toIndex > total) {
      toIndex = total;
    }
    return Pair.of(fromIndex, toIndex);
  }

  /**
   * 手动分页，计算总页数.
   */
  public static int getTotalPage(int total, int pageSize) {
    // pageSize <= 0 不分页.
    if (pageSize <= 0) {
      return 1;
    }
    if (total % pageSize == 0) {
      return total / pageSize;
    } else {
      return total / pageSize + 1;
    }
  }

  /**
   * getPages.
   *
   * @param: listSize
   */
  public static Pages getPages(Integer listSize) {

    Pair<Integer, Integer> startPageNunAndSizePair = getStartPageNumAndSize();
    int pageNum = startPageNunAndSizePair.getLeft();
    int pageSize = startPageNunAndSizePair.getRight();

    return new Pages()
        .pageNum(pageNum)
        .pageSize(pageSize)
        .totalRows(listSize)
        .totalPages(getTotalPage(listSize, pageSize));
  }

  /**
   * @description: getStartPageNumAndSize.
   * @param:
   * @return: org.apache.commons.lang3.tuple.Pair java.lang.Integer, java.lang.Integer
   */
  public static Pair<Integer, Integer> getStartPageNumAndSize() {
    int pageNum = NumberUtils.toInt(String.valueOf(MDC.get(REQUEST_PAGE_NUM)), 1);
    int pageSize = NumberUtils.toInt(String.valueOf(MDC.get(REQUEST_PAGE_SIZE)), Integer.MAX_VALUE - 1);
    if (pageNum <= 0) {
      pageNum = 1;
    }
    return Pair.of(pageNum, pageSize);
  }


  public static Page buildPage(String key) {
    int pageNum = NumberUtils.toInt(String.valueOf(MDC.get(REQUEST_PAGE_NUM)), 1);
    int pageSize = NumberUtils.toInt(String.valueOf(MDC.get(REQUEST_PAGE_SIZE)), Integer.MAX_VALUE - 1);
    Page page = new Page(pageNum, pageSize);

    if (StringUtils.isNotEmpty(key)) {
      ThreadKit.set(key, page);
    }
    return page;
  }


  public static Pages buildPages(Page page) {
    return new Pages()
        .pageNum((int) page.getCurrent())
        .pageSize((int) page.getSize())
        .totalRows((int) page.getTotal())
        .totalPages((int) page.getPages());
  }


  public static <T> IPageData<List<T>> buildManualPagingResponse(List<T> data) {

    if (CollectionUtils.isEmpty(data)) {
      return buildEmptyPageResponse();
    }
    int total = data.size();
    Pair<Integer, Integer> pagingIndexPair = PagingManual.getPagingIndexPair(total);
    int fromIndex = pagingIndexPair.getLeft();
    if (fromIndex >= total) {
      throw new BusinessException("page error");
    }

    return (IPageData<List<T>>)SpringUtil.getBean(IPageData.class).create(PagingManual.getPages(total),
        data.subList(fromIndex, pagingIndexPair.getRight()));
  }


  public static IPageData buildEmptyPageResponse() {
    return buildPagingResponse(Collections.emptyList(), PagingManual.getPages(0));
  }

  public static <T> IPageData<T> buildPagingResponse(T data, Pages pages) {
    return (IPageData<T>)(SpringUtil.getBean(IPageData.class).create(pages, data));
  }

}
