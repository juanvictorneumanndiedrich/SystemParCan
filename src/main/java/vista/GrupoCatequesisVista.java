package vista;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;

import componentes.JComboCheckList;
import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.GrupoCatequesisController;
import modelo.CatequistaModelo;
import modelo.EtapaModelo;
import utilidades.FechaUtil;

public class GrupoCatequesisVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JtextFieldGenerico tfNombre;
	private JFormattedTextField tfAnho;
	private JComboBox<EtapaModelo> cbEtapa;
	private JComboCheckList<CatequistaModelo> comboCatequistas;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GrupoCatequesisVista dialog = new GrupoCatequesisVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new GrupoCatequesisController(this);

	}

	/**
	 * Create the dialog.
	 */
	public GrupoCatequesisVista() {
		setTitle("Grupos de Catequesis");
		setBounds(100, 100, 1080, 720);
		getPanelFormulario().setLayout(null);

		JLabelGenerico lblgnrcNombre = new JLabelGenerico((String) null);
		lblgnrcNombre.setText("Nombre:");
		lblgnrcNombre.setBounds(31, 40, 62, 28);
		getPanelFormulario().add(lblgnrcNombre);

		tfNombre = new JtextFieldGenerico();
		tfNombre.setBounds(103, 42, 373, 24);
		getPanelFormulario().add(tfNombre);

		JLabelGenerico lblgnrcAnho = new JLabelGenerico((String) null);
		lblgnrcAnho.setText("Año:");
		lblgnrcAnho.setBounds(31, 91, 62, 28);
		getPanelFormulario().add(lblgnrcAnho);

		tfAnho = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfAnho.setBounds(103, 93, 117, 24);
		getPanelFormulario().add(tfAnho);

		JLabelGenerico lblgnrcEtapa = new JLabelGenerico((String) null);
		lblgnrcEtapa.setText("Etapa:");
		lblgnrcEtapa.setBounds(31, 142, 62, 28);
		getPanelFormulario().add(lblgnrcEtapa);

		cbEtapa = new JComboBox<EtapaModelo>();
		cbEtapa.setBounds(103, 144, 373, 24);
		cbEtapa.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof EtapaModelo) {
					setText(((EtapaModelo) value).getEtap_descripcion());
				}
				return this;
			}
		});
		getPanelFormulario().add(cbEtapa);

		JLabelGenerico lblgnrcCatequistas = new JLabelGenerico((String) null);
		lblgnrcCatequistas.setText("Catequistas:");
		lblgnrcCatequistas.setBounds(31, 196, 90, 28);
		getPanelFormulario().add(lblgnrcCatequistas);

		comboCatequistas = new JComboCheckList<CatequistaModelo>();
		comboCatequistas.setBounds(103, 198, 373, 24);
		getPanelFormulario().add(comboCatequistas);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

	public JFormattedTextField getTfAnho() {
		return tfAnho;
	}

	public JComboBox<EtapaModelo> getCbEtapa() {
		return cbEtapa;
	}

	public JComboCheckList<CatequistaModelo> getComboCatequistas() {
		return comboCatequistas;
	}

}