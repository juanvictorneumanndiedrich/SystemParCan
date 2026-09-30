package componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * Boton de acceso directo de la pantalla principal.
 *
 * Muestra unicamente el icono, arriba, sin ningun fondo ni recuadro
 * detras. Al pasar el mouse por encima, el icono "crece" un poco y el
 * nombre completo (sin abreviar con "...") aparece ABAJO del icono, en el
 * espacio propio que el boton reserva para eso (ver PantallaPrincipalVista,
 * donde estos botones son mas altos que el icono a proposito), sin
 * superponerse a el.
 */
public class JButtonAccesoDirecto extends JButton {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Los archivos de icono son de 64x64. Agrandarlos mucho mas alla de
	// ese tamaño los hace ver borrosos (no hay como "inventar" detalle al
	// escalar una imagen de mapa de bits hacia arriba). Por eso el tamaño
	// normal se dejo en su resolucion nativa (64, sin escalar) y el del
	// hover crece solo un poco (68, ~6%), lo justo para notar el efecto
	// sin que se note turbio.
	/** Tamaño del icono en reposo, en pixeles. */
	private static final int TAMANO_ICONO_NORMAL = 64;
	private static final int TAMANO_ICONO_HOVER = 68; // el icono crece apenas con el mouse encima
	private static final int ESPACIO_ICONO_ARRIBA = 8; // margen entre el borde de arriba del boton y el icono

	/** Color del nombre que aparece al pasar el mouse. */
	private static final Color COLOR_TEXTO = new Color(44, 62, 80); // Midnight Blue
	/** Color del halo blanco translucido que se dibuja detras del nombre para que se lea sobre cualquier fondo. */
	private static final Color COLOR_HALO = new Color(255, 255, 255, 210);

	/** Indica si el mouse esta encima del boton. */
	private boolean mouseEncima = false;
	/** Nombre completo que se muestra debajo del icono mientras el mouse esta encima. */
	private String nombreCompleto = "";
	/** Icono en tamaño normal. */
	private ImageIcon iconoNormal;
	/** Icono un poco mas grande, usado con el mouse encima. */
	private ImageIcon iconoHover;

	/** Crea el boton sin fondo ni borde, con el icono arriba y los efectos de mouse encima (icono mas grande y nombre). */
	public JButtonAccesoDirecto() {
		super();
		setSize(new Dimension(140, 140));
		setMinimumSize(new Dimension(140, 140));
		setFont(new Font("Segoe UI", Font.BOLD, 13));
		setForeground(COLOR_TEXTO);
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.TOP);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);
		setOpaque(false);
		setFocusable(false);
		setMargin(new java.awt.Insets(ESPACIO_ICONO_ARRIBA, 0, 0, 0));
		addMouseListener(new MouseAdapter() {

		    @Override
		    public void mouseEntered(MouseEvent e) {
		        setCursor(new Cursor(Cursor.HAND_CURSOR));
		        mouseEncima = true;
		        if (iconoHover != null) {
		            setIcon(iconoHover);
		        }
		        repaint();
		    }

		    @Override
		    public void mouseExited(MouseEvent e) {
		        mouseEncima = false;
		        if (iconoNormal != null) {
		            setIcon(iconoNormal);
		        }
		        repaint();
		    }
		});
	}

	/**
	 * El texto NO se pasa al JButton (no se llama a super.setText): asi
	 * nunca se dibuja permanentemente debajo del icono. Solo se guarda el
	 * nombre completo, para mostrarlo mientras el mouse esta encima.
	 */
	@Override
	public void setText(String text) {
		cargarIcono(text);
		nombreCompleto = formatearNombre(text);
		setToolTipText(nombreCompleto);
	}

	/** Dibuja el icono y, mientras el mouse esta encima, el nombre completo debajo de el. */
	@Override
	protected void paintComponent(Graphics g) {
		// Icono arriba (lo dibuja el propio JButton; ya viene mas grande
		// cuando el mouse esta encima).
		super.paintComponent(g);

		// El nombre completo (sin abreviar) solo se dibuja mientras el
		// mouse esta encima del boton, ABAJO del icono, en el espacio que
		// el boton reserva para eso.
		if (mouseEncima && nombreCompleto != null && !nombreCompleto.isEmpty()) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

			// Si el nombre es largo, se achica la letra hasta que entre
			// completo en el ancho del boton: nunca se corta con "...".
			Font fuente = new Font("Segoe UI", Font.BOLD, 13);
			FontMetrics fm = g2.getFontMetrics(fuente);
			while (fm.stringWidth(nombreCompleto) > getWidth() - 6 && fuente.getSize() > 8) {
				fuente = fuente.deriveFont((float) (fuente.getSize() - 1));
				fm = g2.getFontMetrics(fuente);
			}
			g2.setFont(fuente);

			// Se ubica debajo del icono: el icono ocupa ESPACIO_ICONO_ARRIBA
			// + TAMANO_ICONO_HOVER (que es el que esta activo en este
			// momento, porque mouseEncima es true), y el nombre va en la
			// franja que queda libre abajo de eso, dentro del boton.
			int yIconoAbajo = ESPACIO_ICONO_ARRIBA + TAMANO_ICONO_HOVER;
			int espacioLibreAbajo = getHeight() - yIconoAbajo;
			int xTexto = (getWidth() - fm.stringWidth(nombreCompleto)) / 2;
			int yTexto = yIconoAbajo + Math.max(fm.getAscent(), (espacioLibreAbajo + fm.getAscent() - fm.getDescent()) / 2);

			// Halo/sombra blanca detras de las letras (sin caja ni
			// recuadro) para que el nombre se lea bien sobre cualquier
			// zona de la foto, clara u oscura.
			g2.setColor(COLOR_HALO);
			int[] dx = { -1, 0, 1, -1, 1, -1, 0, 1 };
			int[] dy = { -1, -1, -1, 0, 0, 1, 1, 1 };
			for (int i = 0; i < dx.length; i++) {
				g2.drawString(nombreCompleto, xTexto + dx[i], yTexto + dy[i]);
			}

			g2.setColor(COLOR_TEXTO);
			g2.drawString(nombreCompleto, xTexto, yTexto);
			g2.dispose();
		}
	}

	/**
	 * Busca el icono {@code /iconos/<icono>64.png} y prepara sus dos tamaños (normal y con el mouse encima).
	 * Si no existe, avisa por consola.
	 *
	 * @param icono texto del boton, del que se deduce el nombre del archivo
	 */
	private void cargarIcono(String icono) {
		try {
			URL url = JMenuItemPersonalizado.class.getResource("/iconos/"+icono.toLowerCase().replace(" ", "_")+"64.png");
			ImageIcon iconoOriginal = new ImageIcon(url);
			Image imgNormal = iconoOriginal.getImage().getScaledInstance(TAMANO_ICONO_NORMAL, TAMANO_ICONO_NORMAL,
					Image.SCALE_SMOOTH);
			Image imgHover = iconoOriginal.getImage().getScaledInstance(TAMANO_ICONO_HOVER, TAMANO_ICONO_HOVER,
					Image.SCALE_SMOOTH);
			iconoNormal = new ImageIcon(imgNormal);
			iconoHover = new ImageIcon(imgHover);
			this.setIcon(iconoNormal);
		} catch (Exception e) {
			System.err.println("No se encontro el icono /iconos/"+icono.toLowerCase().replace(" ", "_")+"64.png");
		}
	}

	/**
	 * Convierte "grupo_catequesis" en "Grupo Catequesis" para mostrarlo
	 * completo (sin abreviar).
	 */
	private String formatearNombre(String texto) {
		if (texto == null) {
			return "";
		}
		String[] palabras = texto.replace("_", " ").trim().split("\\s+");
		StringBuilder resultado = new StringBuilder();
		for (String palabra : palabras) {
			if (palabra.isEmpty()) {
				continue;
			}
			resultado.append(Character.toUpperCase(palabra.charAt(0)));
			if (palabra.length() > 1) {
				resultado.append(palabra.substring(1).toLowerCase());
			}
			resultado.append(" ");
		}
		return resultado.toString().trim();
	}

}