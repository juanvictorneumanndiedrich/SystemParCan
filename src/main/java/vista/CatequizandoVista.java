package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import javax.swing.JCheckBox;

public class CatequizandoVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfNombre;
	private JtextFieldGenerico tfApellido;
	private JtextFieldGenerico tfDocumento;
	private JtextFieldGenerico tfTelefono;
	private JtextFieldGenerico tfCorreo;
	private JtextFieldGenerico tfDireccion;
	private JtextFieldGenerico tfFecha_nac;
	private JCheckBox jcbEstado;
	private JtextFieldGenerico tfFecha_reg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CatequizandoVista dialog = new CatequizandoVista();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public CatequizandoVista() {
		setBounds(100, 100, 1080, 720);
		getPanelFormulario().setLayout(null);
		
		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(45, 35, 62, 28);
		getPanelFormulario().add(lblgnrcNombre);
		
		JLabelGenerico lblgnrcApellido = new JLabelGenerico((String) null);
		lblgnrcApellido.setText("Apellido:");
		lblgnrcApellido.setBounds(45, 85, 62, 28);
		getPanelFormulario().add(lblgnrcApellido);
		
		JLabelGenerico lblgnrcDocumento = new JLabelGenerico((String) null);
		lblgnrcDocumento.setText("Documento:");
		lblgnrcDocumento.setBounds(45, 140, 84, 28);
		getPanelFormulario().add(lblgnrcDocumento);
		
		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(45, 195, 62, 28);
		getPanelFormulario().add(lblgnrcTelefono);
		
		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(45, 264, 62, 28);
		getPanelFormulario().add(lblgnrcCorreo);
		
		JLabelGenerico lblgnrcDireccin = new JLabelGenerico((String) null);
		lblgnrcDireccin.setText("Dirección ");
		lblgnrcDireccin.setBounds(45, 326, 62, 28);
		getPanelFormulario().add(lblgnrcDireccin);
		
		JLabelGenerico lblgnrcFechaDeNacimiento = new JLabelGenerico((String) null);
		lblgnrcFechaDeNacimiento.setText("Fecha de Nacimiento:");
		lblgnrcFechaDeNacimiento.setBounds(45, 387, 139, 28);
		getPanelFormulario().add(lblgnrcFechaDeNacimiento);
		
		JLabelGenerico lblgnrcFechaDeRegistro = new JLabelGenerico((String) null);
		lblgnrcFechaDeRegistro.setText("Fecha de Registro:");
		lblgnrcFechaDeRegistro.setBounds(45, 501, 118, 28);
		getPanelFormulario().add(lblgnrcFechaDeRegistro);
		
		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(117, 37, 229, 24);
		getPanelFormulario().add(tfNombre);
		
		tfApellido = new JtextFieldGenerico();
		tfApellido.setBounds(117, 87, 229, 24);
		getPanelFormulario().add(tfApellido);
		
		tfDocumento = new JtextFieldGenerico();
		tfDocumento.setBounds(139, 142, 130, 24);
		getPanelFormulario().add(tfDocumento);
		
		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(117, 197, 152, 24);
		getPanelFormulario().add(tfTelefono);
		
		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(117, 266, 229, 24);
		getPanelFormulario().add(tfCorreo);
		
		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(117, 328, 332, 24);
		getPanelFormulario().add(tfDireccion);
		
		tfFecha_nac = new JtextFieldGenerico();
		tfFecha_nac.setBounds(194, 389, 132, 24);
		getPanelFormulario().add(tfFecha_nac);
		
		tfFecha_reg = new JtextFieldGenerico();
		tfFecha_reg.setBounds(173, 503, 139, 24);
		getPanelFormulario().add(tfFecha_reg);
		
		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(45, 443, 51, 28);
		getPanelFormulario().add(lblgnrcEstado);
		
		jcbEstado = new JCheckBox("Activo");
		jcbEstado.setBounds(102, 448, 62, 20);
		getPanelFormulario().add(jcbEstado);

	}
	
	
	
}
