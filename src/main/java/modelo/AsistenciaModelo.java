package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity(name = "tb_asistencia")
@Table(name = "tb_asistencia", uniqueConstraints = @UniqueConstraint(columnNames = { "inscrip_id", "clase_id" }))
public class AsistenciaModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer asist_id;

	// Estado de la asistencia: PRESENTE, AUSENTE o JUSTIFICADO
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private EstadoAsistencia estado;

	// Motivo de la ausencia; se completa cuando el estado es JUSTIFICADO
	@Lob
	@Column(columnDefinition = "TEXT")
	private String observaciones;

	// ==================== MUCHOS A UNO ===========================
	@ManyToOne(optional = false)
	@JoinColumn(name = "inscrip_id")
	private InscripcionModelo inscripcion;

	@ManyToOne(optional = false)
	@JoinColumn(name = "clase_id")
	private ClaseModelo clase;

	public AsistenciaModelo() {
		super();
	}

	public Integer getAsist_id() {
		return asist_id;
	}

	public void setAsist_id(Integer asist_id) {
		this.asist_id = asist_id;
	}

	public EstadoAsistencia getEstado() {
		return estado;
	}

	public void setEstado(EstadoAsistencia estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public InscripcionModelo getInscripcion() {
		return inscripcion;
	}

	public void setInscripcion(InscripcionModelo inscripcion) {
		this.inscripcion = inscripcion;
	}

	public ClaseModelo getClase() {
		return clase;
	}

	public void setClase(ClaseModelo clase) {
		this.clase = clase;
	}

}