package br.ufrn.PDSgrupo5.framework.enumeration;

public enum EnumTipoPapel {
	CLIENTE("Cliente"), PROFISSIONAL("Profissional"), VALIDADOR("Validador");

	private String descricao;

	private EnumTipoPapel(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
