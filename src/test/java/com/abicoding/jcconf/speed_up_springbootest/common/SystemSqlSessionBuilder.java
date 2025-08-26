package com.abicoding.jcconf.speed_up_springbootest.common;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

public class SystemSqlSessionBuilder {
    private final DataSource dataSource;
    private final Configuration configuration;
    private final Properties properties;

    public SystemSqlSessionBuilder() throws IOException {

        properties = initialProperties();

        dataSource = initialDataSource();

        configuration = initialConfiguration();

        initialSchema();

        addAllMappers();
    }

    private Properties initialProperties() throws IOException {
        Resource resource = new ClassPathResource("application.properties");
        return PropertiesLoaderUtils.loadProperties(resource);
    }

    private DataSource initialDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(properties.getProperty("spring.datasource.driverClassName"));
        dataSourceBuilder.url(properties.getProperty("spring.datasource.url"));
        dataSourceBuilder.username(properties.getProperty("spring.datasource.username"));
        dataSourceBuilder.password(properties.getProperty("spring.datasource.password"));
        return dataSourceBuilder.build();
    }

    private Configuration initialConfiguration() {
        Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration configuration = new Configuration(environment);

        boolean mapUnderscoreToCamelCase = Boolean.parseBoolean(properties.getProperty("mybatis.configuration.map-underscore-to-camel-case"));
        configuration.setMapUnderscoreToCamelCase(mapUnderscoreToCamelCase);

        return configuration;
    }

    private void initialSchema() {

        String[] locations = properties.getProperty("spring.flyway.locations").split(",");

        FluentConfiguration flywayConfiguration = Flyway.configure().loadDefaultConfigurationFiles().envVars();
        flywayConfiguration.cleanDisabled(false);
        flywayConfiguration.locations(locations);
        flywayConfiguration.dataSource(dataSource);
        if (flywayConfiguration.getConnectRetries() == 0) {
            flywayConfiguration.connectRetries(120);
        }

        Flyway flyway = flywayConfiguration.load();
        flyway.clean();
        flyway.migrate();
    }

    private void addAllMappers() {
        this.configuration.addMappers("com.abicoding.jcconf.speed_up_springbootest");
    }

    public SqlSession buildSqlSession() {
        SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator();
        translator.setDataSource(dataSource);
        return new SqlSessionTemplate(
                new SqlSessionFactoryBuilder().build(configuration),
                configuration.getDefaultExecutorType(),
                new MyBatisExceptionTranslator(() -> translator, true));
    }
}
