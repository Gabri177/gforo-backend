package com.yugao.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("""
        SELECT u.* FROM user u
        LEFT JOIN user_role ur ON u.id = ur.user_id
        LEFT JOIN role r ON ur.role_id = r.id
        WHERE u.id != #{curUserId}
        GROUP BY u.id
        HAVING MAX(r.level) > #{curUserLevel}
    """)
    List<User> getUsersWithLowerRoleLevel(
            Page<?> page,
            @Param("curUserId") Long curUserId,
            @Param("curUserLevel") Integer curUserLevel
    );
}
