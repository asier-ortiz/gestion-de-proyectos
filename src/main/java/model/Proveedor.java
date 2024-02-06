package model;

import org.hibernate.annotations.GenericGenerator;
import util.SequenceGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "proveedores")
public class Proveedor implements Comparable<Proveedor>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proveedor_seq")
    @GenericGenerator(
            name = "proveedor_seq",
            strategy = "util.SequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = util.SequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = util.SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "PV"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    @Column(name = "codigo", length = 7, updatable = false, nullable = false)
    private String codigo;
    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre;
    @Column(name = "apellidos", length = 30, nullable = false)
    private String apellidos;
    @Column(name = "direccion", length = 40, nullable = false)
    private String direccion;
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Gestion> gestiones;

    public Proveedor() {
    }

    public Proveedor(String codigo) {
        this.codigo = codigo;
    }

    public Proveedor(String nombre, String apellidos, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
        Proveedor proveedor = (Proveedor) o;
        return codigo.equals(proveedor.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public int compareTo(Proveedor p) {
        return this.getCodigo().compareTo(p.getCodigo());
    }

    @Override
    public String toString() {
        return "Proveedor{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", direccion='" + direccion + '\'' +
                '}';
    }




}