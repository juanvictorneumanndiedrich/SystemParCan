package vista;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;

import componentes.JComboCheckList;
import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.CatequizandoController;
import modelo.SacramentoModelo;
import utilidades.FechaUtil;

import javax.swing.JCheckBox;

/**
 * Pantalla del ABM de Catequizandos: formulario (datos personales, responsable, estado, fecha de registro y sacramentos) y tabla
 * de registros, sobre la base de {@link JDialogGenerico}. Solo arma la interfaz y expone sus componentes con getters;
 * la logica vive en CatequizandoController.
 */
public class CatequizandoVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	/** Campo de texto: nombre */
	private JtextFieldGenerico tfNombre;
	/** Campo de texto: apellido */
	private JtextFieldGenerico tfApellido;
	/** Campo de texto: documento */
	private JtextFieldGenerico tfDocumento;
	/** Campo de texto: telefono */
	private JtextFieldGenerico tfTelefono;
	/** Campo de texto: correo electronico */
	private JtextFieldGenerico tfCorreo;
	/** Campo de texto: direccion */
	private JtextFieldGenerico tfDireccion;
	/** Checkbox de estado (Activo/Inactivo) */
	private JCheckBox chbEstado;
	/** Campo de texto: fecha de nacimiento (dd/MM/yyyy) */
	private JFormattedTextField tfFecha_nac;
	/** Campo de texto: fecha de registro (dd/MM/yyyy) */
	private JFormattedTextField tfFecha_reg;
	/** Campo de texto: nombre del responsable */
	private JtextFieldGenerico tfNombreResponsable;
	/** Campo de texto: contacto (telefono) del responsable */
	private JtextFieldGenerico tfContactoResponsable;
	/** Combo con checks (seleccion multiple) de sacramentos */
	private JComboCheckList<SacramentoModelo> comboSacramentos;


	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CatequizandoVista dialog = new CatequizandoVista();
					dialog.setUpControlador();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Crea el controlador que maneja esta pantalla (se usa solo desde {@code main}).
	 */
	private void setUpControlador() {
		new CatequizandoController(this);


	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public CatequizandoVista() {
		setTitle("Catequizandos");
		setBounds(100, 100, 1080, 720);
		getPanelFormulario().setLayout(null);

		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(34, 10, 62, 28);
		getPanelFormulario().add(lblgnrcNombre);

		JLabelGenerico lblgnrcApellido = new JLabelGenerico((String) null);
		lblgnrcApellido.setText("Apellido:");
		lblgnrcApellido.setBounds(34, 48, 62, 28);
		getPanelFormulario().add(lblgnrcApellido);

		JLabelGenerico lblgnrcDocumento = new JLabelGenerico((String) null);
		lblgnrcDocumento.setText("Documento:");
		lblgnrcDocumento.setBounds(34, 97, 84, 28);
		getPanelFormulario().add(lblgnrcDocumento);

		JLabelGenerico lblgnrcTelefono = new JLabelGenerico((String) null);
		lblgnrcTelefono.setText("Telefono:");
		lblgnrcTelefono.setBounds(34, 156, 62, 28);
		getPanelFormulario().add(lblgnrcTelefono);

		JLabelGenerico lblgnrcCorreo = new JLabelGenerico((String) null);
		lblgnrcCorreo.setText("Correo:");
		lblgnrcCorreo.setBounds(34, 219, 62, 28);
		getPanelFormulario().add(lblgnrcCorreo);

		JLabelGenerico lblgnrcDireccin = new JLabelGenerico((String) null);
		lblgnrcDireccin.setText("Dirección:");
		lblgnrcDireccin.setBounds(34, 273, 62, 28);
		getPanelFormulario().add(lblgnrcDireccin);

		JLabelGenerico lblgnrcFechaDeNacimiento = new JLabelGenerico((String) null);
		lblgnrcFechaDeNacimiento.setText("Fecha de Nacimiento:");
		lblgnrcFechaDeNacimiento.setBounds(34, 322, 139, 28);
		getPanelFormulario().add(lblgnrcFechaDeNacimiento);

		JLabelGenerico lblgnrcFechaDeRegistro = new JLabelGenerico((String) null);
		lblgnrcFechaDeRegistro.setText("Fecha de Registro:");
		lblgnrcFechaDeRegistro.setBounds(45, 501, 118, 28);
		getPanelFormulario().add(lblgnrcFechaDeRegistro);

		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(106, 12, 229, 24);
		getPanelFormulario().add(tfNombre);

		tfApellido = new JtextFieldGenerico();
		tfApellido.setBounds(106, 50, 229, 24);
		getPanelFormulario().add(tfApellido);

		tfDocumento = new JtextFieldGenerico();
		tfDocumento.setBounds(128, 99, 130, 24);
		getPanelFormulario().add(tfDocumento);

		tfTelefono = new JtextFieldGenerico();
		tfTelefono.setBounds(106, 158, 152, 24);
		getPanelFormulario().add(tfTelefono);

		tfCorreo = new JtextFieldGenerico();
		tfCorreo.setBounds(106, 221, 229, 24);
		getPanelFormulario().add(tfCorreo);

		tfDireccion = new JtextFieldGenerico();
		tfDireccion.setBounds(106, 275, 332, 24);
		getPanelFormulario().add(tfDireccion);

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(45, 443, 51, 28);
		getPanelFormulario().add(lblgnrcEstado);

		chbEstado = new JCheckBox("Activo");
		chbEstado.setBounds(102, 448, 62, 20);
		getPanelFormulario().add(chbEstado);

		tfFecha_nac = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha_nac.setBounds(183, 326, 167, 24);
		getPanelFormulario().add(tfFecha_nac);

		tfFecha_reg = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha_reg.setBounds(173, 503, 167, 24);
		getPanelFormulario().add(tfFecha_reg);

		JLabelGenerico lblgnrcNombreResponsable = new JLabelGenerico((String) null);
		lblgnrcNombreResponsable.setText("Nombre Responsable:");
		lblgnrcNombreResponsable.setBounds(34, 367, 139, 28);
		getPanelFormulario().add(lblgnrcNombreResponsable);

		JLabelGenerico lblgnrcContactoResponsable = new JLabelGenerico((String) null);
		lblgnrcContactoResponsable.setText("Contacto del Responsable:");
		lblgnrcContactoResponsable.setBounds(39, 405, 167, 28);
		getPanelFormulario().add(lblgnrcContactoResponsable);

		tfNombreResponsable = new JtextFieldGenerico();
		tfNombreResponsable.setBounds(173, 369, 290, 24);
		getPanelFormulario().add(tfNombreResponsable);

		tfContactoResponsable = new JtextFieldGenerico();
		tfContactoResponsable.setBounds(216, 407, 290, 24);
		getPanelFormulario().add(tfContactoResponsable);

		JLabelGenerico lblgnrcSacramentos = new JLabelGenerico((String) null);
		lblgnrcSacramentos.setText("Sacramentos:");
		lblgnrcSacramentos.setBounds(34, 538, 90, 28);
		getPanelFormulario().add(lblgnrcSacramentos);

		comboSacramentos = new JComboCheckList<SacramentoModelo>();
		comboSacramentos.setBounds(128, 540, 300, 24);
		getPanelFormulario().add(comboSacramentos);

	}


	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	/** @return campo de texto: nombre */
	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}


	/** @return campo de texto: apellido */
	public JtextFieldGenerico getTfApellido() {
		return tfApellido;
	}


	/** @return campo de texto: documento */
	public JtextFieldGenerico getTfDocumento() {
		return tfDocumento;
	}


	/** @return campo de texto: telefono */
	public JtextFieldGenerico getTfTelefono() {
		return tfTelefono;
	}


	/** @return campo de texto: correo electronico */
	public JtextFieldGenerico getTfCorreo() {
		return tfCorreo;
	}


	/** @return campo de texto: direccion */
	public JtextFieldGenerico getTfDireccion() {
		return tfDireccion;
	}


	/** @return campo de texto: fecha de nacimiento (dd/MM/yyyy) */
	public JFormattedTextField getTfFecha_nac() {
		return tfFecha_nac;
	}


	/** @return checkbox de estado (Activo/Inactivo) */
	public JCheckBox getJcbEstado() {
		return chbEstado;
	}


	/** @return campo de texto: fecha de registro (dd/MM/yyyy) */
	public JFormattedTextField getTfFecha_reg() {
		return tfFecha_reg;
	}

	/** @return campo de texto: nombre del responsable */
	public JtextFieldGenerico getTfNombreResponsable() {
		return tfNombreResponsable;

	}

	/** @return campo de texto: contacto (telefono) del responsable */
	public JtextFieldGenerico getTfContactoResponsable() {
		return tfContactoResponsable;

	}

	/** @return combo con checks (seleccion multiple) de sacramentos */
	public JComboCheckList<SacramentoModelo> getComboSacramentos() {
		return comboSacramentos;
	}
}