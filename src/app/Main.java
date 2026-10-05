package app;

import dominio.Caja;
import repositorio.RepositorioAsistentes;
import repositorio.RepositorioAsistentesMemoria;
import repositorio.RepositorioFacturas;
import repositorio.RepositorioFacturasMemoria;
import repositorio.RepositorioPromociones;
import repositorio.RepositorioPromocionesMemoria;
import repositorio.RepositorioReservas;
import repositorio.RepositorioReservasMemoria;
import servicio.GestorAsistentes;
import servicio.GestorCaja;
import servicio.GestorPagos;
import servicio.GestorPromociones;
import servicio.GestorReservas;
import ui.VentanaPrincipal;
import util.GeneradorCodigo;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        RepositorioAsistentes repositorioAsistentes = new RepositorioAsistentesMemoria();
        GestorAsistentes gestorAsistentes = new GestorAsistentes(repositorioAsistentes);

        RepositorioReservas repositorioReservas = new RepositorioReservasMemoria();
        GestorReservas gestorReservas = new GestorReservas(repositorioReservas, gestorAsistentes);

        RepositorioPromociones repositorioPromociones = new RepositorioPromocionesMemoria();
        GestorPromociones gestorPromociones = new GestorPromociones(repositorioPromociones);

        RepositorioFacturas repositorioFacturas = new RepositorioFacturasMemoria();
        Caja caja = new Caja(GeneradorCodigo.siguiente("CAJA"), 0);
        GestorPagos gestorPagos = new GestorPagos(gestorReservas, repositorioPromociones, repositorioFacturas, caja);
        GestorCaja gestorCaja = new GestorCaja(caja); // misma Caja que usa GestorPagos

        // Datos de ejemplo para no arrancar con las tablas vacías
        gestorAsistentes.registrar("Marcos Torti", "marcos@mail.com", "1122334455");
        gestorAsistentes.registrar("Ana Gómez", "ana@mail.com", "1133445566");

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventanaPrincipal = new VentanaPrincipal(
                    gestorAsistentes, gestorReservas, gestorPromociones, gestorPagos, gestorCaja);
            ventanaPrincipal.setVisible(true);
        });
    }
}
