package com.example.caba.application.service;

import com.example.caba.application.dto.TorneoDto;
import com.example.caba.application.dto.TorneoRequest;
import com.example.caba.application.mapper.SharedMapper;
import com.example.caba.application.mapper.TorneoMapper;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.domain.torneo.Torneo;
import com.example.caba.infrastructure.repository.TorneoRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final TorneoMapper torneoMapper;
    private final SharedMapper sharedMapper;

    @Transactional
    public TorneoDto crearTorneo(@Valid TorneoRequest request) {
        torneoRepository
                .findByNombreIgnoreCase(request.nombre())
                .ifPresent(torneo -> {
                    throw new BusinessRuleException("Ya existe un torneo con ese nombre");
                });

        Torneo torneo = torneoMapper.toEntity(request);
        torneo.getTarifas().clear();
        torneo.getTarifas().addAll(sharedMapper.toTarifas(request.tarifas()));
        torneo.setPeriodo(sharedMapper.toPeriodo(request.periodo()));

        return torneoMapper.toDto(torneoRepository.save(torneo));
    }

    @Transactional
    public TorneoDto actualizarTorneo(UUID torneoId, @Valid TorneoRequest request) {
        Torneo torneo = obtenerEntidad(torneoId);

        torneo.setNombre(request.nombre());
        torneo.setDescripcion(request.descripcion());
        torneo.setPeriodo(sharedMapper.toPeriodo(request.periodo()));
        torneo.getTarifas().clear();
        torneo.getTarifas().addAll(sharedMapper.toTarifas(request.tarifas()));

        return torneoMapper.toDto(torneo);
    }

    @Transactional(readOnly = true)
    public List<TorneoDto> listarTorneos() {
        return torneoMapper.toDtoList(torneoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TorneoDto obtenerTorneo(UUID torneoId) {
        return torneoMapper.toDto(obtenerEntidad(torneoId));
    }

    @Transactional
    public void eliminarTorneo(UUID torneoId) {
        Torneo torneo = obtenerEntidad(torneoId);
        torneoRepository.delete(torneo);
    }

    Torneo obtenerEntidad(UUID torneoId) {
        return torneoRepository
                .findById(torneoId)
                .orElseThrow(() -> new NotFoundException("Torneo no encontrado"));
    }
}

