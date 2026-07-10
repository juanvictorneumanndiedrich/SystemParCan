package componentes;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

/**
 * Componente tipo "combo box desplegable" cuyo contenido es una lista de
 * JCheckBox, para poder seleccionar varios elementos (por ejemplo, varios
 * catequistas para un mismo grupo) sin tener que usar Ctrl/Shift + clic.
 *
 * El campo muestra un resumen de lo seleccionado; al hacer clic se despliega
 * un popup con un checkbox por cada opcion disponible.
 */
public class JComboCheckList<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	public interface ProveedorTexto<T> {
		String getTexto(T item);
	}

	/**
	 * Extrae una "clave" de comparacion (por ejemplo el ID) para cada item.
	 * Es necesario porque los objetos que llegan por setSeleccionados(...)
	 * (recuperados de la base en otra consulta/sesion de Hibernate) NO son
	 * las mismas instancias que las cargadas por setItems(...), asi que
	 * comparar con equals()/contains() por defecto (identidad de objeto)
	 * nunca encuentra coincidencia y no se marca ningun checkbox.
	 */
	public interface ExtractorClave<T> {
		Object getClave(T item);
	}

	private JtextFieldGenerico campoResumen;
	private JButton botonDesplegable;
	private JPopupMenu popup;
	private JPanel panelOpciones;
	private Map<T, JCheckBox> checkboxes = new LinkedHashMap<T, JCheckBox>();
	private ProveedorTexto<T> proveedorTexto;
	private ExtractorClave<T> extractorClave;

	public JComboCheckList() {
		super(new BorderLayout());

		campoResumen = new JtextFieldGenerico();
		campoResumen.setEditable(false);
		campoResumen.setText("Ninguno seleccionado");

		botonDesplegable = new JButton("▼");
		botonDesplegable.setFocusable(false);
		botonDesplegable.setMargin(new Insets(0, 6, 0, 6));

		add(campoResumen, BorderLayout.CENTER);
		add(botonDesplegable, BorderLayout.EAST);

		panelOpciones = new JPanel();
		panelOpciones.setLayout(new BoxLayout(panelOpciones, BoxLayout.Y_AXIS));

		JScrollPane scroll = new JScrollPane(panelOpciones);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		scroll.setPreferredSize(new Dimension(300, 220));
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		popup = new JPopupMenu();
		popup.setLayout(new BorderLayout());
		popup.add(scroll, BorderLayout.CENTER);

		MouseAdapter alternarPopup = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isEnabled()) return;
				if (popup.isVisible()) {
					popup.setVisible(false);
				} else {
					int ancho = Math.max(getWidth(), 250);
					scroll.setPreferredSize(new Dimension(ancho, 220));
					popup.show(JComboCheckList.this, 0, getHeight());
				}
			}
		};

		campoResumen.addMouseListener(alternarPopup);
		botonDesplegable.addMouseListener(alternarPopup);
	}

	public void setProveedorTexto(ProveedorTexto<T> proveedorTexto) {
		this.proveedorTexto = proveedorTexto;
	}

	public void setExtractorClave(ExtractorClave<T> extractorClave) {
		this.extractorClave = extractorClave;
	}

	private String textoDe(T item) {
		return proveedorTexto != null ? proveedorTexto.getTexto(item) : String.valueOf(item);
	}

	private Object claveDe(T item) {
		return extractorClave != null ? extractorClave.getClave(item) : item;
	}

	public void setItems(List<T> items) {
		panelOpciones.removeAll();
		checkboxes.clear();

		if (items != null) {
			for (T item : items) {
				JCheckBox chk = new JCheckBox(textoDe(item));
				chk.addItemListener(e -> actualizarResumen());
				checkboxes.put(item, chk);
				panelOpciones.add(chk);
			}
		}

		panelOpciones.revalidate();
		panelOpciones.repaint();
		actualizarResumen();
	}

	public void setSeleccionados(List<T> seleccionados) {
		List<Object> clavesSeleccionadas = new ArrayList<Object>();
		if (seleccionados != null) {
			for (T sel : seleccionados) clavesSeleccionadas.add(claveDe(sel));
		}

		for (Map.Entry<T, JCheckBox> entrada : checkboxes.entrySet()) {
			boolean marcado = clavesSeleccionadas.contains(claveDe(entrada.getKey()));
			entrada.getValue().setSelected(marcado);
		}
		actualizarResumen();
	}

	public List<T> getSeleccionados() {
		List<T> seleccionados = new ArrayList<T>();
		for (Map.Entry<T, JCheckBox> entrada : checkboxes.entrySet()) {
			if (entrada.getValue().isSelected()) seleccionados.add(entrada.getKey());
		}
		return seleccionados;
	}

	public void limpiarSeleccion() {
		for (JCheckBox chk : checkboxes.values()) chk.setSelected(false);
		actualizarResumen();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (campoResumen != null) campoResumen.setEnabled(enabled);
		if (botonDesplegable != null) botonDesplegable.setEnabled(enabled);
	}

	private void actualizarResumen() {
		List<T> seleccionados = getSeleccionados();
		if (seleccionados.isEmpty()) {
			campoResumen.setText("Ninguno seleccionado");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < seleccionados.size(); i++) {
			if (i > 0) sb.append(", ");
			sb.append(textoDe(seleccionados.get(i)));
		}
		campoResumen.setText(sb.toString());
	}

}