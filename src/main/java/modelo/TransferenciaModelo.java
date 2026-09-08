package modelo;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Entidad JPA que registra el HISTORIAL de cambios de grupo de un
 * catequizando (transferencias de fin de año, de etapa, o por cualquier
 * otro motivo).
 *
 * La inscripcion activa del catequizando ({@link InscripcionModelo})
 * siempre apunta al grupo ACTUAL; cada vez que se transfiere, se actualiza
 * esa inscripcion y se deja constancia aca de desde que grupo y hacia que
 * grupo se hizo el cambio, y cuando.
 *
 * Tabla asociada: tb_transferencia
 */
// Registra el historial de cambios de grupo de un catequizando (transferencias de fin de anho / de etapa).
// La inscripcion activa del catequizando (InscripcionModelo) siempre apunta al grupo actual;
// cada vez que se transfiere, se actualiza esa inscripcion y se deja constancia aca de
// desde que grupo y hacia que grupo se hizo el cambio, y cuando.
@Entity(name = "tb_transferencia")
public class TransferenciaModelo {

	/** Identificador unico de la transferencia (clave primaria, autogenerada por la base de datos). */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transf_id;

	/** Fecha en que se realizo la transferencia (obligatoria). */
	@Column(name = "transf_fecha", nullable = false)
	private LocalDate transf_fecha;

	/** Observacion/motivo de la transferencia (opcional, maximo 255 caracteres). */
	@Column(name = "transf_observacion", length = 255, nullable = true)
	private String transf_observacion;

	// ======================== MUCHOS A UNO ==================================
	/** Catequizando que fue transferido (obligatorio). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "catz_id")
	private CatequizandoModelo catequizando;

	/** Grupo del que salio el catequizando (grupo de origen, obligatorio). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_origen_id")
	private GrupoCatequesisModelo grupoOrigen;

	/** Grupo al que ingreso el catequizando (grupo de destino, obligatorio). */
	@ManyToOne(optional = false)
	@JoinColumn(name = "grupo_destino_id")
	private GrupoCatequesisModelo grupoDestino;

	/** Constructor vacio requerido por JPA/Hibernate. */
	public TransferenciaModelo() {
		super();
	}

	// ------------------------- Getters y Setters -------------------------
	// Acceso estandar (JavaBeans) a cada atributo. No contienen logica de negocio.

	/** @return el id de la transferencia. */
	public Integer getTransf_id() {
		return transf_id;
	}

	/** Establece el id de la transferencia (normalmente lo asigna JPA al persistir). */
	public void setTransf_id(Integer transf_id) {
		this.transf_id = transf_id;
	}

	/** @return la fecha en que se realizo la transferencia. */
	public LocalDate getTransf_fecha() {
		return transf_fecha;
	}

	/** Cambia la fecha de la transferencia. */
	public void setTransf_fecha(LocalDate transf_fecha) {
		this.transf_fecha = transf_fecha;
	}

	/** @return la observacion/motivo de la transferencia (puede ser nula). */
	public String getTransf_observacion() {
		return transf_observacion;
	}

	/** Cambia la observacion/motivo de la transferencia. */
	public void setTransf_observacion(String transf_observacion) {
		this.transf_observacion = transf_observacion;
	}

	/** @return el catequizando que fue transferido. */
	public CatequizandoModelo getCatequizando() {
		return catequizando;
	}

	/** Asigna el catequizando que fue transferido. */
	public void setCatequizando(CatequizandoModelo catequizando) {
		this.catequizando = catequizando;
	}

	/** @return el grupo del que salio el catequizando (origen). */
	public GrupoCatequesisModelo getGrupoOrigen() {
		return grupoOrigen;
	}

	/** Asigna el grupo del que salio el catequizando (origen). */
	public void setGrupoOrigen(GrupoCatequesisModelo grupoOrigen) {
		this.grupoOrigen = grupoOrigen;
	}

	/** @return el grupo al que ingreso el catequizando (destino). */
	public GrupoCatequesisModelo getGrupoDestino() {
		return grupoDestino;
	}

	/** Asigna el grupo al que ingreso el catequizando (destino). */
	public void setGrupoDestino(GrupoCatequesisModelo grupoDestino) {
		this.grupoDestino = grupoDestino;
	}

}