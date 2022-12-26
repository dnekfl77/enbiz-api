package com.x2bee.api.common.base.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.x2bee.common.base.token.TokenFilter;
import com.x2bee.common.base.token.MemberTokenService;
import com.x2bee.common.base.token.ServiceTokenService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private ServiceTokenService serviceTokenService;

	@Autowired
	private MemberTokenService memberTokenService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
//			.authorizeRequests()
//				.antMatchers("/", "/home", "/api/tokens").permitAll()
//				.antMatchers("/error", "/samples/createToken", "/actuator/**").permitAll()
//				.anyRequest().hasAnyRole("SERVICE", "ADMIN")
//				.anyRequest().authenticated()
//            .and()
            .sessionManagement()
            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(new TokenFilter(serviceTokenService), UsernamePasswordAuthenticationFilter.class) // 서비스간 호출 인증
        	.addFilterBefore(new TokenFilter(memberTokenService), UsernamePasswordAuthenticationFilter.class); // front 회원 인증
			;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**",
        		"/v2/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**");
	}

}
