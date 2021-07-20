package com.myproject.myweb.repository.post;

import com.myproject.myweb.domain.Post;
import com.myproject.myweb.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;


public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByWriter(User writer);

    Long countByCategory_Id(Long cateId);

    List<Post> findAllByCategory_IdAndIsPublic(Long cateId, Boolean isPublic);
    List<Post> findAllByCategory_IdAndWriter_Id(Long cateId, Long writerId);

    // ToOne관계 join fetch 하느라 query 작성함
    @Query("select p from Post p join fetch p.writer w join fetch p.category c where p.isPublic = true")
    List<Post> findAllFetch();

    @Query("select p from Post p join fetch p.writer w join fetch p.category c where p.writer.id = :writerId")
    List<Post> findByWriterFetch(@Param(value="writerId") Long writerId);

    @Query("select p from Post p join fetch p.writer w join fetch p.category c join fetch p.likeList l where p.id = :id")
    Optional<Post> findByIdFetch(@Param(value="id") Long id);


    /*

And	findBy가And나
Or	findBy가Or나

Is, Equals, Not(보통 메서드 뒤에 붙임)
Between	(findBy가Between	where x.가 between ? and ?)

LessThan
LessThanEqual
GreaterThan
GreaterThanEqual

IsNull
IsNotNull	(findByAge(Is)Null)

Contains
In
NotIn	(findByAgeIn(Collection<Age> ages>))

IgnoreCase	(findByFirstnameIgnoreCase	where UPPER(x.firstname) = UPPER(:firstname))

distinct : find[Distinct]By
limit : find[First숫자/Top숫자]By
orderBy : findBy[OrderBy컬럼Desc/Asc]
count : countBy

select, count

클래스변수로 조인 조회 예시 : findByTeam_TeamName(String teamName)

*/
}