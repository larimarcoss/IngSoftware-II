package dominio;

import java.time.LocalDate;

public class Asistente {
    private final String codigoAsistente;
    private String nombreApellido;
    private String email;
    private String telefono;
    private EstadoAsistente estado;
    private LocalDate fechaBaja;
    private String motivoBaja;

    public Asistente(String codigoAsistente, String nombreApellido, String email, String telefono) {
        this.codigoAsistente = codigoAsistente;
        this.nombreApellido = nombreApellido;
        this.email = email;
        this.telefono = telefono;
        this.estado = EstadoAsistente.ACTIVO;
    }

    public void actualizarDatos(String nombreApellido, String email, String telefono) {
        this.nombreApellido = nombreApellido;
        this.email = email;
        this.telefono = telefono;
    }

    public void darDeBaja(String motivo) {
        if (estado == EstadoAsistente.INACTIVO) {
            throw new IllegalStateException("El asistente ya está dado de baja");
        }
        this.estado = EstadoAsistente.INACTIVO;
        this.fechaBaja = LocalDate.now();
        this.motivoBaja = motivo;
    }

    public boolean estaActivo() { return estado == EstadoAsistente.ACTIVO; }

    public String getCodigoAsistente() { return codigoAsistente; }
    public String getNombreApellido() { return nombreApellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public EstadoAsistente getEstado() { return estado; }
    public LocalDate getFechaBaja() { return fechaBaja; }
    public String getMotivoBaja() { return motivoBaja; }

    @Override
    public String toString() {
        return codigoAsistente + " - " + nombreApellido + " (" + estado + ")";
    }
}
