package com.myproject.myweb.repository.user;

import com.myproject.myweb.domain.user.Role;
import com.myproject.myweb.domain.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class) // junit
@SpringBootTest //
@Transactional
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입(){

        String email = "test@naver.com";
        String password = "xhxh5314";

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);

        userRepository.save(
                User.builder()
                .email(email)
                .password(password)
                .name("정혜우")
                .role(Role.NORMAL_USER)
                .build()
        );

        User user = userRepository.findByEmail(email).get();

        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }

    // 회원가입 V2


    @Test
    public void 회원탈퇴_이메일(){

        String email = "jhw127@naver.com";
        User user = userRepository.findByEmail(email).get();

        userRepository.delete(user);
        Boolean isPresent = userRepository.findByEmail(email).isPresent();

        assertThat(isPresent).isEqualTo(false);



    }

    @Test
    public void 회원_명수(){

        List<User> userList = userRepository.findAll();
        for(User user: userList){
            System.out.println(user.getEmail());
        }

        


    }


}


