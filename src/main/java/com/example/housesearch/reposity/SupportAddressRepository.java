package com.example.housesearch.reposity;

import com.example.housesearch.domain.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportAddressRepository extends JpaRepository<SupportAddress, Integer> {
    /**
     * 根据名称和级别查找支持的地址
     * @param cityEnName
     * @param level
     * @return
     */
    SupportAddress findByEnNameAndLevel(String cityEnName, String level);

    /**
     * 根据级别查找
     * @param level
     * @return
     */
    List<SupportAddress> findAllByLevel(String level);

    /**
     * 根据名称与属于查找
     * @param enName
     * @param belongTo
     * @return
     */
    SupportAddress findByEnNameAndBelongTo(String enName, String belongTo);

    /**
     * 根据级别与属于查找
     * @param level
     * @param belongTo
     * @return
     */
    List<SupportAddress> findAllByLevelAndBelongTo(String level, String belongTo);
}
