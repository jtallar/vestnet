package ar.edu.itba.paw.webapp.config;


import ar.edu.itba.paw.interfaces.TokenExtractor;
import ar.edu.itba.paw.webapp.auth.MyCustomLoginSuccessHandler;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import ar.edu.itba.paw.webapp.auth.jwt.LoginProcessingFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    public static final String AUTH_HEADER = "Authorization";

    private static final String LOGIN_ENTRY_POINT = "/login";

    private static final int TOKEN_DAYS = 365;

    @Autowired
    private PawUserDetailsService userDetails;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private TokenExtractor tokenExtractor;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:public.pem");
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .invalidSessionUrl("/welcome")
                .and().authorizeRequests()
//                .antMatchers("/login","/signUp", "/location/**").anonymous()
                    .antMatchers("/login", "/signUp", "/projects", "/welcome", "/",
                        "/requestPassword", "/resetPassword", "/verify", "/projects/**", "/addHit/**"
                        , "/users/**").permitAll()
                    .antMatchers("/admin").hasRole("ADMIN")
                    .antMatchers("/requests").hasRole("INVESTOR")
                    .antMatchers("/newProject", "/deals", "/dashboard", "/**", "/stopFunding").hasRole("ENTREPRENEUR")
                    .antMatchers("/**").authenticated()
                .and().formLogin()
                    .loginPage("/login")
//                    .successHandler(myAuthenticationSuccessHandler())

                    .usernameParameter("username")
                    .passwordParameter("password")
                .and().rememberMe()
                    .rememberMeParameter("remember_me")
                    .key(asString(resource))
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(TOKEN_DAYS))


                .and().logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login")

                .and()
                    .addFilterBefore(buildLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(buildJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // TODO: Check si tengo que hacer el requestMatcher o lo frenan antes
    }

    //                .defaultSuccessUrl("/", false)

    private LoginProcessingFilter buildLoginFilter() throws Exception {
        LoginProcessingFilter filter = new LoginProcessingFilter(LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager());
        return filter;
    }

    private JwtTokenAuthenticationProcessingFilter buildJwtTokenFilter() throws Exception {
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(new RequestMatcher() {
            List<String> skipPaths = Collections.singletonList(LOGIN_ENTRY_POINT);
            RequestMatcher matcher = new OrRequestMatcher(skipPaths.stream().map(AntPathRequestMatcher::new).collect(Collectors.toList()));

            @Override
            public boolean matches(HttpServletRequest request) {
                return !matcher.matches(request);
            }
        }, tokenExtractor, failureHandler);
        filter.setAuthenticationManager(this.authenticationManager());
        return filter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/images/**", "/error/**", "/favicon.ico",
                        "/location/**", "/imageController/**");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



    /*@Bean
    public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
        return new MyCustomLoginSuccessHandler();
    }*/


    /** Auxiliary functions */

    /**
     * Converts key resource to a string.
     * @param resource The resource to convert.
     * @return The resource converted to string.
     */
    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
