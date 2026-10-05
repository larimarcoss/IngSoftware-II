package repositorio;

import dominio.Promocion;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioPromocionesMemoria implements RepositorioPromociones {
    private final Map<String, Promocion> datos = new LinkedHashMap<>();

    @Override
    public void guardar(Promocion promocion) {
        datos.put(promocion.getCodigoPromocion(), promocion);
    }

    @Override
    public Optional<Promocion> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Promocion> listarTodas() {
        return new ArrayList<>(datos.values());
    }
}
