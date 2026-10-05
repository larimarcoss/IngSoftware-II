package dominio;

import java.time.LocalDateTime;

public class Notificacion {
    private final String codigoNotificacion;
    private final TipoNotificacion tipo;
    private final String usuarioNotificado;
    private final LocalDateTime fechaEnvio;
    private final MedioEnvio medioEnvio;
    private final String mensaje;

    public Notificacion(String codigoNotificacion, TipoNotificacion tipo, String usuarioNotificado,
                        MedioEnvio medioEnvio, String mensaje) {
        this.codigoNotificacion = codigoNotificacion;
        this.tipo = tipo;
        this.usuarioNotificado = usuarioNotificado;
        this.medioEnvio = medioEnvio;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
    }

    public String getCodigoNotificacion() { return codigoNotificacion; }
    public TipoNotificacion getTipo() { return tipo; }
    public String getUsuarioNotificado() { return usuarioNotificado; }
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public MedioEnvio getMedioEnvio() { return medioEnvio; }
    public String getMensaje() { return mensaje; }

    @Override
    public String toString() {
        return "[" + tipo + "] a " + usuarioNotificado + " por " + medioEnvio + ": " + mensaje;
    }
}
