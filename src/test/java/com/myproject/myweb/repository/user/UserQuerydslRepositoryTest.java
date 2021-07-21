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
    public void 쿼리비교(){

      log.info("------------------생쿼리 시작-------------------------------");
      userQuerydslRepository.findAllByMap();
      log.info("------------------생쿼리 완료-------------------------------");

      log.info("------------------fetch쿼리 시작----------------------------");
      userQuerydslRepository.findAllByFetch();
      log.info("------------------fetch쿼리 완료----------------------------");

      log.info("------------------in쿼리 시작-------------------------------");
      userQuerydslRepository.findAllInQeury();
      log.info("------------------in쿼리 완료-------------------------------");


    }

    @Test
    public void 쿼리확인(){
        userQuerydslRepository.findAllWritersByLikeCount(5L);
    }

}
