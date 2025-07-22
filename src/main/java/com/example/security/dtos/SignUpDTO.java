package com.example.security.dtos;

import com.example.security.entities.enums.Permission;
import com.example.security.entities.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignUpDTO {
    private String email;
    private String name;
    private String password;
    private Set<Role> roles;
    private Set<Permission> permissions;
}
