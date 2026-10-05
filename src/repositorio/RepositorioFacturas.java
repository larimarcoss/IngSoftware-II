package repositorio;

import dominio.Factura;

import java.util.List;
import java.util.Optional;

public interface RepositorioFacturas {
    void guardar(Factura factura);
    Optional<Factura> buscarPorCodigo(String codigo);
    List<Factura> listarTodas();
}
