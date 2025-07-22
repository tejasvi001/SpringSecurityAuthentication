package com.example.security.utils;

import com.example.security.entities.enums.Permission;
import com.example.security.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.security.entities.enums.Permission.*;
import static com.example.security.entities.enums.Role.*;

public class PermissionsMapping {
    private static final Map<Role, Set<Permission>> map=Map.of(
            USER, Set.of(USER_VIEW,POST_VIEW),
            CREATOR, Set.of(USER_VIEW,POST_CREATE, POST_VIEW,POST_UPDATE),
            ADMIN, Set.of(USER_VIEW,USER_CREATE,USER_UPDATE,USER_DELETE,POST_DELETE,POST_VIEW,POST_CREATE,POST_UPDATE)
    );
    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role)
                .stream().map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());

    }
}
