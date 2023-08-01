package com.mcu.examportal.service;

import com.mcu.examportal.entity.UserEntity;
import com.mcu.examportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isPresent()){
            return  new User(userByEmail.get().getEmail(), userByEmail.get().getPassword(),
                    authorities(userByEmail.get().getRoles()));
        }
        return null;
    }

    public List<SimpleGrantedAuthority> authorities(String roles){
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
}
