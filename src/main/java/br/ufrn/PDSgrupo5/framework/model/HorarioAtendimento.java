package br.ufrn.PDSgrupo5.framework.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "horario_atendimento")
public class HorarioAtendimento extends EntidadeAbstrata{

	@Column(name="dia_semana")
	private String diaSemana;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_inicio", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date horarioInicio;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "horario_fim", nullable=true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date horarioFim;
	
	private boolean livre = true;
	
	private Double preco;

	public HorarioAtendimento() {
	}

	public String getDiaSemana() {
		return diaSemana;
	}

	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}

	public Date getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(Date horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public Date getHorarioFim() {
		return horarioFim;
	}

	public void setHorarioFim(Date horarioFim) {
		this.horarioFim = horarioFim;
	}

	public boolean isLivre() {
		return livre;
	}

	public void setLivre(boolean livre) {
		this.livre = livre;
	}
	
	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	public String statusToString() {
		if(livre) {
			return "Livre";
		}
		return "Ocupado";
	}
	
	public String dataToString() {
		return new SimpleDateFormat("dd/MM/yyyy").format(horarioInicio);
	}
	
	public String horaInicioToString() {
		return new SimpleDateFormat("HH:mm").format(horarioInicio);
	}
	
	public String horaFimToString() {
		return new SimpleDateFormat("HH:mm").format(horarioFim);
	}
	
	public String precoToString() {
		String precoString = "R$ " + new DecimalFormat("#,##0.00").format(preco); 
		return precoString;
	}
}
