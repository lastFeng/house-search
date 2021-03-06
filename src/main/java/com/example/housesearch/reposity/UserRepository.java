package com.example.housesearch.reposity;

import com.example.housesearch.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 通过名称查找用户
     * @param username
     * @return
     */
    User findByName(String username);

    /**
     * 通过手机号查找用户
     * @param telephone
     * @return
     */
    User findUserByPhoneNumber(String telephone);

    /**
     * 通过id修改用户名
     * @param id
     * @param name
     */
    @Modifying
    @Query("update user set name = :name where id = :id")
    void updateName(@Param("id") Integer id, @Param("name") String name);

    /**
     * 通过Id修改邮箱
     * @param id
     * @param email
     */
    @Modifying
    @Query("update user set email = :email where id = :id")
    void updateEmail(@Param("id")Integer id, @Param("email") String email);

    /***
     * 通过id修改密码
     * @param id
     * @param password
     */
    @Modifying
    @Query("update user set password = :password where id = :id")
    void updatePassword(@Param("id")Integer id, @Param("password") String password);
}
