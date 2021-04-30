package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseTagRepository extends JpaRepository<HouseTag, Integer> {
    /***
     * 查找标签
     * @param id
     * @return
     */
    List<HouseTag> findAllByHouseId(Integer id);

    /***
     * 删除标签
     * @param houseId
     */
    void deleteByHouseId(Integer houseId);

    /**
     * 通过名称与房源id查找tag
     * @param name
     * @param houseId
     * @return
     */
    HouseTag findByNameAndHouseId(String name, Integer houseId);

    /**
     * 根据名称与房源id删除
     * @param name
     * @param houseId
     */
    void deleteAllByNameAndHouseId(String name, Integer houseId);
}
