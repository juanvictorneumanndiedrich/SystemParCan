package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequizandoDAO;
import dao.SacramentoDAO;
import interfaces.InterfaceABM;
import validaciones.ValidadorCampos;
import modelo.CatequizandoModelo;
import tabla.EstadoCellRenderer;
import tabla.ModeloTablaCatequizando;
import vista.CatequizandoVista;
import utilidades.FechaUtil;

/**
 * Controlador del ABM de Catequizandos. Conecta {@link CatequizandoVista} con {@link CatequizandoDAO}:
 * lista los catequizandos en la tabla, permite buscarlos por nombre en tiempo real y gestiona
 * alta, modificacion y baja, incluyendo los datos del responsable y los sacramentos recibidos.
 * 
 * Implementa {@link InterfaceABM}.
 */
public class CatequizandoController implements InterfaceABM {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private CatequizandoVista vista;
	/**
	 * Catequizando en edicion o seleccionado; es {@code null} cuando no hay ninguno.
	 */
	private CatequizandoModelo catequizando;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private CatequizandoDAO dao;
	/**
	 * DAO auxiliar para cargar los sacramentos en el combo.
	 */
	private SacramentoDAO sacramentoDao;
	/**
	 * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
	 * con el de esta lista.
	 */
	private List<CatequizandoModelo> catequizandos;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaCatequizando tabla;

	/**
	 * Crea el controlador: asocia la vista ({@code setInterfaceABM}), instancia los DAO y el modelo de tabla,
	 * carga los datos iniciales y registra las acciones de la pantalla.
	 *
	 * @param vista pantalla CatequizandoVista que se va a controlar
	 */
	public CatequizandoController(CatequizandoVista catequizandoVista) {
		super();
		this.vista = catequizandoVista;
		this.vista.setInterfaceABM(this);
		dao = new CatequizandoDAO();
		sacramentoDao = new SacramentoDAO();
		tabla = new ModeloTablaCatequizando();
		this.vista.getTabla().setModel(tabla);
		this.vista.getTabla().getColumnModel().getColumn(4)
        .setCellRenderer(new EstadoCellRenderer()); 
		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}
	
	/**
	 * Configura el combo de sacramentos (texto visible y clave de cada item) y lo llena con todos los sacramentos.
	 */
	private void cargarCombos() {
		this.vista.getComboSacramentos().setProveedorTexto(s -> s.getSacr_nombre());
		this.vista.getComboSacramentos().setExtractorClave(s -> s.getSacr_id());
		this.vista.getComboSacramentos().setItems(sacramentoDao.recuperarTodo());
	}
	
	/**
	 * Recarga la tabla con los catequizandos. Si hay filtro, solo quedan los que tienen ese texto en el nombre
	 * (sin distinguir mayusculas).
	 *
	 * @param filtro texto a buscar en el nombre; vacio o {@code null} para traer todos
	 */
	private void cargarTabla(String filtro) {
	    if (filtro == null || filtro.isEmpty()) {
	        catequizandos = dao.recuperarTodo();
	    } else {
	        catequizandos = dao.recuperarTodo().stream()
	            .filter(c -> c.getCatz_nombre().toLowerCase()
	            .contains(filtro.toLowerCase()))
	            .collect(java.util.stream.Collectors.toList());
	    }
	    tabla.setLista(catequizandos);
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

	    // Búsqueda en tiempo real
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
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		// desactiva los textfield
		this.vista.getTfFecha_reg().setEnabled(false);
		this.vista.getTfNombre().setEnabled(false);
		this.vista.getTfApellido().setEnabled(false);
		this.vista.getTfDocumento().setEnabled(false);
		this.vista.getTfFecha_nac().setEnabled(false);
		this.vista.getTfCorreo().setEnabled(false);
		this.vista.getTfDireccion().setEnabled(false);
		this.vista.getTfTelefono().setEnabled(false);
		this.vista.getTfNombreResponsable().setEnabled(false);
		this.vista.getTfContactoResponsable().setEnabled(false);
		this.vista.getComboSacramentos().setEnabled(false);

		// Limpiar los campos
		this.vista.getTfFecha_reg().setValue(null);
		this.vista.getTfFecha_nac().setValue(null);
		this.vista.getTfNombre().setText("");
		this.vista.getTfApellido().setText("");
		this.vista.getTfDocumento().setText("");
		this.vista.getTfCorreo().setText("");
		this.vista.getTfDireccion().setText("");
		this.vista.getTfTelefono().setText("");
		this.vista.getTfNombreResponsable().setText("");
		this.vista.getTfContactoResponsable().setText("");
		this.vista.getComboSacramentos().limpiarSeleccion();
		catequizando = null;

	}

	/**
	 * Prepara la pantalla para cargar un registro nuevo: habilita los campos y Guardar,
	 * y crea una instancia vacia del modelo.
	 */
	@Override
	public void nuevo() {
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		// desactiva los textfield
		this.vista.getTfFecha_reg().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfFecha_nac().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getTfNombreResponsable().setEnabled(true);
		this.vista.getTfContactoResponsable().setEnabled(true);
		this.vista.getJcbEstado().setEnabled(true);
		this.vista.getJcbEstado().setSelected(true); // activo por defecto
		this.vista.getComboSacramentos().setEnabled(true);
		this.vista.getComboSacramentos().limpiarSeleccion();
		
		//Carga el campo fecha y crea el cliente
		catequizando = new CatequizandoModelo();
		this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(LocalDate.now()));

	}
	
	
	/**
	 * Toma la fila elegida en la tabla como registro actual y habilita Editar y Eliminar.
	 * Todavia no carga los datos en el formulario; eso ocurre al llamar a {@link #editar()}.
	 */
	private void seleccionarRegistro() {
	    int fila = this.vista.getTabla().getSelectedRow();
	    if (fila < 0) return;
	    catequizando = catequizandos.get(fila);

	    // Solo habilitar botones, sin cargar campos
	    this.vista.getBtnEditar().setEnabled(true);
	    this.vista.getBtnEliminar().setEnabled(true);
	}

	/**
	 * Carga los datos del catequizando seleccionado (incluidos responsable y sacramentos) en el formulario y habilita
	 * los campos. No hace nada si no hay un catequizando seleccionado.
	 */
	@Override
	public void editar() {
	    if (catequizando == null) return;

	    // Cargar datos en los campos
	    this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(catequizando.getCatz_fechaRegistro()));
	    this.vista.getTfNombre().setText(catequizando.getCatz_nombre());
	    this.vista.getTfApellido().setText(catequizando.getCatz_apellido());
	    this.vista.getTfDocumento().setText(catequizando.getCatz_documento());
	    this.vista.getTfTelefono().setText(catequizando.getCatz_telefono());
	    this.vista.getTfCorreo().setText(catequizando.getCatz_correo());
	    this.vista.getTfFecha_nac().setText(FechaUtil.fechaAString(catequizando.getCatz_fechaNacimiento()));
	    this.vista.getTfDireccion().setText(catequizando.getCatz_direccion());
	    this.vista.getTfNombreResponsable().setText(catequizando.getCatz_nombreResponsable());
	    this.vista.getTfContactoResponsable().setText(catequizando.getCatz_contactoResponsable());
	    this.vista.getJcbEstado().setSelected(catequizando.isCatz_estado());
	    this.vista.getComboSacramentos().setSeleccionados(catequizando.getSacramentos());

	    // Habilitar campos para edición
	    this.vista.getTfNombre().setEnabled(true);
	    this.vista.getTfApellido().setEnabled(true);
	    this.vista.getTfDocumento().setEnabled(true);
	    this.vista.getTfFecha_nac().setEnabled(true);
	    this.vista.getTfCorreo().setEnabled(true);
	    this.vista.getTfDireccion().setEnabled(true);
	    this.vista.getTfTelefono().setEnabled(true);
	    this.vista.getTfNombreResponsable().setEnabled(true);
		this.vista.getTfContactoResponsable().setEnabled(true);
	    this.vista.getJcbEstado().setEnabled(true);
	    this.vista.getComboSacramentos().setEnabled(true);

	    // Ajustar botones
	    this.vista.getBtnNuevo().setEnabled(false);
	    this.vista.getBtnEditar().setEnabled(false);
	    this.vista.getBtnEliminar().setEnabled(false);
	    this.vista.getBtnGuardar().setEnabled(true);
	    this.vista.getBtnCancelar().setEnabled(true);
	}

	/**
	 * Valida los datos con {@link ValidadorCampos} (ver la clase para las reglas de cada campo). Ante el primer dato
	 * invalido muestra un aviso y no guarda. Si todo es correcto pasa los datos al modelo, lo guarda con el DAO
	 * y recarga la tabla.
	 */
	@Override
	public void guardar() {
		
		String nombre = this.vista.getTfNombre().getText();
		String apellido = this.vista.getTfApellido().getText();
		String documento = this.vista.getTfDocumento().getText();
		String telefono = this.vista.getTfTelefono().getText();
		String correo = this.vista.getTfCorreo().getText();
		String direccion = this.vista.getTfDireccion().getText();
		String nombreResponsable = this.vista.getTfNombreResponsable().getText();
		String contactoResponsable = this.vista.getTfContactoResponsable().getText();

		if (!ValidadorCampos.esObligatorio(nombre) || !ValidadorCampos.esSoloTexto(nombre)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El nombre es obligatorio y solo puede contener letras.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(apellido) || !ValidadorCampos.esSoloTexto(apellido)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El apellido es obligatorio y solo puede contener letras.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(documento) || !ValidadorCampos.esSoloNumeros(documento)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El documento es obligatorio y solo puede contener numeros.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.tieneLongitudEntre(documento, 6, 10)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El documento debe tener entre 6 y 10 digitos.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		// El telefono es opcional: solo se valida si se completo
		if (ValidadorCampos.esObligatorio(telefono)
				&& (!ValidadorCampos.esSoloNumeros(telefono) || !ValidadorCampos.tieneLongitudEntre(telefono, 9, 10))) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El telefono solo puede contener numeros y debe tener entre 9 y 10 digitos.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		// El correo es opcional: no se valida
		if (!ValidadorCampos.esObligatorio(direccion)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"La direccion es obligatoria.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(nombreResponsable) || !ValidadorCampos.esSoloTexto(nombreResponsable)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El nombre del responsable es obligatorio y solo puede contener letras.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(contactoResponsable) || !ValidadorCampos.esSoloNumeros(contactoResponsable)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El contacto del responsable es obligatorio y solo puede contener numeros.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.tieneLongitudEntre(contactoResponsable, 9, 10)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El contacto del responsable debe tener entre 9 y 10 digitos.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (FechaUtil.stringAFecha(this.vista.getTfFecha_nac().getText()) == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"La fecha de nacimiento es obligatoria y debe ser una fecha valida.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		catequizando.setCatz_fechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFecha_reg().getText()));
		catequizando.setCatz_fechaNacimiento(FechaUtil.stringAFecha(this.vista.getTfFecha_nac().getText()));
		catequizando.setCatz_nombre(nombre);
		catequizando.setCatz_apellido(apellido);
		catequizando.setCatz_documento(documento);
		catequizando.setCatz_correo(correo);
		catequizando.setCatz_telefono(telefono);
		catequizando.setCatz_direccion(direccion);
		catequizando.setCatz_nombreResponsable(nombreResponsable);
		catequizando.setCatz_contactoResponsable(contactoResponsable);
		catequizando.setCatz_estado(this.vista.getJcbEstado().isSelected());
		catequizando.setSacramentos(this.vista.getComboSacramentos().getSeleccionados());
		
		try {
			dao.guardar(catequizando);
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
	    if (catequizando == null) return;

	    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
	        this.vista,
	        "¿Está seguro que desea eliminar a: "
	            + catequizando.getCatz_nombre() + " "
	            + catequizando.getCatz_apellido() + "?",
	        "Confirmar eliminación",
	        javax.swing.JOptionPane.YES_NO_OPTION
	    );

	    if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
	        try {
	            dao.eliminar(catequizando);
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
		if(catequizando == null) this.vista.dispose();
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