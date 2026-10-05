package servicio;

import dominio.Asistente;
import dominio.Reserva;
import repositorio.RepositorioReservas;
import util.GeneradorCodigo;

import java.time.LocalDate;
import java.util.List;

public class GestorReservas {
    private final RepositorioReservas repositorio;
    private final GestorAsistentes gestorAsistentes;

    public GestorReservas(RepositorioReservas repositorio, GestorAsistentes gestorAsistentes) {
        this.repositorio = repositorio;
        this.gestorAsistentes = gestorAsistentes;
    }

    // CU04: Inscribir asistente a evento
    public Reserva inscribir(String codigoAsistente, String codigoSolicitud, LocalDate fechaEvento,
                             int cantidadCupos, String observaciones) {
        Asistente asistente = gestorAsistentes.obtener(codigoAsistente);
        Reserva reserva = new Reserva(GeneradorCodigo.siguiente("RES"), asistente, codigoSolicitud,
                fechaEvento, cantidadCupos, observaciones);
        repositorio.guardar(reserva);
        return reserva;
    }

    // CU05: Modificar reserva
    public void modificar(String codigoReserva, int nuevaCantidadCupos, String nuevasObservaciones) {
        Reserva reserva = obtener(codigoReserva);
        reserva.modificar(nuevaCantidadCupos, nuevasObservaciones);
    }

    // CU06: Cancelar reserva
    public void cancelar(String codigoReserva, String motivo) {
        Reserva reserva = obtener(codigoReserva);
        reserva.cancelar(motivo);
    }

    public Reserva obtener(String codigoReserva) {
        return repositorio.buscarPorCodigo(codigoReserva)
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva con código " + codigoReserva));
    }

    // CU17: Consultar estado de reservas (el filtrado por estado/asistente se hace en la ventana)
    public List<Reserva> listarTodas() {
        return repositorio.listarTodas();
    }
}
