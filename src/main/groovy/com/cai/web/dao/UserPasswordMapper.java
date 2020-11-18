package com.cai.web.dao;

import com.cai.general.core.BaseMapper;
import com.cai.web.domain.User;
import com.cai.web.domain.UserPassword;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Mapper
@Component
public interface UserPasswordMapper extends BaseMapper<UserPassword> {

    @Select("select count(id) from g_user_password where userId = #{userId}")
    long countByUserId(@Param("userId") long userId);

    @Delete(value = "delete from #{entityName} where userId = #{userId}")
    long deleteByUserId(@Param("userId") long userId);

    @Select(value = "select * from g_user_password where userId = #{userId}")
    UserPassword getUserPassByUserId(@Param("userId") long id);
}
