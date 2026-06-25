package com.zhanabek.demo.capstone.config;

import com.zhanabek.demo.capstone.datasource.CustomDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import org.springframework.boot.jdbc.init.DataSourceScriptDatabaseInitializer;
import org.springframework.boot.sql.init.DatabaseInitializationSettings;

@Configuration
public class DatabaseConfig {

    @Bean
    DataSource dataSource(
            @Value("${db.pool.url}") String url,
            @Value("${db.pool.username}") String username,
            @Value("${db.pool.password}") String password,
            @Value("${db.pool.size:10}") int poolSize,
            @Value("${db.pool.timeout:30}") int timeout
    ) throws SQLException {
        return new CustomDataSource(url, username, password, poolSize, timeout);
    }

//    @Bean
//    public DataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource) {
//        return new DataSourceScriptDatabaseInitializer(
//                dataSource,
//                new DatabaseInitializationSettings()
//        );
//    }

}
