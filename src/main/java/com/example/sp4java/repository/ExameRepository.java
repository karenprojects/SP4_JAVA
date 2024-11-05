package com.example.sp4java.repository;

import com.example.sp4java.domain.Exame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExameRepository extends JpaRepository<Exame,  Integer> {
}
