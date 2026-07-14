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

public class InscripcionController {

	private InscripcionVista vista;
	private InscripcionDAO dao;
	private CatequizandoDAO catequizandoDao;
	private GrupoCatequesisDAO grupoCatequesisDao;

	private ModeloTablaGrupoCatequesis tablaGrupos;
	private ModeloTablaInscripcion tablaCatequizandos;

	private List<GrupoCatequesisModelo> grupos;
	// Catequizandos que se muestran para el grupo seleccionado: los sin inscripcion
	// (candidatos) + los ya inscriptos en ESE grupo (bloqueados, ver ModeloTablaInscripcion).
	private List<CatequizandoModelo> catequizandosVisibles;
	private GrupoCatequesisModelo grupoSeleccionado;

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

	private void cargarGrupos() {
		grupos = grupoCatequesisDao.recuperarTodo();
		tablaGrupos.setLista(grupos);
	}

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