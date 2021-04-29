package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseSubscribeRepository extends JpaRepository<HouseSubscribe, Integer> {
    /**
     * 通过房源id与订阅用户id获取是否有数据
     * @param houseId
     * @param userId
     * @return
     */
    HouseSubscribe findByHouseIdAndUserId(Integer houseId, Integer userId);
}
