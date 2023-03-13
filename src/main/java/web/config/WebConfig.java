package web.config;

import org.hibernate.jpa.HibernatePersistenceProvider;
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
// Вырубил, так как в проекте не используется
//import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@PropertySource(value = "classpath:db.properties")
@ComponentScan(value = "web")
public class WebConfig implements WebMvcConfigurer {
//    Вырубил, так как в проекте не используетсяprivate
//    final ApplicationContext applicationContext;

    @Resource
    private Environment env;

//    Вырубил, так как в проекте не используется
//    public WebConfig(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    /**
     * В данный  метод добавлена поддержка кириллицы
     */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        // Вырубил, так как в проекте не используется
        // templateResolver.setApplicationContext(applicationContext);
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
     * Данный бин нужен для подключение базы данных
     * Данные настроек беруться из внешнего файла "db.properties" (через доступ к нему
     * посредством объявленного в поле Environment) указанного в папке ресурсов в аннотации
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
     * Данный бин нужен для создания фабрики EntityManagerFactory
     * Данные настроек беруться из внешнего файла "db.properties" (через доступ к нему посредством
     * объявленного в поле Environment) указанного в папке ресурсов в аннотации @PropertySource
     * <p>
     * IntelliJ IDEA сказала что два варианта при @Autoware в UserDAOEntityManagerImpl !!!!!
     *
     * @see web.dao.UserDAOEntityManagerImpl#UserDAOEntityManagerImpl
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager =
                new LocalContainerEntityManagerFactoryBean();
        // Тут ниже явно указал на использование Hibernate
        entityManager.setDataSource(getDataSource());
        entityManager.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setPackagesToScan(env.getRequiredProperty("db.entity.package"));
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));

        entityManager.setJpaProperties(properties);

        return entityManager;
    }

    /**
     * Данный бин нужен для создания TransactionManager под требования аннотации @EnableTransactionManagement
     */
    @Bean
    public JpaTransactionManager getTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getEntityManagerFactory().getObject());
        transactionManager.setDataSource(getDataSource());

        return transactionManager;
    }

    /**
     * Данный бин нужен для создания EntityManager из фабрики EntityManagerFactory
     * Используется в UserDAOEntityManagerImpl
     * <p>
     * IntelliJ IDEA сказала что два варианта при @Autoware в UserDAOEntityManagerImpl !!!!!
     *
     * @see web.dao.UserDAOEntityManagerImpl#UserDAOEntityManagerImpl
     */
    @Bean
    public EntityManager getEntityManager(EntityManagerFactory entityManagerFactory) {

        return entityManagerFactory.createEntityManager();
    }

}