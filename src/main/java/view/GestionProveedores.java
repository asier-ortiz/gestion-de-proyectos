package view;

import controller.ProveedorDao;
import model.Proveedor;
import Enums.SearchType;
import util.DialogManagerUtil;
import util.HibernateUtil;
import workers.SearchProviderTask;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Optional;

public class GestionProveedores {

    private JButton clearButton;
    private JButton insertButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton dismissButton;
    private JButton previousButton;
    private JButton firstButton;
    private JButton nextButton;
    private JButton lastButton;
    private JButton executeQueryButton;
    private JLabel minLabel;
    private JLabel maxLabel;
    private JPanel gestionProveedoresWindow;
    private JTextArea addressTextArea;
    private JTextArea addressInfoTextArea;
    private JTextField codTextField;
    private JTextField nameTextField;
    private JTextField surnamesTextField;
    private JTextField codInfoTextField;
    private JTextField nameInfoTextField;
    private JTextField surnamesInfoTextField;
    private final ProveedorDao dao;
    private int index = 0;
    private int rowCount = 0;
    private final Principal principal;

    public GestionProveedores(Principal principal) {
        this.principal = principal;
        this.dao = new ProveedorDao();
        if (HibernateUtil.getSessionFactory() != null) {
            init();
        } else {
            DialogManagerUtil.showErrorDialog(" Error : No ha sido posible establecer una conexión con la base de datos");
        }

    }

    public JPanel getGestionProveedoresWindow() {
        return gestionProveedoresWindow;
    }

    private void init() {
        setListeners();
        updateInfoTextFields();
    }

    private void setListeners() {

        codTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (codTextField.getText().length() >= 7) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int pos = codTextField.getCaretPosition();
                codTextField.setText(codTextField.getText().toUpperCase());
                codTextField.setCaretPosition(pos);
            }
        });

        nameTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (nameTextField.getText().length() >= 20) {
                    e.consume();
                }
            }
        });

        surnamesTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (surnamesTextField.getText().length() >= 30) {
                    e.consume();
                }
            }
        });

        addressTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (addressTextArea.getText().length() >= 40) {
                    e.consume();
                }
            }
        });

        codTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });

        clearButton.addActionListener(e -> clearFormFields());

        insertButton.addActionListener(e -> {
            if (validateForm()) {
                String code = dao.save(new Proveedor(
                        nameTextField.getText().trim(),
                        surnamesTextField.getText().trim(),
                        addressTextArea.getText().trim()
                ));
                if (code != null) {
                    index = dao.getRowCount() - 1;
                    codTextField.setText(code);
                    updateInfoTextFields();
                }
            } else {
                DialogManagerUtil.showErrorDialog("Por favor rellene los campos { Nombre, Apellidos y Dirección }");
            }
        });

        updateButton.addActionListener(e -> {
            if (validateForm()) {
                Optional<Proveedor> retrieved = dao.get(codTextField.getText().trim());
                retrieved.ifPresentOrElse(proveedor -> {
                    proveedor.setNombre(nameTextField.getText().trim());
                    proveedor.setApellidos(surnamesTextField.getText().trim());
                    proveedor.setDireccion(addressTextArea.getText().trim());
                    if (dao.update(proveedor)) {
                        index = dao.getRowIndex(proveedor.getCodigo());
                        codTextField.setText(codTextField.getText());
                        updateInfoTextFields();
                    }
                }, () -> DialogManagerUtil.showErrorDialog("Error : El código del proveedor no existe"));
            } else {
                DialogManagerUtil.showErrorDialog("Por favor rellene los campos { Nombre, Apellidos y Dirección }");
            }
        });

        deleteButton.addActionListener(e -> {
            if (!codTextField.getText().isBlank() && !codTextField.getText().isEmpty()) {
                int tempIndex = dao.getRowIndex(codTextField.getText().trim());
                if (dao.delete(codTextField.getText().trim())) {
                    index = dao.getRowCount() == 0 ? 0 : tempIndex - 1;
                    updateInfoTextFields();
                    clearFormFields();
                }
            }
        });

        nextButton.addActionListener(e -> {
            if ((rowCount = dao.getRowCount()) > 0) {
                if (index == rowCount - 1) {
                    index = 0;
                } else {
                    index++;
                }
                updateInfoTextFields();
            }
        });

        previousButton.addActionListener(e -> {
            if ((rowCount = dao.getRowCount()) > 0) {
                if (index == 0) {
                    index = rowCount - 1;
                } else {
                    index--;
                }
                updateInfoTextFields();
            }
        });

        firstButton.addActionListener(e -> {
            if ((rowCount = dao.getRowCount()) > 0) {
                index = 0;
                updateInfoTextFields();
            }
        });

        lastButton.addActionListener(e -> {
            if ((rowCount = dao.getRowCount()) > 0) {
                index = rowCount - 1;
                updateInfoTextFields();
            }
        });

        dismissButton.addActionListener(e -> {
            if (!codInfoTextField.getText().isBlank() && !codInfoTextField.getText().isEmpty()) {
                int tempIndex = dao.getRowIndex(codInfoTextField.getText().trim());
                if (dao.delete(codInfoTextField.getText().trim())) {
                    index = dao.getRowCount() == 0 ? 0 : tempIndex - 1;
                    updateInfoTextFields();
                    clearFormFields();
                }
            }
        });

        executeQueryButton.addActionListener(e -> {
            if (!codInfoTextField.getText().isBlank() && !codInfoTextField.getText().isEmpty()) {
                principal.swapView("Consultas", SearchType.PROVIDERS_CODE);
            }
        });
    }

    private void search() {
        if (!codTextField.getText().isBlank() && !codTextField.getText().isEmpty()) {
            SearchProviderTask searchProviderTask = new SearchProviderTask(
                    dao,
                    codTextField,
                    nameTextField,
                    surnamesTextField,
                    addressTextArea
            );
            searchProviderTask.execute();
        }
    }

    private boolean validateForm() {
        if (nameTextField.getText().trim().isEmpty()) {
            return false;
        } else if (surnamesTextField.getText().trim().isEmpty()) {
            return false;
        } else return !addressTextArea.getText().trim().isEmpty();
    }

    private void clearFormFields() {
        codTextField.setText("");
        nameTextField.setText("");
        surnamesTextField.setText("");
        addressTextArea.setText("");
    }

    private void updateInfoTextFields() {
        if ((rowCount = dao.getRowCount()) > 0) {
            minLabel.setText(String.valueOf(index + 1));
            maxLabel.setText(String.valueOf(rowCount));
            Optional<Proveedor> retrieved = dao.getByIndex(index);
            retrieved.ifPresentOrElse(proveedor -> {
                        codInfoTextField.setText(proveedor.getCodigo());
                        nameInfoTextField.setText(proveedor.getNombre());
                        surnamesInfoTextField.setText(proveedor.getApellidos());
                        addressInfoTextArea.setText(proveedor.getDireccion());
                    }, () -> {
                        codInfoTextField.setText("");
                        nameInfoTextField.setText("");
                        surnamesInfoTextField.setText("");
                        addressInfoTextArea.setText("");
                    }
            );
        } else {
            minLabel.setText("0");
            maxLabel.setText("0");
            codInfoTextField.setText("");
            nameInfoTextField.setText("");
            surnamesInfoTextField.setText("");
            addressInfoTextArea.setText("");
        }
    }
}