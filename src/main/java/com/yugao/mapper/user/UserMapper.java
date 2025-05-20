package com.yugao.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yugao.domain.user.User;
import com.yugao.vo.statistics.MonthlyUserStatsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("""
        SELECT u.* FROM user u
        LEFT JOIN user_role ur ON u.id = ur.user_id
        LEFT JOIN role r ON ur.role_id = r.id
        WHERE u.id != #{curUserId}
        GROUP BY u.id
        HAVING MIN(r.level) > #{curUserLevel}
    """)
    List<User> getUsersWithLowerRoleLevel(
            Page<?> page,
            @Param("curUserId") Long curUserId,
            @Param("curUserLevel") Integer curUserLevel
    );

    @Select("""
        SELECT
            DATE_FORMAT(create_time, '%Y-%m') AS month,
            COUNT(*) AS registeredUsers
        FROM user
        WHERE create_time >= DATE_FORMAT(DATE_SUB(CURDATE(), INTERVAL 11 MONTH), '%Y-%m-01')
        GROUP BY month
        ORDER BY month ASC
    """)
    List<MonthlyUserStatsVO> getMonthlyRegisterStats();

    @Select("""
    SELECT COUNT(*) FROM (
        SELECT u.id
        FROM user u
        LEFT JOIN user_role ur ON u.id = ur.user_id
        LEFT JOIN role r ON ur.role_id = r.id
        WHERE u.id != #{curUserId}
        GROUP BY u.id
        HAVING MIN(r.level) > #{curUserLevel}
    ) AS tmp
""")
    Long countUsersWithLowerRoleLevel(
            @Param("curUserId") Long curUserId,
            @Param("curUserLevel") Integer curUserLevel
    );

}
