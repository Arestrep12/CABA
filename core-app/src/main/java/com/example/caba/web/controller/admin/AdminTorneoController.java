package com.example.caba.web.controller.admin;

import com.example.caba.application.dto.PeriodoDto;
import com.example.caba.application.dto.TarifaDto;
import com.example.caba.application.dto.TorneoRequest;
import com.example.caba.application.service.TorneoService;
import com.example.caba.domain.shared.enums.Escalafon;
import com.example.caba.domain.shared.enums.RolAsignacion;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
@RequestMapping("/admin/torneos")
@RequiredArgsConstructor
public class AdminTorneoController {

    private final TorneoService torneoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("torneos", torneoService.listarTorneos());
        model.addAttribute("form", emptyForm());
        return "admin/torneos/list";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("form") TorneoRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("torneos", torneoService.listarTorneos());
            return "admin/torneos/list";
        }
        torneoService.crearTorneo(request);
        return "redirect:/admin/torneos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable UUID id) {
        torneoService.eliminarTorneo(id);
        return "redirect:/admin/torneos";
    }

    @ModelAttribute("escalafones")
    public Escalafon[] escalafones() {
        return Escalafon.values();
    }

    @ModelAttribute("rolesAsignacion")
    public RolAsignacion[] rolesAsignacion() {
        return RolAsignacion.values();
    }

    private TorneoRequest emptyForm() {
        PeriodoDto periodo = new PeriodoDto(LocalDate.now(), LocalDate.now().plusMonths(1));
        List<TarifaDto> tarifas = new java.util.ArrayList<>(
                List.of(new TarifaDto(Escalafon.PRIMERA, RolAsignacion.PRIMER_ARBITRO, BigDecimal.ZERO)));
        return new TorneoRequest("", "", periodo, tarifas);
    }
}

