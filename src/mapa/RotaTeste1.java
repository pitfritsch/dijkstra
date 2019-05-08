/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapa;

/**
 *
 * @author Pedrivo
 */
public class RotaTeste1 {
    
    Cidades cidadeAtual;
    Cidades cidadeOrigem;
    int distancia; //distancia  até a atual

    public RotaTeste1(Cidades cidadeAtual, Cidades cidadeOrigem, int distancia) {
        this.cidadeAtual = cidadeAtual;
        this.cidadeOrigem = cidadeOrigem;
        this.distancia = distancia;
    }

    public Cidades getCidadeAtual() {
        return cidadeAtual;
    }

    public void setCidadeAtual(Cidades cidadeAtual) {
        this.cidadeAtual = cidadeAtual;
    }

    public Cidades getCidadeOrigem() {
        return cidadeOrigem;
    }

    public void setCidadeOrigem(Cidades cidadeOrigem) {
        this.cidadeOrigem = cidadeOrigem;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
    
    
    
}
