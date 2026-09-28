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
import componentes.JtextFieldGenerico;
import controlador.InformeTransferenciaController;
import modelo.GrupoCatequesisModelo;
import utilidades.FechaUtil;

/**
 * Pantalla del Informe de Transferencia (menu "Informes"). Muestra, en una
 * grilla de solo lectura, el historial de transferencias filtrado por
 * grupo de origen, grupo de destino, rango de fecha y un buscador de
 * texto libre (nombre, apellido o documento del catequizando).
 */
public class InformeTransferenciaVista extends JDialog {

	private static final long serialVersionUID = 1L;
	private JComboBox<GrupoCatequesisModelo> cbGrupoOrigen;
	private JComboBox<GrupoCatequesisModelo> cbGrupoDestino;
	private JFormattedTextField tfFechaDesde;
	private JFormattedTextField tfFechaHasta;
	private JtextFieldGenerico tfBuscador;
	private JTable tabla;
	private JButtonInforme btnFiltrar;
	private JButtonInforme btnGenerar;
	private JButtonInforme btnCerrar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InformeTransferenciaVista dialog = new InformeTransferenciaVista();
			dialog.setUpControlador();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpControlador() {
		new InformeTransferenciaController(this);
	}

	public InformeTransferenciaVista() {
		setTitle("Informe de Transferencia");
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

		JLabelGenerico lblgnrcOrigen = new JLabelGenerico((String) null);
		lblgnrcOrigen.setText("Grupo Origen:");
		panelFila1.add(lblgnrcOrigen);

		cbGrupoOrigen = new JComboBox<GrupoCatequesisModelo>();
		cbGrupoOrigen.setPreferredSize(new Dimension(180, 24));
		cbGrupoOrigen.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof GrupoCatequesisModelo) {
					setText(((GrupoCatequesisModelo) value).getGrup_nombre());
				} else {
					setText("-- Todos --");
				}
				return this;
			}
		});
		panelFila1.add(cbGrupoOrigen);

		JLabelGenerico lblgnrcDestino = new JLabelGenerico((String) null);
		lblgnrcDestino.setText("Grupo Destino:");
		panelFila1.add(lblgnrcDestino);

		cbGrupoDestino = new JComboBox<GrupoCatequesisModelo>();
		cbGrupoDestino.setPreferredSize(new Dimension(180, 24));
		cbGrupoDestino.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof GrupoCatequesisModelo) {
					setText(((GrupoCatequesisModelo) value).getGrup_nombre());
				} else {
					setText("-- Todos --");
				}
				return this;
			}
		});
		panelFila1.add(cbGrupoDestino);

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

		JLabelGenerico lblgnrcBuscador = new JLabelGenerico((String) null);
		lblgnrcBuscador.setText("Buscar:");
		panelFila1.add(lblgnrcBuscador);

		tfBuscador = new JtextFieldGenerico();
		tfBuscador.setPreferredSize(new Dimension(180, 24));
		panelFila1.add(tfBuscador);

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JComboBox<GrupoCatequesisModelo> getCbGrupoOrigen() {
		return cbGrupoOrigen;
	}

	public JComboBox<GrupoCatequesisModelo> getCbGrupoDestino() {
		return cbGrupoDestino;
	}

	public JFormattedTextField getTfFechaDesde() {
		return tfFechaDesde;
	}

	public JFormattedTextField getTfFechaHasta() {
		return tfFechaHasta;
	}

	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
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
