package dominio;

public class Egreso extends MovimientoDeCaja {
    private final String motivo;

    public Egreso(String codigoMovimiento, double monto, String descripcion, String motivo) {
        super(codigoMovimiento, monto, descripcion);
        this.motivo = motivo;
    }

    @Override public double efectoSobreSaldo() { return -getMonto(); }
    @Override public String getTipo() { return "EGRESO"; }

    public String getMotivo() { return motivo; }
}
