package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class JButtonAccesoDirecto extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JButtonAccesoDirecto() {
		super();
		setSize(new Dimension(140, 140));
		setMinimumSize(new Dimension(140, 140));
		setFont(new Font("Segoe UI", Font.BOLD, 14));
		setForeground(new Color(44, 62, 80));
		setBackground(new Color(255, 255, 255, 180));
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setBorderPainted(false);
		setOpaque(false);
		setFocusable(false);
		addMouseListener(new MouseAdapter() {

		    @Override
		    public void mouseEntered(MouseEvent e) {
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
		        setFont(getFont().deriveFont(16f)); // aumenta a fonte
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        setFont(getFont().deriveFont(14f)); // volta ao tamanho normal
		    }
		});
	}
	
	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}

	private void cargarIcono(String icono) {
		try {
			URL url = JMenuIntemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"64.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"64.png");
		}
	}
	

}
