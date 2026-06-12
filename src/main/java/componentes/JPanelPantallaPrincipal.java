package componentes;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class JPanelPantallaPrincipal extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Image image;
	
	public JPanelPantallaPrincipal() {
		try {
			this.image = new ImageIcon(getClass().getResource("/imagenes/fondo.jpg")).getImage();
		} catch (Exception e) {
			System.err.println("No se econtro la immagen /imagenes/fondo.jpg");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

}