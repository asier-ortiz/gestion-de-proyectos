package workers;

import controller.PiezaDao;
import model.Pieza;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SearchPartTask extends SwingWorker<Optional<Pieza>, String> {

    private final PiezaDao dao;
    private final JTextField code;
    private final JTextField name;
    private final JTextField price;
    private final JTextArea description;

    public SearchPartTask(PiezaDao dao, JTextField code, JTextField name, JTextField price, JTextArea description) {
        this.dao = dao;
        this.code = code;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Override
    protected Optional<Pieza> doInBackground() {
        String code = this.code.getText().trim();
        return dao.get(code);
    }

    @Override
    protected void done() {
        try {
            Optional<Pieza> retrieved = get();
            retrieved.ifPresentOrElse(pieza -> {
                        name.setText(pieza.getNombre());
                        price.setText(String.valueOf(pieza.getPrecio()));
                        description.setText(pieza.getDescripccion());
                    }, () -> {
                        name.setText("");
                        price.setText("");
                        description.setText("");
                    }
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
