package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.interfaces.TokenExtractor;
import ar.edu.itba.paw.webapp.auth.CorsFilter;
import ar.edu.itba.paw.webapp.auth.PawUserDetailsService;
import ar.edu.itba.paw.webapp.auth.PlainTextBasicAuthenticationEntryPoint;
import ar.edu.itba.paw.webapp.auth.jwt.JwtAuthenticationProcessingFilter;
import ar.edu.itba.paw.webapp.auth.jwt.LoginProcessingFilter;
import ar.edu.itba.paw.webapp.auth.jwt.RememberAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
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
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
@EnableWebSecurity
@ComponentScan({"ar.edu.itba.paw.webapp.auth", "ar.edu.itba.paw.webapp.config"})
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthConfig.class);

    public static final String AUTH_HEADER = "Authorization";
    private static final String API_PREFIX_VERSION = "/api";
    private static final String LOGIN_ENTRY_POINT = API_PREFIX_VERSION + "/auth/login";


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
        http
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, LOGIN_ENTRY_POINT).permitAll()
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/auth/refresh").permitAll()

                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/images/user/**", API_PREFIX_VERSION + "/images/projects/**").permitAll()
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/images/user/**").authenticated()
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/images/projects/**").hasRole("ENTREPRENEUR")

                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/location/**").permitAll()

                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/project/**").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/investor").hasRole("INVESTOR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/notifications/project/**").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/notifications").authenticated()
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/chat/*/*").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/chat/**").hasRole("INVESTOR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/invested").hasRole("INVESTOR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/messages/invested/**").hasRole("ENTREPRENEUR")

                .antMatchers(HttpMethod.POST, API_PREFIX_VERSION + "/messages/*/*").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.POST, API_PREFIX_VERSION + "/messages/**").hasRole("INVESTOR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/messages/status/*/*").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/messages/status/**").hasRole("INVESTOR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/messages/seen/*/*").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/messages/seen/**").hasRole("INVESTOR")


                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/projects/**").permitAll()
                .antMatchers(HttpMethod.POST, API_PREFIX_VERSION + "/projects/**").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/projects/**/stats").permitAll()
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/projects/**").hasRole("ENTREPRENEUR")

                .antMatchers(HttpMethod.POST, API_PREFIX_VERSION + "/users/**").permitAll()
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/users/password", API_PREFIX_VERSION + "/users/verify").permitAll()
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/users/favorites/**").hasRole("INVESTOR")
                .antMatchers(HttpMethod.PUT, API_PREFIX_VERSION + "/users/**").authenticated()
                .antMatchers(HttpMethod.DELETE, API_PREFIX_VERSION + "/users/**").authenticated()
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/users/projects").hasRole("ENTREPRENEUR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/users/**/projects").permitAll()
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/users/favorites/**").hasRole("INVESTOR")
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/users").authenticated()
                .antMatchers(HttpMethod.GET, API_PREFIX_VERSION + "/users/**").permitAll()
                .antMatchers("/**").permitAll();

        if (isDevelopmentMode()) http
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class);

        http
                .addFilterBefore(buildLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(buildJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new PlainTextBasicAuthenticationEntryPoint()); // removes 403 default body from response
    }


    /**
     * Folder and files to ignore applying filters to.
     * @param web The web to config.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/views/**",
                        "/styles/**",
                        "/scripts/**",
                        "/images/**",
                        "/bower_components/**",
                        "/favicon.ico",
                        "/index.html");
    }


    /**
     * Authentication manager bean.
     * @return The created authentication manager.
     * @throws Exception On creating or configuration error.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /**
     * Bean for remembering the authentications provided.
     * @return The created authentication provided.
     */
    @Bean
    public AuthenticationProvider rememberAuthenticationProvider() {
        RememberAuthenticationProvider rememberAuthenticationProvider = new RememberAuthenticationProvider();
        rememberAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        rememberAuthenticationProvider.setUserDetailsService(userDetails);
        return rememberAuthenticationProvider;
    }


    /* Auxiliary functions */

    /**
     * Builds the Login Filter.
     * @return The built processing login filter.
     * @throws Exception On creating or configuration error
     */
    private LoginProcessingFilter buildLoginFilter() throws Exception {
        return new LoginProcessingFilter(LOGIN_ENTRY_POINT, rememberAuthenticationProvider(), successHandler, failureHandler, objectMapper);
    }


    /**
     * Builds the JWT Token Filter
     * @return The JWT Processing Filter.
     */
    private JwtAuthenticationProcessingFilter buildJwtTokenFilter() {
        return new JwtAuthenticationProcessingFilter(tokenExtractor, authenticationProvider);
    }


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


    /**
     * Use LOGGER debug mode for switching development/production mode respectively.
     * @return True for development mode, false if production.
     */
    private boolean isDevelopmentMode() {
        return LOGGER.isDebugEnabled();
    }
}
