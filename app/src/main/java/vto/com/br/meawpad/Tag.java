package vto.com.br.meawpad;

import com.google.firebase.database.Exclude;

public class Tag {

    private String tag;
    private String conteudo;

    public Tag(String tag, String conteudo) {
        this.tag = tag;
        this.conteudo = conteudo;
    }

    public Tag (){

    }
    @Exclude
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tag='" + tag + '\'' +
                ", conteudo='" + conteudo + '\'' +
                '}';
    }
}
