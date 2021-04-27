package com.example.housesearch.reposity;

import com.example.housesearch.domain.SupportAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupportAddressRepository extends JpaRepository<SupportAddress, Integer> {
}
