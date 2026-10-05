package repositorio;

import dominio.Asistente;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorioAsistentesMemoria implements RepositorioAsistentes {
    private final Map<String, Asistente> datos = new LinkedHashMap<>();

    @Override
    public void guardar(Asistente asistente) {
        datos.put(asistente.getCodigoAsistente(), asistente);
    }

    @Override
    public Optional<Asistente> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Asistente> buscarPorNombre(String textoBusqueda) {
        String texto = textoBusqueda.toLowerCase();
        return datos.values().stream()
                .filter(a -> a.getNombreApellido().toLowerCase().contains(texto))
                .collect(Collectors.toList());
    }

    @Override
    public List<Asistente> listarTodos() {
        return new ArrayList<>(datos.values());
    }
}
