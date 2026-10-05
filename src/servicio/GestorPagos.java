package servicio;

import dominio.Caja;
import dominio.Factura;
import dominio.Ingreso;
import dominio.Promocion;
import dominio.Reserva;
import repositorio.RepositorioFacturas;
import repositorio.RepositorioPromociones;
import util.GeneradorCodigo;

import java.time.LocalDate;

public class GestorPagos {
    private final GestorReservas gestorReservas;
    private final RepositorioPromociones repositorioPromociones;
    private final RepositorioFacturas repositorioFacturas;
    private final Caja caja;

    public GestorPagos(GestorReservas gestorReservas, RepositorioPromociones repositorioPromociones,
                       RepositorioFacturas repositorioFacturas, Caja caja) {
        this.gestorReservas = gestorReservas;
        this.repositorioPromociones = repositorioPromociones;
        this.repositorioFacturas = repositorioFacturas;
        this.caja = caja;
    }

    // CU09 + CU10: Registrar el pago de una reserva, aplicando una promoción opcional
    public Factura registrarPago(String codigoReserva, double importeTotal, String medioDePago, String codigoPromocion) {
        Reserva reserva = gestorReservas.obtener(codigoReserva);
        if (!reserva.estaActiva()) {
            throw new IllegalStateException("No se puede pagar una reserva cancelada");
        }

        Factura factura = new Factura(GeneradorCodigo.siguiente("FAC"), "Factura B", importeTotal);

        if (codigoPromocion != null && !codigoPromocion.isBlank()) {
            Promocion promocion = repositorioPromociones.buscarPorCodigo(codigoPromocion)
                    .orElseThrow(() -> new IllegalArgumentException("No existe una promoción con código " + codigoPromocion));
            if (!promocion.estaVigente(LocalDate.now())) {
                throw new IllegalStateException("La promoción no está vigente");
            }
            double descuento = promocion.calcularDescuento(importeTotal);
            factura.aplicarDescuento(descuento);
            reserva.aplicarPromocion(promocion);
        }

        factura.marcarPagada();
        repositorioFacturas.guardar(factura);

        reserva.setFactura(factura);
        reserva.registrarPago(factura.getImporteFinal());
        reserva.confirmar();

        Ingreso ingreso = new Ingreso(GeneradorCodigo.siguiente("MOV"), factura.getImporteFinal(),
                "Pago de reserva " + reserva.getCodigoReserva(), medioDePago);
        ingreso.setFactura(factura);
        caja.registrarMovimiento(ingreso);

        return factura;
    }
}
