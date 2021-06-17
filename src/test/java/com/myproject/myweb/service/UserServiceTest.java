package com.myproject.myweb.service;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.repository.user.UserRepository;
import com.myproject.myweb.dto.user.UserRequestDto;
import com.myproject.myweb.dto.user.UserResponseDto;
import com.myproject.myweb.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;


    @Test @Commit
    public void 회원가입_비번암호화(){
        String password = "xhxh5314";
        Long id = userService.join(UserRequestDto.builder()
                .email("jhw127@naver.com")
                .name("정혜우")
                .password(password)
                .build());

        UserResponseDto user = userService.findById(id);

        assertThat(user.getName()).isEqualTo("정혜우");
    }

    @Test
    public void 회원가입_중복예외(){
        UserRequestDto registerRequestDto = UserRequestDto.builder()
                .email("jhw127@naver.com")
                .password("1234")
                .build();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> userService.join(registerRequestDto));

        assertThat(e.getMessage()).isEqualTo("UserAlreadyExistException");
    }

    @Test
    public void 회원_수정(){

        User user = userRepository.findByEmail("jhw127@naver.com").get();
        Long id = user.getId();
        String password = "xhxh5314";
        userService.passwordUpdate(id, password);

    }

    // roleUpdate
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