package be.intecbrussel.blogapplication.web_security_config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/user").setViewName("redirect:/user/");
//        registry.addViewController("/user/").setViewName("forward:/user/frontpage.html");
//    }

    public ViewResolver viewResolver()
    {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("src/main/resources/templates**/user*");
        viewResolver.setSuffix(".html");
        viewResolver.setOrder(0);
        viewResolver.setExposeContextBeansAsAttributes(true);
        return viewResolver;
    }
}
