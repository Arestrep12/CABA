package com.example.caba.application.report;

import com.example.caba.application.dto.LiquidacionDto;

public interface ReporteGenerador {

    boolean soportaFormato(String formato);

    Reporte generar(LiquidacionDto liquidacion);
}

