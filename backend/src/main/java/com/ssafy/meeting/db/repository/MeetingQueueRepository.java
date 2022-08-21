package com.ssafy.meeting.db.repository;

import com.ssafy.meeting.db.entitiy.MeetingQueue;
import com.ssafy.user.db.entity.User;
import org.kurento.client.internal.server.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface MeetingQueueRepository extends JpaRepository<MeetingQueue, Long> {

    @Query(
            value = "SELECT u from User u where u.userid = ?1"
    )
    List<User> findByUserId(@Param("userid") String userid);

    List<MeetingQueue> findAll();

    List<MeetingQueue> findByGender(@Param("gender") String gender);

    @Transactional
    void deleteByUserid(@Param("userid") String userid);
}
