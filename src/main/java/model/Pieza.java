package model;

import org.hibernate.annotations.GenericGenerator;
import util.SequenceGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "piezas")
public class Pieza implements Serializable, Comparable<Pieza> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pieza_seq")
    @GenericGenerator(
            name = "pieza_seq",
            strategy = "util.SequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "PI"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    @Column(name = "codigo", length = 7, unique = true, updatable = false, nullable = false)
    private String codigo;
    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;
    @Column(name = "precio", nullable = false)
    private float precio;
    @Column(name = "descripcion")
    private String descripccion;
    @OneToMany(mappedBy = "pieza", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Gestion> gestiones;

    public Pieza() {
    }

    public Pieza(String codigo) {
        this.codigo = codigo;
    }

    public Pieza(String nombre, float precio, String descripccion) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripccion = descripccion;
        gestiones = new HashSet<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getDescripccion() {
        return descripccion;
    }

    public void setDescripccion(String descripccion) {
        this.descripccion = descripccion;
    }

    public Set<Gestion> getGestiones() {
        return gestiones;
    }

    public void setGestiones(Set<Gestion> gestiones) {
        this.gestiones = gestiones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pieza pieza = (Pieza) o;
        return codigo.equals(pieza.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public int compareTo(Pieza p) {
        return this.getCodigo().compareTo(p.getCodigo());
    }

    @Override
    public String toString() {
        return "Pieza{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripccion='" + descripccion + '\'' +
                '}';
    }
}