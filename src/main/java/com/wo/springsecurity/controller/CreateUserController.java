package com.wo.springsecurity.controller;

import com.wo.springsecurity.controller.dto.CreateUserDto;
import com.wo.springsecurity.entities.Role;
import com.wo.springsecurity.entities.User;
import com.wo.springsecurity.repository.RoleRepository;
import com.wo.springsecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
public class CreateUserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CreateUserController(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

      var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
      var userFromDb = userRepository.findByUsername(dto.username());

      if (userFromDb.isPresent()) {
          throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
      }

      var user = new User();
      user.setUsername(dto.username());
      user.setPassword(passwordEncoder.encode(dto.password()));
      user.setRoles(Set.of(basicRole));

      userRepository.save(user);

      return ResponseEntity.ok().build();
    }
}
