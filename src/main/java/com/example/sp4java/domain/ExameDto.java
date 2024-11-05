package com.example.sp4java.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExameDto {

    private int id_exame;

    @NotEmpty
    private String data_hora_exame;

    @NotEmpty
    private String tipo;
}
