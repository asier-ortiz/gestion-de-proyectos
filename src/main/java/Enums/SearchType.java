package Enums;

import model.Pieza;
import model.Proveedor;
import model.Proyecto;

public enum SearchType {

    PROVIDERS_CODE(Proveedor.class, Proveedor.class.getDeclaredFields()[0].getName()),
    PROVIDERS_NAME(Proveedor.class, Proveedor.class.getDeclaredFields()[1].getName()),
    PROVIDERS_SURNAME(Proveedor.class, Proveedor.class.getDeclaredFields()[2].getName()),
    PROVIDERS_ADDRESS(Proveedor.class, Proveedor.class.getDeclaredFields()[3].getName()),
    PART_CODE(Pieza.class, Pieza.class.getDeclaredFields()[0].getName()),
    PART_NAME(Pieza.class, Pieza.class.getDeclaredFields()[1].getName()),
    PART_PRICE(Pieza.class, Pieza.class.getDeclaredFields()[2].getName()),
    PART_DESCRIPTION(Pieza.class, Pieza.class.getDeclaredFields()[3].getName()),
    PROJECT_CODE(Proyecto.class, Proyecto.class.getDeclaredFields()[0].getName()),
    PROJECT_NAME(Proyecto.class, Proyecto.class.getDeclaredFields()[1].getName()),
    PROJECT_CITY(Proyecto.class, Proyecto.class.getDeclaredFields()[2].getName());

    private final Class<?> aClass;

    private final String tableColumn;

    public Class<?> getaClass() {
        return aClass;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    SearchType(Class<?> aClass, String tableColumn) {
        this.aClass = aClass;
        this.tableColumn = tableColumn;

    }
}
