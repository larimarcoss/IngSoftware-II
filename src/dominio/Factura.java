package dominio;

import java.time.LocalDate;

public class Factura {
    private final String codigoFactura;
    private final String tipo;
    private final LocalDate fecha;
    private final double importeTotal;
    private double importeFinal;
    private EstadoPago estado;

    public Factura(String codigoFactura, String tipo, double importeTotal) {
        if (importeTotal < 0) throw new IllegalArgumentException("El importe no puede ser negativo");
        this.codigoFactura = codigoFactura;
        this.tipo = tipo;
        this.fecha = LocalDate.now();
        this.importeTotal = importeTotal;
        this.importeFinal = importeTotal;
        this.estado = EstadoPago.PENDIENTE;
    }

    public void aplicarDescuento(double descuento) {
        this.importeFinal = Math.max(0, importeTotal - descuento);
    }

    public void marcarPagada() { this.estado = EstadoPago.PAGADO; }
    public void marcarReembolsada() { this.estado = EstadoPago.REEMBOLSADO; }

    public String getCodigoFactura() { return codigoFactura; }
    public String getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public double getImporteTotal() { return importeTotal; }
    public double getImporteFinal() { return importeFinal; }
    public EstadoPago getEstado() { return estado; }
}
