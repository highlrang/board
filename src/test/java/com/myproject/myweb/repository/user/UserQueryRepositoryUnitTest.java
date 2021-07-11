package com.myproject.myweb.repository.user;

import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class UserQueryRepositoryUnitTest {

    @Autowired UserQueryRepository userQueryRepository;

    @Test
    public void findAllTest(){
        List<WriterByLikeCountQueryDto> users = userQueryRepository.findAllWritersByLikeCount(2L);
        assertThat(users.size()).isNotEqualTo(0);

    }
}

/*
@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryUnitTest {

    @Mock
    UserRepository userRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void findByIdTest(){

        User mockUser = new User(Role.NORMAL_USER, "이름", "비번", "이메일");
        Optional<User> mockO = Optional.of(mockUser);

        when(userRepository.findById(1L)).thenReturn(mockO);

        Optional<User> user = userRepository.findById(1L);

        assertThat(user.get().getName()).isEqualTo("이름");

    }

}
*/