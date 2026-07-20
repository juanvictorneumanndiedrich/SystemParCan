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
import dao.InscripcionDAO;
import modelo.AsistenciaModelo;
import modelo.ClaseModelo;
import modelo.EstadoAsistencia;
import modelo.InscripcionModelo;
import tabla.ModeloTablaAsistencia;
import utilidades.FechaUtil;
import vista.AsistenciaVista;

// Pantalla contextual: siempre se abre para una Clase puntual ya elegida
// (ver ClaseController.tomarAsistencia()). No implementa InterfaceABM como
// los demas controladores porque aca no se edita "un registro a la vez":
// se marca el estado de cada catequizando inscripto en el grupo, para esa
// clase, y se guarda todo junto con un unico boton Guardar.
public class AsistenciaController {

	private AsistenciaVista vista;
	private ClaseModelo clase;
	private AsistenciaDAO dao;
	private InscripcionDAO inscripcionDao;
	private ModeloTablaAsistencia tabla;

	public AsistenciaController(AsistenciaVista vista, ClaseModelo clase) {
		super();
		this.vista = vista;
		this.clase = clase;

		dao = new AsistenciaDAO();
		inscripcionDao = new InscripcionDAO();
		tabla = new ModeloTablaAsistencia();

		String nombreGrupo = clase.getGrupoCatequesis() != null ? clase.getGrupoCatequesis().getGrup_nombre() : "";
		String fechaClase = clase.getClase_fechaClase() != null
				? FechaUtil.fechaAString(clase.getClase_fechaClase())
				: "";
		this.vista.setTitle("Asistencia - " + nombreGrupo);
		this.vista.getLblInfoClase().setText("Grupo: " + nombreGrupo + "     Clase del: " + fechaClase);

		this.vista.getTabla().setModel(tabla);
		configurarColumnaEstado();

		cargarTabla();
		setAcciones();
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

	private void cargarTabla() {
		// Inscripciones activas del grupo de esta clase.
		List<InscripcionModelo> inscripciones = inscripcionDao.recuperarTodo().stream()
				.filter(i -> i.getGrupoCatequesis() != null
						&& i.getGrupoCatequesis().getGrup_id().equals(clase.getGrupoCatequesis().getGrup_id()))
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.collect(Collectors.toList());

		// Asistencias que ya se hayan guardado antes para esta clase puntual.
		List<AsistenciaModelo> existentes = dao.recuperarTodo().stream()
				.filter(a -> a.getClase() != null && a.getClase().getClase_id().equals(clase.getClase_id()))
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
				nueva.setClase(clase);
				filas.add(nueva);
			}
		}

		tabla.setLista(filas);
	}

	private void setAcciones() {
		this.vista.getBtnGuardar().addActionListener(e -> guardar());
		this.vista.getBtnCerrar().addActionListener(e -> this.vista.dispose());
	}

	private void guardar() {
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

		cargarTabla();
	}

}