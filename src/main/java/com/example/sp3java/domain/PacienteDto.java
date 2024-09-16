package com.example.sp3java.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDto {

    @NotEmpty(message = "CPF não pode ser vazio")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotEmpty(message = "Nome não pode ser vazio")
    private String nome_completo;

    @NotEmpty(message = "Data de nascimento não pode ser vazia")
    private String data_nasc;

    @NotEmpty(message = "Endereço não pode ser vazio")
    private String end_paciente;

    @NotEmpty(message = "Telefone não pode ser vazio")
    private String tel_paciente;

    @NotEmpty(message = "Email não pode ser vazio")
    @Email(message = "Email deve ser válido")
    private String email_paciente;

    @NotEmpty(message = "Senha não pode ser vazia")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    private String senha;
}
