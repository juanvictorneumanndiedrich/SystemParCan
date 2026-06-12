package vista;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import componentes.JPanelPantallaPrincipal;
import javax.swing.JButton;
import componentes.JButtonAccesoDirecto;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class PantallaPrincipalVista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanelPantallaPrincipal contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PantallaPrincipalVista frame = new PantallaPrincipalVista();
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
		
		JMenuItem mntmNewMenuItem = new JMenuItem("catequizando");
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("catequista");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("clases");
		mnNewMenu.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("etapa");
		mnNewMenu.add(mntmNewMenuItem_3);
		
		JMenuItem mntmNewMenuItem_4 = new JMenuItem("grupocatequesis");
		mnNewMenu.add(mntmNewMenuItem_4);
		
		JMenu mnInformes = new JMenu("Listados");
		menuBar.add(mnInformes);
		
		JMenuItem mntmNewMenuItem_6 = new JMenuItem("catequizando");
		mnInformes.add(mntmNewMenuItem_6);
		
		JMenuItem mntmNewMenuItem_1_2 = new JMenuItem("catequista");
		mnInformes.add(mntmNewMenuItem_1_2);
		
		JMenuItem mntmNewMenuItem_2_1 = new JMenuItem("clases");
		mnInformes.add(mntmNewMenuItem_2_1);
		
		JMenuItem mntmNewMenuItem_4_2 = new JMenuItem("grupocatequesis");
		mnInformes.add(mntmNewMenuItem_4_2);
		
		JMenu mnInformes_1 = new JMenu("Informes");
		menuBar.add(mnInformes_1);
		
		JMenuItem mntmNewMenuItem_5 = new JMenuItem("catequizando");
		mnInformes_1.add(mntmNewMenuItem_5);
		
		JMenuItem mntmNewMenuItem_1_1 = new JMenuItem("catequista");
		mnInformes_1.add(mntmNewMenuItem_1_1);
		
		JMenuItem mntmNewMenuItem_4_1 = new JMenuItem("grupo_catequesis");
		mnInformes_1.add(mntmNewMenuItem_4_1);
		
		JMenu mnUtilidades = new JMenu("Utilidades");
		menuBar.add(mnUtilidades);
		
		JMenuItem mntmNewMenuItem_7 = new JMenuItem("Inscripcion");
		mnUtilidades.add(mntmNewMenuItem_7);
		
		JMenuItem mntmTransferencia = new JMenuItem("transferencia");
		mnUtilidades.add(mntmTransferencia);
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
}
