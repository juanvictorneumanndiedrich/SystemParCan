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
 *
 * @param <T> tipo de los elementos que se pueden elegir
 */
public class JComboCheckList<T> extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Define que texto se muestra para cada item (en su checkbox y en el resumen).
	 *
	 * @param <T> tipo del item
	 */
	public interface ProveedorTexto<T> {
		/**
		 * @param item elemento a mostrar
		 * @return el texto que lo representa
		 */
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
		/**
		 * @param item elemento a comparar
		 * @return la clave que lo identifica (por ejemplo su id)
		 */
		Object getClave(T item);
	}

	/** Campo de solo lectura que muestra el resumen de lo seleccionado. */
	private JtextFieldGenerico campoResumen;
	/** Boton (flecha) que despliega el popup. */
	private JButton botonDesplegable;
	/** Popup desplegable que contiene la lista de checkboxes. */
	private JPopupMenu popup;
	/** Panel dentro del popup donde se apilan los checkboxes. */
	private JPanel panelOpciones;
	/** Un checkbox por cada item disponible, en el orden en que se cargaron. */
	private Map<T, JCheckBox> checkboxes = new LinkedHashMap<T, JCheckBox>();
	/** Forma de obtener el texto de cada item; si es {@code null} se usa {@code toString()}. */
	private ProveedorTexto<T> proveedorTexto;
	/** Forma de obtener la clave de cada item; si es {@code null} se compara el objeto mismo. */
	private ExtractorClave<T> extractorClave;

	/** Crea el componente: campo de resumen, boton desplegable y popup con la lista de opciones (vacia). */
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

	/**
	 * Define como se obtiene el texto de cada item.
	 *
	 * @param proveedorTexto proveedor de texto
	 */
	public void setProveedorTexto(ProveedorTexto<T> proveedorTexto) {
		this.proveedorTexto = proveedorTexto;
	}

	/**
	 * Define como se obtiene la clave de comparacion de cada item (ver {@link ExtractorClave}).
	 *
	 * @param extractorClave extractor de clave
	 */
	public void setExtractorClave(ExtractorClave<T> extractorClave) {
		this.extractorClave = extractorClave;
	}

	/** @return el texto del item segun el proveedor, o su {@code toString()} si no hay proveedor */
	private String textoDe(T item) {
		return proveedorTexto != null ? proveedorTexto.getTexto(item) : String.valueOf(item);
	}

	/** @return la clave del item segun el extractor, o el item mismo si no hay extractor */
	private Object claveDe(T item) {
		return extractorClave != null ? extractorClave.getClave(item) : item;
	}

	/**
	 * Reemplaza las opciones disponibles por las de la lista (todas desmarcadas) y actualiza el resumen.
	 *
	 * @param items opciones a mostrar; puede ser {@code null} para dejarlo vacio
	 */
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

	/**
	 * Marca las opciones que coinciden (segun su clave) con las de la lista recibida y desmarca el resto.
	 *
	 * @param seleccionados elementos a marcar; puede ser {@code null}
	 */
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

	/** @return las opciones que estan marcadas, en el orden en que se cargaron */
	public List<T> getSeleccionados() {
		List<T> seleccionados = new ArrayList<T>();
		for (Map.Entry<T, JCheckBox> entrada : checkboxes.entrySet()) {
			if (entrada.getValue().isSelected()) seleccionados.add(entrada.getKey());
		}
		return seleccionados;
	}

	/** Desmarca todas las opciones. */
	public void limpiarSeleccion() {
		for (JCheckBox chk : checkboxes.values()) chk.setSelected(false);
		actualizarResumen();
	}

	/**
	 * Habilita o deshabilita el componente completo (campo de resumen y boton desplegable).
	 *
	 * @param enabled {@code true} para habilitarlo
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (campoResumen != null) campoResumen.setEnabled(enabled);
		if (botonDesplegable != null) botonDesplegable.setEnabled(enabled);
	}

	/** Actualiza el campo de resumen con los textos de lo marcado, separados por coma, o "Ninguno seleccionado". */
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