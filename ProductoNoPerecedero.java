package modelo;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Producto cuyo vencimiento no se controla en este sistema. */
public class ProductoNoPerecedero extends Producto {
    private final String categoria;
    public ProductoNoPerecedero(String codigo, String nombre, int cantidad, BigDecimal precio,
                                LocalDate ingreso, String categoria) {
        super(codigo, nombre, cantidad, precio, ingreso);
        if (categoria == null || categoria.isBlank()) throw new IllegalArgumentException("La categoría es obligatoria.");
        this.categoria = categoria.trim();
    }
    @Override public String obtenerEstado(LocalDate fecha) { return "SIN CONTROL DE CADUCIDAD"; }
    @Override public String getDetalle() { return "Categoría: " + categoria; }
}
