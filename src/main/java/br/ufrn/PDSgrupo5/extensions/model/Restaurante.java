package br.ufrn.PDSgrupo5.extensions.model;

import br.ufrn.PDSgrupo5.framework.model.Profissional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "restaurante")
@PrimaryKeyJoinColumn(name = "profissional_id")
public class Restaurante extends Profissional {
    @Column(name = "quantidade_mesas")
    @NotNull(message = "Quantidade de mesas não pode ser nula")
    private int quantidadeMesas;
    
    @Column(name = "cadeiras_por_mesa")
    @NotNull(message = "Quantidade de cadeiras por mesa não pode ser nula")
    private int cadeirasPorMesa;

    public Restaurante() {
    }

	public int getQuantidadeMesas() {
		return quantidadeMesas;
	}

	public void setQuantidadeMesas(int quantidadeMesas) {
		this.quantidadeMesas = quantidadeMesas;
	}

	public int getCadeirasPorMesa() {
		return cadeirasPorMesa;
	}

	public void setCadeirasPorMesa(int cadeirasPorMesa) {
		this.cadeirasPorMesa = cadeirasPorMesa;
	}
    
}
