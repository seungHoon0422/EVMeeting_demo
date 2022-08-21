package com.ssafy.user.db.repository;

import com.ssafy.user.db.entity.User;
import org.kurento.client.internal.server.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 유저 모델 관련 디비 쿼리 생성을 위한 JPA Query Method 인터페이스 정의.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 아래와 같이, Query Method 인터페이스(반환값, 메소드명, 인자) 정의를 하면 자동으로 Query Method 구현됨.
    Optional<User> findByUserid(String userid);
    Optional<User> findById(Long id);
    boolean existsByUserid(String userid);
    @Override
    List<User> findAll();

    @Query("SELECT COUNT(*) FROM User as u WHERE u.gender = :gender")
    int countUserByGender(@Param("gender") String gender);

    @Query(value = "select * from user u where u.id in (select s.userid2 from statistics s where s.userid1= :id and s.matched = 1) or u.id in (select s.userid1 from statistics s where s.userid2= :id  and s.matched = 1)", nativeQuery = true,
            countQuery = "select * from user u where u.id in (select s.userid2 from statistics s where s.userid1= :id and s.matched = 1) or u.id in (select s.userid1 from statistics s where s.userid2= :id and s.matched = 1)")
    public List<User> findMatchedThemById(@Param("id") Long id);
}