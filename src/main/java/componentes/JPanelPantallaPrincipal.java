package componentes;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Panel de la pantalla principal que dibuja la imagen {@code /imagenes/fondo.jpg} estirada como fondo.
 * Si no encuentra la imagen lo avisa por consola y queda con el fondo normal.
 */
public class JPanelPantallaPrincipal extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Imagen de fondo; es {@code null} si no se pudo cargar. */
	private Image image;
	
	/** Crea el panel y carga la imagen de fondo. */
	public JPanelPantallaPrincipal() {
		try {
			this.image = new ImageIcon(getClass().getResource("/imagenes/fondo.jpg")).getImage();
		} catch (Exception e) {
			System.err.println("No se econtro la immagen /imagenes/fondo.jpg");
		}
	}
	
	/** Dibuja el fondo normal y encima la imagen estirada al tamaño del panel. */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(image != null) {
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

}