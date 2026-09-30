package vista;

import javax.swing.JDialog;

import componentes.JDialogGenericMini;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.SacramentoController;

/**
 * Pantalla del ABM de Sacramentos (nombre), basada en {@link JDialogGenericMini}. Solo arma la interfaz;
 * la logica vive en SacramentoController.
 */
public class SacramentoVista extends JDialogGenericMini {

	private static final long serialVersionUID = 1L;
	/** Campo de texto: nombre */
	private JtextFieldGenerico tfNombre;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
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

	/**
	 * Crea el controlador que maneja esta pantalla (se usa solo desde {@code main}).
	 */
	private void setUpControlador() {
		new SacramentoController(this);

	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
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

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return campo de texto: nombre */
	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

}