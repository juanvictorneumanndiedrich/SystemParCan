package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequistaDAO;
import dao.EtapaDAO;
import dao.GrupoCatequesisDAO;
import interfaces.InterfaceABM;
import validaciones.ValidadorCampos;
import modelo.EtapaModelo;
import modelo.GrupoCatequesisModelo;
import tabla.ModeloTablaGrupoCatequesis;
import utilidades.FechaUtil;
import vista.ClaseVista;
import vista.GrupoCatequesisVista;

/**
 * Controlador del ABM de Grupos de Catequesis. Conecta {@link GrupoCatequesisVista} con {@link GrupoCatequesisDAO}:
 * lista los grupos en la tabla, permite buscarlos por nombre en tiempo real y gestiona alta, modificacion y baja,
 * incluyendo la etapa y los catequistas de cada grupo. Desde aqui tambien se abre la pantalla de clases del grupo
 * seleccionado ({@link ClaseController}).
 * 
 * Implementa {@link InterfaceABM}.
 */
public class GrupoCatequesisController implements InterfaceABM {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private GrupoCatequesisVista vista;
	/**
	 * Grupo en edicion o seleccionado; es {@code null} cuando no hay ninguno.
	 */
	private GrupoCatequesisModelo grupo;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private GrupoCatequesisDAO dao;
	/**
	 * DAO auxiliar para cargar las etapas en el combo.
	 */
	private EtapaDAO etapaDao;
	/**
	 * DAO auxiliar para cargar los catequistas en el combo.
	 */
	private CatequistaDAO catequistaDao;
	/**
	 * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
	 * con el de esta lista.
	 */
	private List<GrupoCatequesisModelo> grupos;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaGrupoCatequesis tabla;

	/**
	 * Crea el controlador: asocia la vista ({@code setInterfaceABM}), instancia los DAO y el modelo de tabla,
	 * carga los datos iniciales y registra las acciones de la pantalla.
	 *
	 * @param vista pantalla GrupoCatequesisVista que se va a controlar
	 */
	public GrupoCatequesisController(GrupoCatequesisVista grupoCatequesisVista) {
		super();
		this.vista = grupoCatequesisVista;
		this.vista.setInterfaceABM(this);
		dao = new GrupoCatequesisDAO();
		etapaDao = new EtapaDAO();
		catequistaDao = new CatequistaDAO();
		tabla = new ModeloTablaGrupoCatequesis();
		this.vista.getTabla().setModel(tabla);

		cargarCombos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	/**
	 * Llena el combo de etapas y configura el combo de catequistas (texto visible = nombre y apellido, clave = id)
	 * con todos los catequistas.
	 */
	private void cargarCombos() {
		this.vista.getCbEtapa().removeAllItems();
		for (EtapaModelo etapa : etapaDao.recuperarTodo()) {
			this.vista.getCbEtapa().addItem(etapa);
		}

		this.vista.getComboCatequistas().setProveedorTexto(
				c -> c.getCat_nombre() + " " + c.getCat_apellido());
		this.vista.getComboCatequistas().setExtractorClave(c -> c.getCat_id());
		this.vista.getComboCatequistas().setItems(catequistaDao.recuperarTodo());
	}

	/**
	 * Selecciona en el combo de etapas la que tiene el mismo id que la recibida. Hace falta porque el combo y el grupo
	 * viene de consultas distintas (instancias distintas) y {@code EtapaModelo} no define equals por id, asi que
	 * {@code setSelectedItem} directo no encontraria coincidencia.
	 *
	 * @param etapa etapa a seleccionar; si es {@code null} o no esta en el combo, queda sin seleccion
	 */
	// El combo de Etapa se carga una sola vez con instancias propias (via etapaDao).
	// grupo.getEtapa() viene de otra consulta (GrupoCatequesisDAO), por lo tanto es
	// una instancia distinta aunque represente la misma fila en la base de datos.
	// Como EtapaModelo no tiene equals()/hashCode() por ID, setSelectedItem(grupo.getEtapa())
	// nunca encuentra coincidencia por identidad de objeto y el combo queda vacio.
	// Por eso buscamos manualmente, dentro de los items ya cargados en el combo,
	// el que tenga el mismo etap_id.
	private void seleccionarEtapaEnCombo(EtapaModelo etapa) {
		if (etapa == null) {
			this.vista.getCbEtapa().setSelectedIndex(-1);
			return;
		}

		for (int i = 0; i < this.vista.getCbEtapa().getItemCount(); i++) {
			EtapaModelo candidata = this.vista.getCbEtapa().getItemAt(i);
			if (candidata.getEtap_id().equals(etapa.getEtap_id())) {
				this.vista.getCbEtapa().setSelectedItem(candidata);
				return;
			}
		}

		this.vista.getCbEtapa().setSelectedIndex(-1);
	}

	/**
	 * Recarga la tabla con los grupos. Si hay filtro, solo quedan los que tienen ese texto en el nombre
	 * (sin distinguir mayusculas).
	 *
	 * @param filtro texto a buscar en el nombre; vacio o {@code null} para traer todos
	 */
	private void cargarTabla(String filtro) {
		if (filtro == null || filtro.isEmpty()) {
			grupos = dao.recuperarTodo();
		} else {
			grupos = dao.recuperarTodo().stream()
				.filter(g -> g.getGrup_nombre().toLowerCase()
				.contains(filtro.toLowerCase()))
				.collect(java.util.stream.Collectors.toList());
		}
		tabla.setLista(grupos);
	}

	/**
	 * Registra los listeners: doble clic en la tabla selecciona el grupo, el campo de busqueda filtra en tiempo real
	 * y el boton Ver Clases abre las clases del grupo seleccionado.
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

		this.vista.getBtnVerClases().addActionListener(e -> verClases());
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
		this.vista.getBtnVerClases().setEnabled(false);

		// desactiva los campos
		this.vista.getTfNombre().setEnabled(false);
		this.vista.getTfAnho().setEnabled(false);
		this.vista.getCbEtapa().setEnabled(false);
		this.vista.getComboCatequistas().setEnabled(false);

		// limpia los campos
		this.vista.getTfNombre().setText("");
		this.vista.getTfAnho().setValue(null);
		this.vista.getCbEtapa().setSelectedIndex(-1);
		this.vista.getComboCatequistas().limpiarSeleccion();

		grupo = null;
	}

	/**
	 * Toma el grupo elegido en la tabla como grupo actual y habilita Editar, Eliminar y Ver Clases.
	 */
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		grupo = grupos.get(fila);

		this.vista.getBtnEditar().setEnabled(true);
		this.vista.getBtnEliminar().setEnabled(true);
		this.vista.getBtnVerClases().setEnabled(true);
	}

	/**
	 * Abre la pantalla de clases ({@link ClaseController}) para el grupo seleccionado en la tabla. Si no hay un grupo
	 * seleccionado muestra un aviso.
	 */
	// Abre la pantalla de Clases para el grupo seleccionado en la tabla.
	// El grupo viaja fijo al ClaseController: esa pantalla no tiene combo
	// de grupo, toda clase que se cree ahi queda asociada a este grupo.
	private void verClases() {
		if (grupo == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Seleccione un grupo en la tabla para ver sus clases.");
			return;
		}

		ClaseVista claseVista = new ClaseVista();
		new ClaseController(claseVista, grupo);
		claseVista.setLocationRelativeTo(this.vista);
		claseVista.setVisible(true);
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
		this.vista.getBtnVerClases().setEnabled(false);

		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfAnho().setEnabled(true);
		this.vista.getCbEtapa().setEnabled(true);
		this.vista.getComboCatequistas().setEnabled(true);

		this.vista.getCbEtapa().setSelectedIndex(-1);
		this.vista.getComboCatequistas().limpiarSeleccion();

		grupo = new GrupoCatequesisModelo();
		this.vista.getTfAnho().setText(FechaUtil.fechaAString(LocalDate.now()));
	}

	/**
	 * Carga los datos del registro seleccionado en el formulario y habilita los campos
	 * para modificarlos. No hace nada si no hay un registro seleccionado.
	 */
	@Override
	public void editar() {
		if (grupo == null) return;

		// Cargar datos en los campos
		this.vista.getTfNombre().setText(grupo.getGrup_nombre());
		this.vista.getTfAnho().setText(FechaUtil.fechaAString(grupo.getGrup_anho()));
		seleccionarEtapaEnCombo(grupo.getEtapa());
		this.vista.getComboCatequistas().setSeleccionados(grupo.getCatequistas());

		// Habilitar campos para edición
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfAnho().setEnabled(true);
		this.vista.getCbEtapa().setEnabled(true);
		this.vista.getComboCatequistas().setEnabled(true);

		// Ajustar botones
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnVerClases().setEnabled(false);
	}

	/**
	 * Valida los datos ingresados con {@link ValidadorCampos}; ante el primer dato invalido muestra un aviso y no guarda.
	 * Si todo es correcto pasa los datos al modelo (incluidos la etapa y los catequistas elegidos), lo guarda con el DAO
	 * y recarga la tabla.
	 */
	@Override
	public void guardar() {
		String nombre = this.vista.getTfNombre().getText();

		if (!ValidadorCampos.esObligatorio(nombre)) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"El nombre del grupo es obligatorio.",
					"Dato invalido", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		grupo.setGrup_nombre(nombre);
		grupo.setGrup_anho(FechaUtil.stringAFecha(this.vista.getTfAnho().getText()));
		grupo.setEtapa((EtapaModelo) this.vista.getCbEtapa().getSelectedItem());
		grupo.setCatequistas(this.vista.getComboCatequistas().getSeleccionados());

		try {
			dao.guardar(grupo);
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
		if (grupo == null) return;

		int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
			this.vista,
			"¿Está seguro que desea eliminar el grupo: "
				+ grupo.getGrup_nombre() + "?",
			"Confirmar eliminación",
			javax.swing.JOptionPane.YES_NO_OPTION
		);

		if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(grupo);
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
		if (grupo == null) this.vista.dispose();
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