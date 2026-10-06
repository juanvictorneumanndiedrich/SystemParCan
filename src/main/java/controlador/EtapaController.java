package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import dao.EtapaDAO;
import interfaces.InterfaceABM;
import validaciones.ValidadorCampos;
import modelo.EtapaModelo;
import tabla.EstadoCellRenderer;
import tabla.ModeloTablaEtapa;
import vista.EtapaVista;

/**
 * Controlador del ABM de Etapas. Conecta {@link EtapaVista} con {@link EtapaDAO}:
 * lista las etapas en la tabla, permite buscarlas por descripcion en tiempo real y gestiona
 * alta, modificacion y baja.
 * 
 * Implementa {@link InterfaceABM}.
 */
public class EtapaController implements InterfaceABM {

    /**
     * Pantalla (vista) que maneja este controlador.
     */
    private EtapaVista vista;
    /**
     * Etapa en edicion o seleccionada; es {@code null} cuando no hay ninguna.
     */
    private EtapaModelo etapa;
    /**
     * DAO principal de la entidad que administra esta pantalla.
     */
    private EtapaDAO dao;
    /**
     * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
     * con el de esta lista.
     */
    private List<EtapaModelo> etapas;
    /**
     * Modelo de la tabla donde se listan los registros.
     */
    private ModeloTablaEtapa tabla;

    /**
     * Crea el controlador: asocia la vista ({@code setInterfaceABM}), instancia los DAO y el modelo de tabla,
     * carga los datos iniciales y registra las acciones de la pantalla.
     *
     * @param vista pantalla EtapaVista que se va a controlar
     */
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

    /**
     * Recarga la tabla con las etapas. Si hay filtro, solo quedan las que tienen ese texto en la descripcion
     * (sin distinguir mayusculas).
     *
     * @param filtro texto a buscar en la descripcion; vacio o {@code null} para traer todas
     */
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

    /**
     * Registra los listeners de la pantalla: doble clic en la tabla selecciona el registro
     * y el campo de busqueda filtra la tabla en tiempo real mientras se escribe.
     */
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

    /**
     * Deja la pantalla en su estado inicial: solo Nuevo y Cancelar habilitados, campos
     * deshabilitados y vacios, y sin registro seleccionado.
     */
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

    /**
     * Toma la fila elegida en la tabla como registro actual y habilita Editar y Eliminar.
     * Todavia no carga los datos en el formulario; eso ocurre al llamar a {@link #editar()}.
     */
    private void seleccionarRegistro() {
        int fila = this.vista.getTabla().getSelectedRow();
        if (fila < 0) return;
        etapa = etapas.get(fila);

        this.vista.getBtnEditar().setEnabled(true);
        this.vista.getBtnEliminar().setEnabled(true);
    }

    /**
     * Prepara la pantalla para cargar un registro nuevo: habilita los campos y Guardar,
     * y crea una instancia vacia del modelo.
     */
    @Override
    public void nuevo() {
        this.vista.getBtnNuevo().setEnabled(false);
        this.vista.getBtnEditar().setEnabled(false);
        this.vista.getBtnEliminar().setEnabled(false);
        this.vista.getBtnCancelar().setEnabled(true);
        this.vista.getBtnGuardar().setEnabled(true);

        this.vista.getTfDescripcion().setEnabled(true);
        this.vista.getCbEstado().setEnabled(true);
        this.vista.getCbEstado().setSelected(true); // activo por defecto

        etapa = new EtapaModelo();
    }

    /**
     * Carga descripcion y estado de la etapa seleccionada en el formulario y habilita los campos.
     */
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

    /**
     * Valida que la descripcion sea obligatoria ({@link ValidadorCampos}); si no lo es avisa y no guarda. Si es correcta
     * pasa descripcion y estado al modelo, lo guarda con el DAO y recarga la tabla.
     */
    @Override
    public void guardar() {
        String descripcion = this.vista.getTfDescripcion().getText();

        if (!ValidadorCampos.esObligatorio(descripcion)) {
            javax.swing.JOptionPane.showMessageDialog(this.vista,
                    "La descripcion de la etapa es obligatoria.",
                    "Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        etapa.setEtap_descripcion(descripcion);
        etapa.setEtap_estado(this.vista.getCbEstado().isSelected());

        try {
            dao.guardar(etapa);
        } catch (Exception e) {
            e.printStackTrace();
        }

        estadoInicial();
        cargarTabla("");
    }

    /**
     * Elimina el registro seleccionado previa confirmacion del usuario. Si la baja falla
     * (por ejemplo, por registros relacionados) muestra el error y no recarga la tabla.
     */
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

    /**
     * Cancela la operacion en curso: si no hay un registro en uso cierra la ventana,
     * y si lo hay vuelve la pantalla a su estado inicial.
     */
    @Override
    public void cancelar() {
        if (etapa == null) this.vista.dispose();
        else estadoInicial();
    }

    /**
     * Toma el texto del campo de busqueda (sin espacios sobrantes) y recarga la tabla filtrada.
     */
    @Override
    public void buscar() {
        String filtro = this.vista.getTfBuscador().getText().trim();
        cargarTabla(filtro);
    }
}