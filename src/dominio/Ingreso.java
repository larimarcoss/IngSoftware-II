package dominio;

public class Ingreso extends MovimientoDeCaja {
    private final String medioDePago;

    public Ingreso(String codigoMovimiento, double monto, String descripcion, String medioDePago) {
        super(codigoMovimiento, monto, descripcion);
        this.medioDePago = medioDePago;
    }

    @Override public double efectoSobreSaldo() { return getMonto(); }
    @Override public String getTipo() { return "INGRESO"; }

    public String getMedioDePago() { return medioDePago; }
}
