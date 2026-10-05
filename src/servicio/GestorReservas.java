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

    // Crea la reserva "vacía", asociada a una solicitud del Grupo 1
    public Reserva crear(String codigoSolicitud, LocalDate fechaEvento, String observaciones) {
        Reserva reserva = new Reserva(GeneradorCodigo.siguiente("RES"), codigoSolicitud, fechaEvento, observaciones);
        repositorio.guardar(reserva);
        return reserva;
    }

    // CU04: Inscribir asistente a evento (dentro de una reserva ya creada)
    public void agregarAsistente(String codigoReserva, String codigoAsistente) {
        Reserva reserva = obtener(codigoReserva);
        Asistente asistente = gestorAsistentes.obtener(codigoAsistente);
        reserva.agregarAsistente(asistente);
    }

    public void quitarAsistente(String codigoReserva, String codigoAsistente) {
        Reserva reserva = obtener(codigoReserva);
        reserva.quitarAsistente(codigoAsistente);
    }

    // CU05: Modificar reserva (las observaciones; los cupos se derivan de los asistentes cargados)
    public void modificarObservaciones(String codigoReserva, String nuevasObservaciones) {
        obtener(codigoReserva).modificarObservaciones(nuevasObservaciones);
    }

    // CU06: Cancelar reserva
    public void cancelar(String codigoReserva, String motivo) {
        obtener(codigoReserva).cancelar(motivo);
    }

    public Reserva obtener(String codigoReserva) {
        return repositorio.buscarPorCodigo(codigoReserva)
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva con código " + codigoReserva));
    }

    // CU17: Consultar estado de reservas
    public List<Reserva> listarTodas() {
        return repositorio.listarTodas();
    }
}
