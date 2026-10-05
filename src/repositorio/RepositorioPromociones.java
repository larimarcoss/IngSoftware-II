package repositorio;

import dominio.Promocion;

import java.util.List;
import java.util.Optional;

public interface RepositorioPromociones {
    void guardar(Promocion promocion);
    Optional<Promocion> buscarPorCodigo(String codigo);
    List<Promocion> listarTodas();
}
