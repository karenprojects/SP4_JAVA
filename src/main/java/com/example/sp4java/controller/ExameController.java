package com.example.sp4java.controller;

import com.example.sp4java.domain.Exame;
import com.example.sp4java.domain.ExameDto;
import com.example.sp4java.repository.ExameRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/exames")
public class ExameController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private ExameRepository exameRepository;

    //Tela com todas os exames
    @GetMapping
    public String getAllExames(Model model){
        List<Exame> todosExames = exameRepository.findAll();
        model.addAttribute("exames", todosExames);
        return "exames/index";
    }

    //Tela para marcar um exame
    @GetMapping("/marcar")
    public String addExamePage(Model model){
        ExameDto exameDto = new ExameDto();
        model.addAttribute("exameDto", exameDto);
        return "exames/marcar";
    }

    //Marcar uma novo exame
    @PostMapping("/marcar")
    public String addExame(
            @Valid @ModelAttribute("exameDto") ExameDto exameDto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "exames/marcar";
        }

        Exame exame = new Exame();
        exame.setData_hora_exame(exameDto.getData_hora_exame());
        exame.setTipo(exameDto.getTipo());

        exameRepository.save(exame);

        return "redirect:/exames";
    }

    //Tela para atualizar a data e hora do exame
    @GetMapping("/atualizar")
    public String updateExamePage(Model model, @RequestParam int id_exame) {
        try {

            Exame exame = exameRepository.findById(id_exame)
                    .orElseThrow(() -> new NoSuchElementException("Exame não encontrado"));


            ExameDto exameDto = new ExameDto();
            exameDto.setId_exame(exame.getId_exame());
            exameDto.setData_hora_exame(exame.getData_hora_exame());
            exameDto.setTipo(exame.getTipo());

            model.addAttribute("exameDto", exameDto);
            return "exames/atualizar";
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return "redirect:/exames";
        }
    }

    //Atualizar a data e hora do exame
    @PostMapping("atualizar")
    public String updateExame(
            @Valid @ModelAttribute("exameDto") ExameDto exameDto,
            BindingResult result) {

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError ->
                    logger.error("Erro no campo: {} - Mensagem: {}", fieldError.getField(), fieldError.getDefaultMessage()));
            return "exames/atualizar";
        }

        try {

            Exame exame = exameRepository.findById(exameDto.getId_exame())
                    .orElseThrow(() -> new NoSuchElementException("Exame não encontrado"));

            exame.setData_hora_exame(exameDto.getData_hora_exame());
            exameRepository.save(exame);

            logger.info("Exame atualizado com sucesso para o ID: {}", exameDto.getId_exame());

        } catch (Exception e) {
            logger.error("Erro ao atualizar o exame com ID: {}", exameDto.getId_exame(), e);
            return "redirect:/exames";
        }

        return "redirect:/exames";
    }

    //Deletar um exame
    @GetMapping("/deletar")
    public String deleteExamePage(@RequestParam int id_exame) {
        try {
            Exame exame = exameRepository.findById(id_exame)
                    .orElseThrow(() -> new NoSuchElementException("Exame não encontrada"));

            exameRepository.delete(exame);

        } catch (Exception e) {
            logger.error("Erro ao deletar o exame com ID: {}", id_exame, e.getMessage());
            return "redirect:/exames";
        }

        return "redirect:/exames";
    }

}
