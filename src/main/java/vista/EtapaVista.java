package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import componentes.JDialogGenericMini;
import componentes.JLabelGenerico;
import javax.swing.JCheckBox;
import componentes.JtextFieldGenerico;
import controlador.CatequistaController;
import controlador.EtapaController;

/**
 * Pantalla del ABM de Etapas (descripcion y estado), basada en {@link JDialogGenericMini}. Solo arma la interfaz;
 * la logica vive en EtapaController.
 */
public class EtapaVista extends JDialogGenericMini {

	private static final long serialVersionUID = 1L;
	/** Panel de contenido de la pantalla */
	private final JPanel contentPanel = new JPanel();
	/** Campo de texto: descripcion */
	private JtextFieldGenerico tfDescripcion;
	/** Checkbox de estado (Activo/Inactivo) */
	private JCheckBox cbEstado;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 */
	public static void main(String[] args) {
		try {
			EtapaVista dialog = new EtapaVista();
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
		new EtapaController(this);


	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public EtapaVista() {
		setTitle("Etapas");
		setBounds(100, 100, 720, 720);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);

			JLabelGenerico lblgnrcDescripcion = new JLabelGenerico((String) null);
			lblgnrcDescripcion.setText("Descripcion:");
			lblgnrcDescripcion.setBounds(25, 59, 81, 28);
			getPanelFormulario().add(lblgnrcDescripcion);

			cbEstado = new JCheckBox("Activo");
			cbEstado.setBounds(116, 121, 92, 20);
			getPanelFormulario().add(cbEstado);

			tfDescripcion = new JtextFieldGenerico();
			tfDescripcion.setBounds(116, 61, 532, 24);
			getPanelFormulario().add(tfDescripcion);

			JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
			lblgnrcEstado.setText("Estado:");
			lblgnrcEstado.setBounds(55, 116, 51, 28);
			getPanelFormulario().add(lblgnrcEstado);



		}
	}

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return panel de contenido de la pantalla */
	public JPanel getContentPanel() {
		return contentPanel;
	}

	/** @return campo de texto: descripcion */
	public JtextFieldGenerico getTfDescripcion() {
		return tfDescripcion;
	}

	/** @return checkbox de estado (Activo/Inactivo) */
	public JCheckBox getCbEstado() {
		return cbEstado;
	}
}