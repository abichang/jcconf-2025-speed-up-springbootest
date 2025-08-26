package com.abicoding.jcconf.speed_up_springbootest.common;

import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

public class SystemDbTestExtension implements BeforeEachCallback, AfterEachCallback {

    private final SqlSession sqlSession;

    @SneakyThrows
    public SystemDbTestExtension() {
        sqlSession = new SystemSqlSessionBuilder().buildSqlSession();
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        injectDecoratedMappers(extensionContext.getRequiredTestInstance());
    }

    private void injectDecoratedMappers(Object testInstance) throws IllegalAccessException {
        List<Field> mapperFields = AnnotationSupport.findAnnotatedFields(testInstance.getClass(), InjectMapper.class);
        for (Field field : mapperFields) {

            Object originalMapper = sqlSession.getMapper(field.getType());

            Object decoratedMapper = Optional.ofNullable(field.getAnnotation(Spy.class))
                    .map((spyAnnotation) -> Mockito.spy(originalMapper))
                    .orElse(originalMapper);

            field.setAccessible(true);
            field.set(testInstance, decoratedMapper);
        }
    }


    @Override
    public void afterEach(ExtensionContext extensionContext) {
        truncateAllDbTables();
    }

    public void truncateAllDbTables() {
        GetAllTableNamesMapper getAllTableNamesMapper = sqlSession.getMapper(GetAllTableNamesMapper.class);
        TruncateTableMapper truncateTableMapper = sqlSession.getMapper(TruncateTableMapper.class);

        String[] allTableNames = getAllTableNamesMapper.getAllTableNames();
        for (String tableName : allTableNames) {
            if (!"flyway_schema_history".equalsIgnoreCase(tableName)) {
                truncateTableMapper.truncateTable(tableName);
            }
        }
    }
}
