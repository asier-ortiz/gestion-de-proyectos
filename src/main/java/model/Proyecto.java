package model;

import org.hibernate.annotations.GenericGenerator;
import util.SequenceGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "proyectos")
public class Proyecto implements Comparable<Proyecto>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "proyecto_seq")
    @GenericGenerator(
            name = "proyecto_seq",
            strategy = "util.SequenceGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = util.SequenceGenerator.INCREMENT_PARAM, value = "1"),
                    @org.hibernate.annotations.Parameter(name = util.SequenceGenerator.VALUE_PREFIX_PARAMETER, value = "PY"),
                    @org.hibernate.annotations.Parameter(name = SequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            })
    @Column(name = "codigo", length = 7, updatable = false, nullable = false)
    private String codigo;
    @Column(name = "nombre", length = 40, nullable = false)
    private String nombre;
    @Column(name = "ciudad", length = 40)
    private String ciudad;
    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Gestion> gestiones;

    public Proyecto() {
    }

    public Proyecto(String codigo) {
        this.codigo = codigo;
    }

    public Proyecto(String nombre, String ciudad) {
        this.nombre = nombre;
        this.ciudad = ciudad;
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

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
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
        Proyecto proyecto = (Proyecto) o;
        return codigo.equals(proyecto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }


    @Override
    public int compareTo(Proyecto p) {
        return this.getCodigo().compareTo(p.getCodigo());
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
