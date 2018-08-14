package com.jayqqaa12.j2cache;

@FunctionalInterface
public interface CacheDataSource<T> {

    T load();

}
