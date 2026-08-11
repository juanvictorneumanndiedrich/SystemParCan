package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonABM;
import componentes.JLabelGenerico;
import controlador.AsistenciasController;
import modelo.ClaseModelo;
import modelo.GrupoCatequesisModelo;
import utilidades.FechaUtil;

/**
 * Pantalla de Asistencia de acceso directo (accesible desde el acceso
 * rapido de la pantalla principal, sin depender de una Clase ya elegida).
 * A diferencia de AsistenciaVista (que se abre desde adentro de Clases,
 * con la clase puntual ya fija), aca la clase se elige con dos combos en
 * cascada: primero el Grupo de Catequesis, y despues la Clase de ese
 * grupo. Al elegir la clase se carga la misma grilla de asistencia de
 * siempre.
 */
public class AsistenciasVista extends JDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox<GrupoCatequesisModelo> cbGrupo;
	private JComboBox<ClaseModelo> cbClase;
	private JTable tabla;
	private JButtonABM btnGuardar;
	private JButtonABM btnCerrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AsistenciasVista dialog = new AsistenciasVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new AsistenciasController(this);
	}

	public AsistenciasVista() {
		setTitle("Asistencia");
		setBounds(100, 100, 640, 600);
		getContentPane().setLayout(new BorderLayout());

		JPanel panelSeleccion = new JPanel();
		panelSeleccion.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSeleccion.setBorder(new EmptyBorder(10, 10, 0, 10));
		getContentPane().add(panelSeleccion, BorderLayout.NORTH);

		JLabelGenerico lblgnrcGrupo = new JLabelGenerico((String) null);
		lblgnrcGrupo.setText("Grupo:");
		panelSeleccion.add(lblgnrcGrupo);

		cbGrupo = new JComboBox<GrupoCatequesisModelo>();
		cbGrupo.setPreferredSize(new Dimension(220, 24));
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
		panelSeleccion.add(cbGrupo);

		JLabelGenerico lblgnrcClase = new JLabelGenerico((String) null);
		lblgnrcClase.setText("Clase:");
		panelSeleccion.add(lblgnrcClase);

		cbClase = new JComboBox<ClaseModelo>();
		cbClase.setPreferredSize(new Dimension(260, 24));
		cbClase.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof ClaseModelo) {
					ClaseModelo clase = (ClaseModelo) value;
					String fecha = clase.getClase_fechaClase() != null
							? FechaUtil.fechaAString(clase.getClase_fechaClase())
							: "";
					String descripcion = clase.getClase_descripcion() != null ? clase.getClase_descripcion() : "";
					setText(fecha + (descripcion.isEmpty() ? "" : " - " + descripcion));
				}
				return this;
			}
		});
		panelSeleccion.add(cbClase);

		tabla = new JTable();
		tabla.setRowHeight(26);
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBorder(new EmptyBorder(10, 10, 0, 10));
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		btnGuardar = new JButtonABM();
		btnGuardar.setText("Guardar");
		btnGuardar.setPreferredSize(new Dimension(95, 80));
		panelBotones.add(btnGuardar);

		btnCerrar = new JButtonABM();
		btnCerrar.setText("Cerrar");
		btnCerrar.setPreferredSize(new Dimension(95, 80));
		panelBotones.add(btnCerrar);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JComboBox<GrupoCatequesisModelo> getCbGrupo() {
		return cbGrupo;
	}

	public JComboBox<ClaseModelo> getCbClase() {
		return cbClase;
	}

	public JTable getTabla() {
		return tabla;
	}

	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	public JButtonABM getBtnCerrar() {
		return btnCerrar;
	}

}