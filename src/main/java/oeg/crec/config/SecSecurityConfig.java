package oeg.crec.config;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security setting. 
 * Al añadir esto, y modificar el POM, se tiene seguridad básica.
 * https://www.javainuse.com/spring/sprboot_sec
 * @author vrdoddon
 */
@Configuration
@EnableWebSecurity
public class SecSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        String CLAVES = "../users.cfg";
        auth.userDetailsService(username -> {
            try {
                List<String> users = Files.readAllLines(Paths.get(CLAVES));
                System.out.println("Se han leido un total de usuarios: " + users.size());
                for (String user : users) {
                    String[] parts = user.split("\\s+", 2);
                    String theUsername = parts[0];
                    String password = parts[1];
                    if (username.equals(theUsername)) {
                        return new User(theUsername, passwordEncoder().encode(password), Collections.singleton(new SimpleGrantedAuthority("USER")));
                    }
                }
                throw new UsernameNotFoundException("Invalid username");
            } catch (Exception e) {
                throw new UsernameNotFoundException("Invalid username");
            }
        });
        /*
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
                .and()
                .withUser("cponet").password(passwordEncoder().encode("cponet")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");*/
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
           //     .authorizeRequests()
           //     .antMatchers("/**").access("hasIpAddress(\"127.0.0.1\")") //experimental
                .antMatchers("/**").permitAll()                     //debería ser un *. con ** son carpetas y subcarpetas.
                .antMatchers("/header.html").permitAll()
                .antMatchers("/footer.html").permitAll()
                .antMatchers("/images/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/csv2skos/**").permitAll()
                .antMatchers("/scheme/**").permitAll()
                .antMatchers("/templates/**").permitAll()
                .antMatchers("/login*").permitAll()
                .antMatchers("/converter.html").permitAll()
                .antMatchers("/documentation.html").permitAll()
/*                .antMatchers("/admin/**").hasRole("ADMIN")
                //          .antMatchers("/cpv.html").anonymous()
                .antMatchers("/cpv*").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/cpvs/").permitAll()
                .antMatchers("/news/**").permitAll()
                .antMatchers("/org/**").permitAll()
                .antMatchers("/evidences/**").permitAll()
                .antMatchers("/search/**").permitAll()
                .antMatchers("/config/**").permitAll()
                //          .antMatchers("/login.html").permitAll()*/
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login.html?error=true")
                //          .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID");
//          .logoutSuccessHandler(logoutSuccessHandler());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
