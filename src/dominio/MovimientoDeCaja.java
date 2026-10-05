package dominio;

import java.time.LocalDate;

public abstract class MovimientoDeCaja {
    private final String codigoMovimiento;
    private final LocalDate fecha;
    private final double monto;
    private final String descripcion;
    private Factura factura;   // vínculo con Factura

    protected MovimientoDeCaja(String codigoMovimiento, double monto, String descripcion) {
        if (monto <= 0) throw new IllegalArgumentException("El monto debe ser mayor a 0");
        this.codigoMovimiento = codigoMovimiento;
        this.fecha = LocalDate.now();
        this.monto = monto;
        this.descripcion = descripcion;
    }

    // Cuánto suma (+) o resta (-) al saldo de la caja
    public abstract double efectoSobreSaldo();
    public abstract String getTipo();

    public void setFactura(Factura factura) { this.factura = factura; }

    public String getCodigoMovimiento() { return codigoMovimiento; }
    public LocalDate getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }
    public Factura getFactura() { return factura; }

    @Override
    public String toString() {
        return codigoMovimiento + " | " + getTipo() + " | $" + monto + " | " + descripcion;
    }
}
