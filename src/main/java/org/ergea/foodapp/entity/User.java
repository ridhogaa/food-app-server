package org.ergea.foodapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ergea.foodapp.entity.oauth.Role;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "email_address")
    private String emailAddress;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @JsonIgnore
    private String verifyToken;

    @JsonIgnore
    private Date expiredVerifyToken;

    @Column(length = 100, nullable = true)
    private String otp;

    private Date otpExpiredDate;

    @JsonIgnore
    private boolean enabled = true;

    @JsonIgnore
    @Column(name = "not_expired")
    private boolean accountNonExpired = true;

    @JsonIgnore
    @Column(name = "not_locked")
    private boolean accountNonLocked = true;

    @JsonIgnore
    @Column(name = "credential_not_expired")
    private boolean credentialsNonExpired = true;

    @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "oauth_user_role",
            joinColumns = {
                    @JoinColumn(name = "user_id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private List<Role> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
