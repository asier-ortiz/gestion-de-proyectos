package model;

public class EstadisticaProveedor {

    private final String codigo;
    private final String nombre;
    private final String apellidos;
    private final int numeroProyectos;
    private final int numeroPiezas;
    private final int cantidadTotal;

    public EstadisticaProveedor(String codigo, String nombre, String apellidos, int numeroProyectos, int numeroPiezas, int cantidadTotal) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.numeroProyectos = numeroProyectos;
        this.numeroPiezas = numeroPiezas;
        this.cantidadTotal = cantidadTotal;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getNumeroProyectos() {
        return numeroProyectos;
    }

    public int getNumeroPiezas() {
        return numeroPiezas;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }
}
