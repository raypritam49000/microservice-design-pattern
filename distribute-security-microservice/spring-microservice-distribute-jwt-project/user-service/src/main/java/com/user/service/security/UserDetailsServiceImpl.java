package com.user.service.security;

import com.user.service.dto.UserDTO;
import com.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userService.getUserByUsername(username);
        return new UserDetailsImpl(userDTO.username(), userDTO.password(), List.of(new SimpleGrantedAuthority(userDTO.role())), Map.of("id", userDTO.id(), "username", userDTO.username(), "email", userDTO.email(), "roles", List.of(userDTO.role())));
    }
}
