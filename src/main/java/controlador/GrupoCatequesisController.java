package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequistaDAO;
import dao.EtapaDAO;
import dao.GrupoCatequesisDAO;
import interfaces.InterfaceABM;
import modelo.EtapaModelo;
import modelo.GrupoCatequesisModelo;
import tabla.ModeloTablaGrupoCatequesis;
import utilidades.FechaUtil;
import vista.GrupoCatequesisVista;

public class GrupoCatequesisController implements InterfaceABM {

	private GrupoCatequesisVista vista;
	private GrupoCatequesisModelo grupo;
	private GrupoCatequesisDAO dao;
	private EtapaDAO etapaDao;
	private CatequistaDAO catequistaDao;
	private List<GrupoCatequesisModelo> grupos;
	private ModeloTablaGrupoCatequesis tabla;

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

	private void seleccionarRegistro() {
		int fila = this.vista.getTabla().getSelectedRow();
		if (fila < 0) return;
		grupo = grupos.get(fila);

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
		this.vista.getTfAnho().setEnabled(true);
		this.vista.getCbEtapa().setEnabled(true);
		this.vista.getComboCatequistas().setEnabled(true);

		this.vista.getCbEtapa().setSelectedIndex(-1);
		this.vista.getComboCatequistas().limpiarSeleccion();

		grupo = new GrupoCatequesisModelo();
		this.vista.getTfAnho().setText(FechaUtil.fechaAString(LocalDate.now()));
	}

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
	}

	@Override
	public void guardar() {
		grupo.setGrup_nombre(this.vista.getTfNombre().getText());
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

	@Override
	public void cancelar() {
		if (grupo == null) this.vista.dispose();
		else estadoInicial();
	}

	@Override
	public void buscar() {
		String filtro = this.vista.getTfBuscador().getText().trim();
		cargarTabla(filtro);
	}

}