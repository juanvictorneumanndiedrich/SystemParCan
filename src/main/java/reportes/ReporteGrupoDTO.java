package reportes;

import java.util.List;
import java.util.stream.Collectors;

import modelo.CatequistaModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;

/**
 * Bean plano para el listado Jasper de Grupos de Catequesis.
 */
public class ReporteGrupoDTO {

	/** Nombre del grupo */
	private String nombre;
	/** Año del grupo (solo el numero, vacio si no tiene) */
	private String anho;
	/** Descripcion de la etapa del grupo */
	private String etapa;
	/** Catequistas del grupo en formato "Apellido, Nombre" separados por punto y coma */
	private String catequistas;
	/** Cantidad de catequizandos con inscripcion activa en el grupo */
	private Integer catequizandosInscritos;
	/** Cantidad de clases registradas para el grupo */
	private Integer clasesDictadas;

	/**
	 * Crea una fila del listado de Grupos.
	 *
	 * @param nombre nombre del grupo
	 * @param anho año del grupo (solo el numero, vacio si no tiene)
	 * @param etapa descripcion de la etapa del grupo
	 * @param catequistas catequistas del grupo en formato "Apellido, Nombre" separados por punto y coma
	 * @param catequizandosInscritos cantidad de catequizandos con inscripcion activa en el grupo
	 * @param clasesDictadas cantidad de clases registradas para el grupo
	 */
	public ReporteGrupoDTO(String nombre, String anho, String etapa, String catequistas,
			Integer catequizandosInscritos, Integer clasesDictadas) {
		super();
		this.nombre = nombre;
		this.anho = anho;
		this.etapa = etapa;
		this.catequistas = catequistas;
		this.catequizandosInscritos = catequizandosInscritos;
		this.clasesDictadas = clasesDictadas;
	}

	/**
	 * Convierte un GrupoCatequesisModelo en una fila del listado.
	 *
	 * @param grupo grupo de origen
	 */
	public static ReporteGrupoDTO desde(GrupoCatequesisModelo grupo) {
		return new ReporteGrupoDTO(
				grupo.getGrup_nombre(),
				grupo.getGrup_anho() != null ? String.valueOf(grupo.getGrup_anho().getYear()) : "",
				grupo.getEtapa() != null ? grupo.getEtapa().getEtap_descripcion() : "",
				nombresCatequistas(grupo.getCatequistas()),
				cantidadInscritosActivos(grupo.getInscripciones()),
				grupo.getClases() != null ? grupo.getClases().size() : 0);
	}

	/**
	 * Une los catequistas en un solo texto "Apellido, Nombre" separados por punto y coma.
	 *
	 * @param catequistas lista de catequistas; puede ser {@code null}
	 */
	private static String nombresCatequistas(List<CatequistaModelo> catequistas) {
		if (catequistas == null) {
			return "";
		}
		return catequistas.stream()
				.map(c -> c.getCat_apellido() + ", " + c.getCat_nombre())
				.collect(Collectors.joining("; "));
	}

	/**
	 * Cuenta las inscripciones activas.
	 *
	 * @param inscripciones inscripciones del grupo; puede ser {@code null}
	 */
	private static Integer cantidadInscritosActivos(List<InscripcionModelo> inscripciones) {
		if (inscripciones == null) {
			return 0;
		}
		return (int) inscripciones.stream()
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.count();
	}

	/** @return nombre del grupo */
	public String getNombre() {
		return nombre;
	}

	/** @return año del grupo (solo el numero, vacio si no tiene) */
	public String getAnho() {
		return anho;
	}

	/** @return descripcion de la etapa del grupo */
	public String getEtapa() {
		return etapa;
	}

	/** @return catequistas del grupo en formato "Apellido, Nombre" separados por punto y coma */
	public String getCatequistas() {
		return catequistas;
	}

	/** @return cantidad de catequizandos con inscripcion activa en el grupo */
	public Integer getCatequizandosInscritos() {
		return catequizandosInscritos;
	}

	/** @return cantidad de clases registradas para el grupo */
	public Integer getClasesDictadas() {
		return clasesDictadas;
	}

}
