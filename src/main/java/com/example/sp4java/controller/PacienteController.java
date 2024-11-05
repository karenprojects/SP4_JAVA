package com.example.sp4java.controller;

import com.example.sp4java.domain.Paciente;
import com.example.sp4java.domain.PacienteDto;
import com.example.sp4java.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {
    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private PacienteRepository pacienteRepository;

    //Tela com todos os pacientes
    @GetMapping
    public String getAllPacientes(Model model){
        List<Paciente> todosPacientes = pacienteRepository.findAll();
        model.addAttribute("pacientes", todosPacientes);
        return "pacientes/index";
    }

    @GetMapping("/homeUser")
    public String homeUserPage() {
        return "pacientes/homeUser";
    }


    //Tela para cadastrar um novo paciente
    @GetMapping("/cadastrar")
    public String addPacientePage(Model model){
        PacienteDto pacienteDto = new PacienteDto();
        model.addAttribute("pacienteDto", pacienteDto);
        return "pacientes/cadastrar";
    }

    //Cadastrar um novo paciente
    @PostMapping("/cadastrar")
    public String addPaciente(
            @Valid @ModelAttribute("pacienteDto") PacienteDto pacienteDto,
            BindingResult result
            ) {
        if (result.hasErrors()) {
            return "pacientes/cadastrar";
        }

        Paciente paciente = new Paciente();
        paciente.setCpf(pacienteDto.getCpf());
        paciente.setNome_completo(pacienteDto.getNome_completo());
        paciente.setData_nasc(pacienteDto.getData_nasc());
        paciente.setEnd_paciente(pacienteDto.getEnd_paciente());
        paciente.setTel_paciente(pacienteDto.getTel_paciente());
        paciente.setEmail_paciente(pacienteDto.getEmail_paciente());
        paciente.setSenha(pacienteDto.getSenha());

        pacienteRepository.save(paciente);

        return "redirect:/pacientes/login";
    }

    // Tela para atualizar a senha do paciente
    @GetMapping("atualizar")
    public String updatePacientePage(
            Model model, @RequestParam String cpf) {
        try {
            Paciente paciente = pacienteRepository.findById(cpf).orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));
            PacienteDto pacienteDto = new PacienteDto();

            pacienteDto.setCpf(paciente.getCpf());
            pacienteDto.setNome_completo(paciente.getNome_completo());
            pacienteDto.setData_nasc(paciente.getData_nasc());
            pacienteDto.setEnd_paciente(paciente.getEnd_paciente());
            pacienteDto.setTel_paciente(paciente.getTel_paciente());
            pacienteDto.setEmail_paciente(paciente.getEmail_paciente());
            pacienteDto.setSenha(paciente.getSenha());

            model.addAttribute("pacienteDto", pacienteDto);

        } catch (Exception e) {
            logger.error("Erro ao carregar a página de atualização do paciente com CPF {}: {}", cpf, e.getMessage());
            return "redirect:/pacientes";
        }

        return "pacientes/atualizar";
    }


    // Atualizar a senha do paciente
    @PostMapping("atualizar")
    public String updatePaciente(
            @Valid @ModelAttribute("pacienteDto") PacienteDto pacienteDto,
                                 BindingResult result) {

        logger.info("Tentando atualizar a senha do paciente com CPF: {}", pacienteDto.getCpf());

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError ->
                    logger.error("Erro no campo: {} - Mensagem: {}", fieldError.getField(), fieldError.getDefaultMessage()));
            return "pacientes/atualizar";
        }

        try {
            Paciente paciente = pacienteRepository.findById(pacienteDto.getCpf())
                    .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));

            logger.info("Senha antiga: {}", paciente.getSenha());
            paciente.setSenha(pacienteDto.getSenha());
            logger.info("Nova senha: {}", paciente.getSenha());

            pacienteRepository.save(paciente);

            logger.info("Senha atualizada com sucesso para o paciente com CPF: {}", pacienteDto.getCpf());

        } catch (Exception e) {
            logger.error("Erro ao atualizar senha do paciente com CPF: {}", pacienteDto.getCpf(), e);
            return "redirect:/pacientes";
        }

        return "redirect:/pacientes";
    }


    // Deletar um paciente
    @GetMapping("deletar")
    public String deletePacientePage(
            @RequestParam String cpf) {
        try {
            Paciente paciente = pacienteRepository.findById(cpf).orElseThrow(() -> new NoSuchElementException("Paciente não encontrado"));

            pacienteRepository.delete(paciente);

        } catch (Exception e) {
            logger.error("Erro ao carregar a página de deletar o paciente com CPF {}: {}", cpf, e.getMessage());
            return "redirect:/pacientes";
        }

        return "redirect:/pacientes";
    }

    // Tela de login
    @GetMapping("/login")
    public String loginPage(Model model) {
        PacienteDto pacienteDto = new PacienteDto();
        model.addAttribute("pacienteDto", pacienteDto);
        return "pacientes/login";  // Nome da página de login
    }

    // Realizar o login
    @PostMapping("/login")
    public String loginPaciente(@ModelAttribute("pacienteDto") PacienteDto pacienteDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "pacientes/login";
        }

        try {
            Optional<Paciente> optionalPaciente = pacienteRepository.findById(pacienteDto.getCpf());

            if (optionalPaciente.isPresent()) {
                Paciente paciente = optionalPaciente.get();

                if (paciente.getSenha().equals(pacienteDto.getSenha())) {
                    return "redirect:/pacientes/homeUser";
                } else {
                    model.addAttribute("error", "CPF ou senha inválidos");
                    return "pacientes/login";
                }
            } else {
                model.addAttribute("error", "CPF ou senha inválidos");
                return "pacientes/login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Ocorreu um erro durante o login");
            return "pacientes/login";
        }
    }
}
