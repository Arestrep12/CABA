package com.example.caba.application.report;

import com.example.caba.application.dto.LiquidacionDetalleDto;
import com.example.caba.application.dto.LiquidacionDto;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class PdfReporteGenerador implements ReporteGenerador {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(new Locale("es", "AR"));

    @Override
    public boolean soportaFormato(String formato) {
        return "pdf".equalsIgnoreCase(formato);
    }

    @Override
    public Reporte generar(LiquidacionDto liquidacion) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();
            document.add(new Paragraph("Liquidación #" + liquidacion.id()));
            document.add(new Paragraph("Estado: " + liquidacion.estado().name()));
            if (liquidacion.periodo() != null) {
                document.add(new Paragraph(String.format(
                        "Periodo: %s - %s",
                        DATE_FORMATTER.format(liquidacion.periodo().fechaInicio()),
                        DATE_FORMATTER.format(liquidacion.periodo().fechaFin()))));
            }
            document.add(new Paragraph(String.format("Total: $%s", liquidacion.total())));
            document.add(new Paragraph(" "));

            Table table = new Table(4);
            table.addCell("Partido");
            table.addCell("Rol");
            table.addCell("Fecha");
            table.addCell("Monto");

            List<LiquidacionDetalleDto> detalles =
                    liquidacion.detalles() != null ? liquidacion.detalles() : List.<LiquidacionDetalleDto>of();
            for (LiquidacionDetalleDto detalle : detalles) {
                table.addCell(detalle.partidoId().toString());
                table.addCell(detalle.rol().name());
                table.addCell(DATE_FORMATTER.format(detalle.fechaPartido()));
                table.addCell("$" + detalle.monto());
            }

            document.add(table);
            document.close();

            return new Reporte(
                    String.format("liquidacion-%s.pdf", liquidacion.id()),
                    "application/pdf",
                    outputStream.toByteArray());
        } catch (DocumentException | IOException ex) {
            throw new IllegalStateException("No se pudo generar el reporte PDF de la liquidación", ex);
        }
    }
}

