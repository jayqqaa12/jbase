//package com.jayqqaa12.jbase.spring.boot.base;
//
//import com.baomidou.mybatisplus.core.conditions.Wrapper;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//
//
///**
// * Created by 12 on 2018/1/30.
// * <p>
// * 分页工具
// * <p>
// * spring page ->>> mybatis plus page
// */
//public class PageUtil {
//
//
//    public static PageImpl selectPage(BaseMapper dao, Pageable pageable, Wrapper wrapper) {
//
//        int p = pageable.getPageNumber() == 0 ? 1 : pageable.getPageNumber() + 1;
//
//        Page page = new Page(p, pageable.getPageSize());
//        Sort sort = pageable.getSort();
//        if (sort != null) {
//            if (wrapper == null) wrapper = new QueryWrapper();
//            wrapper.orderBy(sort.toString().replaceAll(":", ""));
//        }
//
//
//        return new PageImpl(dao.selectPage(page, wrapper), pageable, page.getTotal());
//    }
//
//}
