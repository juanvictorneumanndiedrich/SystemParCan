package vista;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;

import componentes.JButtonABM;
import componentes.JComboCheckList;
import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.GrupoCatequesisController;
import modelo.CatequistaModelo;
import modelo.EtapaModelo;
import utilidades.FechaUtil;

/**
 * Pantalla del ABM de Grupos de Catequesis (nombre, año, etapa y catequistas), basada en {@link JDialogGenerico}. Incluye el boton
 * Ver Clases. Solo arma la interfaz; la logica vive en GrupoCatequesisController.
 */
public class GrupoCatequesisVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	/** Campo de texto: nombre */
	private JtextFieldGenerico tfNombre;
	/** Campo de texto: año del grupo */
	private JFormattedTextField tfAnho;
	/** Combo de etapas */
	private JComboBox<EtapaModelo> cbEtapa;
	/** Combo con checks (seleccion multiple) de catequistas */
	private JComboCheckList<CatequistaModelo> comboCatequistas;
	/** Boton "Ver Clases" */
	private JButtonABM btnVerClases;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
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

	/**
	 * Crea el controlador que maneja esta pantalla (se usa solo desde {@code main}).
	 */
	private void setUpControlador() {
		new GrupoCatequesisController(this);

	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
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

		// Acceso a la pantalla de Clases del grupo seleccionado en la tabla.
		// Se ubica en el espacio libre arriba del buscador (que empieza en y=65).
		btnVerClases = new JButtonABM();
		btnVerClases.setText("Ver Clases");
		btnVerClases.setBounds(535, 10, 150, 50);
		getContentPane().add(btnVerClases);
	}

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return campo de texto: nombre */
	public JtextFieldGenerico getTfNombre() {
		return tfNombre;
	}

	/** @return campo de texto: año del grupo */
	public JFormattedTextField getTfAnho() {
		return tfAnho;
	}

	/** @return combo de etapas */
	public JComboBox<EtapaModelo> getCbEtapa() {
		return cbEtapa;
	}

	/** @return combo con checks (seleccion multiple) de catequistas */
	public JComboCheckList<CatequistaModelo> getComboCatequistas() {
		return comboCatequistas;
	}

	/** @return boton "Ver Clases" */
	public JButtonABM getBtnVerClases() {
		return btnVerClases;
	}

}