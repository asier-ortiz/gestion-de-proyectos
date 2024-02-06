package view;

import com.sun.istack.Nullable;
import gui.CustomCardLayout;
import Enums.SearchType;
import util.HibernateUtil;

import javax.swing.*;
import java.awt.*;

public class Principal extends JFrame {

    private JMenuBar menuBar;
    private JPanel cards;
    private JPanel principalWindow;
    public static CardLayout cardLayout;
    private final String CONSULTAS = "Consultas";
    private final String GESTION_PIEZAS = "Gestion Piezas";
    private final String GESTION_PROVEEDORES = "Gestion Proveedores";
    private final String GESTION_PROYECTOS = "Gestion Proyectos";
    private final String PIEZAS_PROVEEDORES_PROYECTOS = "Piezas Proveedores Proyectos";
    private final String SUMINISTROS_POR_PROVEEDOR = "Suministros por Proveedor";
    private final String SUMINISTROS_POR_PIEZAS = "Suministros por Piezas";
    private final String ESTADISTICAS = "Estadisticas";

    public Principal() {
        init();
    }

    public JPanel getPrincipalWindow() {
        return principalWindow;
    }

    private void init() {
        setSize(1800, 900);
        setLocationRelativeTo(null);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        createMenuBar();
    }

    private void createMenuBar() {
        var fileMenu = new JMenu("Archivo");
        var exitMenuItem = new JMenuItem("Salir");
        exitMenuItem.setToolTipText("Exit application");
        exitMenuItem.addActionListener((e) -> {
            HibernateUtil.shutdown();
            System.exit(0);
        });
        fileMenu.add(exitMenuItem);
        var proveedoresMenu = new JMenu("Proveedores");
        var gestionProveedoresMenuItem = new JMenuItem("Gestión de proveedores");
        gestionProveedoresMenuItem.addActionListener(e -> swapView("Gestion Proveedores", null));
        var consultaProveedoresMenu = new JMenu("Consulta de proveedores");
        var porCodigoProveedoresMenuItem = new JMenuItem("Por Código");
        porCodigoProveedoresMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROVIDERS_CODE));
        var porNombreProveedoresMenuItem = new JMenuItem("Por Nombre");
        porNombreProveedoresMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROVIDERS_NAME));
        var porDireccionProveedoresMenuItem = new JMenuItem("Por Dirección");
        porDireccionProveedoresMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROVIDERS_ADDRESS));
        consultaProveedoresMenu.add(porCodigoProveedoresMenuItem);
        consultaProveedoresMenu.add(porNombreProveedoresMenuItem);
        consultaProveedoresMenu.add(porDireccionProveedoresMenuItem);
        proveedoresMenu.add(gestionProveedoresMenuItem);
        proveedoresMenu.addSeparator();
        proveedoresMenu.add(consultaProveedoresMenu);
        proveedoresMenu.addSeparator();
        var piezasMenu = new JMenu("Piezas");
        var gestionPiezasMenuItem = new JMenuItem("Gestión de piezas");
        gestionPiezasMenuItem.addActionListener(e -> swapView("Gestion Piezas", null));
        var consultaPiezasMenu = new JMenu("Consulta de piezas");
        var porCodigoPiezaMenuItem = new JMenuItem("Por Código");
        porCodigoPiezaMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PART_CODE));
        var porNombrePiezaMenuItem = new JMenuItem("Por Nombre");
        porNombrePiezaMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PART_NAME));
        var porPrecioPiezaMenuItem = new JMenuItem("Por Precio");
        porNombrePiezaMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PART_PRICE));
        consultaPiezasMenu.add(porCodigoPiezaMenuItem);
        consultaPiezasMenu.add(porNombrePiezaMenuItem);
        consultaPiezasMenu.add(porPrecioPiezaMenuItem);
        piezasMenu.add(gestionPiezasMenuItem);
        piezasMenu.addSeparator();
        piezasMenu.add(consultaPiezasMenu);
        piezasMenu.addSeparator();
        var proyectosMenu = new JMenu("Proyectos");
        var gestionProyectosMenuItem = new JMenuItem("Gestión de proyectos");
        gestionProyectosMenuItem.addActionListener(e -> swapView("Gestion Proyectos", null));
        var consultaProyectosMenu = new JMenu("Consulta de proyectos");
        var porCodigoProyectoMenuItem = new JMenuItem("Por Código");
        porCodigoProyectoMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROJECT_CODE));
        var porNombreProyectoMenuItem = new JMenuItem("Por Nombre");
        porNombreProyectoMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROJECT_NAME));
        var porCiudadProyectoMenuItem = new JMenuItem("Por Ciudad");
        porCiudadProyectoMenuItem.addActionListener(e -> swapView("Consultas", SearchType.PROJECT_CITY));
        consultaProyectosMenu.add(porCodigoProyectoMenuItem);
        consultaProyectosMenu.add(porNombreProyectoMenuItem);
        consultaProyectosMenu.add(porCiudadProyectoMenuItem);
        proyectosMenu.add(gestionProyectosMenuItem);
        proyectosMenu.addSeparator();
        proyectosMenu.add(consultaProyectosMenu);
        proyectosMenu.addSeparator();
        var gestionGlobalMenu = new JMenu("Gestión Global");
        var piezasProveedoresProyectosMenuItem = new JMenuItem("Piezas, Proveedores y Proyectos");
        piezasProveedoresProyectosMenuItem.addActionListener(e -> swapView("Piezas Proveedores Proyectos", null));
        var suministrosProveedorMenuItem = new JMenuItem("Suministros por Proveedor");
        suministrosProveedorMenuItem.addActionListener(e -> swapView("Suministros por Proveedor", null));
        var suministrosPiezasMenuItem = new JMenuItem("Suministros por Piezas");
        suministrosPiezasMenuItem.addActionListener(e -> swapView("Suministros por Piezas", null));
        var estadisticasMenuItem = new JMenuItem("Estadísticas");
        estadisticasMenuItem.addActionListener(e -> swapView("Estadisticas", null));
        gestionGlobalMenu.add(piezasProveedoresProyectosMenuItem);
        gestionGlobalMenu.add(suministrosProveedorMenuItem);
        gestionGlobalMenu.add(suministrosPiezasMenuItem);
        gestionGlobalMenu.add(estadisticasMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(proveedoresMenu);
        menuBar.add(piezasMenu);
        menuBar.add(proyectosMenu);
        menuBar.add(gestionGlobalMenu);
        setJMenuBar(menuBar);
        cardLayout = new CustomCardLayout();
        cards.setLayout(cardLayout);
    }

    private JPanel createQueriesPanel(SearchType searchType) {
        return new Queries(searchType).getQueriesWindow();
    }

    private JPanel createGestionProveedoresPanel() {
        return new GestionProveedores(this).getGestionProveedoresWindow();
    }

    private JPanel createGestionPiezasPanel() {
        return new GestionPiezas(this).getGestionPiezasWindow();
    }

    private JPanel createGestionProyectosPanel() {
        return new GestionProyectos(this).getGestionProyectosWindow();
    }

    private JPanel createPiezasProveedoresProyectosPanel() {
        return new PiezasProveedoresProyectos().getPiezasProveedoresProyectosWindow();
    }

    private JPanel createSuministrosPorProveedorPanel() {
        return new SuministrosPorProveedor().getSuministrosPorProveedorWindow();
    }

    private JPanel createSuministrosPorPiezasPanel() {
        return new SuministrosPorPiezas().getSuministrosPorPiezasWindow();
    }

    private JPanel createEstadisticasPanel() {
        return new Estadisticas().getEstadisticasWindow();
    }

    protected void swapView(String key, @Nullable SearchType searchType) {
        SwingUtilities.invokeLater(() -> {
            cards.removeAll();
            switch (key) {
                case "Consultas" -> cards.add(createQueriesPanel(searchType), CONSULTAS);
                case "Gestion Proveedores" -> cards.add(createGestionProveedoresPanel(), GESTION_PROVEEDORES);
                case "Gestion Piezas" -> cards.add(createGestionPiezasPanel(), GESTION_PIEZAS);
                case "Gestion Proyectos" -> cards.add(createGestionProyectosPanel(), GESTION_PROYECTOS);
                case "Piezas Proveedores Proyectos" -> cards.add(createPiezasProveedoresProyectosPanel(), PIEZAS_PROVEEDORES_PROYECTOS);
                case "Suministros por Proveedor" -> cards.add(createSuministrosPorProveedorPanel(), SUMINISTROS_POR_PROVEEDOR);
                case "Suministros por Piezas" -> cards.add(createSuministrosPorPiezasPanel(), SUMINISTROS_POR_PIEZAS);
                case "Estadisticas" -> cards.add(createEstadisticasPanel(), ESTADISTICAS);
            }
            cardLayout.show(cards, key);
            setVisible(true);
        });
    }
}