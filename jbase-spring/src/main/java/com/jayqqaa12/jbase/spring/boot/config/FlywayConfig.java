package com.jayqqaa12.jbase.spring.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.internal.dbsupport.FlywaySqlScriptException;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Configuration;


/**
 *
 * 修改设置 如果出现sql出差 自动删除schema 错误信息
 *
 * 需要数据库升级完才能加载数据 所以在需要的地方加上
 * @DependsOn("flywayInitializer")
 */
@Configuration
@Slf4j
public class FlywayConfig implements FlywayMigrationStrategy {


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
