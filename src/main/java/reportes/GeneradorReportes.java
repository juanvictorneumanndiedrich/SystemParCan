package reportes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dao.CatequistaDAO;
import dao.CatequizandoDAO;
import dao.EtapaDAO;
import dao.GrupoCatequesisDAO;
import dao.SacramentoDAO;
import modelo.CatequistaModelo;
import modelo.CatequizandoModelo;
import modelo.EtapaModelo;
import modelo.GrupoCatequesisModelo;
import modelo.SacramentoModelo;

/**
 * Arma las listas de DTOs que van a los listados Jasper del menu
 * "Listados" de la pantalla principal. Cada metodo estatico corresponde
 * a un listado: busca los datos con el/los DAO recibido(s), ordena y
 * mapea al bean plano correspondiente (ver paquete reportes).
 */
public class GeneradorReportes {

	/**
	 * Listado de Catequistas. Necesita ademas el GrupoCatequesisDAO porque
	 * CatequistaModelo no tiene una lista propia de grupos: hay que cruzar
	 * con todos los grupos para saber en cuales participa cada catequista.
	 */
	public static List<ReportePersonaDTO> listarCatequistas(CatequistaDAO catequistaDao,
			GrupoCatequesisDAO grupoDao) {
		List<CatequistaModelo> catequistas = catequistaDao.recuperarTodo();
		catequistas.sort(Comparator.comparing(CatequistaModelo::getCat_apellido)
				.thenComparing(CatequistaModelo::getCat_nombre));

		List<GrupoCatequesisModelo> grupos = grupoDao.recuperarTodo();

		return catequistas.stream()
				.map(catequista -> {
					String nombresGrupos = grupos.stream()
							.filter(grupo -> grupo.getCatequistas() != null
									&& grupo.getCatequistas().stream()
											.anyMatch(c -> c.getCat_id().equals(catequista.getCat_id())))
							.map(GrupoCatequesisModelo::getGrup_nombre)
							.collect(Collectors.joining(", "));
					return ReportePersonaDTO.desdeCatequista(catequista, nombresGrupos);
				})
				.collect(Collectors.toList());
	}

	/** Listado de Catequizandos. */
	public static List<ReportePersonaDTO> listarCatequizandos(CatequizandoDAO catequizandoDao) {
		List<CatequizandoModelo> catequizandos = catequizandoDao.recuperarTodo();
		catequizandos.sort(Comparator.comparing(CatequizandoModelo::getCatz_apellido)
				.thenComparing(CatequizandoModelo::getCatz_nombre));

		return catequizandos.stream()
				.map(ReportePersonaDTO::desdeCatequizando)
				.collect(Collectors.toList());
	}

	/** Listado de Etapas. */
	public static List<ReporteEtapaDTO> listarEtapas(EtapaDAO etapaDao) {
		List<EtapaModelo> etapas = etapaDao.recuperarTodo();
		etapas.sort(Comparator.comparing(EtapaModelo::getEtap_descripcion));

		return etapas.stream()
				.map(ReporteEtapaDTO::desde)
				.collect(Collectors.toList());
	}

	/** Listado de Grupos de Catequesis. */
	public static List<ReporteGrupoDTO> listarGrupos(GrupoCatequesisDAO grupoDao) {
		List<GrupoCatequesisModelo> grupos = grupoDao.recuperarTodo();
		grupos.sort(Comparator
				.comparing(GrupoCatequesisModelo::getGrup_anho, Comparator.nullsLast(Comparator.naturalOrder()))
				.thenComparing(GrupoCatequesisModelo::getGrup_nombre));

		return grupos.stream()
				.map(ReporteGrupoDTO::desde)
				.collect(Collectors.toList());
	}

	/** Listado de Sacramentos. */
	public static List<ReporteSacramentoDTO> listarSacramentos(SacramentoDAO sacramentoDao) {
		List<SacramentoModelo> sacramentos = sacramentoDao.recuperarTodo();
		sacramentos.sort(Comparator.comparing(SacramentoModelo::getSacr_nombre));

		return sacramentos.stream()
				.map(ReporteSacramentoDTO::desde)
				.collect(Collectors.toList());
	}

}
