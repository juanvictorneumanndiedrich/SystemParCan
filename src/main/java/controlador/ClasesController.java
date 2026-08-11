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

	private ClasesVista vista;
	private ClaseModelo clase;
	private ClaseDAO dao;
	private GrupoCatequesisDAO grupoDao;
	private List<ClaseModelo> clases;
	private ModeloTablaClaseGeneral tabla;

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

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		clase = clases.get(fila);

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

		this.vista.getCbGrupo().setEnabled(true);
		this.vista.getTfFecha().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);

		this.vista.getCbGrupo().setSelectedIndex(-1);
		this.vista.getTfDescripcion().setText("");
		// Por comodidad, la fecha arranca en el dia de hoy; el usuario la puede cambiar.
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));

		clase = new ClaseModelo();
	}

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

	@Override
	public void cancelar() {
		if (clase == null) this.vista.dispose();
		else estadoInicial();
	}

	@Override
	public void buscar() {
		String filtro = this.vista.getTfBuscador().getText().trim();
		cargarTabla(filtro);
	}

}