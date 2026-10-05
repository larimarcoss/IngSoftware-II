package repositorio;

import dominio.Reserva;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioReservasMemoria implements RepositorioReservas {
    private final Map<String, Reserva> datos = new LinkedHashMap<>();

    @Override
    public void guardar(Reserva reserva) {
        datos.put(reserva.getCodigoReserva(), reserva);
    }

    @Override
    public Optional<Reserva> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Reserva> listarTodas() {
        return new ArrayList<>(datos.values());
    }

    @Override
    public List<Reserva> listarPorAsistente(String codigoAsistente) {
        return datos.values().stream()
                .filter(r -> r.getAsistentes().stream()
                        .anyMatch(a -> a.getCodigoAsistente().equals(codigoAsistente)))
                .collect(Collectors.toList());
    }
}
