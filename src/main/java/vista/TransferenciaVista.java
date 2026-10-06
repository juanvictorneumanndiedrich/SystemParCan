package vista;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonInforme;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.TransferenciaController;
import modelo.GrupoCatequesisModelo;

// Pantalla de Transferencia, con el mismo estilo que InscripcionVista:
// a la izquierda se elige el Grupo de ORIGEN (tabla de solo lectura) y a
// la derecha aparecen los catequizandos inscriptos en ese grupo con un
// checkbox. Abajo se elige el Grupo DESTINO, se escribe una observacion
// opcional y se confirma con "Transferir".
// No extiende JDialogGenerico porque no es un ABM (Nuevo/Editar/Eliminar):
// solo registra movimientos de un grupo a otro.
/**
 * Pantalla de Transferencia de catequizandos entre grupos: tabla de grupos de origen, tabla de catequizandos con check,
 * buscador, combo de grupo destino, observacion y botones Transferir y Cancelar. Usa posicionamiento absoluto. Solo
 * arma la interfaz; la logica vive en TransferenciaController.
 */
public class TransferenciaVista extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Tabla de grupos de origen */
	private JTable tablaGrupos;
	/** Tabla de catequizandos del grupo de origen */
	private JTable tablaCatequizandos;
	/** Campo de texto: buscador de texto libre */
	private JtextFieldGenerico tfBuscador;
	/** Etiqueta: grupo de origen seleccionado */
	private JLabelGenerico lblGrupoSeleccionado;
	/** Combo: grupo destino */
	private JComboBox<GrupoCatequesisModelo> cbGrupoDestino;
	/** Campo de texto: observacion (opcional) */
	private JtextFieldGenerico tfObservacion;
	/** Boton "Transferir" */
	private JButtonInforme btnTransferir;
	/** Boton "Cancelar" */
	private JButtonInforme btnCancelar;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TransferenciaVista dialog = new TransferenciaVista();
					dialog.setUpControlador();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Crea el controlador que maneja esta pantalla (se usa solo desde {@code main}).
	 */
	private void setUpControlador() {
		new TransferenciaController(this);
	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public TransferenciaVista() {
		setTitle("Transferencia de Catequizandos entre Grupos");
		setModal(true);
		setBounds(100, 100, 1080, 720);
		getContentPane().setLayout(null);
		((JComponent) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabelGenerico lblgnrcGrupos = new JLabelGenerico((String) null);
		lblgnrcGrupos.setText("Grupo de origen:");
		lblgnrcGrupos.setBounds(10, 10, 220, 24);
		getContentPane().add(lblgnrcGrupos);

		JScrollPane scrollGrupos = new JScrollPane();
		scrollGrupos.setBounds(10, 40, 340, 520);
		getContentPane().add(scrollGrupos);

		tablaGrupos = new JTable();
		scrollGrupos.setViewportView(tablaGrupos);

		lblGrupoSeleccionado = new JLabelGenerico((String) null);
		lblGrupoSeleccionado.setText("Seleccione el grupo de origen para ver sus catequizandos");
		lblGrupoSeleccionado.setBounds(360, 10, 430, 24);
		getContentPane().add(lblGrupoSeleccionado);

		JLabelGenerico lblgnrcBuscar = new JLabelGenerico((String) null);
		lblgnrcBuscar.setText("Buscar:");
		lblgnrcBuscar.setBounds(800, 12, 55, 24);
		getContentPane().add(lblgnrcBuscar);

		tfBuscador = new JtextFieldGenerico();
		tfBuscador.setBounds(860, 10, 200, 24);
		getContentPane().add(tfBuscador);

		JScrollPane scrollCatequizandos = new JScrollPane();
		scrollCatequizandos.setBounds(360, 40, 700, 520);
		getContentPane().add(scrollCatequizandos);

		tablaCatequizandos = new JTable();
		scrollCatequizandos.setViewportView(tablaCatequizandos);

		// Datos de la transferencia: grupo destino y observacion.
		JLabelGenerico lblgnrcDestino = new JLabelGenerico((String) null);
		lblgnrcDestino.setText("Grupo destino:");
		lblgnrcDestino.setBounds(360, 572, 110, 24);
		getContentPane().add(lblgnrcDestino);

		cbGrupoDestino = new JComboBox<GrupoCatequesisModelo>();
		cbGrupoDestino.setBounds(475, 572, 300, 24);
		// Muestra el nombre del grupo; el item null es "-- Seleccione --".
		cbGrupoDestino.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index,
					boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof GrupoCatequesisModelo) {
					setText(((GrupoCatequesisModelo) value).getGrup_nombre());
				} else {
					setText("-- Seleccione --");
				}
				return this;
			}
		});
		getContentPane().add(cbGrupoDestino);

		JLabelGenerico lblgnrcObservacion = new JLabelGenerico((String) null);
		lblgnrcObservacion.setText("Observación:");
		lblgnrcObservacion.setBounds(360, 604, 110, 24);
		getContentPane().add(lblgnrcObservacion);

		tfObservacion = new JtextFieldGenerico();
		tfObservacion.setBounds(475, 604, 585, 24);
		getContentPane().add(tfObservacion);

		// Botones redondeados (mismo estilo que InscripcionVista). El ancho y
		// alto salen del tamaño preferido del boton, asi el texto nunca se corta.
		btnTransferir = new JButtonInforme("Transferir");
		Dimension tamTransferir = btnTransferir.getPreferredSize();
		btnTransferir.setBounds(360, 655 - tamTransferir.height / 2, tamTransferir.width, tamTransferir.height);
		getContentPane().add(btnTransferir);

		btnCancelar = new JButtonInforme("Cancelar");
		btnCancelar.aplicarEstiloSecundario();
		Dimension tamCancelar = btnCancelar.getPreferredSize();
		btnCancelar.setBounds(360 + tamTransferir.width + 10, 655 - tamCancelar.height / 2, tamCancelar.width,
				tamCancelar.height);
		getContentPane().add(btnCancelar);
	}

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return tabla de grupos de origen */
	public JTable getTablaGrupos() {
		return tablaGrupos;
	}

	/** @return tabla de catequizandos del grupo de origen */
	public JTable getTablaCatequizandos() {
		return tablaCatequizandos;
	}

	/** @return campo de texto: buscador de texto libre */
	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	/** @return etiqueta: grupo de origen seleccionado */
	public JLabelGenerico getLblGrupoSeleccionado() {
		return lblGrupoSeleccionado;
	}

	/** @return combo: grupo destino */
	public JComboBox<GrupoCatequesisModelo> getCbGrupoDestino() {
		return cbGrupoDestino;
	}

	/** @return campo de texto: observacion */
	public JtextFieldGenerico getTfObservacion() {
		return tfObservacion;
	}

	/** @return boton "Transferir" */
	public JButtonInforme getBtnTransferir() {
		return btnTransferir;
	}

	/** @return boton "Cancelar" */
	public JButtonInforme getBtnCancelar() {
		return btnCancelar;
	}

}
