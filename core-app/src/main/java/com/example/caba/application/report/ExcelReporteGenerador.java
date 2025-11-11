package com.example.caba.application.report;

import com.example.caba.application.dto.LiquidacionDetalleDto;
import com.example.caba.application.dto.LiquidacionDto;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelReporteGenerador implements ReporteGenerador {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("es", "AR"));

    @Override
    public boolean soportaFormato(String formato) {
        return "xlsx".equalsIgnoreCase(formato) || "excel".equalsIgnoreCase(formato);
    }

    @Override
    public Reporte generar(LiquidacionDto liquidacion) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Liquidacion");

            int rowIndex = 0;
            rowIndex = crearEncabezado(liquidacion, sheet, rowIndex);
            rowIndex = crearTablaDetalles(liquidacion, sheet, rowIndex);

            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return new Reporte(
                    String.format("liquidacion-%s.xlsx", liquidacion.id()),
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                    outputStream.toByteArray());
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo generar el reporte Excel de la liquidación", ex);
        }
    }

    private int crearEncabezado(LiquidacionDto liquidacion, Sheet sheet, int rowIndex) {
        Row titulo = sheet.createRow(rowIndex++);
        titulo.createCell(0).setCellValue("Liquidación #" + liquidacion.id());

        Row estado = sheet.createRow(rowIndex++);
        estado.createCell(0).setCellValue("Estado");
        estado.createCell(1).setCellValue(liquidacion.estado().name());

        if (liquidacion.periodo() != null) {
            Row periodo = sheet.createRow(rowIndex++);
            periodo.createCell(0).setCellValue("Periodo");
            periodo.createCell(1)
                    .setCellValue(String.format(
                            "%s - %s",
                            DATE_FORMATTER.format(liquidacion.periodo().fechaInicio()),
                            DATE_FORMATTER.format(liquidacion.periodo().fechaFin())));
        }

        Row total = sheet.createRow(rowIndex++);
        total.createCell(0).setCellValue("Total");
        total.createCell(1).setCellValue(liquidacion.total().doubleValue());

        return rowIndex;
    }

    private int crearTablaDetalles(LiquidacionDto liquidacion, Sheet sheet, int rowIndex) {
        Row header = sheet.createRow(rowIndex++);
        header.createCell(0).setCellValue("Partido");
        header.createCell(1).setCellValue("Rol");
        header.createCell(2).setCellValue("Fecha");
        header.createCell(3).setCellValue("Monto");

        CellStyle currencyStyle = sheet.getWorkbook().createCellStyle();
        currencyStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat("$#,##0.00"));

        List<LiquidacionDetalleDto> detalles = liquidacion.detalles() != null ? liquidacion.detalles() : List.of();
        for (LiquidacionDetalleDto detalle : detalles) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(detalle.partidoId().toString());
            row.createCell(1).setCellValue(detalle.rol().name());
            row.createCell(2).setCellValue(DATE_FORMATTER.format(detalle.fechaPartido()));
            Cell montoCell = row.createCell(3);
            montoCell.setCellValue(detalle.monto().doubleValue());
            montoCell.setCellStyle(currencyStyle);
        }

        return rowIndex;
    }
}

