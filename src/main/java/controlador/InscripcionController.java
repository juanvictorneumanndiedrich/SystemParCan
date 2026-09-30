package controlador;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dao.CatequizandoDAO;
import dao.GrupoCatequesisDAO;
import dao.InscripcionDAO;
import modelo.CatequizandoModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;
import componentes.*;
import tabla.ModeloTablaGrupoCatequesis;
import tabla.ModeloTablaInscripcion;
import vista.InscripcionVista;

/**
 * Controlador de la pantalla de Inscripcion. A la izquierda se elige un grupo de catequesis y a la derecha se marcan
 * con un check los catequizandos que se quieren inscribir en el. Se muestran los catequizandos sin ninguna inscripcion
 * (candidatos) y los ya inscriptos en ese mismo grupo (bloqueados, no se pueden destildar desde aqui).
 * 
 * No implementa {@link interfaces.InterfaceABM}: no es un ABM, solo da de alta inscripciones nuevas.
 */
public class InscripcionController {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private InscripcionVista vista;
	/**
	 * DAO principal de la entidad que administra esta pantalla.
	 */
	private InscripcionDAO dao;
	/**
	 * DAO auxiliar de catequizandos.
	 */
	private CatequizandoDAO catequizandoDao;
	/**
	 * DAO auxiliar de grupos de catequesis.
	 */
	private GrupoCatequesisDAO grupoCatequesisDao;

	/**
	 * Modelo de la tabla de grupos (izquierda).
	 */
	private ModeloTablaGrupoCatequesis tablaGrupos;
	/**
	 * Modelo de la tabla de catequizandos con check de inscripcion (derecha).
	 */
	private ModeloTablaInscripcion tablaCatequizandos;

	/**
	 * Grupos que muestra la tabla de grupos; el indice de la fila coincide con el de esta lista.
	 */
	private List<GrupoCatequesisModelo> grupos;
	/** Catequizandos que muestra la tabla de la derecha para el grupo elegido (candidatos + ya inscriptos en el grupo). */
	// Catequizandos que se muestran para el grupo seleccionado: los sin inscripcion
	// (candidatos) + los ya inscriptos en ESE grupo (bloqueados, ver ModeloTablaInscripcion).
	private List<CatequizandoModelo> catequizandosVisibles;
	/**
	 * Grupo elegido en la tabla; es {@code null} hasta que se elige uno.
	 */
	private GrupoCatequesisModelo grupoSeleccionado;

	/**
	 * Crea el controlador: configura las dos tablas (grupos y catequizandos), carga los grupos y registra las acciones.
	 *
	 * @param inscripcionVista pantalla {@link InscripcionVista} que se va a controlar
	 */
	public InscripcionController(InscripcionVista inscripcionVista) {
		super();
		this.vista = inscripcionVista;
		dao = new InscripcionDAO();
		catequizandoDao = new CatequizandoDAO();
		grupoCatequesisDao = new GrupoCatequesisDAO();

		tablaGrupos = new ModeloTablaGrupoCatequesis();
		this.vista.getTablaGrupos().setModel(tablaGrupos);
		this.vista.getTablaGrupos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tablaCatequizandos = new ModeloTablaInscripcion();
		this.vista.getTablaCatequizandos().setModel(tablaCatequizandos);
		this.vista.getTablaCatequizandos().getColumnModel().getColumn(0)
				.setCellRenderer(new InscripcionCheckBoxRenderer(tablaCatequizandos));

		
		this.vista.getBtnGuardar().setEnabled(false);

		cargarGrupos();
		setAcciones();
	}

	/**
	 * Carga todos los grupos de catequesis en la tabla de grupos.
	 */
	private void cargarGrupos() {
		grupos = grupoCatequesisDao.recuperarTodo();
		tablaGrupos.setLista(grupos);
	}

	/**
	 * Registra los listeners: al elegir un grupo se cargan los catequizandos, el buscador filtra en tiempo real
	 * y los botones Guardar y Cancelar (este ultimo cierra la ventana).
	 */
	private void setAcciones() {
		this.vista.getTablaGrupos().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) seleccionarGrupo();
			}
		});

		this.vista.getTfBuscador().getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { filtrar(); }
			public void removeUpdate(DocumentEvent e) { filtrar(); }
			public void changedUpdate(DocumentEvent e) { filtrar(); }
		});

		this.vista.getBtnGuardar().addActionListener(e -> guardarCambios());
		this.vista.getBtnCancelar().addActionListener(e -> this.vista.dispose());
	}

	/**
	 * Toma el grupo elegido en la tabla, actualiza la etiqueta, habilita Guardar, limpia el buscador y carga los
	 * catequizandos. Antes confirma un check que este en edicion para no perderlo.
	 */
	private void seleccionarGrupo() {
		int fila = this.vista.getTablaGrupos().getSelectedRow();
		if (fila < 0) return;

		// Si habia una edicion de checkbox en curso, hay que confirmarla antes de
		// recargar la tabla, sino se pierde.
		if (this.vista.getTablaCatequizandos().isEditing()) {
			this.vista.getTablaCatequizandos().getCellEditor().stopCellEditing();
		}

		grupoSeleccionado = grupos.get(fila);
		this.vista.getLblGrupoSeleccionado().setText("Grupo seleccionado: " + grupoSeleccionado.getGrup_nombre());
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getTfBuscador().setText("");

		cargarCatequizandos();
	}

	/**
	 * Carga en la tabla de la derecha los catequizandos sin inscripcion (candidatos) mas los ya inscriptos en el grupo
	 * seleccionado (se ven bloqueados).
	 */
	// Trae los catequizandos sin inscripcion (candidatos a sumarse) + los que ya
	// estan inscriptos en el grupo seleccionado (para poder verlos, aunque no se
	// puedan tildar/destildar desde aca). Los inscriptos en OTRO grupo no entran.
	private void cargarCatequizandos() {
		List<CatequizandoModelo> todos = catequizandoDao.recuperarTodo();
		Integer grupoId = grupoSeleccionado.getGrup_id();

		catequizandosVisibles = todos.stream()
				.filter(c -> !ModeloTablaInscripcion.tieneAlgunaInscripcion(c)
						|| ModeloTablaInscripcion.obtenerInscripcionEnGrupo(c, grupoId) != null)
				.collect(Collectors.toList());

		tablaCatequizandos.cargar(catequizandosVisibles, grupoSeleccionado);
	}

	/**
	 * Filtra la lista de catequizandos visibles por el texto del buscador (nombre y apellido, o documento).
	 */
	private void filtrar() {
		if (catequizandosVisibles == null) return;

		String filtro = this.vista.getTfBuscador().getText().trim().toLowerCase();
		if (filtro.isEmpty()) {
			tablaCatequizandos.filtrar(catequizandosVisibles);
			return;
		}

		List<CatequizandoModelo> filtrados = catequizandosVisibles.stream()
				.filter(c -> (c.getCatz_nombre() + " " + c.getCatz_apellido()).toLowerCase().contains(filtro)
						|| (c.getCatz_documento() != null && c.getCatz_documento().toLowerCase().contains(filtro)))
				.collect(Collectors.toList());

		tablaCatequizandos.filtrar(filtrados);
	}

	/**
	 * Inscribe en el grupo seleccionado a los catequizandos tildados que no estaban inscriptos. Pide confirmacion,
	 * crea una {@link InscripcionModelo} activa con la fecha de hoy por cada uno y avisa si hubo errores. Al terminar
	 * recarga la lista, donde los recien inscriptos aparecen bloqueados.
	 */
	private void guardarCambios() {
		if (grupoSeleccionado == null || catequizandosVisibles == null) return;

		if (this.vista.getTablaCatequizandos().isEditing()) {
			this.vista.getTablaCatequizandos().getCellEditor().stopCellEditing();
		}

		// Solo se procesan altas nuevas: los que ya estaban inscriptos en este grupo
		// quedan bloqueados en la tabla y no generan ningun cambio.
		List<CatequizandoModelo> aInscribir = catequizandosVisibles.stream()
				.filter(c -> !tablaCatequizandos.estaBloqueado(c.getCatz_id()))
				.filter(c -> tablaCatequizandos.estaSeleccionado(c.getCatz_id()))
				.collect(Collectors.toList());

		if (aInscribir.isEmpty()) {
			JOptionPane.showMessageDialog(this.vista, "No hay catequizandos nuevos tildados para inscribir.");
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(
				this.vista,
				"Se inscribirán " + aInscribir.size() + " catequizando(s) en el grupo "
						+ grupoSeleccionado.getGrup_nombre() + ". ¿Confirmar?",
				"Confirmar inscripción",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion != JOptionPane.YES_OPTION) return;

		int errores = 0;
		for (CatequizandoModelo catequizando : aInscribir) {
			try {
				InscripcionModelo nueva = new InscripcionModelo();
				nueva.setCatequizando(catequizando);
				nueva.setGrupoCatequesis(grupoSeleccionado);
				nueva.setInscrip_fecha(LocalDate.now());
				nueva.setInscrip_estado(true);
				dao.guardar(nueva);
			} catch (Exception e) {
				e.printStackTrace();
				errores++;
			}
		}

		if (errores > 0) {
			JOptionPane.showMessageDialog(
					this.vista,
					"Se guardaron los cambios, pero " + errores + " no se pudieron aplicar. Revise la consola.");
		} else {
			JOptionPane.showMessageDialog(this.vista, "Catequizandos inscriptos correctamente.");
		}

		cargarCatequizandos(); // los recien inscriptos van a aparecer ahora bloqueados, ya como miembros
	}

}