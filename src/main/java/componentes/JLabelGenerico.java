package componentes;

import java.awt.Font;

import javax.swing.JLabel;

public class JLabelGenerico extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JLabelGenerico(String text) {
		super();
		setFont(new Font("Segoe UI", Font.BOLD, 13));
		setText(text);
	}
	

}
