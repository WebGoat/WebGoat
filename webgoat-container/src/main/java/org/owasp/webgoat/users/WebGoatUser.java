package org.owasp.webgoat.users;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Collection;
import java.util.Collections;

/**
 * @author nbaars
 * @since 3/19/17.
 */
@Getter
@Entity
public class WebGoatUser implements UserDetails {

    public static final String ROLE_USER = "WEBGOAT_USER";
    public static final String ROLE_ADMIN = "WEBGOAT_ADMIN";

    @Id
    private String username;
    private String password;
    private String role = ROLE_USER;
    @Transient
    private User user;

    protected WebGoatUser() {
    }

    public WebGoatUser(String username, String password) {
        this.username = username;
        this.password = password;
        createUser();
    }

    public WebGoatUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }


    public void createUser() {
        this.user = new User(username, password, getAuthorities());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(getRole()));
    }

    public String getRole() {
        return this.role;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }


}


