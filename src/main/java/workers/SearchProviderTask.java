package workers;

import controller.ProveedorDao;
import model.Proveedor;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class SearchProviderTask extends SwingWorker<Optional<Proveedor>, String> {

    private final ProveedorDao dao;
    private final JTextField code;
    private final JTextField name;
    private final JTextField surnames;
    private final JTextArea address;

    public SearchProviderTask(ProveedorDao dao, JTextField code, JTextField name, JTextField surnames, JTextArea address) {
        this.dao = dao;
        this.code = code;
        this.name = name;
        this.surnames = surnames;
        this.address = address;
    }

    @Override
    protected Optional<Proveedor> doInBackground() {
        String code = this.code.getText().trim();
        return dao.get(code);
    }

    @Override
    protected void done() {
        try {
            Optional<Proveedor> retrieved = get();
            retrieved.ifPresentOrElse(proveedor -> {
                        name.setText(proveedor.getNombre());
                        surnames.setText(proveedor.getApellidos());
                        address.setText(proveedor.getDireccion());
                    }, () -> {
                        name.setText("");
                        surnames.setText("");
                        address.setText("");
                    }
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}