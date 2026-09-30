package reportes;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

import modelo.CatequistaModelo;
import modelo.CatequizandoModelo;
import modelo.InscripcionModelo;
import modelo.SacramentoModelo;
import utilidades.FechaUtil;

/**
 * Bean plano para el listado Jasper de personas (Catequistas y
 * Catequizandos comparten la misma estructura de columnas, por eso un
 * solo DTO sirve para los dos).
 *
 * JasperReports lee estos campos por reflexion via JRBeanCollectionDataSource,
 * asi que cada campo lleva su getter.
 */
public class ReportePersonaDTO {

	/** Nombre en formato "Apellido, Nombre" */
	private String nombreCompleto;
	/** Documento de identidad */
	private String documento;
	/** Fecha de nacimiento (dd/MM/yyyy) */
	private String fechaNacimiento;
	/** Edad en años cumplidos (puede ser {@code null} si no hay fecha) */
	private Integer edad;
	/** Telefono */
	private String telefono;
	/** Correo electronico */
	private String correo;
	/** Nombres de los grupos, separados por coma */
	private String grupos;
	/** Nombres de los sacramentos, separados por coma */
	private String sacramentos;
	/** Estado como texto (Activo o Inactivo) */
	private String estado;

	/**
	 * Crea una fila del listado de personas.
	 *
	 * @param nombreCompleto nombre en formato "Apellido, Nombre"
	 * @param documento documento de identidad
	 * @param fechaNacimiento fecha de nacimiento (dd/MM/yyyy)
	 * @param edad edad en años cumplidos (puede ser {@code null} si no hay fecha)
	 * @param telefono telefono
	 * @param correo correo electronico
	 * @param grupos nombres de los grupos, separados por coma
	 * @param sacramentos nombres de los sacramentos, separados por coma
	 * @param estado estado como texto (Activo o Inactivo)
	 */
	public ReportePersonaDTO(String nombreCompleto, String documento, String fechaNacimiento, Integer edad,
			String telefono, String correo, String grupos, String sacramentos, String estado) {
		super();
		this.nombreCompleto = nombreCompleto;
		this.documento = documento;
		this.fechaNacimiento = fechaNacimiento;
		this.edad = edad;
		this.telefono = telefono;
		this.correo = correo;
		this.grupos = grupos;
		this.sacramentos = sacramentos;
		this.estado = estado;
	}

	/**
	 * Convierte un CatequistaModelo en una fila del listado.
	 *
	 * @param catequista catequista de origen.
	 * @param grupos     nombres de los grupos donde participa este catequista,
	 *                   ya unidos en un solo texto (separados por coma). Se
	 *                   calculan aparte porque CatequistaModelo no tiene una
	 *                   lista propia de grupos (la relacion es del lado de
	 *                   GrupoCatequesisModelo).
	 */
	public static ReportePersonaDTO desdeCatequista(CatequistaModelo catequista, String grupos) {
		return new ReportePersonaDTO(
				catequista.getCat_apellido() + ", " + catequista.getCat_nombre(),
				catequista.getCat_documento(),
				FechaUtil.fechaAString(catequista.getCat_fechaNacimiento()),
				calcularEdad(catequista.getCat_fechaNacimiento()),
				catequista.getCat_telefono(),
				catequista.getCat_correo(),
				grupos,
				nombresSacramentos(catequista.getSacramentos()),
				catequista.isCat_estado() ? "Activo" : "Inactivo");
	}

	/**
	 * Convierte un CatequizandoModelo en una fila del listado. El grupo se
	 * obtiene directamente de sus inscripciones (relacion propia de
	 * CatequizandoModelo), no hace falta cruzar con otra entidad.
	 */
	public static ReportePersonaDTO desdeCatequizando(CatequizandoModelo catequizando) {
		return new ReportePersonaDTO(
				catequizando.getCatz_apellido() + ", " + catequizando.getCatz_nombre(),
				catequizando.getCatz_documento(),
				FechaUtil.fechaAString(catequizando.getCatz_fechaNacimiento()),
				calcularEdad(catequizando.getCatz_fechaNacimiento()),
				catequizando.getCatz_telefono(),
				catequizando.getCatz_correo(),
				nombresGruposDeInscripciones(catequizando.getInscripciones()),
				nombresSacramentos(catequizando.getSacramentos()),
				catequizando.isCatz_estado() ? "Activo" : "Inactivo");
	}

	/**
	 * Calcula la edad en años cumplidos a hoy.
	 *
	 * @param fechaNacimiento fecha de nacimiento; puede ser {@code null}
	 */
	private static Integer calcularEdad(LocalDate fechaNacimiento) {
		if (fechaNacimiento == null) {
			return null;
		}
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	/**
	 * Une los nombres de los sacramentos en un solo texto separado por coma.
	 *
	 * @param sacramentos lista de sacramentos; puede ser {@code null}
	 */
	private static String nombresSacramentos(List<SacramentoModelo> sacramentos) {
		if (sacramentos == null) {
			return "";
		}
		return sacramentos.stream()
				.map(SacramentoModelo::getSacr_nombre)
				.collect(Collectors.joining(", "));
	}

	/**
	 * Une los nombres de los grupos de las inscripciones activas, separados por coma.
	 *
	 * @param inscripciones inscripciones del catequizando; puede ser {@code null}
	 */
	private static String nombresGruposDeInscripciones(List<InscripcionModelo> inscripciones) {
		if (inscripciones == null) {
			return "";
		}
		return inscripciones.stream()
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.map(i -> i.getGrupoCatequesis().getGrup_nombre())
				.collect(Collectors.joining(", "));
	}

	/** @return nombre en formato "Apellido, Nombre" */
	public String getNombreCompleto() {
		return nombreCompleto;
	}

	/** @return documento de identidad */
	public String getDocumento() {
		return documento;
	}

	/** @return fecha de nacimiento (dd/MM/yyyy) */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/** @return edad en años cumplidos (puede ser {@code null} si no hay fecha) */
	public Integer getEdad() {
		return edad;
	}

	/** @return telefono */
	public String getTelefono() {
		return telefono;
	}

	/** @return correo electronico */
	public String getCorreo() {
		return correo;
	}

	/** @return nombres de los grupos, separados por coma */
	public String getGrupos() {
		return grupos;
	}

	/** @return nombres de los sacramentos, separados por coma */
	public String getSacramentos() {
		return sacramentos;
	}

	/** @return estado como texto (Activo o Inactivo) */
	public String getEstado() {
		return estado;
	}

}
