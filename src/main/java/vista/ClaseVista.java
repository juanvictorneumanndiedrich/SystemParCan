package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import componentes.JButtonABM;
import componentes.JDialogGenericMini;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import utilidades.FechaUtil;

/**
 * Pantalla del ABM de Clases de un grupo de catequesis (se abre desde la pantalla de Grupos). Se basa en {@link JDialogGenericMini}
 * y agrega el boton Tomar Asistencia. Solo arma la interfaz; la logica vive en ClaseController.
 */
public class ClaseVista extends JDialogGenericMini {

	private static final long serialVersionUID = 1L;
	/** Panel de contenido de la pantalla */
	private final JPanel contentPanel = new JPanel();
	/** Campo de texto: fecha (dd/MM/yyyy) */
	private JFormattedTextField tfFecha;
	/** Campo de texto: descripcion */
	private JtextFieldGenerico tfDescripcion;
	/** Boton "Tomar Asistencia" */
	private JButtonABM btnTomarAsistencia;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 * Nota: este main() solo sirve para previsualizar el layout en WindowBuilder.
	 * La pantalla real se abre desde GrupoCatequesisController.verClases(),
	 * que instancia ClaseController(vista, grupo) con el grupo ya seleccionado.
	 */
	public static void main(String[] args) {
		try {
			ClaseVista dialog = new ClaseVista();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public ClaseVista() {
		setTitle("Clases");
		setBounds(100, 100, 720, 720);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		JLabelGenerico lblgnrcFecha = new JLabelGenerico((String) null);
		lblgnrcFecha.setText("Fecha:");
		lblgnrcFecha.setBounds(33, 67, 81, 28);
		getPanelFormulario().add(lblgnrcFecha);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(124, 69, 117, 24);
		getPanelFormulario().add(tfFecha);

		JLabelGenerico lblgnrcDescripcion = new JLabelGenerico((String) null);
		lblgnrcDescripcion.setText("Descripcion:");
		lblgnrcDescripcion.setBounds(33, 122, 81, 28);
		getPanelFormulario().add(lblgnrcDescripcion);

		tfDescripcion = new JtextFieldGenerico();
		tfDescripcion.setBounds(124, 124, 532, 24);
		getPanelFormulario().add(tfDescripcion);

		// Acceso a la pantalla de Asistencia de la clase seleccionada en la tabla.
		// Va dentro de panelFormulario (que usa layout null propio) y no
		// directamente en getContentPane(): este ultimo cambia a BorderLayout
		// mas arriba en este mismo constructor, y agregar ahi un componente sin
		// una constraint explicita lo manda a CENTER, pisando a contentPanel y
		// quedando mal posicionado/tapado. Dentro de panelFormulario no hay ese
		// problema.
		btnTomarAsistencia = new JButtonABM();
		btnTomarAsistencia.setText("Tomar Asistencia");
		btnTomarAsistencia.setBounds(33, 165, 220, 35);
		getPanelFormulario().add(btnTomarAsistencia);
	}

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return panel de contenido de la pantalla */
	public JPanel getContentPanel() {
		return contentPanel;
	}

	/** @return campo de texto: fecha (dd/MM/yyyy) */
	public JFormattedTextField getTfFecha() {
		return tfFecha;
	}

	/** @return campo de texto: descripcion */
	public JtextFieldGenerico getTfDescripcion() {
		return tfDescripcion;
	}

	/** @return boton "Tomar Asistencia" */
	public JButtonABM getBtnTomarAsistencia() {
		return btnTomarAsistencia;
	}

}