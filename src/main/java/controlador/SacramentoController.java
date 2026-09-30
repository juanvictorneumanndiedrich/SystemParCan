package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import dao.SacramentoDAO;
import interfaces.InterfaceABM;
import validaciones.ValidadorCampos;
import modelo.SacramentoModelo;
import tabla.ModeloTablaSacramento;
import vista.SacramentoVista;

/**
 * Controlador del ABM de Sacramentos. Conecta {@link SacramentoVista} con {@link SacramentoDAO}:
 * lista los sacramentos en la tabla, permite buscarlos por nombre en tiempo real y gestiona
 * alta, modificacion y baja.
 * 
 * Implementa {@link InterfaceABM}.
 */
public class SacramentoController implements InterfaceABM {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private SacramentoVista vista;
	/**
	 * Sacramento en edicion o seleccionado; es {@code null} cuando no hay ninguno.
	 */
	private SacramentoModelo sacramento;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private SacramentoDAO dao;
	/**
	 * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
	 * con el de esta lista.
	 */
	private List<SacramentoModelo> sacramentos;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaSacramento tabla;

	/**
	 * Crea el controlador: asocia la vista ({@code setInterfaceABM}), instancia los DAO y el modelo de tabla,
	 * carga los datos iniciales y registra las acciones de la pantalla.
	 *
	 * @param vista pantalla SacramentoVista que se va a controlar
	 */
	public SacramentoController(SacramentoVista sacramentoVista) {
		super();
		this.vista = sacramentoVista;
		this.vista.setInterfaceABM(this);
		dao = new SacramentoDAO();
		tabla = new ModeloTablaSacramento();
		this.vista.getTabla().setModel(tabla);

		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	/**
	 * Recarga la tabla con los sacramentos. Si hay filtro, solo quedan los que tienen ese texto en el nombre
	 * (sin distinguir mayusculas).
	 *
	 * @param filtro texto a buscar en el nombre; vacio o {@code null} para traer todos
	 */
	private void cargarTabla(String filtro) {
		if (filtro == null || filtro.isEmpty()) {
			sacramentos = dao.recuperarTodo();
		} else {
			sacramentos = dao.recuperarTodo().stream()
				.filter(s -> s.getSacr_nombre().toLowerCase()
				.contains(filtro.toLowerCase()))
				.collect(java.util.stream.Collectors.toList());
		}
		tabla.setLista(sacramentos);
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
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);

		// desactiva el campo
		this.vista.getTfNombre().setEnabled(false);

		// limpia el campo
		this.vista.getTfNombre().setText("");

		sacramento = null;
	}

	/**
	 * Toma la fila elegida en la tabla como registro actual y habilita Editar y Eliminar.
	 * Todavia no carga los datos en el formulario; eso ocurre al llamar a {@link #editar()}.
	 */
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		sacramento = sacramentos.get(fila);

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

		this.vista.getTfNombre().setEnabled(true);

		sacramento = new SacramentoModelo();
	}

	/**
	 * Carga los datos del registro seleccionado en el formulario y habilita los campos
	 * para modificarlos. No hace nada si no hay un registro seleccionado.
	 */
	@Override
	public void editar() {
		if (sacramento == null) return;

		// Cargar datos en los campos
		this.vista.getTfNombre().setText(sacramento.getSacr_nombre());

		// Habilitar campo para edición
		this.vista.getTfNombre().setEnabled(true);

		// Ajustar botones
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnCancelar().setEnabled(true);
	}

	/**
	 * Valida los datos con {@link ValidadorCampos}; ante el primer dato invalido muestra un aviso y no guarda.
	 * Si todo es correcto pasa los datos al modelo, lo guarda con el DAO y recarga la tabla.
	 */
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText();

		if (!ValidadorCampos.esObligatorio(nombre) || !ValidadorCampos.esSoloTexto(nombre)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El nombre del sacramento es obligatorio y solo puede contener letras.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		sacramento.setSacr_nombre(nombre.trim());

		try {
			dao.guardar(sacramento);
		} catch (Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(
				this.vista,
				"No se pudo guardar. Verifique que no exista otro sacramento con el mismo nombre."
			);
			return;
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
		if (sacramento == null) return;

		int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
			this.vista,
			"¿Está seguro que desea eliminar el sacramento: "
				+ sacramento.getSacr_nombre() + "?",
			"Confirmar eliminación",
			javax.swing.JOptionPane.YES_NO_OPTION
		);

		if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(sacramento);
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
		if (sacramento == null) this.vista.dispose();
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