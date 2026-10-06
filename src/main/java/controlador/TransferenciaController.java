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
import dao.TransferenciaDAO;
import modelo.CatequizandoModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;
import modelo.TransferenciaModelo;
import tabla.ModeloTablaGrupoCatequesis;
import tabla.ModeloTablaInscripcion;
import tabla.ModeloTablaTransferencia;
import vista.TransferenciaVista;

/**
 * Controlador de la pantalla de Transferencia. A la izquierda se elige el grupo de origen, a la derecha se tildan los
 * catequizandos inscriptos en el que se quieren mover, y abajo se elige el grupo destino. Al transferir, la inscripcion
 * de cada catequizando pasa a apuntar al grupo destino y se deja constancia en una {@link TransferenciaModelo}
 * (origen, destino, fecha y observacion).
 *
 * No implementa {@link interfaces.InterfaceABM}: no es un ABM, solo registra movimientos entre grupos.
 */
public class TransferenciaController {

	/**
	 * Pantalla (vista) que maneja este controlador.
	 */
	private TransferenciaVista vista;
	/**
	 * DAO principal: registra la transferencia junto con el cambio de la inscripcion.
	 */
	private TransferenciaDAO dao;
	/**
	 * DAO auxiliar de catequizandos.
	 */
	private CatequizandoDAO catequizandoDao;
	/**
	 * DAO auxiliar de grupos de catequesis.
	 */
	private GrupoCatequesisDAO grupoCatequesisDao;

	/**
	 * Modelo de la tabla de grupos de origen (izquierda).
	 */
	private ModeloTablaGrupoCatequesis tablaGrupos;
	/**
	 * Modelo de la tabla de catequizandos con check de transferencia (derecha).
	 */
	private ModeloTablaTransferencia tablaCatequizandos;

	/**
	 * Grupos que muestra la tabla de grupos; el indice de la fila coincide con el de esta lista.
	 */
	private List<GrupoCatequesisModelo> grupos;
	/**
	 * Catequizandos inscriptos en el grupo de origen elegido.
	 */
	private List<CatequizandoModelo> catequizandosDelGrupo;
	/**
	 * Grupo de origen elegido en la tabla; es {@code null} hasta que se elige uno.
	 */
	private GrupoCatequesisModelo grupoOrigen;

	/**
	 * Crea el controlador: configura las dos tablas, carga los grupos y registra las acciones.
	 *
	 * @param transferenciaVista pantalla {@link TransferenciaVista} que se va a controlar
	 */
	public TransferenciaController(TransferenciaVista transferenciaVista) {
		super();
		this.vista = transferenciaVista;
		dao = new TransferenciaDAO();
		catequizandoDao = new CatequizandoDAO();
		grupoCatequesisDao = new GrupoCatequesisDAO();

		tablaGrupos = new ModeloTablaGrupoCatequesis();
		this.vista.getTablaGrupos().setModel(tablaGrupos);
		this.vista.getTablaGrupos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tablaCatequizandos = new ModeloTablaTransferencia();
		this.vista.getTablaCatequizandos().setModel(tablaCatequizandos);

		this.vista.getBtnTransferir().setEnabled(false);
		this.vista.getCbGrupoDestino().setEnabled(false);
		this.vista.getTfObservacion().setEnabled(false);

		cargarGrupos();
		setAcciones();
	}

	/**
	 * Carga todos los grupos de catequesis en la tabla de grupos de origen.
	 */
	private void cargarGrupos() {
		grupos = grupoCatequesisDao.recuperarTodo();
		tablaGrupos.setLista(grupos);
	}

	/**
	 * Carga el combo de grupo destino con todos los grupos menos el de origen. El primer item es {@code null}
	 * ("-- Seleccione --"), para obligar a elegir uno.
	 */
	private void cargarComboDestino() {
		this.vista.getCbGrupoDestino().removeAllItems();
		this.vista.getCbGrupoDestino().addItem(null);
		for (GrupoCatequesisModelo grupo : grupos) {
			if (!grupo.getGrup_id().equals(grupoOrigen.getGrup_id())) {
				this.vista.getCbGrupoDestino().addItem(grupo);
			}
		}
		this.vista.getCbGrupoDestino().setSelectedIndex(0);
	}

	/**
	 * Registra los listeners: al elegir un grupo se cargan sus catequizandos, el buscador filtra en tiempo real,
	 * Transferir confirma el movimiento y Cancelar cierra la ventana.
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

		this.vista.getBtnTransferir().addActionListener(e -> transferir());
		this.vista.getBtnCancelar().addActionListener(e -> this.vista.dispose());
	}

	/**
	 * Toma el grupo de origen elegido en la tabla, actualiza la etiqueta, habilita los datos de la transferencia y
	 * carga los catequizandos de ese grupo. Antes confirma un check que este en edicion para no perderlo.
	 */
	private void seleccionarGrupo() {
		int fila = this.vista.getTablaGrupos().getSelectedRow();
		if (fila < 0) return;

		if (this.vista.getTablaCatequizandos().isEditing()) {
			this.vista.getTablaCatequizandos().getCellEditor().stopCellEditing();
		}

		grupoOrigen = grupos.get(fila);
		this.vista.getLblGrupoSeleccionado().setText("Grupo de origen: " + grupoOrigen.getGrup_nombre());
		this.vista.getBtnTransferir().setEnabled(true);
		this.vista.getCbGrupoDestino().setEnabled(true);
		this.vista.getTfObservacion().setEnabled(true);
		this.vista.getTfBuscador().setText("");

		cargarComboDestino();
		cargarCatequizandos();
	}

	/**
	 * Carga en la tabla de la derecha los catequizandos que tienen inscripcion en el grupo de origen.
	 */
	private void cargarCatequizandos() {
		Integer grupoId = grupoOrigen.getGrup_id();
		catequizandosDelGrupo = catequizandoDao.recuperarTodo().stream()
				.filter(c -> ModeloTablaInscripcion.obtenerInscripcionEnGrupo(c, grupoId) != null)
				.collect(Collectors.toList());

		tablaCatequizandos.cargar(catequizandosDelGrupo);
	}

	/**
	 * Filtra los catequizandos del grupo por el texto del buscador (nombre y apellido, o documento).
	 */
	private void filtrar() {
		if (catequizandosDelGrupo == null) return;

		String filtro = this.vista.getTfBuscador().getText().trim().toLowerCase();
		if (filtro.isEmpty()) {
			tablaCatequizandos.filtrar(catequizandosDelGrupo);
			return;
		}

		List<CatequizandoModelo> filtrados = catequizandosDelGrupo.stream()
				.filter(c -> (c.getCatz_nombre() + " " + c.getCatz_apellido()).toLowerCase().contains(filtro)
						|| (c.getCatz_documento() != null && c.getCatz_documento().toLowerCase().contains(filtro)))
				.collect(Collectors.toList());

		tablaCatequizandos.filtrar(filtrados);
	}

	/**
	 * Transfiere al grupo destino a los catequizandos tildados. Valida que haya grupo destino y al menos un
	 * catequizando tildado, pide confirmacion y, por cada uno, cambia el grupo de su inscripcion y guarda la
	 * {@link TransferenciaModelo} con la fecha de hoy. Al terminar recarga la lista del grupo de origen.
	 */
	private void transferir() {
		if (grupoOrigen == null || catequizandosDelGrupo == null) return;

		if (this.vista.getTablaCatequizandos().isEditing()) {
			this.vista.getTablaCatequizandos().getCellEditor().stopCellEditing();
		}

		GrupoCatequesisModelo grupoDestino = (GrupoCatequesisModelo) this.vista.getCbGrupoDestino().getSelectedItem();
		if (grupoDestino == null) {
			JOptionPane.showMessageDialog(this.vista,
					"Debe elegir el grupo destino.",
					"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		List<CatequizandoModelo> aTransferir = catequizandosDelGrupo.stream()
				.filter(c -> tablaCatequizandos.estaSeleccionado(c.getCatz_id()))
				.collect(Collectors.toList());

		if (aTransferir.isEmpty()) {
			JOptionPane.showMessageDialog(this.vista,
					"Debe seleccionar por lo menos un catequizando.",
					"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// La observacion es opcional: si queda vacia se guarda como null.
		String observacion = this.vista.getTfObservacion().getText().trim();
		if (observacion.length() > 255) {
			JOptionPane.showMessageDialog(this.vista,
					"La observacion no puede tener mas de 255 caracteres.",
					"Dato invalido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(
				this.vista,
				"Se transferirán " + aTransferir.size() + " catequizando(s) del grupo "
						+ grupoOrigen.getGrup_nombre() + " al grupo " + grupoDestino.getGrup_nombre()
						+ ". ¿Confirmar?",
				"Confirmar transferencia",
				JOptionPane.YES_NO_OPTION);
		if (confirmacion != JOptionPane.YES_OPTION) return;

		int errores = 0;
		for (CatequizandoModelo catequizando : aTransferir) {
			try {
				// La inscripcion siempre apunta al grupo actual: se mueve al destino.
				InscripcionModelo inscripcion = ModeloTablaInscripcion.obtenerInscripcionEnGrupo(catequizando,
						grupoOrigen.getGrup_id());
				inscripcion.setGrupoCatequesis(grupoDestino);

				// Constancia historica del movimiento.
				TransferenciaModelo transferencia = new TransferenciaModelo();
				transferencia.setCatequizando(catequizando);
				transferencia.setGrupoOrigen(grupoOrigen);
				transferencia.setGrupoDestino(grupoDestino);
				transferencia.setTransf_fecha(LocalDate.now());
				transferencia.setTransf_observacion(observacion.isEmpty() ? null : observacion);

				dao.transferir(inscripcion, transferencia);
			} catch (Exception e) {
				e.printStackTrace();
				errores++;
			}
		}

		if (errores > 0) {
			JOptionPane.showMessageDialog(
					this.vista,
					"Se realizaron las transferencias, pero " + errores + " no se pudieron aplicar. Revise la consola.");
		} else {
			JOptionPane.showMessageDialog(this.vista, "Catequizandos transferidos correctamente.");
		}

		this.vista.getTfObservacion().setText("");
		this.vista.getTfBuscador().setText("");
		cargarCatequizandos(); // los transferidos ya no aparecen en el grupo de origen
	}

}
