package com.myproject.myweb.service;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;


    @Test // @Commit
    public void 회원가입_비번암호화(){
        String password = "xhxh5314";
        Long id = userService.join(UserRequestDto.builder()
                .email("test@naver.com")
                .name("정혜우")
                .password(password)
                .build());

        UserResponseDto user = userService.findById(id);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        encoder.matches(password, user.getPassword());

        assertThat(user.getName()).isEqualTo("정혜우");
        assertTrue(encoder.matches(password, user.getPassword()));
    }

    @Test
    public void 회원가입_중복예외(){
        UserRequestDto registerRequestDto = UserRequestDto.builder()
                .email("jhw127@naver.com")
                .password("1234")
                .build();

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> userService.join(registerRequestDto));

        assertThat(e.getMessage()).isEqualTo("UserAlreadyExistException");
    }

    @Test
    public void 회원_수정(){

        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Long id = user.getId();
        String newPW = "passwordChange";
        userService.passwordUpdate(id, newPW);

        UserResponseDto updateUser = userService.findById(id);

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches(newPW, updateUser.getPassword()));

    }

    @Test
    public void role_업데이트(){
        UserResponseDto user = userService.loadUserByUsername("jhw127@naver.com");
        userService.roleUpdate(user.getId(), Role.SILVAL_USER);

        UserResponseDto result = userService.loadUserByUsername("jhw127@naver.com");
        assertThat(result.getRoleTitle()).isEqualTo(Role.SILVAL_USER.getTitle());
    }

    @Test
    public void 회원_탈퇴(){
        UserResponseDto user = userService.loadUserByUsername("jhw127@naver.com");
        userService.signOut(user.getId());

        Boolean isPresent = userRepository.findById(user.getId()).isPresent();
        assertThat(isPresent).isEqualTo(false);
    }
}