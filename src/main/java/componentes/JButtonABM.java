package componentes;

import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Boton de las acciones de ABM (Nuevo, Editar, Eliminar, Cancelar, Guardar): grande, con el texto centrado
 * debajo de un icono. El icono se busca solo a partir del texto del boton en {@code /iconos/<texto>32.png}
 * (en minusculas y con guiones bajos en lugar de espacios).
 */
public class JButtonABM extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Crea el boton con el tamaño, la fuente y la posicion de texto estandar de los botones ABM. */
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
	
	/**
	 * Cambia el texto del boton y carga el icono que corresponde a ese texto.
	 *
	 * @param text texto del boton; tambien define el nombre del icono
	 */
	@Override
	public void setText(String text) {
		cargarIcono(text);
		super.setText(text);
	}

	/**
	 * Busca y asigna el icono {@code /iconos/<icono>32.png}. Si no existe, avisa por consola y el boton queda sin icono.
	 *
	 * @param icono texto del boton, del que se deduce el nombre del archivo
	 */
	private void cargarIcono(String icono) {
		try {
			URL url = JMenuItemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"32.png");
			this.setIcon(new ImageIcon(url));
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"32.png");
		}
	}
	

}
