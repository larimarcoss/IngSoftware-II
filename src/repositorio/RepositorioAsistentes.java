package repositorio;

import dominio.Asistente;

import java.util.List;
import java.util.Optional;

public interface RepositorioAsistentes {
    void guardar(Asistente asistente);
    Optional<Asistente> buscarPorCodigo(String codigo);
    List<Asistente> buscarPorNombre(String textoBusqueda);
    List<Asistente> listarTodos();
}
