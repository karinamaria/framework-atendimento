package br.ufrn.PDSgrupo5.framework.enumeration;

public enum EnumSituacaoProfissionalSaude {
    APROVADO("APROVADO"),
    AGUARDANDO_ANALISE("AGUARDANDO ANÁLISE"),
    CANCELADO("CANCELADO"),
    PENDENCIAS_CADASTRO("PENDÊNCIAS NO CADASTRO"),
    NAO_APROVADO("NÃO APROVADO");

    private String descricao;

    private EnumSituacaoProfissionalSaude(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
