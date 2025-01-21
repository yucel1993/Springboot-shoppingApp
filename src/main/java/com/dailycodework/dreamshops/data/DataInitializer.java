package com.dailycodework.dreamshops.data;

import com.dailycodework.dreamshops.model.Role;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
     Set<String> defaultRoles =  Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRoleIfNotExits(defaultRoles);
      createDefaultUserIfNotExits();
       createDefaultAdminIfNotExits();
    }


    private void createDefaultUserIfNotExits(){
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Role 'ROLE_USER' not found. Please check your data initialization."));

        for (int i = 1; i<=5; i++){
            String defaultEmail = "sam"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }



    private void createDefaultAdminIfNotExits(){
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalStateException("Role 'ROLE_ADMIN' not found. Please check your data initialization."));

        for (int i = 1; i<=2; i++){
            String defaultEmail = "admin"+i+"@email.com";
            if (userRepository.existsByEmail(defaultEmail)){
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }
    private void createDefaultRoleIfNotExits(Set<String> roles){
//        roles.stream()
//                .filter(role -> roleRepository.findByName(role).isEmpty())
//                .map(Role:: new).forEach(roleRepository::save);

        roles.stream()
                .filter(role -> roleRepository.findByName(role).isEmpty())
                .forEach(role -> {
                    Role newRole = new Role();
                    newRole.setName(role);
                    roleRepository.save(newRole);
                });

    }

}
