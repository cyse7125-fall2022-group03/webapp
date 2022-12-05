package cyse7125.fall2022.group03.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("---configure(HttpSecurity");
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/v1/create").permitAll()
                .antMatchers("/healthz").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/actuator/*").permitAll()
                .antMatchers("/v1/user/self").authenticated()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        System.out.println("--- configure(WebSecurity");
        super.configure(web);
        //web.ignoring().antMatchers(HttpMethod.GET);
    }

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      //  auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    //}
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        System.out.println("--- configure(AuthenticationManagerBuilder");
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
    

    

}
