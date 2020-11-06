package com.cai.web.dao;

import com.cai.cache.annotation.FindCache;
import com.cai.cache.annotation.UpdateCache;
import com.cai.web.domain.User;
import com.cai.web.domain.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface UserPasswordRepository extends JpaRepository<UserPassword, Long> {

    long countByUserId(long userId);

    long deleteAllByUserId(long userId);
}
