package com.cai.web.dao;

import com.cai.cache.annotation.FindCache;
import com.cai.cache.annotation.GetCache;
import com.cai.cache.annotation.UpdateCache;
import com.cai.general.core.BaseMapper;
import com.cai.web.domain.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;


@Component
public interface UserMapper extends BaseMapper<User> {

    @FindCache(targetClass = User.class, validateSql = "select gu.id,gu.lastUpdated from g_user gu " +
            "inner join g_user_password gup on gup.userId = gu.id and gup.password = ?" +
            " where gu.account = ?", param = "1,0")
    @Select(value = "select gu.* from g_user gu " +
                    "inner join g_user_password gup on gup.userId = gu.id and gup.password = #{password}" +
                        " where gu.account = #{account}"
    )
    List<User> getUserByAccountAndPassword(@Param("account") String account, @Param("password") String password);


    @UpdateCache(targetClass = User.class, validateSql = "select * from g_user where id in (?)", param = "0")
    @Select(value = "update g_user set status = 0 where id in ( #{ids} )")
    Integer disableAccountStatusByIds(@Param("ids") List<Long> ids);

    @Select(value = "select * from g_user where account = #{account}")
    List<User> existsByAccount(@Param("account") String account);

    @GetCache(targetClass = User.class)
    @Select(value = "select * from g_user where account = #{account} limit 1")
    User getFirstByAccount(@Param("account") String account);
}
