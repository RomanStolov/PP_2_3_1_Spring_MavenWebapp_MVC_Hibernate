package web.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@PropertySource(value = "classpath:db.properties")
@ComponentScan(value = "web")
public class WebConfig implements WebMvcConfigurer {
    private final ApplicationContext applicationContext;
    private final Environment env;

    @Autowired
    public WebConfig(ApplicationContext applicationContext, Environment env) {
        this.applicationContext = applicationContext;
        this.env = env;
    }

    /**
     * В данный  метод добавлена поддержка кириллицы
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);

        return templateEngine;
    }

    /**
     * В данный  метод добавлена поддержка кириллицы
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setContentType("text/html; charset=UTF-8");
        registry.viewResolver(resolver);
    }

    /**
     * Данный бин нужен для подключение базы данных MySQL или PostgreSQL
     * Данные настроек беруться из внешнего файла "db.properties" (через доступ к нему посредством
     * объявленного в поле Environment) указанного в папке ресурсов с внедрением через конструктор
     * c аннотацией @Autowired
     */
    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("db.driver"));
        dataSource.setUrl(env.getRequiredProperty("db.url"));
        dataSource.setUsername(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));

        return dataSource;
    }

    /**
     * Данный метод нужен для настройки Hibernate
     * Данные настроек беруться из внешнего файла "db.properties" (через доступ к нему посредством
     * объявленного в поле Environment) указанного в папке ресурсов с внедрением через конструктор
     * c аннотацией @Autowired
     */
    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

        return properties;
    }

    /**
     * Данный бин нужен для создания фабрики EntityManagerFactory
     * Данные настроек беруться из внешнего файла "db.properties" (через доступ к нему посредством
     * объявленного в поле Environment) указанного в папке ресурсов с внедрением через конструктор
     * c аннотацией @Autowired
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager =
                new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(getDataSource());
        entityManager.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        entityManager.setJpaProperties(getHibernateProperties());

        return entityManager;
    }

    /**
     * Данный бин нужен для создания TransactionManager под требования аннотации @EnableTransactionManagement
     */
    @Bean
    public JpaTransactionManager getHibernateTransactionManagerFromEntityManagerFactory() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        transactionManager.setDataSource(getDataSource());

        return transactionManager;
    }

}