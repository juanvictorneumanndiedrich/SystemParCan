package tabla;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import modelo.CatequizandoModelo;
import modelo.GrupoCatequesisModelo;
import modelo.InscripcionModelo;

/**
 * Tabla de catequizandos para el grupo de catequesis seleccionado. El Controller
 * le pasa dos tipos de fila:
 * <ul>
 * <li>candidatos: catequizandos sin ninguna inscripcion vigente (checkbox habilitado,
 * tildarlo los inscribe al grupo al guardar).</li>
 * <li>ya inscriptos en ESTE grupo: aparecen tildados pero con el checkbox bloqueado,
 * para que se pueda ver quien ya integra el grupo sin poder sacarlo desde aca
 * por error.</li>
 * </ul>
 * Un catequizando inscripto en OTRO grupo no aparece en esta tabla.
 */
public class ModeloTablaInscripcion extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	/** Titulos de las columnas, en el orden en que se muestran. */
	private String[] columnas = { "Inscribir", "Nombre", "Apellido", "Documento" };

	/** Catequizandos que muestra la tabla (ya filtrados por el buscador). */
	private List<CatequizandoModelo> catequizandos = new ArrayList<CatequizandoModelo>();

	/**
	 * Estado marcado/desmarcado por catz_id, para no perderlo al filtrar la tabla con el buscador.
	 */
	private Map<Integer, Boolean> seleccionados = new HashMap<Integer, Boolean>();

	/** catz_id de catequizandos ya inscriptos en el grupo seleccionado (checkbox bloqueado). */
	private Set<Integer> bloqueados = new HashSet<Integer>();

	/**
	 * Recalcula desde cero el estado de todas las filas en base a los datos reales
	 * (catequizando.getInscripciones()) y el grupo actualmente seleccionado.
	 *
	 * @param lista              catequizandos a mostrar
	 * @param grupoSeleccionado  grupo elegido en la pantalla; define quienes aparecen ya inscriptos (tildados y bloqueados)
	 */
	public void cargar(List<CatequizandoModelo> lista, GrupoCatequesisModelo grupoSeleccionado) {
		this.catequizandos = lista;
		this.seleccionados.clear();
		this.bloqueados.clear();

		Integer grupoId = grupoSeleccionado != null ? grupoSeleccionado.getGrup_id() : null;

		for (CatequizandoModelo catequizando : lista) {
			boolean yaEnEsteGrupo = obtenerInscripcionEnGrupo(catequizando, grupoId) != null;

			seleccionados.put(catequizando.getCatz_id(), yaEnEsteGrupo);
			if (yaEnEsteGrupo) bloqueados.add(catequizando.getCatz_id());
		}

		fireTableDataChanged();
	}

	/**
	 * Cambia que filas se muestran (resultado del buscador) sin tocar los mapas de
	 * seleccion/bloqueo, para no perder los checkbox ya tildados por el usuario.
	 *
	 * @param listaFiltrada catequizandos que deben verse ahora
	 */
	public void filtrar(List<CatequizandoModelo> listaFiltrada) {
		this.catequizandos = listaFiltrada;
		fireTableDataChanged();
	}

	/**
	 * Busca, entre TODAS las inscripciones del catequizando (puede haber mas de una
	 * si quedaron datos de pruebas viejas), la que corresponde puntualmente al grupo
	 * indicado. Antes esto se resolvia con inscripciones.get(0), que devolvia
	 * cualquiera (a veces de otro grupo) y hacia que el catequizando desapareciera
	 * de la tabla aunque si perteneciera al grupo que se estaba mirando.
	 *
	 * @param catequizando catequizando a revisar
	 * @param grupoId      id del grupo buscado
	 * @return la inscripcion en ese grupo, o {@code null} si no existe
	 */
	public static InscripcionModelo obtenerInscripcionEnGrupo(CatequizandoModelo catequizando, Integer grupoId) {
		if (grupoId == null) return null;
		List<InscripcionModelo> inscripciones = catequizando.getInscripciones();
		if (inscripciones == null) return null;

		for (InscripcionModelo inscripcion : inscripciones) {
			if (inscripcion.getGrupoCatequesis() != null
					&& grupoId.equals(inscripcion.getGrupoCatequesis().getGrup_id())) {
				return inscripcion;
			}
		}
		return null;
	}

	/**
	 * Indica si el catequizando tiene alguna inscripcion cargada, sin importar en que grupo.
	 *
	 * @param catequizando catequizando a revisar
	 * @return {@code true} si tiene al menos una inscripcion
	 */
	public static boolean tieneAlgunaInscripcion(CatequizandoModelo catequizando) {
		List<InscripcionModelo> inscripciones = catequizando.getInscripciones();
		return inscripciones != null && !inscripciones.isEmpty();
	}

	/**
	 * Indica si el catequizando esta tildado en la tabla.
	 *
	 * @param catzId id del catequizando
	 * @return {@code true} si esta tildado
	 */
	public boolean estaSeleccionado(Integer catzId) {
		return Boolean.TRUE.equals(seleccionados.get(catzId));
	}

	/**
	 * Indica si el catequizando ya esta inscripto en el grupo seleccionado (checkbox bloqueado).
	 *
	 * @param catzId id del catequizando
	 * @return {@code true} si esta bloqueado
	 */
	public boolean estaBloqueado(Integer catzId) {
		return bloqueados.contains(catzId);
	}

	/**
	 * Igual que {@link #estaBloqueado(Integer)} pero a partir del indice de fila;
	 * devuelve {@code false} si el indice no es valido.
	 *
	 * @param rowIndex indice de la fila
	 * @return {@code true} si el catequizando de esa fila esta bloqueado
	 */
	public boolean estaBloqueadoEnFila(int rowIndex) {
		if (rowIndex < 0 || rowIndex >= catequizandos.size()) return false;
		return estaBloqueado(catequizandos.get(rowIndex).getCatz_id());
	}

	/** @return los catequizandos que muestra actualmente la tabla */
	public List<CatequizandoModelo> getCatequizandos() {
		return catequizandos;
	}

	/** @return cantidad de filas (una por catequizando visible) */
	@Override
	public int getRowCount() {
		return catequizandos.size();
	}

	/** @return cantidad de columnas de la tabla */
	@Override
	public int getColumnCount() {
		return columnas.length;
	}

	/**
	 * @param column indice de la columna (desde 0)
	 * @return el titulo de la columna indicada
	 */
	@Override
	public String getColumnName(int column) {
		return columnas[column];
	}

	/**
	 * La columna 0 es un checkbox ({@code Boolean}); el resto es texto.
	 *
	 * @param columnIndex indice de la columna (desde 0)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnIndex == 0 ? Boolean.class : String.class;
	}

	/**
	 * Solo se puede editar el checkbox de la columna 0, y unicamente si el catequizando no esta bloqueado.
	 *
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex != 0) return false;
		return !estaBloqueadoEnFila(rowIndex);
	}

	/**
	 * Guarda el estado del checkbox de la fila (ignora columnas distintas de 0 y filas bloqueadas).
	 *
	 * @param aValue      nuevo valor (se toma como tildado solo si es {@code true})
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex != 0 || estaBloqueadoEnFila(rowIndex)) return;
		Integer catzId = catequizandos.get(rowIndex).getCatz_id();
		seleccionados.put(catzId, Boolean.TRUE.equals(aValue));
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/**
	 * Devuelve el valor de la celda: checkbox de inscripcion, nombre, apellido o documento.
	 *
	 * @param rowIndex    indice de la fila
	 * @param columnIndex indice de la columna
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CatequizandoModelo catequizando = catequizandos.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return estaSeleccionado(catequizando.getCatz_id());
		case 1:
			return catequizando.getCatz_nombre();
		case 2:
			return catequizando.getCatz_apellido();
		case 3:
			return catequizando.getCatz_documento();
		}
		return null;
	}

}
