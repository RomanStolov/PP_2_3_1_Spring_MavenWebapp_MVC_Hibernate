package web.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import java.util.EnumSet;

public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Для корректного отображения кириллицы в данных при вводе - Работает !!!
     * Принудительно делаю @Override метода и дописываю в метод настройки для корректного отображения
     * кириллицы в данных при вводе. Дополнительно добавил фильтр из уроков Алишева для корректной
     * работы PATCH-DELETE-PUT через POST в HTML5
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        FilterRegistration.Dynamic characterEncoding = servletContext
                .addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

        servletContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter())
                .addMappingForUrlPatterns(null, true, "/*");
    }

    /**
     * Метод, указывающий на класс конфигурации
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    /**
     * Добавление конфигурации, в которой инициализируем ViewResolver, для корректного отображения jsp.
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{
                WebConfig.class
        };
    }

    /**
     * Данный метод указывает url, на котором будет базироваться приложение
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{
                "/"
        };

    }

}