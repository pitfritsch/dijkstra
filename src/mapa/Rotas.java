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
public class Rotas {
    
    Cidades cidadeOrigem;
    Cidades cidadeDestino;
    int distancia;

    public Rotas(Cidades cidadeOrigem, Cidades cidadeDestino, int distancia) {
        this.cidadeOrigem = cidadeOrigem;
        this.cidadeDestino = cidadeDestino;
        this.distancia = distancia;
    }
    
    
    
}
