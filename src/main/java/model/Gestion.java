package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gestiones")
public class Gestion implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cod_proyecto")
    private Proyecto proyecto;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cod_proveedor")
    private Proveedor proveedor;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cod_pieza")
    private Pieza pieza;
    @Column(name = "cantidad")
    private int cantidad;

    public Gestion() {
    }

    public Gestion(Proyecto proyecto, Proveedor proveedor, Pieza pieza, int cantidad) {
        this.proyecto = proyecto;
        this.proveedor = proveedor;
        this.pieza = pieza;
        this.cantidad = cantidad;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Pieza getPieza() {
        return pieza;
    }

    public void setPieza(Pieza pieza) {
        this.pieza = pieza;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gestion gestion = (Gestion) o;
        return proyecto.equals(gestion.proyecto) &&
                proveedor.equals(gestion.proveedor) &&
                pieza.equals(gestion.pieza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(proyecto, proveedor, pieza);
    }

    @Override
    public String toString() {
        return "Gestion{" +
                "proyecto=" + proyecto +
                ", proveedor=" + proveedor +
                ", pieza=" + pieza +
                ", cantidad=" + cantidad +
                '}';
    }
}