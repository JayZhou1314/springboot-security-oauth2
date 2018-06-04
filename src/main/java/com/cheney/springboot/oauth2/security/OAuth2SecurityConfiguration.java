package com.cheney.springboot.oauth2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {



	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
				.withUser("demoUser1").password("123456").authorities("USER")
				.and()
				.withUser("demoUser2").password("123456").authorities("USER");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		AuthenticationManager manager = super.authenticationManagerBean();
		return manager;
	}

	/**
	 * 1\这里记得设置requestMatchers,不拦截需要token验证的url
	 * 不然会优先被这个filter拦截,走用户端的认证而不是token认证
	 * 2\这里记得对oauth的url进行保护,正常是需要登录态才可以
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http
				.requestMatchers().antMatchers("/oauth/**","/login/**","/logout/**")
				.and()
				.authorizeRequests()
				.antMatchers("/oauth/**").authenticated()
				.and()
				.formLogin().permitAll();
	}


	
}
