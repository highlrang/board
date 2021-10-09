package com.myproject.myweb.repository.user;

import com.myproject.myweb.repository.user.query.UserQuerydslRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class UserQuerydslRepositoryTest {

    @Autowired
    UserQuerydslRepository userQuerydslRepository;

    @Test
    public void 쿼리비교() {
    }

}
