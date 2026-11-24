package modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Datos comunes y reglas básicas de cualquier producto. */
public abstract class Producto {
    private final String codigo, nombre;
    private int cantidad;
    private final BigDecimal precio;
    private final LocalDate fechaIngreso;

    protected Producto(String codigo, String nombre, int cantidad, BigDecimal precio, LocalDate fechaIngreso) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Código y nombre son obligatorios.");
        if (precio == null || precio.signum() <= 0 || precio.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("El precio debe ser positivo y tener máximo dos decimales.");
        if (fechaIngreso == null || fechaIngreso.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Fecha de ingreso inválida.");
        this.codigo = codigo.trim().toUpperCase(java.util.Locale.ROOT);
        this.nombre = nombre.trim();
        this.precio = precio.setScale(2);
        this.fechaIngreso = fechaIngreso;
        setCantidad(cantidad);
    }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecio() { return precio; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setCantidad(int cantidad) {
        if (cantidad < 0) throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        this.cantidad = cantidad;
    }
    public BigDecimal calcularValor() { return precio.multiply(BigDecimal.valueOf(cantidad)); }
    public abstract String obtenerEstado(LocalDate fecha);
    public abstract String getDetalle();
}
