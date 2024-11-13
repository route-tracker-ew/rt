package com.kpi.routetracker.config.security.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomUserDetails {

    private final String login;

    private final String password;

    private final Set<Object> authorities = new HashSet<>();

    private final boolean accountNonExpired;

    private final boolean accountNonLocked;

    private final boolean credentialsNonExpired;

    private final boolean enabled;

    public CustomUserDetails(String login, String password, Object authorities, boolean accountNonExpired, boolean accountNonLocked,
                             boolean credentialsNonExpired, boolean enabled) {
        this.login = login;
        this.password = password;
        this.authorities.add(authorities);
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Collection<? extends Object> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return login;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }
}