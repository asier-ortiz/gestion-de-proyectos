package gui;

import model.Pieza;
import model.Proveedor;
import model.Proyecto;
import Enums.SearchType;

public class ComboxItem {

    private String key;
    private final Object value;

    public ComboxItem(SearchType searchType, Object value) {
        this.value = value;
        switch (searchType) {
            case PROVIDERS_CODE -> key = ((Proveedor) value).getCodigo();
            case PROVIDERS_NAME -> key = ((Proveedor) value).getNombre();
            case PROVIDERS_SURNAME -> key = ((Proveedor) value).getApellidos();
            case PROVIDERS_ADDRESS -> key = ((Proveedor) value).getDireccion();
            case PART_CODE -> key = ((Pieza) value).getCodigo();
            case PART_NAME -> key = ((Pieza) value).getNombre();
            case PART_PRICE -> key = String.valueOf(((Pieza) value).getPrecio());
            case PART_DESCRIPTION -> key = ((Pieza) value).getDescripccion();
            case PROJECT_CODE -> key = ((Proyecto) value).getCodigo();
            case PROJECT_NAME -> key = ((Proyecto) value).getNombre();
            case PROJECT_CITY -> key = ((Proyecto) value).getCiudad();
        }
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key;
    }
}
