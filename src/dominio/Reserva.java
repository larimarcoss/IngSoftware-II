package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    private final String codigoReserva;
    private final Asistente asistente;
    private final String codigoSolicitud;      // vínculo con SolicitudEvento (otro grupo)
    private final LocalDate fechaEvento;
    private EstadoReserva estado;
    private final LocalDate fechaInscripcion;
    private LocalDate fechaCancelacion;
    private String motivoCancelacion;
    private double importeAbonado;
    private int cantidadCupos;
    private String observaciones;

    private Promocion promocion;                                          // agregación
    private Factura factura;                                              // composición
    private final List<Notificacion> notificaciones = new ArrayList<>();  // composición

    public Reserva(String codigoReserva, Asistente asistente, String codigoSolicitud,
                   LocalDate fechaEvento, int cantidadCupos, String observaciones) {
        if (!asistente.estaActivo()) {
            throw new IllegalStateException("No se puede inscribir a un asistente dado de baja");
        }
        if (cantidadCupos <= 0) {
            throw new IllegalArgumentException("La cantidad de cupos debe ser mayor a 0");
        }
        this.codigoReserva = codigoReserva;
        this.asistente = asistente;
        this.codigoSolicitud = codigoSolicitud;
        this.fechaEvento = fechaEvento;
        this.cantidadCupos = cantidadCupos;
        this.observaciones = observaciones;
        this.estado = EstadoReserva.PENDIENTE_DE_PAGO;
        this.fechaInscripcion = LocalDate.now();
    }

    public void confirmar() {
        if (estado != EstadoReserva.PENDIENTE_DE_PAGO) {
            throw new IllegalStateException("Solo se puede confirmar una reserva pendiente de pago");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar(String motivo) {
        if (estado == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya está cancelada");
        }
        this.estado = EstadoReserva.CANCELADA;
        this.fechaCancelacion = LocalDate.now();
        this.motivoCancelacion = motivo;
    }

    public void modificar(int nuevaCantidadCupos, String nuevasObservaciones) {
        if (!estaActiva()) {
            throw new IllegalStateException("No se puede modificar una reserva cancelada");
        }
        if (nuevaCantidadCupos <= 0) {
            throw new IllegalArgumentException("La cantidad de cupos debe ser mayor a 0");
        }
        this.cantidadCupos = nuevaCantidadCupos;
        this.observaciones = nuevasObservaciones;
    }

    public void registrarPago(double importe) {
        if (importe <= 0) throw new IllegalArgumentException("El importe debe ser mayor a 0");
        this.importeAbonado += importe;
    }

    public void aplicarPromocion(Promocion promocion) { this.promocion = promocion; }
    public void setFactura(Factura factura) { this.factura = factura; }
    public void agregarNotificacion(Notificacion n) { this.notificaciones.add(n); }

    public boolean estaActiva() { return estado != EstadoReserva.CANCELADA; }

    public String getCodigoReserva() { return codigoReserva; }
    public Asistente getAsistente() { return asistente; }
    public String getCodigoSolicitud() { return codigoSolicitud; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public EstadoReserva getEstado() { return estado; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public LocalDate getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public double getImporteAbonado() { return importeAbonado; }
    public int getCantidadCupos() { return cantidadCupos; }
    public String getObservaciones() { return observaciones; }
    public Promocion getPromocion() { return promocion; }
    public Factura getFactura() { return factura; }
    public List<Notificacion> getNotificaciones() { return Collections.unmodifiableList(notificaciones); }

    @Override
    public String toString() {
        return codigoReserva + " | " + asistente.getNombreApellido() + " | " + estado
                + " | cupos: " + cantidadCupos;
    }
}
