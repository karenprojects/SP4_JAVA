package com.example.sp4java.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "TB_CONSULTA")
@Entity(name = "TB_CONSULTA")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id_consulta;

    private String data_hora_consulta;

    private String especialidade;
}
