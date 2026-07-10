package vista;

import javax.swing.JDialog;

import componentes.JDialogGenericMini;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.SacramentoController;

public class SacramentoVista extends JDialogGenericMini {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfNombre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SacramentoVista dialog = new SacramentoVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new SacramentoController(this);

	}

	/**
	 * Create the dialog.
	 */
	public SacramentoVista() {
		setTitle("Sacramentos");
		setBounds(100, 100, 720, 720);

		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(25, 59, 81, 28);
		getPanelFormulario().add(lblgnrcNombre);

		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(116, 61, 532, 24);
		getPanelFormulario().add(tfNombre);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

}