package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequistaDAO;
import dao.CatequizandoDAO;
import dao.SacramentoDAO;
import interfaces.InterfaceABM;
import validaciones.ValidadorCampos;
import modelo.CatequistaModelo;
import modelo.CatequizandoModelo;
import tabla.EstadoCellRenderer;
import tabla.ModeloTablaCatequista;
import tabla.ModeloTablaCatequizando;
import utilidades.FechaUtil;
import vista.CatequistaVista;


/**
 * Controlador del ABM de Catequistas. Conecta {@link CatequistaVista} con {@link CatequistaDAO}:
 * lista los catequistas en la tabla, permite buscarlos por nombre en tiempo real y gestiona
 * alta, modificacion y baja, incluyendo los sacramentos que tiene cada catequista.
 * 
 * Implementa {@link InterfaceABM}.
 */
public class CatequistaController  implements InterfaceABM{

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private CatequistaVista vista;
	/**
	 * Catequista en edicion o seleccionado; es {@code null} cuando no hay ninguno.
	 */
	private CatequistaModelo catequista;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private CatequistaDAO dao;
	/**
	 * DAO auxiliar para cargar los sacramentos en el combo.
	 */
	private SacramentoDAO sacramentoDao;
	/**
	 * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
	 * con el de esta lista.
	 */
	private List<CatequistaModelo> catequistas;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaCatequista tabla;
	/**
	 * Crea el controlador: asocia la vista ({@code setInterfaceABM}), instancia los DAO y el modelo de tabla,
	 * carga los datos iniciales y registra las acciones de la pantalla.
	 *
	 * @param vista pantalla CatequistaVista que se va a controlar
	 */
	public CatequistaController(CatequistaVista catequistaVista) {
		super();
		this.vista = catequistaVista;
		this.vista.setInterfaceABM(this);
		dao = new CatequistaDAO();
		sacramentoDao = new SacramentoDAO();
		tabla = new ModeloTablaCatequista();
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
	 * Recarga la tabla con los catequistas. Si hay filtro, solo quedan los que tienen ese texto en el nombre
	 * (sin distinguir mayusculas).
	 *
	 * @param filtro texto a buscar en el nombre; vacio o {@code null} para traer todos
	 */
	private void cargarTabla(String filtro) {
	    if (filtro == null || filtro.isEmpty()) {
	        catequistas = dao.recuperarTodo();
	    } else {
	        catequistas = dao.recuperarTodo().stream()
	            .filter(c -> c.getCat_nombre().toLowerCase()
	            .contains(filtro.toLowerCase()))
	            .collect(java.util.stream.Collectors.toList());
	    }
	    tabla.setLista(catequistas);
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
		this.vista.getComboSacramentos().limpiarSeleccion();
		catequista = null;

	}
	
	
	
	/**
	 * Prepara la pantalla para un catequista nuevo: habilita los campos, crea un {@link CatequistaModelo} vacio
	 * y precarga la fecha de registro con la fecha de hoy.
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
				this.vista.getCbEstado().setEnabled(true);
				this.vista.getComboSacramentos().setEnabled(true);
				this.vista.getComboSacramentos().limpiarSeleccion();
				
				//Carga el campo fecha y crea el cliente
				catequista = new CatequistaModelo();
				this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(LocalDate.now()));
		
	}
	
	/**
	 * Toma la fila elegida en la tabla como registro actual y habilita Editar y Eliminar.
	 * Todavia no carga los datos en el formulario; eso ocurre al llamar a {@link #editar()}.
	 */
	private void seleccionarRegistro() {
	    int fila = this.vista.getTabla().getSelectedRow();
	    if (fila < 0) return;
	    catequista = catequistas.get(fila);

	    // Solo habilitar botones, sin cargar campos
	    this.vista.getBtnEditar().setEnabled(true);
	    this.vista.getBtnEliminar().setEnabled(true);
	}
	
	
	/**
	 * Carga los datos del catequista seleccionado (incluidos sus sacramentos) en el formulario y habilita los campos.
	 * No hace nada si no hay un catequista seleccionado.
	 */
	@Override
	public void editar() {
		if (catequista == null) return;
		
		// Cargar datos en los campos
	    this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(catequista.getCat_fechaRegistro()));
	    this.vista.getTfNombre().setText(catequista.getCat_nombre());
	    this.vista.getTfApellido().setText(catequista.getCat_apellido());
	    this.vista.getTfDocumento().setText(catequista.getCat_documento());
	    this.vista.getTfTelefono().setText(catequista.getCat_telefono());
	    this.vista.getTfCorreo().setText(catequista.getCat_correo());
	    this.vista.getTfFecha_nac().setText(FechaUtil.fechaAString(catequista.getCat_fechaNacimiento()));
	    this.vista.getTfDireccion().setText(catequista.getCat_direccion());
	    this.vista.getCbEstado().setSelected(catequista.isCat_estado());
	    this.vista.getComboSacramentos().setSeleccionados(catequista.getSacramentos());

	    // Habilitar campos para edición
	    this.vista.getTfNombre().setEnabled(true);
	    this.vista.getTfApellido().setEnabled(true);
	    this.vista.getTfDocumento().setEnabled(true);
	    this.vista.getTfFecha_nac().setEnabled(true);
	    this.vista.getTfCorreo().setEnabled(true);
	    this.vista.getTfDireccion().setEnabled(true);
	    this.vista.getTfTelefono().setEnabled(true);
	    this.vista.getCbEstado().setEnabled(true);
	    this.vista.getComboSacramentos().setEnabled(true);

	    // Ajustar botones
	    this.vista.getBtnNuevo().setEnabled(false);
	    this.vista.getBtnEditar().setEnabled(false);
	    this.vista.getBtnEliminar().setEnabled(false);
	    this.vista.getBtnGuardar().setEnabled(true);
	    this.vista.getBtnCancelar().setEnabled(true);
		
	}
	/**
	 * Elimina el registro seleccionado previa confirmacion del usuario. Si la baja falla
	 * (por ejemplo, por registros relacionados) muestra el error y no recarga la tabla.
	 */
	@Override
	public void eliminar() {
		if (catequista == null) return;

	    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
	        this.vista,
	        "¿Está seguro que desea eliminar a: "
	            + catequista.getCat_nombre() + " "
	            + catequista.getCat_apellido() + "?",
	        "Confirmar eliminación",
	        javax.swing.JOptionPane.YES_NO_OPTION
	    );

	    if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
	        try {
	            dao.eliminar(catequista);
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
		if(catequista == null) this.vista.dispose();
		else estadoInicial();
		
	}
	/**
	 * Valida los datos con {@link ValidadorCampos} (nombre y apellido: obligatorios y solo letras; documento y telefono:
	 * obligatorios y solo numeros; correo y direccion: obligatorios). Ante el primer dato invalido muestra un aviso y
	 * no guarda. Si todo es correcto pasa los datos al modelo, lo guarda con el DAO y recarga la tabla.
	 */
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText();
		String apellido = this.vista.getTfApellido().getText();
		String documento = this.vista.getTfDocumento().getText();
		String telefono = this.vista.getTfTelefono().getText();
		String correo = this.vista.getTfCorreo().getText();
		String direccion = this.vista.getTfDireccion().getText();

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
		if (!ValidadorCampos.esObligatorio(telefono) || !ValidadorCampos.esSoloNumeros(telefono)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El telefono es obligatorio y solo puede contener numeros.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(correo)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El correo es obligatorio.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}
		if (!ValidadorCampos.esObligatorio(direccion)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"La direccion es obligatoria.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		catequista.setCat_fechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFecha_reg().getText()));
		catequista.setCat_fechaNacimiento(FechaUtil.stringAFecha(this.vista.getTfFecha_nac().getText()));
		catequista.setCat_nombre(nombre);
		catequista.setCat_apellido(apellido);
		catequista.setCat_documento(documento);
		catequista.setCat_correo(correo);
		catequista.setCat_telefono(telefono);
		catequista.setCat_direccion(direccion);
		catequista.setCat_estado(this.vista.getCbEstado().isSelected());
		catequista.setSacramentos(this.vista.getComboSacramentos().getSeleccionados());
		
		try {
			dao.guardar(catequista);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		estadoInicial();
		cargarTabla("");

		
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