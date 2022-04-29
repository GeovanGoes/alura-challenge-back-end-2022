package br.com.goes.analyzer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import br.com.goes.analyzer.service.AuthenticationService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authService;

	@Autowired
	void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(authService).passwordEncoder(new BCryptPasswordEncoder()).and();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		super.configure(web);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll()
			.anyRequest().authenticated()
			.and().csrf().ignoringAntMatchers("/h2-console/**")
	        .and().headers().frameOptions().sameOrigin()
		.and()
			.formLogin()
            .defaultSuccessUrl("/import")
		.and()
			.httpBasic()
		.and()
			.logout()
			.permitAll();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsService() {
		return authService;
	}

}
