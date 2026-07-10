package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import dao.SacramentoDAO;
import interfaces.InterfaceABM;
import modelo.SacramentoModelo;
import tabla.ModeloTablaSacramento;
import vista.SacramentoVista;

public class SacramentoController implements InterfaceABM {

	private SacramentoVista vista;
	private SacramentoModelo sacramento;
	private SacramentoDAO dao;
	private List<SacramentoModelo> sacramentos;
	private ModeloTablaSacramento tabla;

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

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		sacramento = sacramentos.get(fila);

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

		this.vista.getTfNombre().setEnabled(true);

		sacramento = new SacramentoModelo();
	}

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

	@Override
	public void guardar() {
		sacramento.setSacr_nombre(this.vista.getTfNombre().getText().trim());

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

	@Override
	public void cancelar() {
		if (sacramento == null) this.vista.dispose();
		else estadoInicial();
	}

	@Override
	public void buscar() {
		String filtro = this.vista.getTfBuscador().getText().trim();
		cargarTabla(filtro);
	}

}