package org.gocom.coframe.auth.config;

import java.util.Arrays;
import java.util.List;

import org.gocom.coframe.auth.CoframeUserDetailsService;
import org.gocom.coframe.auth.OptionsRequestFilter;
import org.gocom.coframe.auth.handler.AuthFailureHandler;
import org.gocom.coframe.auth.handler.AuthLogoutHandler;
import org.gocom.coframe.auth.jwt.JWTConfiguration;
import org.gocom.coframe.auth.jwt.JWTProvider;
import org.gocom.coframe.auth.jwt.JWTSuccessHandler;
import org.gocom.coframe.auth.up.UPConfiguration;
import org.gocom.coframe.auth.up.UPSuccessHandler;
import org.gocom.coframe.sdk.CofConstants;
import org.gocom.coframe.sdk.config.CofSDKConfiguration;
import org.gocom.coframe.sdk.service.CofTokenService;
import org.gocom.coframe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 认证配置
 *
 * @author qinsc (mailto:qinsc@primeton.com)
 */
@Configuration
@EnableWebSecurity
public class AuthConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private CoframeUserDetailsService userDetailsService;

	@Autowired
	private AuthProperties authProperties;

	@Autowired
	private AuthExcludePaths excludePaths;

	@Autowired
	private IUserService userService;

	@Autowired
	private CofTokenService tokenService;

	@Autowired
	private CofSDKConfiguration sdkConfiguration;
	
	@Autowired(required = false)
    private List<CofHeaderWriter> headWriters;

    public static interface CofHeaderWriter extends HeaderWriter{}

	protected void configure(HttpSecurity http) throws Exception {
				
	    if (headWriters != null) {
            AnnotationAwareOrderComparator.sort(headWriters);
            for (CofHeaderWriter headerWriter : headWriters) {
                http.headers().addHeaderWriter(headerWriter);
            }
        }
	    
		http
		.authorizeRequests()//
				.antMatchers("/api/**").hasRole("USER")//
				.anyRequest().permitAll()//
				.and()//
				.csrf().disable()//
				.formLogin().disable()//
				.sessionManagement().disable()//
				.cors()//
				.and()//
				.headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(//
						new Header("Access-control-Allow-Origin", "*"), //
						new Header("Access-Control-Expose-Headers", "Authorization"))))//
				.frameOptions().disable()
				.and()//
				.addFilterAfter(new OptionsRequestFilter(), CorsFilter.class)//
				.apply(new UPConfiguration<>()).successHandler(upSuccessHandler()).failureHandler(failureHandler())//
				.and()//
				.apply(new JWTConfiguration<>(userService, tokenService, sdkConfiguration)).successHandler(jWTSuccessHandler()).failureHandler(failureHandler())//
				.and()//
				.logout()//
				.logoutUrl(CofConstants.COF_LOGOUT_URI).addLogoutHandler(logoutHandler())//
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())//
				.and()//
				.sessionManagement().disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider()).authenticationProvider(jwtAuthenticationProvider());
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Allow swagger to be accessed without authentication
		web.ignoring().antMatchers("/v2/api-docs")//
				.antMatchers("/swagger-resources/**")//
				.antMatchers("/swagger-ui.html")//
				.antMatchers("/configuration/**")//
				.antMatchers("/webjars/**")//
				.antMatchers("/public") //
				.antMatchers(excludePaths.getExcludePaths()); // 添加用户自定义的排除路径
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean("jwtAuthenticationProvider")
	protected AuthenticationProvider jwtAuthenticationProvider() {
		return new JWTProvider(userDetailsService, tokenService);
	}

	@Bean("daoAuthenticationProvider")
	protected AuthenticationProvider daoAuthenticationProvider() throws Exception {
		// 这里会默认使用BCryptPasswordEncoder比对加密后的密码，注意要跟createUser时保持一致
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder());
		daoProvider.setUserDetailsService(userDetailsService());
		return daoProvider;
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}

	@Bean
	protected UPSuccessHandler upSuccessHandler() {
		return new UPSuccessHandler(userDetailsService);
	}

	@Bean
	protected JWTSuccessHandler jWTSuccessHandler() {
		return new JWTSuccessHandler(userDetailsService, authProperties.getTokenRefreshInterval());
	}

	@Bean
	protected AuthFailureHandler failureHandler() {
		return new AuthFailureHandler();
	}

	@Bean
	protected AuthLogoutHandler logoutHandler() {
		return new AuthLogoutHandler(userDetailsService);
	}

	@Bean
	protected CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "HEAD", "PUT", "DELETE", "OPTION"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.addExposedHeader("Authorization");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

//	@Bean("sso")
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey("non-prod-signature");
//        //converter.setAccessTokenConverter(defaultAccessTokenConverter());
//        return converter;
//    }
//	
//	@Bean("sso")
//	public JwtTokenStore getStore() {
//		return new JwtTokenStore(accessTokenConverter());
//	}
}
