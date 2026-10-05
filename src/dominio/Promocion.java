package dominio;

import java.time.LocalDate;

public class Promocion {
    private final String codigoPromocion;
    private String nombre;
    private TipoDescuento tipoDescuento;
    private double valorDescuento;
    private LocalDate fechaDesde;
    private LocalDate fechaHasta;
    private String condicionAplicacion;
    private boolean activa;

    public Promocion(String codigoPromocion, String nombre, TipoDescuento tipoDescuento,
                     double valorDescuento, LocalDate fechaDesde, LocalDate fechaHasta,
                     String condicionAplicacion) {
        if (valorDescuento <= 0) throw new IllegalArgumentException("El descuento debe ser mayor a 0");
        if (fechaHasta.isBefore(fechaDesde)) throw new IllegalArgumentException("Rango de fechas inválido");
        this.codigoPromocion = codigoPromocion;
        this.nombre = nombre;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.condicionAplicacion = condicionAplicacion;
        this.activa = true;
    }

    public void modificar(String nombre, TipoDescuento tipoDescuento, double valorDescuento,
                          LocalDate fechaDesde, LocalDate fechaHasta, String condicionAplicacion) {
        if (valorDescuento <= 0) throw new IllegalArgumentException("El descuento debe ser mayor a 0");
        if (fechaHasta.isBefore(fechaDesde)) throw new IllegalArgumentException("Rango de fechas inválido");
        this.nombre = nombre;
        this.tipoDescuento = tipoDescuento;
        this.valorDescuento = valorDescuento;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.condicionAplicacion = condicionAplicacion;
    }

    public void desactivar() { this.activa = false; }

    public boolean estaVigente(LocalDate fecha) {
        return activa && !fecha.isBefore(fechaDesde) && !fecha.isAfter(fechaHasta);
    }

    // Devuelve el monto a descontar (nunca mayor al importe)
    public double calcularDescuento(double importe) {
        double descuento = (tipoDescuento == TipoDescuento.PORCENTAJE)
                ? importe * valorDescuento / 100.0
                : valorDescuento;
        return Math.min(descuento, importe);
    }

    public String getCodigoPromocion() { return codigoPromocion; }
    public String getNombre() { return nombre; }
    public TipoDescuento getTipoDescuento() { return tipoDescuento; }
    public double getValorDescuento() { return valorDescuento; }
    public LocalDate getFechaDesde() { return fechaDesde; }
    public LocalDate getFechaHasta() { return fechaHasta; }
    public String getCondicionAplicacion() { return condicionAplicacion; }
    public boolean isActiva() { return activa; }
}
