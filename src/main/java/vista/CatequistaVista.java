package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import javax.swing.JCheckBox;
import componentes.JtextFieldGenerico;
import controlador.CatequistaController;
import controlador.CatequizandoController;
import utilidades.FechaUtil;

public class CatequistaVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfNombre;
	private JtextFieldGenerico tfApellido;
	private JtextFieldGenerico tfDocumento;
	private JtextFieldGenerico tfTelefono;
	private JtextFieldGenerico tfCorreo;
	private JtextFieldGenerico tfDireccion;
	private JCheckBox cbEstado;
	private JFormattedTextField tfFecha_nac;
	private JFormattedTextField tfFecha_reg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CatequistaVista dialog = new CatequistaVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private void setUpControlador() {
		new CatequistaController(this);
		

	}
	
	/**
	 * Create the dialog.
	 */
	public CatequistaVista() {
		setBounds(100, 100, 1080, 720);
		getPanelFormulario().setLayout(null);
		
		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(31, 40, 62, 28);
		getPanelFormulario().add(lblgnrcNombre);
		
		JLabelGenerico lblgnrcApellido = new JLabelGenerico((String) null);
		lblgnrcApellido.setText("Apellido:");
		lblgnrcApellido.setBounds(31, 91, 62, 28);
		getPanelFormulario().add(lblgnrcApellido);
		
		JLabelGenerico lblgnrcDocumento = new JLabelGenerico((String) null);
		lblgnrcDocumento.setText("Documento:");
		lblgnrcDocumento.setBounds(31, 142, 80, 28);
		getPanelFormulario().add(lblgnrcDocumento);
		
		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(31, 196, 62, 28);
		getPanelFormulario().add(lblgnrcTelefono);
		
		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(31, 248, 49, 28);
		getPanelFormulario().add(lblgnrcCorreo);
		
		JLabelGenerico lblgnrcDireccion = new JLabelGenerico((String) null);
		lblgnrcDireccion.setText("Direccion:");
		lblgnrcDireccion.setBounds(31, 303, 62, 28);
		getPanelFormulario().add(lblgnrcDireccion);
		
		JLabelGenerico lblgnrcE = new JLabelGenerico((String) null);
		lblgnrcE.setText("Fecha de Nacimiento:");
		lblgnrcE.setBounds(31, 358, 139, 28);
		getPanelFormulario().add(lblgnrcE);
		
		JLabelGenerico lblgnrcFechaDeRegistro = new JLabelGenerico((String) null);
		lblgnrcFechaDeRegistro.setText("Fecha de Registro:");
		lblgnrcFechaDeRegistro.setBounds(31, 488, 117, 28);
		getPanelFormulario().add(lblgnrcFechaDeRegistro);
		
		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(78, 418, 92, 20);
		getPanelFormulario().add(cbEstado);
		
		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(103, 42, 223, 24);
		getPanelFormulario().add(tfNombre);
		
		tfApellido = new JtextFieldGenerico();
		tfApellido.setBounds(103, 93, 223, 24);
		getPanelFormulario().add(tfApellido);
		
		tfDocumento = new JtextFieldGenerico();
		tfDocumento.setBounds(121, 144, 223, 24);
		getPanelFormulario().add(tfDocumento);
		
		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(103, 198, 223, 24);
		getPanelFormulario().add(tfTelefono);
		
		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(103, 250, 223, 24);
		getPanelFormulario().add(tfCorreo);
		
		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(103, 305, 373, 24);
		getPanelFormulario().add(tfDireccion);
		
		tfFecha_nac = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha_nac.setBounds(180, 360, 117, 24);
		getPanelFormulario().add(tfFecha_nac);
		
		tfFecha_reg = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha_reg.setBounds(158, 490, 117, 24);
		getPanelFormulario().add(tfFecha_reg);
		
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

	public JtextFieldGenerico getTfApellido() {
		return tfApellido;
	}

	public JtextFieldGenerico getTfDocumento() {
		return tfDocumento;
	}

	public JtextFieldGenerico getTfTelefono() {
		return tfTelefono;
	}

	public JtextFieldGenerico getTfCorreo() {
		return tfCorreo;
	}

	public JtextFieldGenerico getTfDireccion() {
		return tfDireccion;
	}

	public JCheckBox getCbEstado() {
		return cbEstado;
	}

	public JFormattedTextField getTfFecha_nac() {
		return tfFecha_nac;
	}

	public JFormattedTextField getTfFecha_reg() {
		return tfFecha_reg;
	}
	
	
	
	
}
