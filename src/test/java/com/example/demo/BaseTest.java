package com.example.demo;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

@SpringBootTest
@ContextConfiguration(initializers = BaseTest.DataSourceInitializer.class)
@Testcontainers
public class BaseTest {

  private static final String imageName = "postgres:13-alpine";

  protected static String tenant_init_script_file;
  protected static int redis_port;

  @ClassRule
  private static PostgreSQLContainer<?> database;


  @Container
  private static final GenericContainer redisContainer = new GenericContainer<>("redis:6-alpine")
    .withExposedPorts(6379)
    .withReuse(true);

  public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

      database = new PostgreSQLContainer<>(imageName)
        .withReuse(true)
        .waitingFor(new WaitAllStrategy())
        .withDatabaseName("tenant")
        .withInitScript("base.sql");
      database.start();
      TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
        applicationContext,
        "spring.test.database.replace=none",
        "spring.datasource.url=" + database.getJdbcUrl(),
        "spring.datasource.username=" + database.getUsername(),
        "spring.datasource.password=" + database.getPassword(),
        "spring.redis.host=" + redisContainer.getHost(),
        "spring.redis.port=" + redisContainer.getMappedPort(6379)
      );
    }
  }
}
