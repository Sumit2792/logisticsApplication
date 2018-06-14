package com.fw.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fw.dao.IUserManager;
import com.fw.domain.Users;
import com.fw.exceptions.APIExceptions;
import com.fw.exceptions.InvalidUsernameException;

@Service
public class MyAppUserDetailsService  implements UserDetailsService{

	@Autowired
	private IUserManager userManager;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		Users login = null;
		try {
			login = userManager.getUserByUserName(userName);
		} catch (APIExceptions e) {
			
			throw new UsernameNotFoundException(userName);
		}
		GrantedAuthority authority = new SimpleGrantedAuthority("A");
		UserDetails userDetails = (UserDetails)new User(login.getUserName(), login.getPassword(), Arrays.asList(authority));		
		return userDetails;
	}

}
