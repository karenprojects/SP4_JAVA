package com.example.sp4java.controller;

import com.example.sp4java.domain.Consulta;
import com.example.sp4java.domain.ConsultaDto;
import com.example.sp4java.repository.ConsultaRepository;
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

@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private ConsultaRepository consultaRepository;

    //Tela com todas as consultas
    @GetMapping
    public String getAllConsulas(Model model){
        List<Consulta> todasConsultas = consultaRepository.findAll();
        model.addAttribute("consultas", todasConsultas);
        return "consultas/index";
    }

    //Tela para marcar uma consulta
    @GetMapping("/marcar")
    public String addConsultaPage(Model model){
        ConsultaDto consultaDto = new ConsultaDto( );
        model.addAttribute("consultaDto", consultaDto);
        return "consultas/marcar";
    }

    //Marcar uma nova consulta
    @PostMapping("/marcar")
    public String addConsulta(
            @Valid @ModelAttribute("consultaDto") ConsultaDto consultaDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "consultas/marcar";
        }

        Consulta consulta = new Consulta();
        consulta.setData_hora_consulta(consultaDto.getData_hora_consulta());
        consulta.setEspecialidade(consultaDto.getEspecialidade());

        consultaRepository.save(consulta);

        return "redirect:/consultas";
    }

    //Tela para atualizar a data e hora de uma consulta
    @GetMapping("/atualizar")
    public String updateConsultaPage(Model model, @RequestParam int id_consulta) {
        try {

            Consulta consulta = consultaRepository.findById(id_consulta)
                    .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));


            ConsultaDto consultaDto = new ConsultaDto();
            consultaDto.setId_consulta(consulta.getId_consulta());
            consultaDto.setData_hora_consulta(consulta.getData_hora_consulta());
            consultaDto.setEspecialidade(consulta.getEspecialidade());

            model.addAttribute("consultaDto", consultaDto);
            return "consultas/atualizar";
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return "redirect:/consultas";
        }
    }

    //Atualiza a data e hora de uma consulta
    @PostMapping("atualizar")
    public String updateConsulta(
            @Valid @ModelAttribute("consultaDto") ConsultaDto consultaDto,
            BindingResult result) {

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError ->
                    logger.error("Erro no campo: {} - Mensagem: {}", fieldError.getField(), fieldError.getDefaultMessage()));
            return "consultas/atualizar";
        }

        try {

            Consulta consulta = consultaRepository.findById(consultaDto.getId_consulta())
                    .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));

            consulta.setData_hora_consulta(consultaDto.getData_hora_consulta());
            consultaRepository.save(consulta);

            logger.info("Consulta atualizada com sucesso para o ID: {}", consultaDto.getId_consulta());

        } catch (Exception e) {
            logger.error("Erro ao atualizar consulta com ID: {}", consultaDto.getId_consulta(), e);
            return "redirect:/consultas";
        }

        return "redirect:/consultas";
    }

    //Deletar uma consulta
    @GetMapping("/deletar")
    public String deleteConsultaPage(@RequestParam int id_consulta) {
        try {
            Consulta consulta = consultaRepository.findById(id_consulta)
                    .orElseThrow(() -> new NoSuchElementException("Consulta não encontrada"));

            consultaRepository.delete(consulta);

        } catch (Exception e) {
            logger.error("Erro ao deletar a consulta com ID: {}", id_consulta, e.getMessage());
            return "redirect:/consultas";
        }

        return "redirect:/consultas";
    }

}
