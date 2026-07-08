package modelo;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity(name = "tb_grupo_catequesis")
public class GrupoCatequesisModelo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer grup_id;

	@Column(name = "grup_nombre", length = 100, nullable = false)
	private String grup_nombre;

	@Column(nullable = false)
	private LocalDate grup_anho;

	// ===================== MUCHOS A UNO ===================

	@ManyToOne(optional = false)
	@JoinColumn(name = "etap_id")
	private EtapaModelo etapa;

	// ===================== MUCHOS A MUCHOS ===================

	// LISTA: catequistas asignados a este grupo (un catequista puede participar en varios grupos)
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "tb_grupo_catequista",
			joinColumns = @JoinColumn(name = "grupo_id"),
			inverseJoinColumns = @JoinColumn(name = "cat_id")
	)
	private List<CatequistaModelo> catequistas;

	// ====================== UNO A MUCHOS ====================

	// LISTA para obtener todos los alumnos inscritos en este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<InscripcionModelo> inscripciones;

	// LISTA para obtener todas las sesiones o clases de este grupo
	@OneToMany(mappedBy = "grupoCatequesis", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ClaseModelo> clases;

	// LISTA: transferencias en las que este grupo fue el grupo de origen (alumnos que salieron de aca)
	@OneToMany(mappedBy = "grupoOrigen", fetch = FetchType.EAGER)
	private List<TransferenciaModelo> transferenciasOrigen;

	// LISTA: transferencias en las que este grupo fue el grupo de destino (alumnos que llegaron aca)
	@OneToMany(mappedBy = "grupoDestino", fetch = FetchType.EAGER)
	private List<TransferenciaModelo> transferenciasDestino;

	public GrupoCatequesisModelo() {
		super();

	}

	public Integer getGrup_id() {
		return grup_id;
	}

	public void setGrup_id(Integer grup_id) {
		this.grup_id = grup_id;
	}

	public String getGrup_nombre() {
		return grup_nombre;
	}

	public void setGrup_nombre(String grup_nombre) {
		this.grup_nombre = grup_nombre;
	}

	public LocalDate getGrup_anho() {
		return grup_anho;
	}

	public void setGrup_anho(LocalDate grup_anho) {
		this.grup_anho = grup_anho;
	}

	public EtapaModelo getEtapa() {
		return etapa;
	}

	public void setEtapa(EtapaModelo etapa) {
		this.etapa = etapa;
	}

	public List<CatequistaModelo> getCatequistas() {
		return catequistas;
	}

	public void setCatequistas(List<CatequistaModelo> catequistas) {
		this.catequistas = catequistas;
	}

	public List<InscripcionModelo> getInscripciones() {
		return inscripciones;
	}

	public void setInscripciones(List<InscripcionModelo> inscripciones) {
		this.inscripciones = inscripciones;
	}

	public List<ClaseModelo> getClases() {
		return clases;
	}

	public void setClases(List<ClaseModelo> clases) {
		this.clases = clases;
	}

	public List<TransferenciaModelo> getTransferenciasOrigen() {
		return transferenciasOrigen;
	}

	public void setTransferenciasOrigen(List<TransferenciaModelo> transferenciasOrigen) {
		this.transferenciasOrigen = transferenciasOrigen;
	}

	public List<TransferenciaModelo> getTransferenciasDestino() {
		return transferenciasDestino;
	}

	public void setTransferenciasDestino(List<TransferenciaModelo> transferenciasDestino) {
		this.transferenciasDestino = transferenciasDestino;
	}

}