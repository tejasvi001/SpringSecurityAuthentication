package com.example.security.entities;

import com.example.security.entities.enums.Permission;
import com.example.security.entities.enums.Role;
import com.example.security.utils.PermissionsMapping;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Builder
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @ElementCollection(fetch= FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;
//    @ElementCollection(fetch= FetchType.EAGER)
//    @Enumerated(EnumType.STRING)
//    private Set<Permission> permissions;





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities=new HashSet<>();
        roles.forEach(
                role->{
                   Set<SimpleGrantedAuthority> permissions= PermissionsMapping.getAuthoritiesForRole(role);
                   authorities.addAll(permissions);
                   authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
                }
        );
        return authorities;
    }



    @Override
    public String getUsername() {
        return this.email;
    }



}
