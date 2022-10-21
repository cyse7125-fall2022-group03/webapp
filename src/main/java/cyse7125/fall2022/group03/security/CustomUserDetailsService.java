package cyse7125.fall2022.group03.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cyse7125.fall2022.group03.model.User;
import cyse7125.fall2022.group03.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userInfo = userRepository.findByEmail(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("User " + username + " was not found in database");
        }
        String password = userInfo.getPassword();
        
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        grantedAuthorities.add(grantedAuthority);
        
        return new org.springframework.security.core.userdetails.User(username,
                password, grantedAuthorities);

    }
    
    


}