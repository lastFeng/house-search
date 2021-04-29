package com.example.housesearch.service;

import com.example.housesearch.domain.Role;
import com.example.housesearch.domain.User;
import com.example.housesearch.domain.base.ServiceResult;
import com.example.housesearch.domain.dto.UserDTO;
import com.example.housesearch.reposity.UserRepository;
import com.example.housesearch.utils.LoginUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

import static com.example.housesearch.utils.Contant.AUTHORITY_PREFIX;
import static com.example.housesearch.utils.Contant.USER_ROLE_NAME;

/**
 * @author : guoweifeng
 * @date : 2021/4/28
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /***
     * 通过手机号查找用户
     * @param telephone
     * @return
     */
    public User findUserByTelephone(String telephone) {
        User user = userRepository.findUserByTelephone(telephone);
        return getUser(user);
    }

    /***
     * 通过id查找用户
     */
    public ServiceResult<UserDTO> findById(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (Objects.isNull(user)) {
            return ServiceResult.<UserDTO>builder().success(false).message("用户未注册").build();
        }
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ServiceResult.<UserDTO>builder().success(true).result(userDTO).build();
    }

    /***
     * 使用手机号注册用户
     * @param telephone
     * @return
     */
    @Transactional
    public User addUserByPhone(String telephone) {
        User user = new User();
        user.setPhoneNumber(telephone);
        user.setName(telephone.substring(0, 3) + "****" + telephone.substring(7));
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setLastUpdateTime(now);
        user.setPassword(bCryptPasswordEncoder.encode("123456"));
        user = userRepository.save(user);

        Role role = new Role();
        role.setUserId(user.getId());
        role.setName(USER_ROLE_NAME);
        roleService.save(role);
        user.setAuthorityList(Lists.newArrayList(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role.getName())));
        return user;
    }

    /***
     * 通过用户名查询
     * @param username
     * @return
     */
    public User findUserByName(String username) {
        User user = userRepository.findByName(username);
        return getUser(user);
    }

    /***
     * 设置权限
     * @param user
     * @return
     */
    private User getUser(User user) {
        if (Objects.isNull(user)) {
            return user;
        }

        List<Role> roles = roleService.findRolesByUserId(user.getId());
        if (CollectionUtils.isEmpty(roles)) {
            throw new DisabledException("权限非法!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }

    /***
     * 修改用户属性，名称、邮箱、密码，其他不能修改
     */
    @Transactional
    public ServiceResult modifyUserProfile(String profile, String value) {
        Integer userId = LoginUtils.getLoginUserId();
        if (Strings.isBlank(profile)) {
            return ServiceResult.builder().success(false).message("属性不能为空").build();
        }
        switch (profile) {
            case "name":
                userRepository.updateNameById(userId, value);
                break;
            case "email":
                userRepository.updateEmailById(userId, value);
                break;
            case "password":
                userRepository.updatePasswordById(userId, bCryptPasswordEncoder.encode(value));
                break;
            default:
                return ServiceResult.builder().success(false).message("不支持的属性").build();
        }
        return ServiceResult.builder().success(true).build();
    }
}
