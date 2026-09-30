package componentes;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

/**
 * Item de menu que carga solo su icono a partir del texto: {@code /iconos/<texto>24.png}
 * (en minusculas y con guiones bajos en lugar de espacios).
 */
public class JMenuItemPersonalizado extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	/** Crea el item con fondo transparente. */
	public JMenuItemPersonalizado() {
		super();
		setOpaque(false);
		//setFont(new Font("Arial", Font.BOLD, 16));
	}
	
	/**
	 * Cambia el texto del item y carga el icono que corresponde a ese texto.
	 *
	 * @param text texto del item; tambien define el nombre del icono
	 */
	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}



	/**
	 * Busca y asigna el icono {@code /iconos/<icono>24.png}. Si no existe, avisa por consola y el item queda sin icono.
	 *
	 * @param icono texto del item, del que se deduce el nombre del archivo
	 */
	private void cargarIcono(String icono) {
		try {
			URL url = JMenuItemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"24.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"24.png");
		}
	}

}
