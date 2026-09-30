package componentes;

import java.awt.Font;

import javax.swing.JLabel;

/**
 * Etiqueta con la fuente estandar de los formularios (Segoe UI, negrita, 13).
 */
public class JLabelGenerico extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Crea la etiqueta con la fuente estandar.
	 *
	 * @param text texto de la etiqueta
	 */
	public JLabelGenerico(String text) {
		super();
		setFont(new Font("Segoe UI", Font.BOLD, 13));
		setText(text);
	}
	

}
