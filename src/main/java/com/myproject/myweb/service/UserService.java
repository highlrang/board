package com.myproject.myweb.service;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto loadUserByUsername(String email){
        log.info(email);
        User entity = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));

        return new UserResponseDto(entity);

    }

    public UserResponseDto findById(Long writerId) {
        User user = userRepository.findById(writerId)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));

        return new UserResponseDto(user);
    }

    @Transactional
    public Long join(UserRequestDto registerRequestDto){
        Boolean userDuplicate = userRepository.findByEmail(registerRequestDto.getEmail()).isPresent();
        
        if(userDuplicate){
            throw new IllegalStateException("UserAlreadyExistException");
        }

        registerRequestDto.passwordEncode(
            passwordEncoder.encode(registerRequestDto.getPassword())
        );

        return userRepository.save(registerRequestDto.toEntity()).getId();
    }


    @Transactional
    public Long passwordUpdate(Long id, String password){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));
        user.passwordUpdate(passwordEncoder.encode(password));
        return id;
    }

    @Transactional
    public Long roleUpdate(Long id, Role role){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));
        user.roleUpdate(role);
        return id;
    }

    @Transactional
    public void signOut(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("UserNotFoundException"));
        userRepository.delete(user);
    }

}
