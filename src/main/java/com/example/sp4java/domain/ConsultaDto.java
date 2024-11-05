package com.example.sp4java.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConsultaDto {

    private int id_consulta;

    @NotEmpty
    private String data_hora_consulta;

    @NotEmpty
    private String especialidade;
}
