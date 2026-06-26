package controlador;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import dao.CatequistaDAO;
import dao.CatequizandoDAO;
import interfaces.InterfaceABM;
import modelo.CatequistaModelo;
import modelo.CatequizandoModelo;
import tabla.EstadoCellRenderer;
import tabla.ModeloTablaCatequista;
import tabla.ModeloTablaCatequizando;
import utilidades.FechaUtil;
import vista.CatequistaVista;


public class CatequistaController  implements InterfaceABM{

	private CatequistaVista vista;
	private CatequistaModelo catequista;
	private CatequistaDAO dao;
	private List<CatequistaModelo> catequistas;
	private ModeloTablaCatequista tabla;
	public CatequistaController(CatequistaVista catequistaVista) {
		super();
		this.vista = catequistaVista;
		this.vista.setInterfaceABM(this);
		dao = new CatequistaDAO();
		tabla = new ModeloTablaCatequista();
		this.vista.getTabla().setModel(tabla);
		this.vista.getTabla().getColumnModel().getColumn(4)
        .setCellRenderer(new EstadoCellRenderer()); 
		estadoInicial();
		cargarTabla("");
		setAcciones();
	}
	
	private void cargarTabla(String filtro) {
	    if (filtro == null || filtro.isEmpty()) {
	        catequistas = dao.recuperarTodo();
	    } else {
	        catequistas = dao.recuperarTodo().stream()
	            .filter(c -> c.getCat_nombre().toLowerCase()
	            .contains(filtro.toLowerCase()))
	            .collect(java.util.stream.Collectors.toList());
	    }
	    tabla.setLista(catequistas);
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
		catequista = null;

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
				this.vista.getCbEstado().setEnabled(true);
				
				//Carga el campo fecha y crea el cliente
				catequista = new CatequistaModelo();
				this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(LocalDate.now()));
		
	}
	
	private void seleccionarRegistro() {
	    int fila = this.vista.getTabla().getSelectedRow();
	    if (fila < 0) return;
	    catequista = catequistas.get(fila);

	    // Solo habilitar botones, sin cargar campos
	    this.vista.getBtnEditar().setEnabled(true);
	    this.vista.getBtnEliminar().setEnabled(true);
	}
	
	
	@Override
	public void editar() {
		if (catequista == null) return;
		
		// Cargar datos en los campos
	    this.vista.getTfFecha_reg().setText(FechaUtil.fechaAString(catequista.getCat_fechaRegistro()));
	    this.vista.getTfNombre().setText(catequista.getCat_nombre());
	    this.vista.getTfApellido().setText(catequista.getCat_apellido());
	    this.vista.getTfDocumento().setText(catequista.getCat_documento());
	    this.vista.getTfTelefono().setText(catequista.getCat_telefono());
	    this.vista.getTfCorreo().setText(catequista.getCat_correo());
	    this.vista.getTfFecha_nac().setText(FechaUtil.fechaAString(catequista.getCat_fechaNacimiento()));
	    this.vista.getTfDireccion().setText(catequista.getCat_direccion());
	    this.vista.getCbEstado().setSelected(catequista.isCat_estado());

	    // Habilitar campos para edición
	    this.vista.getTfNombre().setEnabled(true);
	    this.vista.getTfApellido().setEnabled(true);
	    this.vista.getTfDocumento().setEnabled(true);
	    this.vista.getTfFecha_nac().setEnabled(true);
	    this.vista.getTfCorreo().setEnabled(true);
	    this.vista.getTfDireccion().setEnabled(true);
	    this.vista.getTfTelefono().setEnabled(true);
	    this.vista.getCbEstado().setEnabled(true);

	    // Ajustar botones
	    this.vista.getBtnNuevo().setEnabled(false);
	    this.vista.getBtnEditar().setEnabled(false);
	    this.vista.getBtnEliminar().setEnabled(false);
	    this.vista.getBtnGuardar().setEnabled(true);
	    this.vista.getBtnCancelar().setEnabled(true);
		
	}
	@Override
	public void eliminar() {
		if (catequista == null) return;

	    int confirmacion = javax.swing.JOptionPane.showConfirmDialog(
	        this.vista,
	        "¿Está seguro que desea eliminar a: "
	            + catequista.getCat_nombre() + " "
	            + catequista.getCat_apellido() + "?",
	        "Confirmar eliminación",
	        javax.swing.JOptionPane.YES_NO_OPTION
	    );

	    if (confirmacion == javax.swing.JOptionPane.YES_OPTION) {
	        try {
	            dao.eliminar(catequista);
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
		if(catequista == null) this.vista.dispose();
		else estadoInicial();
		
	}
	@Override
	public void guardar() {
		catequista.setCat_fechaRegistro(FechaUtil.stringAFecha(this.vista.getTfFecha_reg().getText()));
		catequista.setCat_fechaNacimiento(FechaUtil.stringAFecha(this.vista.getTfFecha_nac().getText()));
		catequista.setCat_nombre(this.vista.getTfNombre().getText());
		catequista.setCat_apellido(this.vista.getTfApellido().getText());
		catequista.setCat_documento(this.vista.getTfDocumento().getText());
		catequista.setCat_correo(this.vista.getTfCorreo().getText());
		catequista.setCat_telefono(this.vista.getTfTelefono().getText());
		catequista.setCat_direccion(this.vista.getTfDireccion().getText());
		catequista.setCat_estado(this.vista.getCbEstado().isSelected());
		
		try {
			dao.guardar(catequista);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		estadoInicial();
		cargarTabla("");

		
	}

	@Override
	public void buscar() {
		String filtro = this.vista.getTfBuscador().getText().trim();
	    cargarTabla(filtro);
		
	}
	
	
	
	
	
	
	
}
