package com.example.housesearch.service;

import com.example.housesearch.domain.Role;
import com.example.housesearch.reposity.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : guoweifeng
 * @date : 2021/4/29
 */
@Service
@Slf4j
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;


    public List<Role> findRolesByUserId(Integer id) {
        return roleRepository.findRolesByUserId(id);
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
