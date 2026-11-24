package negocio;
import modelo.Producto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Administra productos y verifica límites antes de cambiar las existencias. */
public class Inventario {
    private final List<Producto> productos = new ArrayList<>();
    private final int capacidad = 1000;
    private final BigDecimal presupuesto = new BigDecimal("500.00");
    public Producto buscarProducto(String codigo) {
        for (Producto p : productos) if (p.getCodigo().equalsIgnoreCase(codigo.trim())) return p;
        throw new IllegalArgumentException("Producto no encontrado.");
    }
    public void registrarProducto(Producto producto) {
        for (Producto p : productos)
            if (p.getCodigo().equals(producto.getCodigo())) throw new IllegalArgumentException("Código duplicado.");
        verificarLimites((long) totalUnidades() + producto.getCantidad(), calcularTotal().add(producto.calcularValor()));
        productos.add(producto);
    }
    public void actualizarCantidad(String codigo, int cantidad) {
        Producto p = buscarProducto(codigo);
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        long unidades = (long) totalUnidades() - p.getCantidad() + cantidad;
        BigDecimal valor = calcularTotal().subtract(p.calcularValor()).add(p.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
        verificarLimites(unidades, valor);
        p.setCantidad(cantidad);
    }
    public void eliminarProducto(String codigo) { productos.remove(buscarProducto(codigo)); }
    public List<Producto> listar() { return Collections.unmodifiableList(productos); }
    public List<Producto> alertas(LocalDate fecha) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : productos) {
            String estado = p.obtenerEstado(fecha);
            if (p.getCantidad() > 0 && (estado.equals("CADUCADO") || estado.equals("VENCE HOY"))) resultado.add(p);
        }
        return resultado;
    }
    public BigDecimal calcularTotal() {
        BigDecimal total = new BigDecimal("0.00");
        for (Producto p : productos) total = total.add(p.calcularValor());
        return total;
    }
    public int totalUnidades() {
        int total = 0;
        for (Producto p : productos) total += p.getCantidad();
        return total;
    }
    private void verificarLimites(long unidades, BigDecimal valor) {
        if (unidades > capacidad) throw new IllegalArgumentException("Se supera la capacidad de 1000 unidades.");
        if (valor.compareTo(presupuesto) > 0) throw new IllegalArgumentException("Se supera el presupuesto de $500.00.");
    }
}
