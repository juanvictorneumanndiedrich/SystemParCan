package componentes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

/**
 * Campo de texto con la fuente estandar de los formularios (Segoe UI, 13) y texto gris cuando esta deshabilitado.
 */
public class JtextFieldGenerico extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Crea el campo con la fuente y el color de texto deshabilitado estandar. */
	public JtextFieldGenerico() {
		super();
		setFont(new Font("Segoe UI", Font.PLAIN, 13));
		setDisabledTextColor(Color.gray);
	}
	
	

}
