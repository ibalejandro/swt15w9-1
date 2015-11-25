package prototyp.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
* <h1>MvcConfig</h1>
* The MvcConfig associates views with their specific RequestMappings in the
* controllers. It maps a view controller to the given URL path in order to
* render a response with a view.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
    
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
  	registry.addViewController("/home").setViewName("home");
    registry.addViewController("/offer").setViewName("offer");
    registry.addViewController("/search").setViewName("search");
    registry.addViewController("/myOfferedGoods").setViewName("myOfferedGoods");
  }

}

