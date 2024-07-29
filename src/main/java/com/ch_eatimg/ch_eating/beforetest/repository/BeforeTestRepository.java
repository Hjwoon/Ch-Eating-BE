package com.ch_eatimg.ch_eating.beforetest.repository;

import com.ch_eatimg.ch_eating.domain.BeforeTest;
import com.ch_eatimg.ch_eating.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeforeTestRepository extends JpaRepository<BeforeTest, Long> {
    List<BeforeTest> findByUserId(User user);
}
