package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequizandoDAO;
import interfaces.InterfaceABM;
import modelo.CatequizandoModelo;
import tabla.ModeloTablaCatequizando;
import vista.CatequizandoVista;
import utilidades.FechaUtil;

public class CatequizandoController implements InterfaceABM {

	private CatequizandoVista vista;
	private CatequizandoModelo catequizando;
	private CatequizandoDAO dao;
	private List<CatequizandoModelo> catequizandos;
	private ModeloTablaCatequizando tabla;

	public CatequizandoController(CatequizandoVista catequizandoVista) {
		super();
		this.vista = catequizandoVista;
		this.vista.setInterfaceABM(this);
		dao = new CatequizandoDAO();
		tabla = new ModeloTablaCatequizando();
		this.vista.getTabla().setModel(tabla);
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}
	
	private void cargarTabla(String filtro) {
	    if (filtro == null || filtro.isEmpty()) {
	        catequizandos = dao.recuperarTodo();
	    } else {
	        catequizandos = dao.recuperarTodo().stream()
	            .filter(c -> c.getCatz_nombre().toLowerCase()
	            .contains(filtro.toLowerCase()))
	            .collect(java.util.stream.Collectors.toList());
	    }
	    tabla.setLista(catequizandos);
	}
	
	private void setAcciones() {
	    this.vista.getTabla().addMouseListener(new MouseAdapter() {
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) seleccionarRegistro();
	        }
	    });

	    // Búsqueda en tiempo real
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

		// desactiva los textfield
		this.vista.getTfFecha_reg().setEnabled(false);
		this.vista.getTfNombre().setEnabled(false);
		this.vista.getTfApellido().setEnabled(false);
		this.vista.getTfDocumento().setEnabled(false);
		this.vista.getTfFecha_nac().setEnabled(false);
		this.vista.getTfCorreo().setEnabled(false);
		this.vista.getTfDireccion().setEnabled(false);
		this.vista.getTfTelefono().setEnabled(false);

		// Limpiar los campos
		this.vista.getTfFecha_reg().setValue(null);
		this.vista.getTfFecha_nac().setValue(null);
		this.vista.getTfNombre().setText("");
		this.vista.getTfApellido().setText("");
		this.vista.getTfDocumento().setText("");
		this.vista.getTfCorreo().setText("");
		this.vista.getTfDireccion().setText("");
		this.vista.getTfTelefono().setText("");
		catequizando = null;

	}

	@Override
	public void nuevo() {
		// desactiva los botones
		this.vista.getBtnNuevo().setEnabled(false);
		this.vista.getBtnEditar().setEnabled(false);
		this.vista.getBtnEliminar().setEnabled(false);
		this.vista.getBtnCancelar().setEnabled(true);
		this.vista.getBtnGuardar().setEnabled(true);

		// desactiva los textfield
		this.vista.getTfFecha_reg().setEnabled(false);
		this.vista.getTfNombre().setEnabled(true);
		this.vista.getTfApellido().setEnabled(true);
		this.vista.getTfDocumento().setEnabled(true);
		this.vista.getTfFecha_nac().setEnabled(true);
		this.vista.getTfCorreo().setEnabled(true);
		this.vista.getTfDireccion().setEnabled(true);
		this.vista.getTfTelefono().setEnabled(true);
		this.vista.getJcbEstado().setEnabled(true);
		
		//Carga el campo fecha y crea el cliente
		catequizando = new CatequizandoModelo();
		this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(LocalDate.now()));

	}
	
	
	private void seleccionarRegistro() {
	    int fila = this.vista.getTabla().getSelectedRow();
	    if (fila < 0) return;
	    catequizando = catequizandos.get(fila);

	    // Solo habilitar botones, sin cargar campos
	    this.vista.getBtnEditar().setEnabled(true);
	    this.vista.getBtnEliminar().setEnabled(true);
	}

	@Override
	public void editar() {
	    if (catequizando == null) return;

	    // Cargar datos en los campos
	    this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(catequizando.getCatz_fechaRegistro()));
	    this.vista.getTfNombre().setText(catequizando.getCatz_nombre());
	    this.vista.getTfApellido().setText(catequizando.getCatz_apellido());
	    this.vista.getTfDocumento().setText(catequizando.getCatz_documento());
	    this.vista.getTfTelefono().setText(catequizando.getCatz_telefono());
	    this.vista.getTfCorreo().setText(catequizando.getCatz_correo());
	    this.vista.getTfFecha_nac().setText(FechaUtil.fechaAString(catequizando.getCatz_fechaNacimiento()));
	    this.vista.getTfDireccion().setText(catequizando.getCatz_direccion());
	    this.vista.getJcbEstado().setSelected(catequizando.isCatz_estado());

	    // Habilitar campos para edición
	    this.vista.getTfNombre().setEnabled(true);
	    this.vista.getTfApellido().setEnabled(true);
	    this.vista.getTfDocumento().setEnabled(true);
	    this.vista.getTfFecha_nac().setEnabled(true);
	    this.vista.getTfCorreo().setEnabled(true);
	    this.vista.getTfDireccion().setEnabled(true);
	    this.vista.getTfTelefono().setEnabled(true);
	    this.vista.getJcbEstado().setEnabled(true);

	    // Ajustar botones
	    this.vista.getBtnNuevo().setEnabled(false);
	    this.vista.getBtnEditar().setEnabled(false);
	    this.vista.getBtnEliminar().setEnabled(false);
	    this.vista.getBtnGuardar().setEnabled(true);
	    this.vista.getBtnCancelar().setEnabled(true);
	}

	@Override
	public void guardar() {
		
		catequizando.setCatz_fechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFecha_reg().getText()));
		catequizando.setCatz_fechaNacimiento(FechaUtil.stringAFecha(this.vista.getTfFecha_nac().getText()));
		catequizando.setCatz_nombre(this.vista.getTfNombre().getText());
		catequizando.setCatz_apellido(this.vista.getTfApellido().getText());
		catequizando.setCatz_documento(this.vista.getTfDocumento().getText());
		catequizando.setCatz_correo(this.vista.getTfCorreo().getText());
		catequizando.setCatz_telefono(this.vista.getTfTelefono().getText());
		catequizando.setCatz_direccion(this.vista.getTfDireccion().getText());
		catequizando.setCatz_estado(this.vista.getJcbEstado().isSelected());
		
		try {
			dao.guardar(catequizando);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		estadoInicial();
		cargarTabla("");

	}

	@Override
	public void eliminar() {
	    if (catequizando == null) return;

	    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
	        this.vista,
	        "¿Está seguro que desea eliminar a: "
	            + catequizando.getCatz_nombre() + " "
	            + catequizando.getCatz_apellido() + "?",
	        "Confirmar eliminación",
	        javax.swing.JOptionPane.YES_NO_OPTION
	    );

	    if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
	        try {
	            dao.eliminar(catequizando);
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
		if(catequizando == null) this.vista.dispose();
		else estadoInicial();
	}

	@Override
	public void buscar() {
	    String filtro = this.vista.getTfBuscador().getText().trim();
	    cargarTabla(filtro);
	}

}
