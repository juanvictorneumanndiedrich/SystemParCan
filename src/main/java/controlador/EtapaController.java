package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import dao.EtapaDAO;
import interfaces.InterfaceABM;
import modelo.EtapaModelo;
import tabla.EstadoCellRenderer;
import tabla.ModeloTablaEtapa;
import vista.EtapaVista;

public class EtapaController implements InterfaceABM {

    private EtapaVista vista;
    private EtapaModelo etapa;
    private EtapaDAO dao;
    private List<EtapaModelo> etapas;
    private ModeloTablaEtapa tabla;

    public EtapaController(EtapaVista etapaVista) {
        super();
        this.vista = etapaVista;
        this.vista.setInterfaceABM(this);
        dao = new EtapaDAO();
        tabla = new ModeloTablaEtapa();
        this.vista.getTabla().setModel(tabla);                                  // estava faltando
        this.vista.getTabla().getColumnModel().getColumn(2)
        .setCellRenderer(new EstadoCellRenderer());
        estadoInicial();
        cargarTabla("");
        setAcciones();
    }

    private void cargarTabla(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            etapas = dao.recuperarTodo();                                        // estava: etapa = dao.recuperarTodo()
        } else {
            etapas = dao.recuperarTodo().stream()
                .filter(c -> c.getEtap_descripcion().toLowerCase()
                .contains(filtro.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        }
        tabla.setLista(etapas);
    }

    private void setAcciones() {
        this.vista.getTabla().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) seleccionarRegistro();
            }
        });

        this.vista.getTfBuscador().getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { buscar(); }
        });
    }

    private void estadoInicial() {
        this.vista.getBtnNuevo().setEnabled(true);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnCancelar().setEnabled(true);
        this.vista.getBtnGuardar().setEnabled(false);

        this.vista.getTfDescripcion().setEnabled(false);
        this.vista.getCbEstado().setEnabled(false);                            // usar JCheckBox como Catequizando

        this.vista.getTfDescripcion().setText("");
        this.vista.getCbEstado().setSelected(false);
        etapa = null;
    }

    private void seleccionarRegistro() {
        int fila = this.vista.getTabla().getSelectedRow();
        if (fila < 0) return;
        etapa = etapas.get(fila);

        this.vista.getBtnEditar().setEnabled(true);
        this.vista.getBtnEliminar().setEnabled(true);
    }

    @Override
    public void nuevo() {
        this.vista.getBtnNuevo().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnCancelar().setEnabled(true);
        this.vista.getBtnGuardar().setEnabled(true);

        this.vista.getTfDescripcion().setEnabled(true);
        this.vista.getCbEstado().setEnabled(true);

        etapa = new EtapaModelo();
    }

    @Override
    public void editar() {
        if (etapa == null) return;

        // Carregar dados nos campos
        this.vista.getTfDescripcion().setText(etapa.getEtap_descripcion());
        this.vista.getCbEstado().setSelected(etapa.isEtap_estado());

        // Habilitar campos
        this.vista.getTfDescripcion().setEnabled(true);
        this.vista.getCbEstado().setEnabled(true);

        // Ajustar botones
        this.vista.getBtnNuevo().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnGuardar().setEnabled(true);
        this.vista.getBtnCancelar().setEnabled(true);
    }

    @Override
    public void guardar() {
        etapa.setEtap_descripcion(this.vista.getTfDescripcion().getText());
        etapa.setEtap_estado(this.vista.getCbEstado().isSelected());

        try {
            dao.guardar(etapa);
        } catch (Exception e) {
            e.printStackTrace();
        }

        estadoInicial();
        cargarTabla("");
    }

    @Override
    public void eliminar() {
        if (etapa == null) return;

        int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
            this.vista,
            "¿Está seguro que desea eliminar la etapa: "
                + etapa.getEtap_descripcion() + "?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
            try {
                dao.eliminar(etapa);
            } catch (Exception e) {
                e.printStackTrace();
                javax.swing.JOptionPane.showMessageDialog(
                    this.vista,
                    "Error al eliminar: " + e.getMessage()
                );
                return;
            }

            cargarTabla("");
            estadoInicial();
        }
    }

    @Override
    public void cancelar() {
        if (etapa == null) this.vista.dispose();
        else estadoInicial();
    }

    @Override
    public void buscar() {
        String filtro = this.vista.getTfBuscador().getText().trim();
        cargarTabla(filtro);
    }
}