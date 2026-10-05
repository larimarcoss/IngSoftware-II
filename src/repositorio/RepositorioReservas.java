package repositorio;

import dominio.Reserva;

import java.util.List;
import java.util.Optional;

public interface RepositorioReservas {
    void guardar(Reserva reserva);
    Optional<Reserva> buscarPorCodigo(String codigo);
    List<Reserva> listarTodas();
    List<Reserva> listarPorAsistente(String codigoAsistente);
}
