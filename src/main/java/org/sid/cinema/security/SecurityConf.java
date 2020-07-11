package org.sid.cinema.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConf extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;
@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	PasswordEncoder passwordEncoder=passwordEncoder();
	System.out.println("******************");
	System.out.println(passwordEncoder.encode("1234"));
	System.out.println("******************");
	auth.jdbcAuthentication().dataSource(dataSource)
	.usersByUsernameQuery("select username as principal, password as credentials, active as enabled from users where username=?")
	.authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
	.passwordEncoder(passwordEncoder) 
	.rolePrefix("ROLE_");
	
	/*
	auth.inMemoryAuthentication().withUser("user0").password(passwordEncoder.encode("1234")).roles("ADMIN");
	auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder.encode("1234")).roles("USER");
	auth.inMemoryAuthentication().withUser("user2").password(passwordEncoder.encode("1234")).roles("USER","ADMIN");
	 */
	}
@Override
protected void configure(HttpSecurity http) throws Exception {
	http.formLogin();
	//http.httpBasic();
	//http.authorizeRequests().anyRequest().authenticated();
	http.authorizeRequests().antMatchers("login/").permitAll();
	http.authorizeRequests().antMatchers("/form**/**","/delete**/**","/save**/**","/edit**/**","/ville","/cinema","/film","/projection","/ticket").hasRole("ADMIN");
		//http.authorizeRequests().anyRequest().authenticated();
http.csrf();
http.exceptionHandling().accessDeniedPage("/notAuthorized");
}
@Bean
public PasswordEncoder passwordEncoder()
{
	return new BCryptPasswordEncoder();
}
}
