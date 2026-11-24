package modelo;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Producto con fecha de caducidad. */
public class ProductoPerecedero extends Producto {
    private final LocalDate fechaCaducidad;
    public ProductoPerecedero(String codigo, String nombre, int cantidad, BigDecimal precio,
                              LocalDate ingreso, LocalDate caducidad) {
        super(codigo, nombre, cantidad, precio, ingreso);
        if (caducidad == null || caducidad.isBefore(ingreso))
            throw new IllegalArgumentException("Caducidad anterior al ingreso.");
        this.fechaCaducidad = caducidad;
    }
    @Override public String obtenerEstado(LocalDate fecha) {
        if (fecha.isAfter(fechaCaducidad)) return "CADUCADO";
        if (fecha.equals(fechaCaducidad)) return "VENCE HOY";
        return "VIGENTE";
    }
    @Override public String getDetalle() { return "Caducidad: " + fechaCaducidad; }
}
