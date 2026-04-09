package com.rakbow.kureakurusu.data.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rakbow.kureakurusu.data.constant.PermissionConstant;
import com.rakbow.kureakurusu.toolkit.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Rakbow
 * @since 2026/3/14 13:06
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {

    private Long id;
    private String name;
    private String avatar;

    private String ipAddress;
    private String agent;

    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();

    private String ticket;
    private int expires;

    public boolean isAdmin() {
        return this.permissions.contains(PermissionConstant.ADMIN);
    }

    public void setRoles(Set<String> roles) {
        if(roles == null) {
            this.roles = new HashSet<>();
            return;
        }
        this.roles = roles.stream()
                .filter(StringUtil::isNotBlank)
                .collect(Collectors.toSet());
    }

    public void setPermissions(Set<String> permissions) {
        if(permissions == null) {
            this.permissions = new HashSet<>();
            return;
        }
        this.permissions = permissions.stream()
                .filter(StringUtil::isNotBlank)
                .collect(Collectors.toSet());
    }

    @JsonIgnore
    @Override
    public @NotNull Collection<? extends GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .filter(StringUtil::isNotBlank)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @JsonIgnore
    @Override
    public @NotNull String getUsername() {
        return name;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

}
