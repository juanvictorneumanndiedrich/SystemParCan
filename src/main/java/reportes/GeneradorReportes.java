package reportes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import dao.CatequistaDAO;
import dao.CatequizandoDAO;
import dao.EtapaDAO;
import dao.GrupoCatequesisDAO;
import dao.SacramentoDAO;
import modelo.AsistenciaModelo;
import modelo.CatequistaModelo;
import modelo.CatequizandoModelo;
import modelo.ClaseModelo;
import modelo.EtapaModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;
import modelo.SacramentoModelo;
import modelo.TransferenciaModelo;

/**
 * Arma las listas de DTOs que van a los reportes Jasper de la pantalla
 * principal (menus "Listados" e "Informes"). Cada metodo estatico
 * corresponde a un reporte: para los "Listados" busca los datos con el/los
 * DAO recibido(s); para los "Informes" (Asistencia, Inscripcion,
 * Transferencia, Clase) recibe la lista ya filtrada por la pantalla
 * correspondiente (el filtro por grupo/catequizando/estado/rango de fecha
 * vive en cada Controller de informe, no aca), y solo ordena y mapea al
 * bean plano.
 */
public class GeneradorReportes {

	/**
	 * Listado de Catequistas. Necesita ademas el GrupoCatequesisDAO porque
	 * CatequistaModelo no tiene una lista propia de grupos: hay que cruzar
	 * con todos los grupos para saber en cuales participa cada catequista.
	 *
	 * @param catequistaDao DAO de catequistas
	 * @param grupoDao      DAO de grupos de catequesis, para cruzar los grupos de cada catequista
	 * @return una fila por catequista, ordenada por apellido y nombre
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

	/**
	 * Listado de Catequizandos.
	 *
	 * @param catequizandoDao DAO de catequizandos
	 * @return una fila por catequizando, ordenada por apellido y nombre
	 */
	public static List<ReportePersonaDTO> listarCatequizandos(CatequizandoDAO catequizandoDao) {
		List<CatequizandoModelo> catequizandos = catequizandoDao.recuperarTodo();
		catequizandos.sort(Comparator.comparing(CatequizandoModelo::getCatz_apellido)
				.thenComparing(CatequizandoModelo::getCatz_nombre));

		return catequizandos.stream()
				.map(ReportePersonaDTO::desdeCatequizando)
				.collect(Collectors.toList());
	}

	/**
	 * Listado de Etapas.
	 *
	 * @param etapaDao DAO de etapas
	 * @return una fila por etapa, ordenada por descripcion
	 */
	public static List<ReporteEtapaDTO> listarEtapas(EtapaDAO etapaDao) {
		List<EtapaModelo> etapas = etapaDao.recuperarTodo();
		etapas.sort(Comparator.comparing(EtapaModelo::getEtap_descripcion));

		return etapas.stream()
				.map(ReporteEtapaDTO::desde)
				.collect(Collectors.toList());
	}

	/**
	 * Listado de Grupos de Catequesis.
	 *
	 * @param grupoDao DAO de grupos de catequesis
	 * @return una fila por grupo, ordenada por año y nombre
	 */
	public static List<ReporteGrupoDTO> listarGrupos(GrupoCatequesisDAO grupoDao) {
		List<GrupoCatequesisModelo> grupos = grupoDao.recuperarTodo();
		grupos.sort(Comparator
				.comparing(GrupoCatequesisModelo::getGrup_anho, Comparator.nullsLast(Comparator.naturalOrder()))
				.thenComparing(GrupoCatequesisModelo::getGrup_nombre));

		return grupos.stream()
				.map(ReporteGrupoDTO::desde)
				.collect(Collectors.toList());
	}

	/**
	 * Listado de Sacramentos.
	 *
	 * @param sacramentoDao DAO de sacramentos
	 * @return una fila por sacramento, ordenada por nombre
	 */
	public static List<ReporteSacramentoDTO> listarSacramentos(SacramentoDAO sacramentoDao) {
		List<SacramentoModelo> sacramentos = sacramentoDao.recuperarTodo();
		sacramentos.sort(Comparator.comparing(SacramentoModelo::getSacr_nombre));

		return sacramentos.stream()
				.map(ReporteSacramentoDTO::desde)
				.collect(Collectors.toList());
	}

	// ===================== Informes (menu "Informes") =====================
	// A diferencia de los Listados de arriba, aca la lista ya llega
	// filtrada (grupo/catequizando/estado/rango de fecha, segun el
	// informe) desde el Controller de la pantalla; este metodo solo
	// ordena por fecha y mapea al DTO.

	/**
	 * Informe de Asistencia: un registro por catequizando/clase, ya filtrado.
	 *
	 * @param asistencias asistencias ya filtradas por la pantalla del informe
	 * @return las filas del informe, ordenadas por fecha de clase
	 */
	public static List<ReporteAsistenciaDTO> listarInformeAsistencia(List<AsistenciaModelo> asistencias) {
		List<AsistenciaModelo> ordenadas = asistencias.stream()
				.sorted(Comparator.comparing(
						a -> a.getClase() != null ? a.getClase().getClase_fechaClase() : null,
						Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		return ordenadas.stream()
				.map(ReporteAsistenciaDTO::desde)
				.collect(Collectors.toList());
	}

	/**
	 * Informe de Inscripcion, ya filtrado.
	 *
	 * @param inscripciones inscripciones ya filtradas por la pantalla del informe
	 * @return las filas del informe, ordenadas por fecha
	 */
	public static List<ReporteInscripcionDTO> listarInformeInscripcion(List<InscripcionModelo> inscripciones) {
		List<InscripcionModelo> ordenadas = inscripciones.stream()
				.sorted(Comparator.comparing(InscripcionModelo::getInscrip_fecha,
						Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		return ordenadas.stream()
				.map(ReporteInscripcionDTO::desde)
				.collect(Collectors.toList());
	}

	/**
	 * Informe de Transferencia, ya filtrado.
	 *
	 * @param transferencias transferencias ya filtradas por la pantalla del informe
	 * @return las filas del informe, ordenadas por fecha
	 */
	public static List<ReporteTransferenciaDTO> listarInformeTransferencia(List<TransferenciaModelo> transferencias) {
		List<TransferenciaModelo> ordenadas = transferencias.stream()
				.sorted(Comparator.comparing(TransferenciaModelo::getTransf_fecha,
						Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		return ordenadas.stream()
				.map(ReporteTransferenciaDTO::desde)
				.collect(Collectors.toList());
	}

	/**
	 * Informe de Clase, ya filtrado.
	 *
	 * @param clases clases ya filtradas por la pantalla del informe
	 * @return las filas del informe, ordenadas por fecha
	 */
	public static List<ReporteClaseDTO> listarInformeClase(List<ClaseModelo> clases) {
		List<ClaseModelo> ordenadas = clases.stream()
				.sorted(Comparator.comparing(ClaseModelo::getClase_fechaClase,
						Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());

		return ordenadas.stream()
				.map(ReporteClaseDTO::desde)
				.collect(Collectors.toList());
	}

}
