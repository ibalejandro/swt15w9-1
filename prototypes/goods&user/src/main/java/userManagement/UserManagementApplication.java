package userManagement;

import org.salespointframework.EnableSalespoint;
import org.salespointframework.SalespointSecurityConfiguration;
import org.salespointframework.SalespointWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;



@EnableSalespoint
public class UserManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementApplication.class, args);
    }
	private static final String LOGIN_ROUTE = "/login";

	//Übernommen aus Vidoeshop, Modifikation nötig
	@Configuration
	static class UserManagementApplicationWebConfiguration extends SalespointWebConfiguration {
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController(LOGIN_ROUTE).setViewName("login");
		}
	}
	
	@Configuration
	static class WebSecurityConfiguration extends SalespointSecurityConfiguration {

		/**
		 * Disabling Spring Security's CSRF support as we do not implement pre-flight request handling for the sake of
		 * simplicity. Setting up basic security and defining login and logout options.
		 * 
		 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {

			http.csrf().disable();

			http.authorizeRequests().antMatchers("/**").permitAll().and().//
					formLogin().loginPage(LOGIN_ROUTE).loginProcessingUrl(LOGIN_ROUTE).and(). //
					logout().logoutUrl("/logout").logoutSuccessUrl("/");
		}
	}
}


