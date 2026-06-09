package componentes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

public class JtextFieldGenerico extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JtextFieldGenerico() {
		super();
		setFont(new Font("Segoe UI", Font.PLAIN, 13));
		setDisabledTextColor(Color.gray);
	}
	
	

}
