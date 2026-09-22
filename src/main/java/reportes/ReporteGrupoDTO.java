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

	private String nombre;
	private String anho;
	private String etapa;
	private String catequistas;
	private Integer catequizandosInscritos;
	private Integer clasesDictadas;

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

	public static ReporteGrupoDTO desde(GrupoCatequesisModelo grupo) {
		return new ReporteGrupoDTO(
				grupo.getGrup_nombre(),
				grupo.getGrup_anho() != null ? String.valueOf(grupo.getGrup_anho().getYear()) : "",
				grupo.getEtapa() != null ? grupo.getEtapa().getEtap_descripcion() : "",
				nombresCatequistas(grupo.getCatequistas()),
				cantidadInscritosActivos(grupo.getInscripciones()),
				grupo.getClases() != null ? grupo.getClases().size() : 0);
	}

	private static String nombresCatequistas(List<CatequistaModelo> catequistas) {
		if (catequistas == null) {
			return "";
		}
		return catequistas.stream()
				.map(c -> c.getCat_apellido() + ", " + c.getCat_nombre())
				.collect(Collectors.joining("; "));
	}

	private static Integer cantidadInscritosActivos(List<InscripcionModelo> inscripciones) {
		if (inscripciones == null) {
			return 0;
		}
		return (int) inscripciones.stream()
				.filter(i -> Boolean.TRUE.equals(i.isInscrip_estado()))
				.count();
	}

	public String getNombre() {
		return nombre;
	}

	public String getAnho() {
		return anho;
	}

	public String getEtapa() {
		return etapa;
	}

	public String getCatequistas() {
		return catequistas;
	}

	public Integer getCatequizandosInscritos() {
		return catequizandosInscritos;
	}

	public Integer getClasesDictadas() {
		return clasesDictadas;
	}

}
