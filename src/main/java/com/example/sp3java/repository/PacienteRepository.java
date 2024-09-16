package com.example.sp3java.repository;

import com.example.sp3java.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, String> {

}
