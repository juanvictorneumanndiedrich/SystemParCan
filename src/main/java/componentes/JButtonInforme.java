package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;

/**
 * Boton de accion chico y redondeado, usado en las pantallas de Informe
 * (Filtrar / Generar Informe / Cerrar) en vez del JButtonABM. El
 * JButtonABM es grande, cuadrado y con icono arriba del texto (pensado
 * para las acciones de ABM: Nuevo/Editar/Eliminar/Cancelar/Guardar); este
 * es solo texto, mas chico, con las esquinas redondeadas, y el ancho se
 * calcula solo a partir del texto (nunca queda un texto largo cortado por
 * un tamaño fijo, como pasaba con "Generar Informe" dentro de un
 * JButtonABM de 95px).
 *
 * Tres estilos, del mas al menos destacado:
 * - Primario (default): fondo azul oscuro, texto blanco. Para la accion
 *   principal de la pantalla ("Generar Informe").
 * - {@link #aplicarEstiloSecundario()}: fondo gris solido, texto blanco.
 *   Para "Cerrar".
 * - {@link #aplicarEstiloNeutro()}: fondo claro con borde fino, texto
 *   oscuro. Para una accion utilitaria y discreta como "Filtrar".
 */
public class JButtonInforme extends JButton {

	private static final long serialVersionUID = 1L;
	private static final int RADIO_ARCO = 18;
	private static final int ALTO_MINIMO = 32;

	private static final Color PRIMARIO = new Color(0x2C, 0x3E, 0x50);
	private static final Color PRIMARIO_HOVER = new Color(0x34, 0x49, 0x5E);
	private static final Color SECUNDARIO = new Color(0x7F, 0x8C, 0x8D);
	private static final Color SECUNDARIO_HOVER = new Color(0x6C, 0x7A, 0x7D);
	private static final Color NEUTRO = new Color(0xEC, 0xF0, 0xF1);
	private static final Color NEUTRO_HOVER = new Color(0xDC, 0xE1, 0xE2);
	private static final Color NEUTRO_TEXTO = new Color(0x34, 0x49, 0x5E);
	private static final Color NEUTRO_BORDE = new Color(0xBD, 0xC3, 0xC7);

	private Color colorFondo = PRIMARIO;
	private Color colorFondoHover = PRIMARIO_HOVER;
	private Color colorBorde = null;

	public JButtonInforme(String texto) {
		super(texto);
		setFont(new Font("Segoe UI", Font.BOLD, 12));
		setForeground(Color.WHITE);
		setMargin(new Insets(6, 18, 6, 18));
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setOpaque(false);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/** Estilo secundario (fondo gris solido, texto blanco), usado en "Cerrar". */
	public void aplicarEstiloSecundario() {
		colorFondo = SECUNDARIO;
		colorFondoHover = SECUNDARIO_HOVER;
		colorBorde = null;
		setForeground(Color.WHITE);
		repaint();
	}

	/** Estilo neutro (fondo claro con borde fino, texto oscuro), usado en "Filtrar". */
	public void aplicarEstiloNeutro() {
		colorFondo = NEUTRO;
		colorFondoHover = NEUTRO_HOVER;
		colorBorde = NEUTRO_BORDE;
		setForeground(NEUTRO_TEXTO);
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		boolean resaltado = getModel().isRollover() || getModel().isPressed();

		// La figura se dibuja medio pixel ADENTRO del borde del componente.
		// Si se dibuja justo sobre el borde (en 0,0), la mitad de la linea
		// del contorno cae afuera del area del boton y se recorta; con el
		// escalado de pantalla de Windows (125%, 150%...) eso se nota como
		// el borde de arriba y de la izquierda "cortados".
		RoundRectangle2D forma = new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1.0, getHeight() - 1.0,
				RADIO_ARCO, RADIO_ARCO);
		g2.setColor(resaltado ? colorFondoHover : colorFondo);
		g2.fill(forma);
		if (colorBorde != null) {
			g2.setColor(colorBorde);
			g2.draw(forma);
		}
		g2.dispose();
		super.paintComponent(g);
	}

	// El ancho sale del texto + margen (setMargin de arriba); solo se
	// asegura una altura minima para que la pildora no quede achatada.
	@Override
	public Dimension getPreferredSize() {
		Dimension preferido = super.getPreferredSize();
		return new Dimension(preferido.width, Math.max(preferido.height, ALTO_MINIMO));
	}

}
