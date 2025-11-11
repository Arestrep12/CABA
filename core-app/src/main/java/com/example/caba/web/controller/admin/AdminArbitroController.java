package com.example.caba.web.controller.admin;

import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.service.ArbitroService;
import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.Especialidad;
import com.example.caba.domain.shared.enums.TipoDocumento;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/arbitros")
@RequiredArgsConstructor
public class AdminArbitroController {

    private final ArbitroService arbitroService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("arbitros", arbitroService.listarActivos());
        model.addAttribute(
                "form",
                new ArbitroRequest(
                        "",
                        "",
                        "",
                        TipoDocumento.DNI,
                        "",
                        Especialidad.CAMPO,
                        Escalafon.PRIMERA,
                        null,
                        LocalDate.now(),
                        null));
        return "admin/arbitros/list";
    }

    @PostMapping
    public String crear(
            @Valid @ModelAttribute("form") ArbitroRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("arbitros", arbitroService.listarActivos());
            return "admin/arbitros/list";
        }
        arbitroService.registrarArbitro(request);
        return "redirect:/admin/arbitros";
    }

    @PostMapping("/{id}/desactivar")
    public String desactivar(@PathVariable UUID id) {
        arbitroService.desactivar(id);
        return "redirect:/admin/arbitros";
    }

    @ModelAttribute("escalafones")
    public Escalafon[] escalafones() {
        return Escalafon.values();
    }

    @ModelAttribute("especialidades")
    public Especialidad[] especialidades() {
        return Especialidad.values();
    }

    @ModelAttribute("tiposDocumento")
    public TipoDocumento[] tiposDocumento() {
        return TipoDocumento.values();
    }
}

