package model;

public class EstadisticaPieza {

    private final String codigo;
    private final String nombre;
    private final float precio;
    private final int numeroProyectos;
    private final int numeroProveedores;
    private final int cantidadTotal;

    public EstadisticaPieza(String codigo, String nombre, float precio, int numeroProyectos, int numeroProveedores, int cantidadTotal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.numeroProyectos = numeroProyectos;
        this.numeroProveedores = numeroProveedores;
        this.cantidadTotal = cantidadTotal;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public int getNumeroProyectos() {
        return numeroProyectos;
    }

    public int getNumeroProveedores() {
        return numeroProveedores;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }
}
