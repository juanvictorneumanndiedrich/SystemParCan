package controlador;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import dao.AsistenciaDAO;
import dao.ClaseDAO;
import dao.GrupoCatequesisDAO;
import dao.InscripcionDAO;
import modelo.AsistenciaModelo;
import modelo.ClaseModelo;
import modelo.EstadoAsistencia;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;
import tabla.ModeloTablaAsistencia;
import vista.AsistenciasVista;

/**
 * Controlador de la pantalla de Asistencia de acceso directo (ver
 * AsistenciasVista). A diferencia de AsistenciaController (que siempre
 * recibe la Clase ya elegida desde afuera), aca la clase se elige adentro
 * de la propia pantalla con dos combos en cascada: Grupo de Catequesis y,
 * segun el grupo elegido, Clase. Una vez elegida la clase, la grilla y el
 * guardado funcionan exactamente igual que en AsistenciaController.
 */
public class AsistenciasController {

	private AsistenciasVista vista;
	private ClaseDAO claseDao;
	private GrupoCatequesisDAO grupoDao;
	private AsistenciaDAO dao;
	private InscripcionDAO inscripcionDao;
	private ModeloTablaAsistencia tabla;
	private ClaseModelo claseSeleccionada;

	public AsistenciasController(AsistenciasVista vista) {
		super();
		this.vista = vista;

		claseDao = new ClaseDAO();
		grupoDao = new GrupoCatequesisDAO();
		dao = new AsistenciaDAO();
		inscripcionDao = new InscripcionDAO();
		tabla = new ModeloTablaAsistencia();
		this.vista.getTabla().setModel(tabla);
		configurarColumnaEstado();

		this.vista.getBtnGuardar().setEnabled(false);

		cargarComboGrupos();
		setAcciones();

		// cargarComboGrupos() ya deja seleccionado el primer grupo (asi
		// funciona JComboBox al agregar items), pero eso paso ANTES de
		// engachar el listener de cbGrupo en setAcciones(), asi que esa
		// primera seleccion nunca disparaba la carga del combo de Clase.
		// Por eso hace falta cargarlo una vez mas aca, a mano, ya con todo
		// conectado.
		cargarComboClases((GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem());
	}

	private void cargarComboGrupos() {
		this.vista.getCbGrupo().removeAllItems();
		for (GrupoCatequesisModelo grupo : grupoDao.recuperarTodo()) {
			this.vista.getCbGrupo().addItem(grupo);
		}
	}

	private void cargarComboClases(GrupoCatequesisModelo grupo) {
		JComboBox<ClaseModelo> cbClase = this.vista.getCbClase();
		cbClase.removeAllItems();
		claseSeleccionada = null;
		tabla.setLista(new ArrayList<AsistenciaModelo>());
		this.vista.getBtnGuardar().setEnabled(false);

		if (grupo == null) {
			cbClase.setEnabled(false);
			return;
		}

		List<ClaseModelo> clasesDelGrupo = claseDao.recuperarTodo().stream()
				.filter(c -> c.getGrupoCatequesis() != null
						&& c.getGrupoCatequesis().getGrup_id().equals(grupo.getGrup_id()))
				.collect(Collectors.toList());

		cbClase.setEnabled(!clasesDelGrupo.isEmpty());
		for (ClaseModelo clase : clasesDelGrupo) {
			cbClase.addItem(clase);
		}
	}

	private void setAcciones() {
		this.vista.getCbGrupo().addActionListener(e -> {
			GrupoCatequesisModelo grupo = (GrupoCatequesisModelo) this.vista.getCbGrupo().getSelectedItem();
			cargarComboClases(grupo);
		});

		this.vista.getCbClase().addActionListener(e -> {
			claseSeleccionada = (ClaseModelo) this.vista.getCbClase().getSelectedItem();
			if (claseSeleccionada != null) {
				cargarTablaAsistencia();
				this.vista.getBtnGuardar().setEnabled(true);
			} else {
				tabla.setLista(new ArrayList<AsistenciaModelo>());
				this.vista.getBtnGuardar().setEnabled(false);
			}
		});

		this.vista.getBtnGuardar().addActionListener(e -> guardar());
		this.vista.getBtnCerrar().addActionListener(e -> this.vista.dispose());
	}

	// El combo arranca en null ("-- Seleccionar --") para forzar a elegir el
	// estado de cada catequizando; no se pre-completa nada como "Presente".
	private void configurarColumnaEstado() {
		JComboBox<EstadoAsistencia> comboEstado = new JComboBox<EstadoAsistencia>();
		comboEstado.addItem(null);
		for (EstadoAsistencia estado : EstadoAsistencia.values()) {
			comboEstado.addItem(estado);
		}
		comboEstado.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setText(textoEstado((EstadoAsistencia) value));
				return this;
			}
		});

		this.vista.getTabla().getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboEstado));
		this.vista.getTabla().getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				setText(textoEstado((EstadoAsistencia) value));
				return this;
			}
		});
	}

	private String textoEstado(EstadoAsistencia estado) {
		if (estado == null) return "-- Seleccionar --";
		switch (estado) {
		case PRESENTE:
			return "Presente";
		case AUSENTE:
			return "Ausente";
		case JUSTIFICADO:
			return "Justificado";
		}
		return "";
	}

	private void cargarTablaAsistencia() {
		// Inscripciones activas del grupo de la clase seleccionada.
		List<InscripcionModelo> inscripciones = inscripcionDao.recuperarTodo().stream()
				.filter(i -> i.getGrupoCatequesis() != null && claseSeleccionada.getGrupoCatequesis() != null
						&& i.getGrupoCatequesis().getGrup_id()
								.equals(claseSeleccionada.getGrupoCatequesis().getGrup_id()))
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.collect(Collectors.toList());

		// Asistencias que ya se hayan guardado antes para esta clase puntual.
		List<AsistenciaModelo> existentes = dao.recuperarTodo().stream()
				.filter(a -> a.getClase() != null && a.getClase().getClase_id().equals(claseSeleccionada.getClase_id()))
				.collect(Collectors.toList());

		List<AsistenciaModelo> filas = new ArrayList<AsistenciaModelo>();
		for (InscripcionModelo inscripcion : inscripciones) {
			AsistenciaModelo existente = existentes.stream()
					.filter(a -> a.getInscripcion() != null
							&& a.getInscripcion().getInscrip_id().equals(inscripcion.getInscrip_id()))
					.findFirst()
					.orElse(null);

			if (existente != null) {
				filas.add(existente);
			} else {
				AsistenciaModelo nueva = new AsistenciaModelo();
				nueva.setInscripcion(inscripcion);
				nueva.setClase(claseSeleccionada);
				filas.add(nueva);
			}
		}

		tabla.setLista(filas);
	}

	private void guardar() {
		if (claseSeleccionada == null) return;

		// Si quedo una celda en edicion (el usuario no apreto Enter/Tab), confirmarla
		// antes de leer los valores; si no, el ultimo cambio se pierde.
		if (this.vista.getTabla().isEditing()) {
			this.vista.getTabla().getCellEditor().stopCellEditing();
		}

		List<AsistenciaModelo> filas = tabla.getLista();

		// estado es NOT NULL en la base: solo se guardan las filas ya marcadas.
		// Las que quedan sin marcar simplemente no se persisten todavia; se
		// puede volver a abrir esta pantalla mas tarde para completarlas.
		List<AsistenciaModelo> marcadas = filas.stream()
				.filter(a -> a.getEstado() != null)
				.collect(Collectors.toList());

		try {
			for (AsistenciaModelo asistencia : marcadas) {
				dao.guardar(asistencia);
			}
		} catch (Exception e) {
			e.printStackTrace();
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Error al guardar la asistencia: " + e.getMessage());
			return;
		}

		int sinMarcar = filas.size() - marcadas.size();
		String mensaje = "Asistencia guardada (" + marcadas.size() + " de " + filas.size() + ").";
		if (sinMarcar > 0) {
			mensaje += "\nQuedan " + sinMarcar + " catequizando(s) sin marcar.";
		}
		javax.swing.JOptionPane.showMessageDialog(this.vista, mensaje);

		cargarTablaAsistencia();
	}

}