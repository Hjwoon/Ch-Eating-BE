package com.ch_eatimg.ch_eating.test.repository;

import com.ch_eatimg.ch_eating.domain.Test;
import com.ch_eatimg.ch_eating.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByUserId(User user);
}
