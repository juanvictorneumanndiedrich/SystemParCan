package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import dao.ClaseDAO;
import interfaces.InterfaceABM;
import modelo.ClaseModelo;
import modelo.GrupoCatequesisModelo;
import tabla.ModeloTablaClase;
import utilidades.FechaUtil;
import vista.AsistenciaVista;
import vista.ClaseVista;

public class ClaseController implements InterfaceABM {

	private ClaseVista vista;
	private ClaseModelo clase;
	private ClaseDAO dao;
	private GrupoCatequesisModelo grupo;
	private List<ClaseModelo> clases;
	private ModeloTablaClase tabla;

	// Pantalla contextual: siempre se abre para un Grupo de Catequesis ya
	// elegido (ver GrupoCatequesisController.verClases()). Todas las clases
	// que se creen quedan asociadas a ese grupo, por lo que no hay combo
	// de grupo en el formulario.
	public ClaseController(ClaseVista vista, GrupoCatequesisModelo grupo) {
		super();
		this.vista = vista;
		this.grupo = grupo;
		this.vista.setInterfaceABM(this);
		this.vista.setTitle("Clases del Grupo: " + grupo.getGrup_nombre());

		dao = new ClaseDAO();
		tabla = new ModeloTablaClase();
		this.vista.getTabla().setModel(tabla);

		estadoInicial();
		cargarTabla("");
		setAcciones();
	}

	private void cargarTabla(String filtro) {
		List<ClaseModelo> clasesDelGrupo = dao.recuperarTodo().stream()
				.filter(c -> c.getGrupoCatequesis() != null
						&& c.getGrupoCatequesis().getGrup_id().equals(grupo.getGrup_id()))
				.collect(Collectors.toList());

		if (filtro == null || filtro.isEmpty()) {
			clases = clasesDelGrupo;
		} else {
			clases = clasesDelGrupo.stream()
					.filter(c -> c.getClase_descripcion() != null
							&& c.getClase_descripcion().toLowerCase().contains(filtro.toLowerCase()))
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

		this.vista.getBtnTomarAsistencia().addActionListener(e -> tomarAsistencia());
	}

	private void estadoInicial() {
		this.vista.getBtnNuevo().setEnabled(true);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(false);
		this.vista.getBtnTomarAsistencia().setEnabled(false);

		this.vista.getTfFecha().setEnabled(false);
		this.vista.getTfDescripcion().setEnabled(false);

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
		this.vista.getBtnTomarAsistencia().setEnabled(true);
	}

	// Abre la grilla de Asistencia para la clase seleccionada en la tabla.
	// La clase viaja fija al AsistenciaController: esa pantalla arma las
	// filas a partir del grupo de esta clase (clase.getGrupoCatequesis()).
	private void tomarAsistencia() {
		if (clase == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Seleccione una clase en la tabla para tomar asistencia.");
			return;
		}

		AsistenciaVista asistenciaVista = new AsistenciaVista();
		new AsistenciaController(asistenciaVista, clase);
		asistenciaVista.setLocationRelativeTo(this.vista);
		asistenciaVista.setVisible(true);
	}

	@Override
	public void nuevo() {
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnTomarAsistencia().setEnabled(false);

		this.vista.getTfFecha().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);

		this.vista.getTfDescripcion().setText("");
		// Por comodidad, la fecha arranca en el dia de hoy; el usuario la puede cambiar.
		this.vista.getTfFecha().setText(FechaUtil.fechaAString(LocalDate.now()));

		clase = new ClaseModelo();
	}

	@Override
	public void editar() {
		if (clase == null) return;

		this.vista.getTfFecha().setText(
				clase.getClase_fechaClase() != null ? FechaUtil.fechaAString(clase.getClase_fechaClase()) : "");
		this.vista.getTfDescripcion().setText(clase.getClase_descripcion());

		this.vista.getTfFecha().setEnabled(true);
		this.vista.getTfDescripcion().setEnabled(true);

		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnGuardar().setEnabled(true);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnTomarAsistencia().setEnabled(false);
	}

	@Override
	public void guardar() {
		String textoFecha = this.vista.getTfFecha().getText();
		LocalDate fecha = FechaUtil.stringAFecha(textoFecha);

		if (fecha == null) {
			javax.swing.JOptionPane.showMessageDialog(this.vista,
					"Ingrese una fecha valida (dd/mm/aaaa).",
					"Fecha invalida", javax.swing.JOptionPane.WARNING_MESSAGE);
			return;
		}

		clase.setClase_fechaClase(fecha);
		clase.setClase_descripcion(this.vista.getTfDescripcion().getText());
		clase.setGrupoCatequesis(grupo);

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