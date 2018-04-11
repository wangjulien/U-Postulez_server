package com.obbo.edu.upostulez.config;

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
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

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
		Set<Privilege> userPrivileges = Stream.of(readPrivilege).collect(Collectors.toSet());
				
		Role adminRole = createRoleIfNotFound(RoleName.ROLE_ADMIN, adminPrivileges);
		Role userRole = createRoleIfNotFound(RoleName.ROLE_USER, userPrivileges);

		User adminUser = new User();
		adminUser.setFirstName("Admin");
		adminUser.setLastName("ADMIN");
		adminUser.setEmail("test@test.com");
		adminUser.setPassword(passwordEncoder.encode("test"));
		adminUser.addRole(adminRole);
		adminUser.setAddress(new Address("Paris", 75001, "IdF", "0123456789"));
		createUserIfNotFound(adminUser);
		
		User normUser = new User();
		normUser.setFirstName("Guest");
		normUser.setLastName("GUEST");
		normUser.setEmail("guest@test.com");
		normUser.setPassword(passwordEncoder.encode("test"));
		normUser.addRole(userRole);
		normUser.setAddress(new Address("Paris", 75001, "IdF", "0123456789"));
		createUserIfNotFound(normUser);

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
	
	@Transactional
	private User createUserIfNotFound(User newUser) {

		Optional<User> optUser = userRepository.findByEmail(newUser.getEmail());

		return optUser.orElseGet(() -> userRepository.save(newUser));
	}
}