package com.example.sp3java.repository;

import com.example.sp3java.domain.Exame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExameRepository extends JpaRepository<Exame,  Integer> {
}
