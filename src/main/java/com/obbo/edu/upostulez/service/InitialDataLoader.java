package com.obbo.edu.upostulez.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.obbo.edu.upostulez.domain.Address;
import com.obbo.edu.upostulez.domain.Privilege;
import com.obbo.edu.upostulez.domain.Role;
import com.obbo.edu.upostulez.domain.User;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.PrivilegeName;
import com.obbo.edu.upostulez.protocol.DbEntityProtocol.RoleName;
import com.obbo.edu.upostulez.repository.PrivilegeRepository;
import com.obbo.edu.upostulez.repository.RoleRepository;
import com.obbo.edu.upostulez.repository.UserRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {

		if (alreadySetup) return;
		
		Privilege readPrivilege = createPrivilegeIfNotFound(PrivilegeName.READ_PRIVILEGE);
		Privilege writePrivilege = createPrivilegeIfNotFound(PrivilegeName.WRITE_PRIVILEGE);

		Set<Privilege> adminPrivileges = Stream.of(readPrivilege, writePrivilege).collect(Collectors.toSet());
//		Set<Privilege> userPrivileges = Stream.of(readPrivilege).collect(Collectors.toSet());
				
		Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN, adminPrivileges);
//		Role userRole = createRoleIfNotFound(RoleName.ROLE_USER, userPrivileges);

		User user = new User();
		user.setFirstName("Admin");
		user.setLastName("ADMIN");
		user.setEmail("test@test.com");
		user.setPassword(passwordEncoder.encode("test"));
		user.addRole(adminRole);
		user.setAddress(new Address("Paris", 75001, "IdF", "0123456789"));
		
		userRepository.save(user);

		alreadySetup = true;
	}

	@Transactional
	private Privilege createPrivilegeIfNotFound(PrivilegeName name) {

		Optional<Privilege> optPrivilege = privilegeRepository.findByName(name);

		return optPrivilege.orElseGet(() -> privilegeRepository.save(new Privilege(name)));
	}

	@Transactional
	private Role createRoleIfNotFound(RoleName name, Set<Privilege> privileges) {

		Optional<Role> optRole = roleRepository.findByName(name);

		return optRole.orElseGet(() -> {
			Role role = new Role(name);
			role.setPrivileges(privileges);
			return roleRepository.save(role);
		});
	}
}