package dominio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    private final String codigoReserva;
    private final String codigoSolicitud;      // vínculo con SolicitudEvento (otro grupo)
    private final LocalDate fechaEvento;
    private EstadoReserva estado;
    private final LocalDate fechaInscripcion;
    private LocalDate fechaCancelacion;
    private String motivoCancelacion;
    private double importeAbonado;
    private String observaciones;

    private final List<Asistente> asistentes = new ArrayList<>();            // composición: los asistentes de ESTA reserva
    private Promocion promocion;                                             // agregación
    private Factura factura;                                                 // composición
    private final List<Notificacion> notificaciones = new ArrayList<>();     // composición

    public Reserva(String codigoReserva, String codigoSolicitud, LocalDate fechaEvento, String observaciones) {
        this.codigoReserva = codigoReserva;
        this.codigoSolicitud = codigoSolicitud;
        this.fechaEvento = fechaEvento;
        this.observaciones = observaciones;
        this.estado = EstadoReserva.PENDIENTE_DE_PAGO;
        this.fechaInscripcion = LocalDate.now();
    }

    public void agregarAsistente(Asistente asistente) {
        if (!estaActiva()) {
            throw new IllegalStateException("No se pueden agregar asistentes a una reserva cancelada");
        }
        if (!asistente.estaActivo()) {
            throw new IllegalStateException("No se puede agregar un asistente dado de baja");
        }
        boolean yaEsta = asistentes.stream()
                .anyMatch(a -> a.getCodigoAsistente().equals(asistente.getCodigoAsistente()));
        if (yaEsta) {
            throw new IllegalStateException("El asistente ya está agregado a esta reserva");
        }
        asistentes.add(asistente);
    }

    public void quitarAsistente(String codigoAsistente) {
        if (!estaActiva()) {
            throw new IllegalStateException("No se pueden quitar asistentes de una reserva cancelada");
        }
        boolean eliminado = asistentes.removeIf(a -> a.getCodigoAsistente().equals(codigoAsistente));
        if (!eliminado) {
            throw new IllegalArgumentException("Ese asistente no está en esta reserva");
        }
    }

    public void confirmar() {
        if (estado != EstadoReserva.PENDIENTE_DE_PAGO) {
            throw new IllegalStateException("Solo se puede confirmar una reserva pendiente de pago");
        }
        if (asistentes.isEmpty()) {
            throw new IllegalStateException("La reserva no tiene asistentes cargados");
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

    public void modificarObservaciones(String nuevasObservaciones) {
        if (!estaActiva()) {
            throw new IllegalStateException("No se puede modificar una reserva cancelada");
        }
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
    public String getCodigoSolicitud() { return codigoSolicitud; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public EstadoReserva getEstado() { return estado; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public LocalDate getFechaCancelacion() { return fechaCancelacion; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public double getImporteAbonado() { return importeAbonado; }
    public String getObservaciones() { return observaciones; }
    public int getCantidadCupos() { return asistentes.size(); }
    public List<Asistente> getAsistentes() { return Collections.unmodifiableList(asistentes); }
    public Promocion getPromocion() { return promocion; }
    public Factura getFactura() { return factura; }
    public List<Notificacion> getNotificaciones() { return Collections.unmodifiableList(notificaciones); }

    @Override
    public String toString() {
        return codigoReserva + " | " + estado + " | asistentes: " + asistentes.size();
    }
}
