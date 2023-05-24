//package com.blog.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//	@Bean
//	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//
//		/**
//		 * Below is the custom security configurations
//		 */
//
////	        http.authorizeHttpRequests()
////	                        .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
////	                        .requestMatchers("/notices","/contact").permitAll()
////	                .and().formLogin()
////	                .and().httpBasic();
////	        return http.build();
//
//		/**
//		 * Configuration to deny all the requests
//		 */
//		/*
//		 * http.authorizeHttpRequests().anyRequest().denyAll() .and().formLogin()
//		 * .and().httpBasic(); return http.build();
//		 */
//
//		/**
//		 * Configuration to permit all the requests
//		 */
////	        http.authorizeHttpRequests().anyRequest().permitAll()
////	                .and().formLogin()
////	                .and().httpBasic();
////	        return http.build();
//
//		http.csrf().disable().authorizeHttpRequests().requestMatchers("/api/post/*").authenticated()
//				.requestMatchers("/api/users/*").permitAll().and().formLogin().and().httpBasic();
//		return http.build();
//	}
//
//	@Bean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
//
//}
