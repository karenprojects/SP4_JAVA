package com.example.sp3java.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "TB_EXAMES")
@Entity(name = "TB_EXAMES")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exame {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id_exame;

    private String data_hora_exame;

    private String tipo;
}
