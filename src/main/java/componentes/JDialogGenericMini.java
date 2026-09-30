package componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import interfaces.InterfaceABM;

/**
 * Version "linda" de JDialogGenericMini (la variante angosta de
 * JDialogGenerico, 720x720, usada por SacramentoVista y EtapaVista),
 * con el mismo tratamiento visual que JDialogGenerico:
 *
 * - Fondo propio: a proposito NO usa la misma foto (/imagenes/fondo.jpg) que
 * JPanelPantallaPrincipal, para que un ABM se distinga a simple vista de la
 * pantalla principal. En su lugar usa un degrade solido en la misma
 * paleta "Flat UI Colors" del resto de la app.
 * - Misma paleta "Flat UI Colors" (Midnight Blue / Wet Asphalt / Peter
 * River / Emerald / Alizarin / Naranja) que ya usa JButtonAccesoDirecto.
 * - El formulario y la tabla quedan como tarjetas blancas redondeadas
 * flotando sobre el fondo.
 * - "Buscador:" lleva la misma chapita translucida que JDialogGenerico,
 * para leerse bien sin importar la zona del degrade sobre la que caiga.
 *
 * OJO: a proposito, mantiene exactamente el mismo contrato publico que la
 * version anterior (mismos metodos get*, misma firma de constructor, mismas
 * coordenadas/tamanos de panelFormulario, scrollPane y botones) para que
 * SacramentoVista y EtapaVista sigan funcionando sin tener que tocarlas.
 */
public class JDialogGenericMini extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	// ---------------------------------------------------------------
	// Paleta de colores ("Flat UI Colors"), la misma familia del
	// (44,62,80) "Midnight Blue" que ya usa JButtonAccesoDirecto.
	// ---------------------------------------------------------------
	private static final Color COLOR_FONDO = new Color(236, 240, 241); // Clouds (si no se encuentra la imagen)
	private static final Color COLOR_TARJETA = Color.WHITE;
	private static final Color COLOR_BORDE = new Color(189, 195, 199); // Silver

	private static final Color MIDNIGHT_BLUE = new Color(44, 62, 80); // color de marca (PantallaPrincipal)
	private static final Color WET_ASPHALT = new Color(52, 73, 94);
	private static final Color PETER_RIVER = new Color(52, 152, 219);
	private static final Color BELIZE_HOLE = new Color(41, 128, 185);
	private static final Color EMERALD = new Color(46, 204, 113);
	private static final Color NEPHRITIS = new Color(39, 174, 96);
	private static final Color ALIZARIN = new Color(231, 76, 60);
	private static final Color POMEGRANATE = new Color(192, 57, 43);
	private static final Color CONCRETE = new Color(149, 165, 166);
	private static final Color ASBESTOS = new Color(127, 140, 141);
	private static final Color ORANGE = new Color(243, 156, 18);
	private static final Color PUMPKIN = new Color(211, 84, 0);

	// Fondo propio del ABM: degrade "Wet Asphalt" -> "Midnight Blue", bien
	// distinto de la foto que usa la pantalla principal, para que a simple
	// vista se note que se esta en una pantalla de ABM.
	private static final Color COLOR_DEGRADE_INICIO = new Color(84, 110, 138);
	private static final Color COLOR_DEGRADE_FIN = MIDNIGHT_BLUE;

	private static final Color COLOR_NUEVO = EMERALD;
	private static final Color COLOR_NUEVO_HOVER = NEPHRITIS;
	private static final Color COLOR_EDITAR = PETER_RIVER;
	private static final Color COLOR_EDITAR_HOVER = BELIZE_HOLE;
	private static final Color COLOR_GUARDAR = WET_ASPHALT;
	private static final Color COLOR_GUARDAR_HOVER = MIDNIGHT_BLUE;
	private static final Color COLOR_ELIMINAR = ALIZARIN;
	private static final Color COLOR_ELIMINAR_HOVER = POMEGRANATE;
	// El Cancelar usaba un gris (Concrete) muy parecido al gris de boton
	// deshabilitado, por lo que parecia inactivo. Ahora usa un color propio
	// y bien saturado (naranja) para que se note que es un boton activo.
	private static final Color COLOR_CANCELAR = ORANGE;
	private static final Color COLOR_CANCELAR_HOVER = PUMPKIN;
	private static final Color COLOR_BOTON_DESHABILITADO = new Color(210, 215, 216);
	private static final Color COLOR_TEXTO_DESHABILITADO = ASBESTOS;

	private static final Color COLOR_TABLA_HEADER = MIDNIGHT_BLUE;
	private static final Color COLOR_TABLA_STRIPE = new Color(245, 247, 248);
	private static final Color COLOR_TABLA_SELECCION = PETER_RIVER;
	private static final Color COLOR_TABLA_GRILLA = new Color(223, 228, 229);
	private static final Color COLOR_TABLA_TEXTO = WET_ASPHALT;

	/** Panel (tarjeta blanca) donde cada pantalla concreta agrega los campos de su formulario. */
	private JPanel panelFormulario;
	/** Tabla de registros de la pantalla; cada pantalla le asigna su modelo. */
	private JTable tabla;
	/** Boton Nuevo. */
	private JButtonABM btnNuevo;
	/** Boton Editar. */
	private JButtonABM btnEditar;
	/** Boton Guardar. */
	private JButtonABM btnGuardar;
	/** Boton Eliminar. */
	private JButtonABM btnEliminar;
	/** Boton Cancelar. */
	private JButtonABM btnCancelar;
	/** Campo de busqueda; al apretar Enter se dispara la accion Buscar. */
	private JtextFieldGenerico tfBuscador;
	/** Controlador al que se le delegan las acciones de los botones; se asigna con {@link #setInterfaceABM(InterfaceABM)}. */
	private InterfaceABM interfaceABM;

	/**
	 * Conecta el controlador que va a recibir las acciones de los botones (Nuevo, Editar, Guardar, Eliminar, Cancelar) y de la busqueda.
	 *
	 * @param interfaceABM controlador de la pantalla
	 */
	public void setInterfaceABM(InterfaceABM interfaceABM) {
		this.interfaceABM = interfaceABM;
	}

	/**
	 * Arma el dialogo: boton de acciones, formulario, tabla con su buscador y estilos. Las pantallas concretas lo extienden y
	 * agregan sus campos en {@link #getPanelFormulario()}.
	 */
	public JDialogGenericMini() {
		setBounds(100, 100, 720, 720);
		setContentPane(new PanelFondoDialogo());
		getContentPane().setLayout(null);

		btnNuevo = new BotonAccion(COLOR_NUEVO, COLOR_NUEVO_HOVER);
		btnNuevo.setText("Nuevo");
		btnNuevo.setToolTipText("");
		btnNuevo.setBounds(10, 10, 95, 80);
		getContentPane().add(btnNuevo);

		btnEditar = new BotonAccion(COLOR_EDITAR, COLOR_EDITAR_HOVER);
		btnEditar.setToolTipText("");
		btnEditar.setText("Editar");
		btnEditar.setBounds(157, 10, 95, 80);
		getContentPane().add(btnEditar);

		btnGuardar = new BotonAccion(COLOR_GUARDAR, COLOR_GUARDAR_HOVER);
		btnGuardar.setToolTipText("");
		btnGuardar.setText("Guardar");
		btnGuardar.setBounds(304, 10, 95, 80);
		getContentPane().add(btnGuardar);

		btnEliminar = new BotonAccion(COLOR_ELIMINAR, COLOR_ELIMINAR_HOVER);
		btnEliminar.setToolTipText("");
		btnEliminar.setText("Eliminar");
		btnEliminar.setBounds(451, 10, 95, 80);
		getContentPane().add(btnEliminar);

		btnCancelar = new BotonAccion(COLOR_CANCELAR, COLOR_CANCELAR_HOVER);
		btnCancelar.setToolTipText("");
		btnCancelar.setText("Cancelar");
		btnCancelar.setBounds(601, 10, 95, 80);
		getContentPane().add(btnCancelar);

		panelFormulario = new TarjetaPanel();
		panelFormulario.setBounds(10, 100, 686, 210);
		getContentPane().add(panelFormulario);
		panelFormulario.setLayout(null);

		// La tabla tambien queda en una tarjeta redondeada, para que combine
		// visualmente con el formulario ahora que ambas flotan sobre el fondo.
		JPanel tarjetaTabla = new TarjetaPanel();
		tarjetaTabla.setBounds(10, 355, 686, 318);
		tarjetaTabla.setLayout(null);
		getContentPane().add(tarjetaTabla);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 8, 686 - 16, 318 - 16);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBackground(COLOR_TARJETA);
		scrollPane.getViewport().setBackground(COLOR_TARJETA);
		tarjetaTabla.add(scrollPane);

		tabla = new JTable();
		estilizarTabla(tabla);
		scrollPane.setViewportView(tabla);

		JLabelGenerico lblgnrcBuscador = new EtiquetaFlotante("Buscador:");
		lblgnrcBuscador.setBounds(10, 320, 73, 25);
		getContentPane().add(lblgnrcBuscador);

		tfBuscador = new CampoBusqueda();
		tfBuscador.setBounds(93, 321, 603, 24);
		getContentPane().add(tfBuscador);


		setAcciones();
	}



	/**
	 * @return el serialVersionUID de la clase
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return el panel donde se agregan los campos del formulario
	 */
	public JPanel getPanelFormulario() {
		return panelFormulario;
	}

	/**
	 * @return la tabla de registros
	 */
	public JTable getTabla() {
		return tabla;
	}

	/**
	 * @return el boton Nuevo
	 */
	public JButtonABM getBtnNuevo() {
		return btnNuevo;
	}

	/**
	 * @return el boton Editar
	 */
	public JButtonABM getBtnEditar() {
		return btnEditar;
	}

	/**
	 * @return el boton Guardar
	 */
	public JButtonABM getBtnGuardar() {
		return btnGuardar;
	}

	/**
	 * @return el boton Eliminar
	 */
	public JButtonABM getBtnEliminar() {
		return btnEliminar;
	}

	/**
	 * @return el boton Cancelar
	 */
	public JButtonABM getBtnCancelar() {
		return btnCancelar;
	}

	/**
	 * @return el campo de busqueda
	 */
	public JtextFieldGenerico getTfBuscador() {
		return tfBuscador;
	}

	/**
	 * @return el controlador asignado. A pesar del nombre ({@code set...}) es un getter; se deja asi para no romper el codigo existente.
	 */
	public InterfaceABM setInterfaceABM() {
		return interfaceABM;
	}

	/**
	 * Conecta los botones y el campo de busqueda a {@link #actionPerformed(ActionEvent)}.
	 */
	private void setAcciones() {
		btnNuevo.addActionListener(this);
		btnEditar.addActionListener(this);
		btnGuardar.addActionListener(this);
		btnEliminar.addActionListener(this);
		btnCancelar.addActionListener(this);
		tfBuscador.setActionCommand("Buscar");
		tfBuscador.addActionListener(this);
	}

	/**
	 * Recibe la accion de un boton (segun su texto) o de la busqueda y la delega en el controlador ({@link InterfaceABM}).
	 *
	 * @param e evento con el comando de la accion: Nuevo, Editar, Guardar, Eliminar, Cancelar o Buscar
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Nuevo":
			interfaceABM.nuevo();
			break;
		case "Editar":
			interfaceABM.editar();
			break;
		case "Guardar":
			interfaceABM.guardar();
			break;
		case "Eliminar":
			interfaceABM.eliminar();
			break;
		case "Cancelar":
			interfaceABM.cancelar();
			break;
		case "Buscar":
			interfaceABM.buscar();
			break;
		}
	}

	/**
	 * Aplica el estilo visual a la tabla: altura de filas, colores, rayas alternadas y encabezado oscuro.
	 *
	 * @param tabla tabla a estilizar
	 */
	private static void estilizarTabla(JTable tabla) {
		tabla.setRowHeight(30);
		tabla.setIntercellSpacing(new Dimension(0, 0));
		tabla.setShowVerticalLines(false);
		tabla.setShowHorizontalLines(true);
		tabla.setGridColor(COLOR_TABLA_GRILLA);
		tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabla.setBackground(COLOR_TARJETA);
		tabla.setSelectionBackground(COLOR_TABLA_SELECCION);
		tabla.setSelectionForeground(Color.WHITE);
		tabla.setFillsViewportHeight(true);
		tabla.setDefaultRenderer(Object.class, new RenderizadorFilas());

		tabla.getTableHeader().setReorderingAllowed(false);
		tabla.getTableHeader().setDefaultRenderer(new RenderizadorEncabezado());
		Dimension tamHeader = tabla.getTableHeader().getPreferredSize();
		tabla.getTableHeader().setPreferredSize(new Dimension(tamHeader.width, 38));
	}

	// =================================================================
	// Componentes internos de estilo (privados, no forman parte de la
	// API publica: no requieren cambios en SacramentoVista/EtapaVista).
	// =================================================================

	/**
	 * Fondo del dialogo: degrade solido propio (Wet Asphalt -> Midnight
	 * Blue), a proposito distinto de la foto (/imagenes/fondo.jpg) que usa
	 * JPanelPantallaPrincipal en la pantalla principal, para que un ABM se
	 * distinga claramente de la pantalla principal.
	 */
	private static class PanelFondoDialogo extends JPanel {

		private static final long serialVersionUID = 1L;

		public PanelFondoDialogo() {
			setOpaque(true);
			setBackground(COLOR_FONDO);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g.create();
			GradientPaint degrade = new GradientPaint(0, 0, COLOR_DEGRADE_INICIO, 0, getHeight(), COLOR_DEGRADE_FIN);
			g2.setPaint(degrade);
			g2.fillRect(0, 0, getWidth(), getHeight());
			g2.dispose();
		}
	}

	/**
	 * Boton de accion plano y redondeado, con color propio y efecto hover.
	 * Extiende JButtonABM para conservar la carga automatica de icono por
	 * texto (setText -> /iconos/xxx32.png) que ya usa el resto de la app.
	 */
	private static class BotonAccion extends JButtonABM {

		private static final long serialVersionUID = 1L;
		private final Color colorBase;
		private final Color colorHover;
		private boolean sobreBoton = false;

		public BotonAccion(Color colorBase, Color colorHover) {
			super();
			this.colorBase = colorBase;
			this.colorHover = colorHover;
			setContentAreaFilled(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setOpaque(false);
			setBorder(new EmptyBorder(6, 4, 6, 4));
			setForeground(Color.WHITE);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (isEnabled()) {
						sobreBoton = true;
						repaint();
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					sobreBoton = false;
					repaint();
				}
			});
		}

		@Override
		public void setEnabled(boolean enabled) {
			super.setEnabled(enabled);
			setForeground(enabled ? Color.WHITE : COLOR_TEXTO_DESHABILITADO);
			if (!enabled) {
				sobreBoton = false;
			}
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			boolean presionado = getModel().isArmed() && getModel().isPressed();
			Color relleno;
			if (!isEnabled()) {
				relleno = COLOR_BOTON_DESHABILITADO;
			} else if (presionado) {
				relleno = colorBase.darker();
			} else if (sobreBoton) {
				relleno = colorHover;
			} else {
				relleno = colorBase;
			}

			g2.setColor(relleno);
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
			g2.dispose();

			super.paintComponent(g);
		}
	}

	/**
	 * Panel "tarjeta": fondo blanco con esquinas redondeadas, borde suave y
	 * una leve sombra. Se usa tanto para panelFormulario como para envolver
	 * la tabla, asi ambas mitades flotan igual sobre el fondo. Los
	 * hijos se siguen posicionando con coordenadas relativas a (0,0) del
	 * panel, igual que antes.
	 */
	private static class TarjetaPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		public TarjetaPanel() {
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int arco = 14;
			// sombra sutil
			g2.setColor(new Color(15, 23, 42, 45));
			g2.fillRoundRect(2, 3, getWidth() - 4, getHeight() - 4, arco, arco);
			// tarjeta blanca
			g2.setColor(COLOR_TARJETA);
			g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, arco, arco);
			// borde suave
			g2.setStroke(new BasicStroke(1f));
			g2.setColor(COLOR_BORDE);
			g2.drawRoundRect(0, 0, getWidth() - 4, getHeight() - 4, arco, arco);

			g2.dispose();
			super.paintComponent(g);
		}
	}

	/**
	 * Etiqueta "flotante": el mismo JLabelGenerico de siempre, pero con una
	 * chapita blanca translucida detras del texto, para que se lea bien
	 * sobre cualquier zona del fondo, clara u oscura.
	 */
	private static class EtiquetaFlotante extends JLabelGenerico {

		private static final long serialVersionUID = 1L;

		public EtiquetaFlotante(String texto) {
			super(texto);
			setOpaque(false);
			setHorizontalAlignment(SwingConstants.CENTER);
			setForeground(MIDNIGHT_BLUE);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(255, 255, 255, 215));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
			g2.dispose();
			super.paintComponent(g);
		}
	}

	/**
	 * Campo de busqueda tipo "pill", con icono de lupa dibujado a mano para
	 * no depender de recursos de imagen adicionales.
	 */
	private static class CampoBusqueda extends JtextFieldGenerico {

		private static final long serialVersionUID = 1L;

		public CampoBusqueda() {
			super();
			setOpaque(false);
			setBorder(new EmptyBorder(4, 32, 4, 12));
			setForeground(WET_ASPHALT);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Color.WHITE);
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
			g2.dispose();

			super.paintComponent(g);

			Graphics2D g2i = (Graphics2D) g.create();
			g2i.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2i.setColor(CONCRETE);
			g2i.setStroke(new BasicStroke(2f));
			int cx = 13;
			int cy = getHeight() / 2 - 2;
			int r = 5;
			g2i.drawOval(cx - r, cy - r, r * 2, r * 2);
			g2i.drawLine(cx + r - 1, cy + r - 1, cx + r + 4, cy + r + 4);
			g2i.dispose();
		}

		@Override
		protected void paintBorder(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(COLOR_BORDE);
			g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
			g2.dispose();
		}
	}

	/** Zebra-striping para las filas de la tabla. */
	private static class RenderizadorFilas extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public RenderizadorFilas() {
			setBorder(new EmptyBorder(0, 10, 0, 10));
			setFont(new Font("Segoe UI", Font.PLAIN, 13));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (isSelected) {
				comp.setBackground(COLOR_TABLA_SELECCION);
				comp.setForeground(Color.WHITE);
			} else {
				comp.setBackground(row % 2 == 0 ? COLOR_TARJETA : COLOR_TABLA_STRIPE);
				comp.setForeground(COLOR_TABLA_TEXTO);
			}
			return comp;
		}
	}

	/** Encabezado de tabla oscuro (Midnight Blue), con texto blanco en negrita. */
	private static class RenderizadorEncabezado extends DefaultTableCellRenderer {

		private static final long serialVersionUID = 1L;

		public RenderizadorEncabezado() {
			setOpaque(true);
			setHorizontalAlignment(SwingConstants.LEFT);
			setBorder(new EmptyBorder(0, 10, 0, 10));
			setFont(new Font("Segoe UI", Font.BOLD, 13));
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			lbl.setBackground(COLOR_TABLA_HEADER);
			lbl.setForeground(Color.WHITE);
			return lbl;
		}
	}

}