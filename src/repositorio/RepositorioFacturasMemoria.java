package repositorio;

import dominio.Factura;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioFacturasMemoria implements RepositorioFacturas {
    private final Map<String, Factura> datos = new LinkedHashMap<>();

    @Override
    public void guardar(Factura factura) {
        datos.put(factura.getCodigoFactura(), factura);
    }

    @Override
    public Optional<Factura> buscarPorCodigo(String codigo) {
        return Optional.ofNullable(datos.get(codigo));
    }

    @Override
    public List<Factura> listarTodas() {
        return new ArrayList<>(datos.values());
    }
}
