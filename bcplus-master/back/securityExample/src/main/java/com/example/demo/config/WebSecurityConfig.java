package com.example.demo.config;

import java.util.Arrays;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.filter.CustomAuthenticationFilter;
import com.example.demo.filter.CustomAuthenticationProvider;
import com.example.demo.filter.JwtAuthorizationFilter;
import com.example.demo.handler.CustomLoginSuccessHandler;
import com.example.demo.security.CustomUserDetailsService;
import com.example.demo.security.oauth.CustomOAuth2UserService;
import com.example.demo.security.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.demo.security.oauth.OAuth2AuthenticationFailureHandler;
import com.example.demo.security.oauth.OAuth2AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Resource(name = "userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

	@Autowired
	private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

	@Autowired
	private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

	/*@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}*/
	 @Bean
	    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
	        return new HttpCookieOAuth2AuthorizationRequestRepository();
	    }
	 @Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	        return super.authenticationManagerBean();
	    }

//	private CustomOAuth2UserService customOAuth2UserService;
	// 정적 자원에 대해서는 Security 설정을 적용하지 않음.
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				// 토큰을 활용하는 경우 모든 요청에 대해 접근이 가능하도록 함
				.antMatchers("/admin/*").hasRole("ADMIN")
				.antMatchers("/auth/**", "/oauth2/**").permitAll().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()				
				.anyRequest().permitAll()
				.and().cors().and()
				 .oauth2Login()
                 .authorizationEndpoint()
                     .baseUri("/oauth2/authorize")
                     .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                     .and()
                 .userInfoEndpoint()
                     .userService(customOAuth2UserService)
                     .and()
                 .successHandler(oAuth2AuthenticationSuccessHandler)
                 .failureHandler(oAuth2AuthenticationFailureHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// form 기반의 로그인에 대해 비활성화 한다.
				.formLogin().disable()
				.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userDetailsService));
	}
	
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
	        CustomLoginSuccessHandler successHandler = new CustomLoginSuccessHandler();
	        successHandler.setDefaultTargetUrl("/api/member/login");
	        return successHandler;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/api/user/login");
		customAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());		
		customAuthenticationFilter.afterPropertiesSet();
		return customAuthenticationFilter;
	}

	@Bean
	public CustomLoginSuccessHandler customLoginSuccessHandler() {
		return new CustomLoginSuccessHandler();
	}

	@Bean
	public CustomAuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider(bCryptPasswordEncoder());
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider());
	}
	@Bean
	   public CorsConfigurationSource corsConfigurationSource() {
		  CorsConfiguration configuration = new CorsConfiguration();
		    configuration.setAllowCredentials(true);
		    configuration.addAllowedOrigin("*");
		    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		    configuration.setAllowedHeaders(Arrays.asList("X-Requested-With","Origin","Content-Type","Accept","Authorization"));

		    // This allow us to expose the headers
		    configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
		            "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"));

		    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		    source.registerCorsConfiguration("/**", configuration);
		    return source;
	  }

}
