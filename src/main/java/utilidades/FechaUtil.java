package utilidades;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.text.MaskFormatter;

/**
 * Utilidades estaticas para trabajar con fechas y horas en el formato del sistema:
 * {@code dd/MM/yyyy} para fechas, {@code HH:mm} para horas y {@code dd/MM/yyyy HH:mm} para ambas.
 *
 * Incluye mascaras para los campos de texto de Swing y conversiones entre texto y
 * los tipos {@code java.time}.
 */
public class FechaUtil {

	/** Formato de fecha: dd/MM/yyyy. */
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	/** Formato de hora: HH:mm. */
	private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

	/** Formato de fecha y hora: dd/MM/yyyy HH:mm. */
	private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	
	
	// =================== Creación de Formatos para TextField =================
	
	/** @return mascara {@code ##/##/####} para un campo de fecha (los huecos se muestran con '_') */
	public static MaskFormatter getFormatoFecha() {
		try {
			MaskFormatter mascara = new MaskFormatter("##/##/####");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	/** @return mascara {@code ##:##} para un campo de hora (los huecos se muestran con '_') */
	public static MaskFormatter getFormatoHora() {
		try {
			MaskFormatter mascara = new MaskFormatter("##:##");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	/** @return mascara {@code ##/##/#### ##:##} para un campo de fecha y hora (los huecos se muestran con '_') */
	public static MaskFormatter getFormatoFechaHora() {
		try {
			MaskFormatter mascara = new MaskFormatter("##/##/#### ##:##");
			mascara.setPlaceholderCharacter('_');
			return mascara;
		} catch (ParseException e) {
			throw new RuntimeException();
		}
	}
	
	// ==================== LocalDate a String ==================
	
	/**
	 * @param fecha fecha a formatear
	 * @return la fecha como texto dd/MM/yyyy
	 */
	public static String fechaAString(LocalDate fecha) {
		return fecha.format(FORMATO_FECHA);
	}
	
	/**
	 * @param hora hora a formatear
	 * @return la hora como texto HH:mm
	 */
	public static String horaAString(LocalTime hora) {
		return hora.format(FORMATO_HORA);
	}
	
	/**
	 * @param fechaHora fecha y hora a formatear
	 * @return el valor como texto dd/MM/yyyy HH:mm
	 */
	public static String fechaHoraAString(LocalDateTime fechaHora) {
		return fechaHora.format(FORMATO_FECHA_HORA);
	}
	
	
	// ==================== String a LocalDate ==================

	/**
	 * @param texto fecha escrita como dd/MM/yyyy
	 * @return la fecha convertida, o {@code null} si el texto no es valido (incompleto, dia inexistente, etc.)
	 */
	public static LocalDate stringAFecha(String texto) {
		try {
			return LocalDate.parse(texto, FORMATO_FECHA);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param texto hora escrita como HH:mm
	 * @return la hora convertida, o {@code null} si el texto no es valido
	 */
	public static LocalTime stringAHora(String texto) {
		try {
			return LocalTime.parse(texto, FORMATO_HORA);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @param texto fecha y hora escritas como dd/MM/yyyy HH:mm
	 * @return el valor convertido, o {@code null} si el texto no es valido
	 */
	public static LocalDateTime stringAFechaHora(String texto) {
		try {
			return LocalDateTime.parse(texto, FORMATO_FECHA_HORA);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	
}
