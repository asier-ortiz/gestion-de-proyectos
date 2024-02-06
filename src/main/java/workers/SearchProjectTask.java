package workers;

import controller.ProyectoDao;
import model.Proyecto;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SearchProjectTask extends SwingWorker<Optional<Proyecto>, String> {

    private final ProyectoDao dao;
    private final JTextField code;
    private final JTextField name;
    private final JTextField city;

    public SearchProjectTask(ProyectoDao dao, JTextField code, JTextField name, JTextField city) {
        this.dao = dao;
        this.code = code;
        this.name = name;
        this.city = city;
    }

    @Override
    protected Optional<Proyecto> doInBackground() {
        String code = this.code.getText().trim();
        return dao.get(code);
    }

    @Override
    protected void done() {
        try {
            Optional<Proyecto> retrieved = get();
            retrieved.ifPresentOrElse(proyecto -> {
                        name.setText(proyecto.getNombre());
                        city.setText(proyecto.getCiudad());
                    }, () -> {
                        name.setText("");
                        city.setText("");
                    }
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}