package com.cai.web.dao;

import com.cai.cache.annotation.FindCache;
import com.cai.cache.annotation.UpdateCache;
import com.cai.web.domain.User;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @FindCache(targetClass = User.class, validateSql = "select gu.id,gu.lastUpdated from g_user gu " +
            "left join g_user_password gup on gup.userId = gu.id and gup.password = ?" +
            " where gu.account = ?", param = "1,0")
    @Query(nativeQuery = true,
            value = "select gu.* from g_user gu " +
                    "left join g_user_password gup on gup.userId = gu.id and gup.password = ?2" +
                        " where gu.account = ?1"
    )
    List<User> getUserByAccountAndPassword(String account, String password);


    @Modifying
    @UpdateCache(targetClass = User.class, validateSql = "select * from g_user where id in (?)", param = "0")
    @Query(nativeQuery = true,
            value = "update #{#entityName} set status = 0 where id in (:ids)"
    )
    Integer disableAccountStatusByIds(@Param("ids") List<Long> ids);
}
