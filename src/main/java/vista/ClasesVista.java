package vista;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;

import componentes.JDialogGenerico;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.ClasesController;
import modelo.GrupoCatequesisModelo;
import utilidades.FechaUtil;

/**
 * Pantalla de Clases de acceso directo (accesible desde el acceso rapido
 * de la pantalla principal, sin depender de un Grupo de Catequesis ya
 * elegido). A diferencia de ClaseVista (que se abre desde adentro de
 * Grupo de Catequesis, con el grupo ya fijo), aca la tabla muestra las
 * clases de TODOS los grupos, y el grupo de cada clase se elige con un
 * combo dentro del formulario, igual que el combo de Etapa en
 * GrupoCatequesisVista.
 */
public class ClasesVista extends JDialogGenerico {

	private static final long serialVersionUID = 1L;
	private JComboBox<GrupoCatequesisModelo> cbGrupo;
	private JFormattedTextField tfFecha;
	private JtextFieldGenerico tfDescripcion;
	private JComboBox<GrupoCatequesisModelo> cbFiltroGrupo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ClasesVista dialog = new ClasesVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new ClasesController(this);
	}

	/**
	 * Create the dialog.
	 */
	public ClasesVista() {
		setTitle("Clases");
		setBounds(100, 100, 1080, 720);
		getPanelFormulario().setLayout(null);

		JLabelGenerico lblgnrcGrupo = new JLabelGenerico((String) null);
		lblgnrcGrupo.setText("Grupo:");
		lblgnrcGrupo.setBounds(31, 40, 62, 28);
		getPanelFormulario().add(lblgnrcGrupo);

		cbGrupo = new JComboBox<GrupoCatequesisModelo>();
		cbGrupo.setBounds(103, 42, 373, 24);
		cbGrupo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof GrupoCatequesisModelo) {
					setText(((GrupoCatequesisModelo) value).getGrup_nombre());
				}
				return this;
			}
		});
		getPanelFormulario().add(cbGrupo);

		JLabelGenerico lblgnrcFecha = new JLabelGenerico((String) null);
		lblgnrcFecha.setText("Fecha:");
		lblgnrcFecha.setBounds(31, 91, 62, 28);
		getPanelFormulario().add(lblgnrcFecha);

		tfFecha = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFecha.setBounds(103, 93, 117, 24);
		getPanelFormulario().add(tfFecha);

		JLabelGenerico lblgnrcDescripcion = new JLabelGenerico((String) null);
		lblgnrcDescripcion.setText("Descripcion:");
		lblgnrcDescripcion.setBounds(31, 142, 80, 28);
		getPanelFormulario().add(lblgnrcDescripcion);

		tfDescripcion = new JtextFieldGenerico();
		tfDescripcion.setBounds(115, 144, 361, 24);
		getPanelFormulario().add(tfDescripcion);

		// Filtro por grupo, cerca del buscador: va en el hueco libre que
		// queda arriba de "Buscador:" (esa fila usa x=535 a 1056, y=65 en
		// adelante; esta usa el mismo ancho pero mas arriba, y=12).
		JLabelGenerico lblgnrcFiltrarPor = new JLabelGenerico((String) null);
		lblgnrcFiltrarPor.setText("Filtrar por grupo:");
		lblgnrcFiltrarPor.setBounds(535, 14, 118, 24);
		getContentPane().add(lblgnrcFiltrarPor);

		cbFiltroGrupo = new JComboBox<GrupoCatequesisModelo>();
		cbFiltroGrupo.setBounds(658, 14, 398, 24);
		cbFiltroGrupo.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof GrupoCatequesisModelo) {
					setText(((GrupoCatequesisModelo) value).getGrup_nombre());
				} else {
					setText("-- Todos los grupos --");
				}
				return this;
			}
		});
		getContentPane().add(cbFiltroGrupo);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JComboBox<GrupoCatequesisModelo> getCbGrupo() {
		return cbGrupo;
	}

	public JFormattedTextField getTfFecha() {
		return tfFecha;
	}

	public JtextFieldGenerico getTfDescripcion() {
		return tfDescripcion;
	}

	public JComboBox<GrupoCatequesisModelo> getCbFiltroGrupo() {
		return cbFiltroGrupo;
	}

}