package com.belyabl9.langlearning.service.impl;

import com.belyabl9.langlearning.domain.User;
import com.belyabl9.langlearning.domain.UserRole;
import com.belyabl9.langlearning.service.InternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private InternalUserService internalUserService;
    
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = internalUserService.findByLogin(login);
        if (user == null) throw new UsernameNotFoundException("A user with the specified name does not exist.");
        List<GrantedAuthority> authorities = buildUserAuthority(user.getUserRoles());
        return buildUserForAuthentication(user, authorities);
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user,
                                            List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.isEnabled(), 
                true, true, true, authorities
        );
    }

    private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<>();

        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getName()));
        }

        List<GrantedAuthority> result = new ArrayList<>(setAuths);

        return result;
    }
}
