package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonABM;
import componentes.JLabelGenerico;

/**
 * Pantalla para tomar asistencia de una Clase puntual.
 *
 * No extiende JDialogGenericMini/JDialogGenerico como las demas pantallas:
 * esas estan armadas para el patron "un registro a la vez" (Nuevo / Editar /
 * Eliminar / Guardar sobre una fila seleccionada), y aca el flujo es otro:
 * se edita el estado de TODOS los catequizandos inscriptos en el grupo de
 * la clase, directamente en la grilla, y se guarda todo junto con un unico
 * boton Guardar. Se abre siempre desde ClaseController.tomarAsistencia(),
 * con la clase ya fija.
 */
public class AsistenciaVista extends JDialog {

	private static final long serialVersionUID = 1L;
	/** Etiqueta: info clase */
	private JLabelGenerico lblInfoClase;
	/** Tabla principal de la pantalla */
	private JTable tabla;
	/** Boton "Guardar" */
	private JButtonABM btnGuardar;
	/** Boton "Cerrar" */
	private JButtonABM btnCerrar;

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public AsistenciaVista() {
		setTitle("Asistencia");
		setBounds(100, 100, 640, 560);
		getContentPane().setLayout(new BorderLayout());

		lblInfoClase = new JLabelGenerico((String) null);
		lblInfoClase.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(lblInfoClase, BorderLayout.NORTH);

		tabla = new JTable();
		tabla.setRowHeight(26);
		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBorder(new EmptyBorder(0, 10, 0, 10));
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

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return etiqueta: info clase */
	public JLabelGenerico getLblInfoClase() {
		return lblInfoClase;
	}

	/** @return tabla principal de la pantalla */
	public JTable getTabla() {
		return tabla;
	}

	/** @return boton "Guardar" */
	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	/** @return boton "Cerrar" */
	public JButtonABM getBtnCerrar() {
		return btnCerrar;
	}

}