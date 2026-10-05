package servicio;

import dominio.Asistente;
import repositorio.RepositorioAsistentes;
import util.GeneradorCodigo;

import java.util.List;

public class GestorAsistentes {
    private final RepositorioAsistentes repositorio;

    public GestorAsistentes(RepositorioAsistentes repositorio) {
        this.repositorio = repositorio;
    }

    // CU01: Registrar asistente
    public Asistente registrar(String nombreApellido, String email, String telefono) {
        boolean yaExiste = repositorio.listarTodos().stream()
                .anyMatch(a -> a.getEmail().equalsIgnoreCase(email));
        if (yaExiste) {
            throw new IllegalStateException("Ya existe un asistente registrado con ese email");
        }
        Asistente asistente = new Asistente(GeneradorCodigo.siguiente("ASI"), nombreApellido, email, telefono);
        repositorio.guardar(asistente);
        return asistente;
    }

    // CU02: Modificar datos del asistente
    public void modificarDatos(String codigo, String nombreApellido, String email, String telefono) {
        Asistente asistente = obtener(codigo);
        asistente.actualizarDatos(nombreApellido, email, telefono);
    }

    // CU03: Dar de baja asistente
    public void darDeBaja(String codigo, String motivo) {
        Asistente asistente = obtener(codigo);
        asistente.darDeBaja(motivo);
    }

    public Asistente obtener(String codigo) {
        return repositorio.buscarPorCodigo(codigo)
                .orElseThrow(() -> new IllegalArgumentException("No existe un asistente con código " + codigo));
    }

    public List<Asistente> listarTodos() {
        return repositorio.listarTodos();
    }
}
