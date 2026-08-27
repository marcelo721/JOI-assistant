package com.marcelo271.neva_back_end.JOI.repository;


import com.marcelo271.neva_back_end.JOI.entities.JOI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JOIRepository extends JpaRepository<JOI, Long> {
}
