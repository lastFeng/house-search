package com.example.housesearch.reposity;

import com.example.housesearch.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {
    /**
     * 更新房源装填
     * @param id
     * @param status
     */
    @Modifying
    @Query("update house set status = :status where id = :id")
    void updateStatus(@Param("id") Integer id,@Param("status") int status);
}
