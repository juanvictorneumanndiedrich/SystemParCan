package vista;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.border.EmptyBorder;

import componentes.JButtonAccesoDirecto;
import componentes.JMenuItemPersonalizado;
import componentes.JPanelPantallaPrincipal;
import controlador.PantallaPrincipalController;

public class PantallaPrincipalVista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanelPantallaPrincipal contentPane;
	private JMenuItemPersonalizado mntmprsnlzdCatequizando;
	private JMenuItemPersonalizado mntmprsnlzdCatequista;
	private JMenuItemPersonalizado mntmprsnlzdEtapa;
	private JMenuItemPersonalizado mntmprsnlzdGrupocatequesis;
	private JMenuItemPersonalizado mntmprsnlzdCatequizando_1;
	private JMenuItemPersonalizado mntmprsnlzdCatequistas;
	private JMenuItemPersonalizado mntmprsnlzdGrupocatequesis_1;
	private JMenuItemPersonalizado mntmprsnlzdEtapa_1;
	private JMenuItemPersonalizado mntmprsnlzdSacramentos_1;
	private JMenuItemPersonalizado mntmprsnlzdAsistencia;
	private JMenuItemPersonalizado mntmprsnlzdInscripcion_1;
	private JMenuItemPersonalizado mntmprsnlzdTransferencia_1;
	private JMenuItemPersonalizado mntmprsnlzdClase_1;
	private JMenuItemPersonalizado mntmprsnlzdInscripcion;
	private JMenuItemPersonalizado mntmprsnlzdTransferencia;
	private JMenuItemPersonalizado mntmprsnlzdSacramentos;
	private JMenuItemPersonalizado mntmprsnlzdClase;
	private JButtonAccesoDirecto btncsdrctClases;
	private JButtonAccesoDirecto btncsdrctCatequista;
	private JButtonAccesoDirecto btncsdrctCatequizando;
	private JButtonAccesoDirecto btncsdrctInscripcion;
	private JButtonAccesoDirecto btncsdrctAsistencia;
	private JButtonAccesoDirecto btncsdrctGrupocatequesis;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipalVista frame = new PantallaPrincipalVista();
					new PantallaPrincipalController(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PantallaPrincipalVista() {
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(this);
		setTitle("SystemParCan");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Registros");
		menuBar.add(mnNewMenu);

		mntmprsnlzdCatequizando = new JMenuItemPersonalizado();
		mntmprsnlzdCatequizando.setText("catequizando");
		mnNewMenu.add(mntmprsnlzdCatequizando);

		mntmprsnlzdCatequista = new JMenuItemPersonalizado();
		mntmprsnlzdCatequista.setText("catequista");
		mnNewMenu.add(mntmprsnlzdCatequista);

		mntmprsnlzdEtapa = new JMenuItemPersonalizado();
		mntmprsnlzdEtapa.setText("etapa");
		mnNewMenu.add(mntmprsnlzdEtapa);

		mntmprsnlzdGrupocatequesis = new JMenuItemPersonalizado();
		mntmprsnlzdGrupocatequesis.setText("grupo_catequesis");
		mnNewMenu.add(mntmprsnlzdGrupocatequesis);

		mntmprsnlzdSacramentos = new JMenuItemPersonalizado();
		mntmprsnlzdSacramentos.setText("sacramentos");
		mnNewMenu.add(mntmprsnlzdSacramentos);

		JMenu mnInformes = new JMenu("Listados");
		menuBar.add(mnInformes);

		mntmprsnlzdCatequizando_1 = new JMenuItemPersonalizado();
		mntmprsnlzdCatequizando_1.setText("catequizando");
		mnInformes.add(mntmprsnlzdCatequizando_1);

		mntmprsnlzdCatequistas = new JMenuItemPersonalizado();
		mntmprsnlzdCatequistas.setText("catequista");
		mnInformes.add(mntmprsnlzdCatequistas);

		mntmprsnlzdGrupocatequesis_1 = new JMenuItemPersonalizado();
		mntmprsnlzdGrupocatequesis_1.setText("grupo_catequesis");
		mnInformes.add(mntmprsnlzdGrupocatequesis_1);

		mntmprsnlzdEtapa_1 = new JMenuItemPersonalizado();
		mntmprsnlzdEtapa_1.setText("etapa");
		mnInformes.add(mntmprsnlzdEtapa_1);

		mntmprsnlzdSacramentos_1 = new JMenuItemPersonalizado();
		mntmprsnlzdSacramentos_1.setText("sacramentos");
		mnInformes.add(mntmprsnlzdSacramentos_1);

		// Menu "Informes": un informe por cada ABM que es evento/transaccion
		// (Asistencia, Inscripcion, Transferencia, Clase). Cada uno abre su
		// propia pantalla de filtro + grilla (ver InformeXController).
		JMenu mnInformes_1 = new JMenu("Informes");
		menuBar.add(mnInformes_1);

		mntmprsnlzdAsistencia = new JMenuItemPersonalizado();
		mntmprsnlzdAsistencia.setText("asistencia");
		mnInformes_1.add(mntmprsnlzdAsistencia);

		mntmprsnlzdInscripcion_1 = new JMenuItemPersonalizado();
		mntmprsnlzdInscripcion_1.setText("inscripcion");
		mnInformes_1.add(mntmprsnlzdInscripcion_1);

		mntmprsnlzdTransferencia_1 = new JMenuItemPersonalizado();
		mntmprsnlzdTransferencia_1.setText("transferencia");
		mnInformes_1.add(mntmprsnlzdTransferencia_1);

		mntmprsnlzdClase_1 = new JMenuItemPersonalizado();
		mntmprsnlzdClase_1.setText("clase");
		mnInformes_1.add(mntmprsnlzdClase_1);

		JMenu mnUtilidades = new JMenu("Utilidades");
		menuBar.add(mnUtilidades);

		mntmprsnlzdInscripcion = new JMenuItemPersonalizado();
		mntmprsnlzdInscripcion.setText("inscripcion");
		mnUtilidades.add(mntmprsnlzdInscripcion);

		mntmprsnlzdTransferencia = new JMenuItemPersonalizado();
		mntmprsnlzdTransferencia.setText("transferencia");
		mnUtilidades.add(mntmprsnlzdTransferencia);

		mntmprsnlzdClase = new JMenuItemPersonalizado();
		mntmprsnlzdClase.setText("clase");
		mnUtilidades.add(mntmprsnlzdClase);
		contentPane = new JPanelPantallaPrincipal();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Todos estos botones tienen ahora el mismo tamaño (120x135). El
		// ancho se agrando un poco respecto del original (104/111) para
		// que le entren comodo a todos, incluido "grupo_catequesis" (el
		// nombre mas largo). La altura sigue reservando espacio propio
		// abajo del icono para el nombre que aparece con el mouse encima
		// (ver JButtonAccesoDirecto).
		final int ANCHO_BOTON = 120;
		final int ALTO_BOTON = 135;

		btncsdrctClases = new JButtonAccesoDirecto();
		btncsdrctClases.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctClases.setText("clases");
		btncsdrctClases.setBounds(85, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctClases);

		btncsdrctCatequista = new JButtonAccesoDirecto();
		btncsdrctCatequista.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctCatequista.setText("catequista");
		btncsdrctCatequista.setBounds(287, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctCatequista);

		btncsdrctCatequizando = new JButtonAccesoDirecto();
		btncsdrctCatequizando.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctCatequizando.setText("catequizando");
		btncsdrctCatequizando.setBounds(500, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctCatequizando);

		btncsdrctInscripcion = new JButtonAccesoDirecto();
		btncsdrctInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctInscripcion.setText("inscripcion");
		btncsdrctInscripcion.setBounds(914, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctInscripcion);

		// El boton de Etapas se saco del acceso rapido; Asistencia paso a
		// ocupar ese mismo lugar.
		btncsdrctAsistencia = new JButtonAccesoDirecto();
		btncsdrctAsistencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctAsistencia.setText("asistencia");
		btncsdrctAsistencia.setBounds(1117, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctAsistencia);

		btncsdrctGrupocatequesis = new JButtonAccesoDirecto();
		btncsdrctGrupocatequesis.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctGrupocatequesis.setText("grupo_catequesis");
		btncsdrctGrupocatequesis.setBounds(1323, 52, ANCHO_BOTON, ALTO_BOTON);
		contentPane.add(btncsdrctGrupocatequesis);

	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public JPanelPantallaPrincipal getContentPane() {
		return contentPane;
	}

	public JMenuItemPersonalizado getMntmprsnlzdCatequizando() {
		return mntmprsnlzdCatequizando;
	}

	public JMenuItemPersonalizado getMntmprsnlzdCatequista() {
		return mntmprsnlzdCatequista;
	}

	public JMenuItemPersonalizado getMntmprsnlzdClase() {
		return mntmprsnlzdClase;
	}

	public JMenuItemPersonalizado getMntmprsnlzdEtapa() {
		return mntmprsnlzdEtapa;
	}

	public JMenuItemPersonalizado getMntmprsnlzdGrupocatequesis() {
		return mntmprsnlzdGrupocatequesis;
	}

	public JMenuItemPersonalizado getMntmprsnlzdCatequizando_1() {
		return mntmprsnlzdCatequizando_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdCatequistas() {
		return mntmprsnlzdCatequistas;
	}

	public JMenuItemPersonalizado getMntmprsnlzdGrupocatequesis_1() {
		return mntmprsnlzdGrupocatequesis_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdEtapa_1() {
		return mntmprsnlzdEtapa_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdSacramentos_1() {
		return mntmprsnlzdSacramentos_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdAsistencia() {
		return mntmprsnlzdAsistencia;
	}

	public JMenuItemPersonalizado getMntmprsnlzdInscripcion_1() {
		return mntmprsnlzdInscripcion_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdTransferencia_1() {
		return mntmprsnlzdTransferencia_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdClase_1() {
		return mntmprsnlzdClase_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdInscripcion() {
		return mntmprsnlzdInscripcion;
	}

	public JMenuItemPersonalizado getMntmprsnlzdTransferencia() {
		return mntmprsnlzdTransferencia;
	}

	public JMenuItemPersonalizado getMntmprsnlzdSacramentos() {
		return mntmprsnlzdSacramentos;

	}

	public JButtonAccesoDirecto getBtncsdrctClases() {
		return btncsdrctClases;
	}

	public JButtonAccesoDirecto getBtncsdrctCatequista() {
		return btncsdrctCatequista;
	}

	public JButtonAccesoDirecto getBtncsdrctCatequizando() {
		return btncsdrctCatequizando;
	}

	public JButtonAccesoDirecto getBtncsdrctInscripcion() {
		return btncsdrctInscripcion;
	}

	public JButtonAccesoDirecto getBtncsdrctAsistencia() {
		return btncsdrctAsistencia;
	}

	public JButtonAccesoDirecto getBtncsdrctGrupocatequesis() {
		return btncsdrctGrupocatequesis;
	}
}