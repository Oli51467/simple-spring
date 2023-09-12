package com.sdu.springframework.common;

/**
 * 提供相关配置项常量
 */
public interface DefaultConfig {

    // 配置文件的名称
    String CONFIG_FILE = "application.properties";

    // 数据库
    String JDBC_DRIVER = "spring.datasource.jdbc.driver";
    String JDBC_URL = "spring.datasource.jdbc.url";
    String JDBC_USERNAME = "spring.datasource.jdbc.username";
    String JDBC_PASSWORD = "spring.datasource.jdbc.password";

    // 文件地址
    String APP_BASE_PACKAGE = "spring.datasource.app.base_package";
    String APP_JSP_PATH = "spring.datasource.app.jsp_path";
    String APP_ASSET_PATH = "spring.datasource.app.asset_path";
}
