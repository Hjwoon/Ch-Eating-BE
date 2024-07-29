package com.ch_eatimg.ch_eating.aftertest.repository;

import com.ch_eatimg.ch_eating.domain.AfterTest;
import com.ch_eatimg.ch_eating.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AfterTestRepository extends JpaRepository<AfterTest, Long> {
    List<AfterTest> findByUserId(User user);
}
