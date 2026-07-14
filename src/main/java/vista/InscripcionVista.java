package vista;

import java.awt.EventQueue;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import componentes.JButtonABM;
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
public class InscripcionVista extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTable tablaGrupos;
	private JTable tablaCatequizandos;
	private JtextFieldGenerico tfBuscador;
	private JLabelGenerico lblGrupoSeleccionado;
	private JButtonABM btnGuardar;
	private JButtonABM btnCancelar;

	/**
	 * Launch the application.
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

	private void setUpControlador() {
		new InscripcionController(this);
	}

	/**
	 * Create the dialog.
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

		btnGuardar = new JButtonABM();
		btnGuardar.setText("Guardar");
		btnGuardar.setBounds(360, 612, 150, 60);
		getContentPane().add(btnGuardar);

		btnCancelar = new JButtonABM();
		btnCancelar.setText("Cancelar");
		btnCancelar.setBounds(520, 612, 150, 60);
		getContentPane().add(btnCancelar);
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JTable getTablaGrupos() {
		return tablaGrupos;
	}

	public JTable getTablaCatequizandos() {
		return tablaCatequizandos;
	}

	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	public JLabelGenerico getLblGrupoSeleccionado() {
		return lblGrupoSeleccionado;
	}

	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	public JButtonABM getBtnCancelar() {
		return btnCancelar;
	}

}