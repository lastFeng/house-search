package com.example.housesearch.service;

import com.example.housesearch.domain.User;
import com.example.housesearch.reposity.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /***
     * 通过手机号查找用户
     * TODO
     * @param telephone
     * @return
     */
    public User findUserByTelephone(String telephone) {
        return null;
    }

    /***
     * 使用手机号注册用户
     * @param telephone
     * @return
     */
    public User addUserByPhone(String telephone) {
        return null;
    }

    /***
     * 通过用户名查询
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        return null;
    }
}
