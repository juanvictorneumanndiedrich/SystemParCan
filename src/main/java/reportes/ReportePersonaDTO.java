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

	private String nombreCompleto;
	private String documento;
	private String fechaNacimiento;
	private Integer edad;
	private String telefono;
	private String correo;
	private String grupos;
	private String sacramentos;
	private String estado;

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

	private static Integer calcularEdad(LocalDate fechaNacimiento) {
		if (fechaNacimiento == null) {
			return null;
		}
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	private static String nombresSacramentos(List<SacramentoModelo> sacramentos) {
		if (sacramentos == null) {
			return "";
		}
		return sacramentos.stream()
				.map(SacramentoModelo::getSacr_nombre)
				.collect(Collectors.joining(", "));
	}

	private static String nombresGruposDeInscripciones(List<InscripcionModelo> inscripciones) {
		if (inscripciones == null) {
			return "";
		}
		return inscripciones.stream()
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.map(i -> i.getGrupoCatequesis().getGrup_nombre())
				.collect(Collectors.joining(", "));
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public String getDocumento() {
		return documento;
	}

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Integer getEdad() {
		return edad;
	}

	public String getTelefono() {
		return telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public String getGrupos() {
		return grupos;
	}

	public String getSacramentos() {
		return sacramentos;
	}

	public String getEstado() {
		return estado;
	}

}
