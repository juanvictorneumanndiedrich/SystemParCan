package vista;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonInforme;
import componentes.JLabelGenerico;
import componentes.JtextFieldGenerico;
import controlador.InscripcionController;

// Pantalla de Inscripcion pensada como "armado de roster por grupo":
// a la izquierda se eligen los Grupos de Catequesis (tabla de solo lectura),
// y a la derecha aparecen TODOS los catequizandos con un checkbox: tildado
// significa que ese catequizando queda inscripto en el grupo seleccionado.
// No extiende JDialogGenerico porque el flujo Nuevo/Editar/Eliminar de ese
// componente no aplica aca: la unidad de trabajo es "el roster completo de
// un grupo", no un registro de inscripcion a la vez.
/**
 * Pantalla de Inscripcion por grupo: a la izquierda la tabla de grupos de catequesis y a la derecha la de catequizandos con check para
 * inscribirlos, mas un buscador y los botones Guardar y Cancelar. Usa posicionamiento absoluto. Solo arma la interfaz;
 * la logica vive en InscripcionController.
 */
public class InscripcionVista extends JDialog {

	private static final long serialVersionUID = 1L;

	/** Tabla de grupos */
	private JTable tablaGrupos;
	/** Tabla de catequizandos */
	private JTable tablaCatequizandos;
	/** Campo de texto: buscador de texto libre */
	private JtextFieldGenerico tfBuscador;
	/** Etiqueta: grupo seleccionado */
	private JLabelGenerico lblGrupoSeleccionado;
	/** Boton "Guardar" */
	private JButtonInforme btnGuardar;
	/** Boton "Cancelar" */
	private JButtonInforme btnCancelar;

	/**
	 * Punto de entrada para probar la pantalla de forma aislada, sin la pantalla principal.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InscripcionVista dialog = new InscripcionVista();
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
		new InscripcionController(this);
	}

	/**
	 * Arma la pantalla: titulo, tamaño y componentes.
	 */
	public InscripcionVista() {
		setTitle("Inscripciones por Grupo de Catequesis");
		setModal(true);
		setBounds(100, 100, 1080, 720);
		getContentPane().setLayout(null);
		((JComponent) getContentPane()).setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabelGenerico lblgnrcGrupos = new JLabelGenerico((String) null);
		lblgnrcGrupos.setText("Grupos de Catequesis:");
		lblgnrcGrupos.setBounds(10, 10, 220, 24);
		getContentPane().add(lblgnrcGrupos);

		JScrollPane scrollGrupos = new JScrollPane();
		scrollGrupos.setBounds(10, 40, 340, 560);
		getContentPane().add(scrollGrupos);

		tablaGrupos = new JTable();
		scrollGrupos.setViewportView(tablaGrupos);

		lblGrupoSeleccionado = new JLabelGenerico((String) null);
		lblGrupoSeleccionado.setText("Seleccione un grupo para ver sus catequizandos");
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
		scrollCatequizandos.setBounds(360, 40, 700, 560);
		getContentPane().add(scrollCatequizandos);

		tablaCatequizandos = new JTable();
		scrollCatequizandos.setViewportView(tablaCatequizandos);

		// Botones redondeados (mismo estilo que las pantallas de Informe).
		// Como esta pantalla usa layout absoluto, el ancho y alto se toman
		// del tamaño preferido del boton (calculado desde el texto), asi el
		// texto nunca queda cortado; quedan centrados en la franja de abajo
		// de las tablas (y=612 a 672).
		btnGuardar = new JButtonInforme("Guardar");
		Dimension tamGuardar = btnGuardar.getPreferredSize();
		btnGuardar.setBounds(360, 642 - tamGuardar.height / 2, tamGuardar.width, tamGuardar.height);
		getContentPane().add(btnGuardar);

		btnCancelar = new JButtonInforme("Cancelar");
		btnCancelar.aplicarEstiloSecundario();
		Dimension tamCancelar = btnCancelar.getPreferredSize();
		btnCancelar.setBounds(360 + tamGuardar.width + 10, 642 - tamCancelar.height / 2, tamCancelar.width,
				tamCancelar.height);
		getContentPane().add(btnCancelar);
	}

	/** @return el serialVersionUID de la clase */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/** @return tabla de grupos */
	public JTable getTablaGrupos() {
		return tablaGrupos;
	}

	/** @return tabla de catequizandos */
	public JTable getTablaCatequizandos() {
		return tablaCatequizandos;
	}

	/** @return campo de texto: buscador de texto libre */
	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	/** @return etiqueta: grupo seleccionado */
	public JLabelGenerico getLblGrupoSeleccionado() {
		return lblGrupoSeleccionado;
	}

	/** @return boton "Guardar" */
	public JButtonInforme getBtnGuardar() {
		return btnGuardar;
	}

	/** @return boton "Cancelar" */
	public JButtonInforme getBtnCancelar() {
		return btnCancelar;
	}

}