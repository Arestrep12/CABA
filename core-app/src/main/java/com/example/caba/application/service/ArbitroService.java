package com.example.caba.application.service;

import com.example.caba.application.dto.ArbitroDto;
import com.example.caba.application.dto.ArbitroRequest;
import com.example.caba.application.mapper.ArbitroMapper;
import com.example.caba.application.mapper.SharedMapper;
import com.example.caba.domain.arbitro.Arbitro;
import com.example.caba.domain.shared.exception.BusinessRuleException;
import com.example.caba.domain.shared.exception.NotFoundException;
import com.example.caba.domain.shared.valueobject.Documento;
import com.example.caba.domain.shared.valueobject.Email;
import com.example.caba.infrastructure.repository.ArbitroRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArbitroService {

    private final ArbitroRepository arbitroRepository;
    private final ArbitroMapper arbitroMapper;
    private final SharedMapper sharedMapper;

    @Transactional
    public ArbitroDto registrarArbitro(@Valid ArbitroRequest request) {
        validarEmailDisponible(request.email(), null);
        validarDocumentoDisponible(request.numeroDocumento(), null);

        Arbitro arbitro = arbitroMapper.toEntity(request);
        arbitro.setFechaIngreso(request.fechaIngreso());
        Arbitro saved = arbitroRepository.save(arbitro);
        return arbitroMapper.toDto(saved);
    }

    @Transactional
    public ArbitroDto actualizarArbitro(UUID arbitroId, @Valid ArbitroRequest request) {
        Arbitro arbitro = obtenerEntidad(arbitroId);

        validarEmailDisponible(request.email(), arbitroId);
        validarDocumentoDisponible(request.numeroDocumento(), arbitroId);

        arbitro.setDocumento(Documento.of(request.tipoDocumento(), request.numeroDocumento()));
        arbitro.actualizarEmail(Email.of(request.email()));

        Set<com.example.caba.domain.shared.valueobject.Disponibilidad> disponibilidades =
                sharedMapper.toDisponibilidades(request.disponibilidades());
        arbitro.actualizarDatos(
                request.nombres(),
                request.apellidos(),
                request.escalafon(),
                request.especialidad(),
                request.fotoUrl(),
                disponibilidades);
        arbitro.setFechaIngreso(request.fechaIngreso());

        return arbitroMapper.toDto(arbitro);
    }

    @Transactional
    public void desactivar(UUID arbitroId) {
        Arbitro arbitro = obtenerEntidad(arbitroId);
        arbitro.desactivar();
    }

    @Transactional
    public void reactivar(UUID arbitroId) {
        Arbitro arbitro = obtenerEntidad(arbitroId);
        arbitro.reactivar();
    }

    @Transactional(readOnly = true)
    public List<ArbitroDto> listarActivos() {
        return arbitroRepository.findAll().stream()
                .filter(Arbitro::isActivo)
                .map(arbitroMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Arbitro obtenerEntidad(UUID arbitroId) {
        return arbitroRepository
                .findById(arbitroId)
                .orElseThrow(() -> new NotFoundException("Árbitro no encontrado"));
    }

    private void validarEmailDisponible(String email, UUID arbitroId) {
        arbitroRepository
                .findByEmailValue(email)
                .filter(arbitro -> arbitroId == null || !arbitro.getId().equals(arbitroId))
                .ifPresent(arbitro -> {
                    throw new BusinessRuleException("Ya existe un árbitro con el email proporcionado");
                });
    }

    private void validarDocumentoDisponible(String documento, UUID arbitroId) {
        arbitroRepository
                .findByDocumentoNumero(documento)
                .filter(arbitro -> arbitroId == null || !arbitro.getId().equals(arbitroId))
                .ifPresent(arbitro -> {
                    throw new BusinessRuleException("Ya existe un árbitro con el documento proporcionado");
                });
    }

    @Transactional(readOnly = true)
    public ArbitroDto obtenerArbitro(UUID arbitroId) {
        return arbitroMapper.toDto(obtenerEntidad(arbitroId));
    }
}

