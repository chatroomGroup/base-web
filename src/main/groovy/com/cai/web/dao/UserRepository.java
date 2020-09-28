package com.cai.web.dao;

import com.cai.web.domain.User;
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

    @Query(nativeQuery = true,
            value = "select gu.* from g_user gu " +
                    "left join g_user_password gup on gup.userId = gu.id and gup.password = ?2" +
                        " where gu.account = ?1"
    )
    List<User> getUserByAccountAndPassword(String account, String password);


    @Modifying
    @Query(nativeQuery = true,
            value = "update #{#entityName} set status = 0 where id in (:ids)"
    )
    Integer disableAccountStatusByIds(@Param("ids") List<Long> ids);
}
