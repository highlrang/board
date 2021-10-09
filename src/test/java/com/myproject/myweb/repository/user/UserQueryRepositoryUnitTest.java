package com.myproject.myweb.repository.user;

import com.myproject.myweb.domain.user.User;
import com.myproject.myweb.dto.user.query.WriterByLikeCountQueryDto;
import com.myproject.myweb.repository.user.query.UserQueryRepository;
import com.myproject.myweb.repository.user.query.UserQuerydslRepository;
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
@RunWith(SpringRunner.class) //@RunWith(MockitoJUnitRunner.class)
public class UserQueryRepositoryUnitTest {

    // @Mock UserRepository userRepository;
    @Autowired UserQuerydslRepository userQuerydslRepository;

    @Test
    public void findAllTest(){
        List<WriterByLikeCountQueryDto> users = userQuerydslRepository.findAllWritersByLikeCount(2L);
        assertThat(users.size()).isNotEqualTo(0);

    }
}