package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import componentes.JDialogGenericMini;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import javax.swing.JCheckBox;

public class ClaseVista extends JDialogGenericMini {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JtextFieldGenerico tfDescripcion;
	private JCheckBox cbEstado;

	/**
	 * Launch the application.
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
	 * Create the dialog.
	 */
	public ClaseVista() {
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
		
		JLabelGenerico lblgnrcDescripcion = new JLabelGenerico((String) null);
		lblgnrcDescripcion.setText("Descripcion:");
		lblgnrcDescripcion.setBounds(33, 67, 81, 28);
		getPanelFormulario().add(lblgnrcDescripcion);
		
		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		lblgnrcEstado.setBounds(63, 122, 51, 28);
		getPanelFormulario().add(lblgnrcEstado);
		
		tfDescripcion = new JtextFieldGenerico();
		tfDescripcion.setBounds(124, 69, 532, 24);
		getPanelFormulario().add(tfDescripcion);
		
		cbEstado = new JCheckBox("Activo");
		cbEstado.setBounds(123, 127, 92, 20);
		getPanelFormulario().add(cbEstado);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanel getContentPanel() {
		return contentPanel;
	}

	public JtextFieldGenerico getTfDescripcion() {
		return tfDescripcion;
	}

	public JCheckBox getCbEstado() {
		return cbEstado;
	}
	
}
