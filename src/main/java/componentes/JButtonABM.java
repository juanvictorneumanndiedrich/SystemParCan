package componentes;

import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class JButtonABM extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JButtonABM() {
		super();
		setSize(new Dimension(70, 70));
		setMinimumSize(new Dimension(70, 70));
		setFont(new Font("Segoe UI", Font.BOLD, 14));
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setOpaque(true);
		setFocusable(false);
	}
	
	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}

	private void cargarIcono(String icono) {
		try {
			URL url = JMenuIntemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"32.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"32.png");
		}
	}
	

}
