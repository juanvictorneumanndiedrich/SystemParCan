package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import dao.ClaseDAO;
import dao.GrupoCatequesisDAO;
import interfaces.InterfaceABM;
import modelo.ClaseModelo;
import modelo.GrupoCatequesisModelo;
import tabla.ModeloTablaClaseGeneral;
import utilidades.FechaUtil;
import vista.ClasesVista;

/**
 * Controlador de la pantalla de Clases de acceso directo (ver ClasesVista).
 * A diferencia de ClaseController (que administra las clases de UN grupo
 * ya elegido de antemano), aca se listan las clases de TODOS los grupos y
 * el grupo de cada clase se elige con un combo del formulario.
 */
public class ClasesController implements InterfaceABM {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private ClasesVista vista;
	/**
	 * Clase en edicion o seleccionada; es {@code null} cuando no hay ninguna.
	 */
	private ClaseModelo clase;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private ClaseDAO dao;
	/**
	 * DAO auxiliar de grupos de catequesis (combos de filtro o seleccion).
	 */
	private GrupoCatequesisDAO grupoDao;
	/**
	 * Registros que muestra actualmente la tabla (ya filtrados); el indice de la fila coincide
	 * con el de esta lista.
	 */
	private List<ClaseModelo> clases;
	/**
	 * Modelo de la tabla donde se listan los registros.
	 */
	private ModeloTablaClaseGeneral tabla;

	/**
	 * Crea el controlador de la pantalla de clases de acceso directo: asocia la vista, carga los combos y la tabla
	 * y registra las acciones.
	 *
	 * @param vista pantalla {@link ClasesVista} que se va a controlar
	 */
	public ClasesController(ClasesVista vista) {
		super();
		this.vista = vista;
		this.vista.setInterfaceABM(this);

		dao = new ClaseDAO();
		grupoDao = new GrupoCatequesisDAO();
		tabla = new ModeloTablaClaseGeneral();
		this.vista.getTabla().setModel(tabla);

		cargarComboGrupos();
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	/**
	 * Llena con todos los grupos de catequesis los combos de la pantalla (grupo del formulario y filtro por grupo).
	 */
	private void cargarComboGrupos() {
		List<GrupoCatequesisModelo> grupos = grupoDao.recuperarTodo();

		this.vista.getCbGrupo().removeAllItems();
		for (GrupoCatequesisModelo grupo : grupos) {
			this.vista.getCbGrupo().addItem(grupo);
		}

		// El filtro arranca en "-- Todos los grupos --" (item null, primero
		// de la lista) y despues tiene el resto de los grupos.
		this.vista.getCbFiltroGrupo().removeAllItems();
		this.vista.getCbFiltroGrupo().addItem(null);
		for (GrupoCatequesisModelo grupo : grupos) {
			this.vista.getCbFiltroGrupo().addItem(grupo);
		}
	}

	/**
	 * Selecciona en el combo del formulario el grupo con el mismo id que el recibido (mismo motivo y patron que
	 * {@link GrupoCatequesisController}: los objetos vienen de consultas distintas y no coinciden por identidad).
	 *
	 * @param grupo grupo a seleccionar; si es {@code null} o no esta en el combo, queda sin seleccion
	 */
	// El combo se carga una sola vez con instancias propias (via grupoDao).
	// clase.getGrupoCatequesis() viene de otra consulta (ClaseDAO), por lo
	// tanto es una instancia distinta aunque represente la misma fila en la
	// base de datos. Como GrupoCatequesisModelo no tiene equals()/hashCode()
	// por ID, setSelectedItem(clase.getGrupoCatequesis()) nunca encuentra
	// coincidencia por identidad de objeto y el combo queda vacio. Por eso
	// buscamos manualmente, dentro de los items ya cargados en el combo, el
	// que tenga el mismo grup_id (mismo patron que
	// GrupoCatequesisController.seleccionarEtapaEnCombo).
	private void seleccionarGrupoEnCombo(GrupoCatequesisModelo grupo) {
		if (grupo == null) {
			this.vista.getCbGrupo().setSelectedIndex(-1);
			return;
		}

		for (int i = 0; i < this.vista.getCbGrupo().getItemCount(); i++) {
			GrupoCatequesisModelo candidato = this.vista.getCbGrupo().getItemAt(i);
			if (candidato.getGrup_id().equals(grupo.getGrup_id())) {
				this.vista.getCbGrupo().setSelectedItem(candidato);
				return;
			}
		}

		this.vista.getCbGrupo().setSelectedIndex(-1);
	}

	/**
	 * Recarga la tabla con las clases de todos los grupos, aplicando el filtro por grupo del combo ("Todos" si es
	 * {@code null}) y, si hay texto, buscandolo en la descripcion de la clase o en el nombre de su grupo.
	 *
	 * @param filtro texto a buscar; vacio o {@code null} para no filtrar por texto
	 */
	private void cargarTabla(String filtro) {
		List<ClaseModelo> todas = dao.recuperarTodo();

		// Filtro por grupo (combo cbFiltroGrupo): si esta en null se queda
		// con "-- Todos los grupos --" y no filtra nada por grupo.
		GrupoCatequesisModelo grupoFiltro = (GrupoCatequesisModelo) this.vista.getCbFiltroGrupo().getSelectedItem();
		List<ClaseModelo> porGrupo = todas.stream()
				.filter(c -> grupoFiltro == null || (c.getGrupoCatequesis() != null
						&& c.getGrupoCatequesis().getGrup_id().equals(grupoFiltro.getGrup_id())))
				.collect(Collectors.toList());

		if (filtro == null || filtro.isEmpty()) {
			clases = porGrupo;
		} else {
			String filtroLower = filtro.toLowerCase();
			clases = porGrupo.stream()
					.filter(c -> (c.getClase_descripcion() != null
							&& c.getClase_descripcion().toLowerCase().contains(filtroLower))
							|| (c.getGrupoCatequesis() != null && c.getGrupoCatequesis().getGrup_nombre() != null
									&& c.getGrupoCatequesis().getGrup_nombre().toLowerCase().contains(filtroLower)))
					.collect(Collectors.toList());
		}
		tabla.setLista(clases);
	}

	/**
	 * Registra los listeners: doble clic en la tabla selecciona la clase, el campo de busqueda filtra en tiempo real
	 * y el combo de filtro por grupo vuelve a ejecutar la busqueda actual.
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

		// Cambiar el filtro de grupo re-ejecuta la busqueda actual (respeta
		// lo que haya escrito en el Buscador, igual que hace buscar()).
		this.vista.getCbFiltroGrupo().addActionListener(e -> buscar());
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

		this.vista.getCbGrupo().setEnabled(false);
		this.vista.getTfFecha().setEnabled(false);
		this.vista.getTfDescripcion().setEnabled(false);

		this.vista.getCbGrupo().setSelectedIndex(-1);
		this.vista.getTfFecha().setValue(null);
		this.vista.getTfDescripcion().setText("");

		clase = null;
	}

	/**
	 * Toma la fila elegida en la tabla como registro actual y habilita Editar y Eliminar.
	 * Todavia no carga los datos en el formulario; eso ocurre al llamar a {@link #editar()}.
	 */
	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		clase = clases.get(fila);

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

		this.vista.getCbGrupo().setEnabled(true);
		this.vista.getTfFecha().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);

		this.vista.getCbGrupo().setSelectedIndex(-1);
		this.vista.getTfDescripcion().setText("");
		// Por comodidad, la fecha arranca en el dia de hoy; el usuario la puede cambiar.
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));

		clase = new ClaseModelo();
	}

	/**
	 * Carga los datos del registro seleccionado en el formulario y habilita los campos
	 * para modificarlos. No hace nada si no hay un registro seleccionado.
	 */
	@Override
	public void editar() {
		if (clase == null) return;

		seleccionarGrupoEnCombo(clase.getGrupoCatequesis());
		this.vista.getTfFecha().setText(
				clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase()) : "");
		this.vista.getTfDescripcion().setText(clase.getClase_descripcion());

		this.vista.getCbGrupo().setEnabled(true);
		this.vista.getTfFecha().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnCancelar().setEnabled(true);
	}

	/**
	 * Valida los datos ingresados y guarda la clase con el grupo elegido en el combo; luego recarga la tabla.
	 */
	@Override
	public void guardar() {
		GrupoCatequesisModelo grupoElegido = (GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem();
		if (grupoElegido == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Elija un grupo de catequesis para la clase.",
					"Falta el grupo", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		String textoFecha = this.vista.getTfFecha().getText();
		LocalDate fecha = FechaUtil.stringAFecha(textoFecha);

		if (fecha == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Ingrese una fecha valida (dd/mm/aaaa).",
					"Fecha invalida", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		clase.setGrupoCatequesis(grupoElegido);
		clase.setClase_fechaClase(fecha);
		clase.setClase_descripcion(this.vista.getTfDescripcion().getText());

		try {
			dao.guardar(clase);
		} catch (Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Error al guardar: " + e.getMessage());
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
		if (clase == null) return;

		int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
			this.vista,
			"Esta seguro que desea eliminar la clase del "
				+ (clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase()) : "")
				+ "?",
			"Confirmar eliminacion",
			javax.swing.JOptionPane.YES_NO_OPTION
		);

		if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
			try {
				dao.eliminar(clase);
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
		if (clase == null) this.vista.dispose();
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