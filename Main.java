package interfaz;
import modelo.*;
import negocio.Inventario;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Menú de consola; transforma las entradas del usuario en objetos. */
public class Main {
    private final Scanner teclado = new Scanner(System.in);
    private final Inventario inventario = new Inventario();
    public static void main(String[] args) { new Main().ejecutar(); }
    private void ejecutar() {
        System.out.println("INVENTARIO MINIMARKET");
        System.out.println("1 Registrar | 2 Listar | 3 Buscar | 4 Actualizar");
        System.out.println("5 Eliminar  | 6 Alertas | 7 Total | 0 Salir");
        while (true) {
            System.out.print("Opción: ");
            if (!teclado.hasNextLine()) break;
            String opcion = teclado.nextLine().trim();
            try {
                switch (opcion) {
                    case "1": registrar(); break;
                    case "2": mostrarLista(inventario.listar()); break;
                    case "3": mostrar(inventario.buscarProducto(leer("Código"))); break;
                    case "4": inventario.actualizarCantidad(leer("Código"), entero("Cantidad"));
                        System.out.println("Cantidad actualizada."); break;
                    case "5": inventario.eliminarProducto(leer("Código"));
                        System.out.println("Producto eliminado."); break;
                    case "6": mostrarLista(inventario.alertas(LocalDate.now())); break;
                    case "7": System.out.println("Total: $" + inventario.calcularTotal() + " | Unidades: " + inventario.totalUnidades()); break;
                    case "0": System.out.println("Fin del programa."); return;
                    default: System.out.println("ERROR: Opción inválida.");
                }
            } catch (IllegalArgumentException | DateTimeParseException ex) {
                System.out.println("ERROR: " + ex.getMessage());
            } catch (java.util.NoSuchElementException ex) {
                System.out.println("Entrada finalizada."); return;
            }
        }
    }
    private void registrar() {
        String tipo = leer("Tipo (P/N)").toUpperCase(java.util.Locale.ROOT);
        if (!tipo.equals("P") && !tipo.equals("N")) throw new IllegalArgumentException("Tipo inválido.");
        String codigo = leer("Código"), nombre = leer("Nombre");
        // Se leen todos los campos antes de validar, para evitar entradas sobrantes.
        String cant = leerCrudo("Cantidad"), costo = leerCrudo("Precio");
        String fecha = leerCrudo("Ingreso (AAAA-MM-DD)");
        String detalle = leerCrudo(tipo.equals("P") ? "Caducidad (AAAA-MM-DD)" : "Categoría");
        if (cant.isBlank()) throw new IllegalArgumentException("La cantidad es obligatoria.");
        if (costo.isBlank()) throw new IllegalArgumentException("El precio es obligatorio.");
        int cantidad = convertirEntero(cant);
        BigDecimal precio;
        try { precio = new BigDecimal(costo); } catch (NumberFormatException e) { throw new IllegalArgumentException("Precio inválido."); }
        LocalDate ingreso = LocalDate.parse(fecha);
        Producto p = tipo.equals("P") ? new ProductoPerecedero(codigo,nombre,cantidad,precio,ingreso,LocalDate.parse(detalle))
            : new ProductoNoPerecedero(codigo,nombre,cantidad,precio,ingreso,detalle);
        inventario.registrarProducto(p);
        System.out.println("Producto registrado: " + p.getCodigo());
    }
    private String leerCrudo(String campo) {
        System.out.print(campo + ": ");
        return teclado.nextLine().trim();
    }
    private String leer(String campo) {
        String valor = leerCrudo(campo);
        if (valor.isBlank()) throw new IllegalArgumentException(campo + " es obligatorio.");
        return valor;
    }
    private int entero(String campo) { return convertirEntero(leer(campo)); }
    private int convertirEntero(String valor) {
        try { return Integer.parseInt(valor); } catch (NumberFormatException e) { throw new IllegalArgumentException("Cantidad inválida."); }
    }
    private void mostrar(Producto p) {
        System.out.println(p.getCodigo() + " | " + p.getNombre() + " | Cant: " + p.getCantidad() + " | $" + p.getPrecio());
        System.out.println("Ingreso: " + p.getFechaIngreso() + " | " + p.getDetalle() + " | " + p.obtenerEstado(LocalDate.now()));
    }
    private void mostrarLista(List<Producto> lista) {
        if (lista.isEmpty()) System.out.println("Sin productos para mostrar.");
        for (Producto p : lista) mostrar(p);
    }
}
