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
	private JMenuItemPersonalizado mntmprsnlzdCatequizando_2;
	private JMenuItemPersonalizado mntmprsnlzdCatequista_1;
	private JMenuItemPersonalizado mntmprsnlzdAsistencia;
	private JMenuItemPersonalizado mntmprsnlzdInscripcion;
	private JMenuItemPersonalizado mntmprsnlzdTransferencia;
	private JMenuItemPersonalizado mntmprsnlzdSacramentos;
	private JMenuItemPersonalizado mntmprsnlzdClase;

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
		
		JMenu mnInformes_1 = new JMenu("Informes");
		menuBar.add(mnInformes_1);
		
		mntmprsnlzdCatequizando_2 = new JMenuItemPersonalizado();
		mntmprsnlzdCatequizando_2.setText("catequizando");
		mnInformes_1.add(mntmprsnlzdCatequizando_2);
		
		mntmprsnlzdCatequista_1 = new JMenuItemPersonalizado();
		mntmprsnlzdCatequista_1.setText("catequista");
		mnInformes_1.add(mntmprsnlzdCatequista_1);
		
		mntmprsnlzdAsistencia = new JMenuItemPersonalizado();
		mntmprsnlzdAsistencia.setText("asistencia");
		mnInformes_1.add(mntmprsnlzdAsistencia);
		
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
		
		JButtonAccesoDirecto btncsdrctClases = new JButtonAccesoDirecto();
		btncsdrctClases.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctClases.setText("clases");
		btncsdrctClases.setBounds(85, 52, 104, 99);
		contentPane.add(btncsdrctClases);
		
		JButtonAccesoDirecto btncsdrctCatequista = new JButtonAccesoDirecto();
		btncsdrctCatequista.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctCatequista.setText("catequista");
		btncsdrctCatequista.setBounds(287, 52, 111, 99);
		contentPane.add(btncsdrctCatequista);
		
		JButtonAccesoDirecto btncsdrctCatequizando = new JButtonAccesoDirecto();
		btncsdrctCatequizando.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctCatequizando.setText("catequizando");
		btncsdrctCatequizando.setBounds(500, 52, 111, 99);
		contentPane.add(btncsdrctCatequizando);
		
		JButtonAccesoDirecto btncsdrctAsistencia = new JButtonAccesoDirecto();
		btncsdrctAsistencia.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctAsistencia.setText("asistencia");
		btncsdrctAsistencia.setBounds(703, 52, 111, 99);
		contentPane.add(btncsdrctAsistencia);
		
		JButtonAccesoDirecto btncsdrctInscripcion = new JButtonAccesoDirecto();
		btncsdrctInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctInscripcion.setText("inscripcion");
		btncsdrctInscripcion.setBounds(914, 52, 104, 99);
		contentPane.add(btncsdrctInscripcion);
		
		JButtonAccesoDirecto btncsdrctEtapas = new JButtonAccesoDirecto();
		btncsdrctEtapas.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btncsdrctEtapas.setText("etapas");
		btncsdrctEtapas.setBounds(1117, 52, 104, 99);
		contentPane.add(btncsdrctEtapas);
		
		JButtonAccesoDirecto btncsdrctGrupocatequesis = new JButtonAccesoDirecto();
		btncsdrctGrupocatequesis.setFont(new Font("Segoe UI", Font.BOLD, 10));
		btncsdrctGrupocatequesis.setText("grupo_catequesis");
		btncsdrctGrupocatequesis.setBounds(1323, 53, 120, 99);
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

	public JMenuItemPersonalizado getMntmprsnlzdCatequizando_2() {
		return mntmprsnlzdCatequizando_2;
	}

	public JMenuItemPersonalizado getMntmprsnlzdCatequista_1() {
		return mntmprsnlzdCatequista_1;
	}

	public JMenuItemPersonalizado getMntmprsnlzdAsistencia() {
		return mntmprsnlzdAsistencia;
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
}
