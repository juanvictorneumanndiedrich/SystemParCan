package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonInforme;
import componentes.JLabelGenerico;
import controlador.InformeAsistenciaController;
import modelo.CatequizandoModelo;
import modelo.EstadoAsistencia;
import modelo.GrupoCatequesisModelo;
import utilidades.FechaUtil;

/**
 * Pantalla del Informe de Asistencia (menu "Informes"). Muestra, en una
 * grilla de solo lectura, los registros de asistencia ya guardados,
 * filtrados por grupo, catequizando, estado y rango de fecha (fecha de la
 * clase). El boton "Generar Informe" arma el Jasper con exactamente lo que
 * esta mostrando la grilla en ese momento.
 */
public class InformeAsistenciaVista extends JDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox<GrupoCatequesisModelo> cbGrupo;
	private JComboBox<CatequizandoModelo> cbCatequizando;
	private JComboBox<EstadoAsistencia> cbEstado;
	private JFormattedTextField tfFechaDesde;
	private JFormattedTextField tfFechaHasta;
	private JTable tabla;
	private JButtonInforme btnFiltrar;
	private JButtonInforme btnGenerar;
	private JButtonInforme btnCerrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InformeAsistenciaVista dialog = new InformeAsistenciaVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new InformeAsistenciaController(this);
	}

	public InformeAsistenciaVista() {
		setTitle("Informe de Asistencia");
		setBounds(100, 100, 1080, 650);
		getContentPane().setLayout(new BorderLayout());

		// Los filtros van en dos filas fijas (en vez de un unico FlowLayout):
		// con todos en una sola fila, FlowLayout los envuelve a una segunda
		// linea por falta de ancho, pero calcula la altura del panel como si
		// fuera una linea sola, y esa segunda linea (con el boton Filtrar)
		// quedaba tapada por la tabla. Cada fila aca entra siempre entera.
		JPanel panelFiltros = new JPanel();
		panelFiltros.setLayout(new BoxLayout(panelFiltros, BoxLayout.Y_AXIS));
		panelFiltros.setBorder(new EmptyBorder(10, 10, 0, 10));
		getContentPane().add(panelFiltros, BorderLayout.NORTH);

		// Fila 1: filtros de seleccion (combos) y buscador de texto.
		JPanel panelFila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelFiltros.add(panelFila1);

		// Fila 2: rango de fecha y boton Filtrar.
		JPanel panelFila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelFiltros.add(panelFila2);

		JLabelGenerico lblgnrcGrupo = new JLabelGenerico((String) null);
		lblgnrcGrupo.setText("Grupo:");
		panelFila1.add(lblgnrcGrupo);

		cbGrupo = new JComboBox<GrupoCatequesisModelo>();
		cbGrupo.setPreferredSize(new Dimension(180, 24));
		cbGrupo.setRenderer(new DefaultListCellRenderer() {
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
		panelFila1.add(cbGrupo);

		JLabelGenerico lblgnrcCatequizando = new JLabelGenerico((String) null);
		lblgnrcCatequizando.setText("Catequizando:");
		panelFila1.add(lblgnrcCatequizando);

		cbCatequizando = new JComboBox<CatequizandoModelo>();
		cbCatequizando.setPreferredSize(new Dimension(200, 24));
		cbCatequizando.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof CatequizandoModelo) {
					CatequizandoModelo catequizando = (CatequizandoModelo) value;
					setText(catequizando.getCatz_apellido() + ", " + catequizando.getCatz_nombre());
				} else {
					setText("-- Todos --");
				}
				return this;
			}
		});
		panelFila1.add(cbCatequizando);

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		panelFila1.add(lblgnrcEstado);

		cbEstado = new JComboBox<EstadoAsistencia>();
		cbEstado.setPreferredSize(new Dimension(130, 24));
		cbEstado.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setText(textoEstado((EstadoAsistencia) value));
				return this;
			}
		});
		panelFila1.add(cbEstado);

		JLabelGenerico lblgnrcDesde = new JLabelGenerico((String) null);
		lblgnrcDesde.setText("Desde:");
		panelFila2.add(lblgnrcDesde);

		tfFechaDesde = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaDesde.setPreferredSize(new Dimension(90, 24));
		panelFila2.add(tfFechaDesde);

		JLabelGenerico lblgnrcHasta = new JLabelGenerico((String) null);
		lblgnrcHasta.setText("Hasta:");
		panelFila2.add(lblgnrcHasta);

		tfFechaHasta = new JFormattedTextField(FechaUtil.getFormatoFecha());
		tfFechaHasta.setPreferredSize(new Dimension(90, 24));
		panelFila2.add(tfFechaHasta);

		btnFiltrar = new JButtonInforme("Filtrar");
		btnFiltrar.aplicarEstiloNeutro();
		panelFila2.add(btnFiltrar);

		tabla = new JTable();
		tabla.setRowHeight(26);
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBorder(new EmptyBorder(10, 10, 0, 10));
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();
		panelBotones.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panelBotones, BorderLayout.SOUTH);

		btnGenerar = new JButtonInforme("Generar Informe");
		panelBotones.add(btnGenerar);

		btnCerrar = new JButtonInforme("Cerrar");
		btnCerrar.aplicarEstiloSecundario();
		panelBotones.add(btnCerrar);
	}

	private String textoEstado(EstadoAsistencia estado) {
		if (estado == null) return "-- Todos --";
		switch (estado) {
		case PRESENTE:
			return "Presente";
		case AUSENTE:
			return "Ausente";
		case JUSTIFICADO:
			return "Justificado";
		}
		return "";
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JComboBox<GrupoCatequesisModelo> getCbGrupo() {
		return cbGrupo;
	}

	public JComboBox<CatequizandoModelo> getCbCatequizando() {
		return cbCatequizando;
	}

	public JComboBox<EstadoAsistencia> getCbEstado() {
		return cbEstado;
	}

	public JFormattedTextField getTfFechaDesde() {
		return tfFechaDesde;
	}

	public JFormattedTextField getTfFechaHasta() {
		return tfFechaHasta;
	}

	public JTable getTabla() {
		return tabla;
	}

	public JButtonInforme getBtnFiltrar() {
		return btnFiltrar;
	}

	public JButtonInforme getBtnGenerar() {
		return btnGenerar;
	}

	public JButtonInforme getBtnCerrar() {
		return btnCerrar;
	}

}
