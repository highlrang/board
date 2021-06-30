package com.myproject.myweb.config;

import com.myproject.myweb.handler.LoginSuccessHandler;
import com.myproject.myweb.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private final UserService userService;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    // @Bean
    // public PasswordEncoder passwordEncoder(){
    //    return new BCryptPasswordEncoder();
    //}

    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/static/css/**", "/static/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                    .antMatchers("/", "/register", "/css/**", "/images/**", "/js/**", "/profile").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successForwardUrl("/")
                    .successHandler(new LoginSuccessHandler())
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .permitAll()
                    .and()
                .csrf().disable();
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
