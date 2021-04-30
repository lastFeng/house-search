package com.example.housesearch.reposity;

import com.example.housesearch.domain.HouseSubscribe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /***
     * 更新订阅状态
     * @param id
     * @param subscribeStatusFinish
     */
    @Modifying
    @Query("update house_subscribe set status = :status where id = :id")
    void updateStatus(@Param("id") Integer id,@Param("status") Integer subscribeStatusFinish);

    /**
     * 通过useId， status来查找所有房屋订阅
     * @param userId
     * @param status
     * @param pageable
     * @return
     */
    Page<HouseSubscribe> findAllByUserIdAndStatus(Integer userId, Integer status, Pageable pageable);

    /***
     * 通过管理员id与状态来查找房源订阅信息
     * @param adminId
     * @param status
     * @param pageable
     * @return
     */
    Page<HouseSubscribe> findAllByAdminIdAndStatus(Integer adminId, Integer status, Pageable pageable);
}
