package com.jayqqaa12.jbase.spring.boot.base;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.sqlscript.FlywaySqlScriptException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.stereotype.Component;


/**
 * 修改设置 如果出现sql出差 自动删除schema 错误信息
 * <p>
 * 需要数据库升级完才能加载数据 所以在需要的地方加上
 *
 * @DependsOn("flywayInitializer")
 *
 *
 * 注意只处理sql出错的情况，如果已经执行过的文件是不能修改的！
 * 如果出现修改了文件的情况，应该还原文件，并增加新版本的sql
 *
 *
 */
@Slf4j
@Component
public class FlywayStrategy implements FlywayMigrationStrategy {


    @Override
    public void migrate(Flyway flyway) {

        try {
            flyway.migrate();
        } catch (FlywaySqlScriptException e) {
            flyway.repair();
            throw e;
        }

    }


}
