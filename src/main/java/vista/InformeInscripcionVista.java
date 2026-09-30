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
import controlador.InformeInscripcionController;
import modelo.GrupoCatequesisModelo;
import utilidades.FechaUtil;

/**
 * Pantalla del Informe de Inscripcion (menu "Informes"). Muestra, en una
 * grilla de solo lectura, las inscripciones filtradas por grupo, estado
 * (activo/inactivo), rango de fecha de inscripcion y un buscador de texto
 * libre (nombre, apellido o documento del catequizando).
 */
public class InformeInscripcionVista extends JDialog {

	private static final long serialVersionUID = 1L;
	/** Combo de grupos de catequesis */
	private JComboBox<GrupoCatequesisModelo> cbGrupo;
	/** Combo de estado (filtro) */
	private JComboBox<Boolean> cbEstado;
	/** Campo de texto: fecha inicial del rango ("Desde") */
	private JFormattedTextField tfFechaDesde;
	/** Campo de texto: fecha final del rango ("Hasta") */
	private JFormattedTextField tfFechaHasta;
	/** Campo de texto: buscador de texto libre */
	private JtextFieldGenerico tfBuscador;
	/** Tabla principal de la pantalla */
	private JTable tabla;
	/** Boton "Filtrar" */
	private JButtonInforme btnFiltrar;
	/** Boton "Generar" */
	private JButtonInforme btnGenerar;
	/** Boton "Cerrar" */
	private JButtonInforme btnCerrar;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 */
	public static void main(String[] args) {
		try {
			InformeInscripcionVista dialog = new InformeInscripcionVista();
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
		new InformeInscripcionController(this);
	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public InformeInscripcionVista() {
		setTitle("Informe de Inscripcion");
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
		cbGrupo.setPreferredSize(new Dimension(200, 24));
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

		JLabelGenerico lblgnrcEstado = new JLabelGenerico((String) null);
		lblgnrcEstado.setText("Estado:");
		panelFila1.add(lblgnrcEstado);

		cbEstado = new JComboBox<Boolean>();
		cbEstado.setPreferredSize(new Dimension(110, 24));
		cbEstado.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value == null) {
					setText("-- Todos --");
				} else {
					setText(Boolean.TRUE.equals(value) ? "Activo" : "Inactivo");
				}
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

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return combo de grupos de catequesis */
	public JComboBox<GrupoCatequesisModelo> getCbGrupo() {
		return cbGrupo;
	}

	/** @return combo de estado (filtro) */
	public JComboBox<Boolean> getCbEstado() {
		return cbEstado;
	}

	/** @return campo de texto: fecha inicial del rango ("Desde") */
	public JFormattedTextField getTfFechaDesde() {
		return tfFechaDesde;
	}

	/** @return campo de texto: fecha final del rango ("Hasta") */
	public JFormattedTextField getTfFechaHasta() {
		return tfFechaHasta;
	}

	/** @return campo de texto: buscador de texto libre */
	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	/** @return tabla principal de la pantalla */
	public JTable getTabla() {
		return tabla;
	}

	/** @return boton "Filtrar" */
	public JButtonInforme getBtnFiltrar() {
		return btnFiltrar;
	}

	/** @return boton "Generar" */
	public JButtonInforme getBtnGenerar() {
		return btnGenerar;
	}

	/** @return boton "Cerrar" */
	public JButtonInforme getBtnCerrar() {
		return btnCerrar;
	}

}
