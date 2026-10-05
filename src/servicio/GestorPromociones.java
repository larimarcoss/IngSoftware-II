package servicio;

import dominio.Promocion;
import dominio.TipoDescuento;
import repositorio.RepositorioPromociones;
import util.GeneradorCodigo;

import java.time.LocalDate;
import java.util.List;

public class GestorPromociones {
    private final RepositorioPromociones repositorio;

    public GestorPromociones(RepositorioPromociones repositorio) {
        this.repositorio = repositorio;
    }

    // CU11: Alta de promoción
    public Promocion crear(String nombre, TipoDescuento tipoDescuento, double valorDescuento,
                           LocalDate fechaDesde, LocalDate fechaHasta, String condicionAplicacion) {
        Promocion promocion = new Promocion(GeneradorCodigo.siguiente("PROMO"), nombre, tipoDescuento,
                valorDescuento, fechaDesde, fechaHasta, condicionAplicacion);
        repositorio.guardar(promocion);
        return promocion;
    }

    // CU11: Modificación
    public void modificar(String codigo, String nombre, TipoDescuento tipoDescuento, double valorDescuento,
                          LocalDate fechaDesde, LocalDate fechaHasta, String condicionAplicacion) {
        Promocion promocion = obtener(codigo);
        promocion.modificar(nombre, tipoDescuento, valorDescuento, fechaDesde, fechaHasta, condicionAplicacion);
    }

    // CU11: Baja
    public void desactivar(String codigo) {
        obtener(codigo).desactivar();
    }

    public Promocion obtener(String codigo) {
        return repositorio.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe una promoción con código " + codigo));
    }

    public List<Promocion> listarTodas() {
        return repositorio.listarTodas();
    }
}
