package componentes;

import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

public class JMenuIntemPersonalizado extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public JMenuIntemPersonalizado() {
		super();
		setOpaque(false);
		//setFont(new Font("Arial", Font.BOLD, 16));
	}
	
	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}



	private void cargarIcono(String icono) {
		try {
			URL url = JMenuIntemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"24.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"24.png");
		}
	}

}
