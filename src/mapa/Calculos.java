/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapa;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 0194324
 */
public class Calculos {
    
    public static Cidades origemDaRota;
    public static Cidades destinoRota;
    public static boolean concluiu = false;
    public static List<Cidades> cidadesQueJaPassou = new ArrayList<>();

    public static List<Cidades> getCidadesQueJaPassou() {
        return cidadesQueJaPassou;
    }
    public static void setCidadesQueJaPassou(Cidades cidadesQueJaPassou) {
        getCidadesQueJaPassou().add(cidadesQueJaPassou);
    }
    public static boolean verificaSeJaPassou(Cidades verificaCidade){
        if ( !getCidadesQueJaPassou().stream().anyMatch((cidade) -> cidade.noCidade.equals(verificaCidade.noCidade)) ){
            setCidadesQueJaPassou(verificaCidade);
            return false;
        } else {
            return true;
        }
    }
    
    
    public static Cidades getDestinoRota() {
        return destinoRota;
    }

    public static void setDestinoRota(Cidades destinoRota) {
        Calculos.destinoRota = destinoRota;
    }

    
    public static void setOrigemDaRota(Cidades origemDaRota) {
        Calculos.origemDaRota = origemDaRota;
    }

    public static Cidades getOrigemDaRota() {
        return origemDaRota;
    }
    
//                                cidadeOrig  cidadeDest
    public static void calculaRota(String co, String cd, List<CidadesVizinhos> vizinhos, List<RotaTeste1> rota){
        
        getCidadesQueJaPassou().clear();
        setOrigemDaRota( vizinhos.stream().filter( (v) -> v.cidade.noCidade.equals(co) ).findAny().orElse(null).cidade );
        setDestinoRota( vizinhos.stream().filter( (v) -> v.cidade.noCidade.equals(cd) ).findAny().orElse(null).cidade );
        setCidadesQueJaPassou(getOrigemDaRota());
        
//        System.out.println("Origem: " + getOrigemDaRota().noCidade + " - Destino: " + getDestinoRota().noCidade);
        
        for(CidadesVizinhos vizinhosCidadeOrigem : vizinhos){
            if (!vizinhosCidadeOrigem.cidade.noCidade.equals(co)) continue;
            
            rota.add(new RotaTeste1(vizinhosCidadeOrigem.cidade, vizinhosCidadeOrigem.cidade, 0));
            
            if (co.equals(cd)){
                return;
            }
            
            for (Vizinho vizinho : vizinhosCidadeOrigem.vizinhos){
                
                
                    
                for(CidadesVizinhos vizinhoComVizinhos : vizinhos){
                    if (!vizinhoComVizinhos.cidade.noCidade.equals(vizinho.cidade.noCidade)) continue;

                    if ( verificaSeJaPassou(vizinho.cidade) ) {
                    
//                        System.out.println("calculaRota");
                        if (!verificaSeDistanciaMenor(rota, vizinhoComVizinhos.cidade, vizinhosCidadeOrigem.cidade, vizinho.distancia)) continue;
                    }
                    
                    concluiu = false;
                    alimentaVizinhos(vizinhoComVizinhos, vizinhosCidadeOrigem.cidade, vizinhos, rota, vizinho.distancia);
                }
                
            }
            
        }
        
    }
    
    public static void alimentaVizinhos(CidadesVizinhos atual, Cidades origem, List<CidadesVizinhos> vizinhos, List<RotaTeste1> rota, int distancia){
        
        if (concluiu == true) return;
        
//        System.out.println("\n" + atual.cidade.noCidade + " - " + origem.noCidade + " = " + distancia);
        
        if (atual.cidade.noCidade.equals(getDestinoRota().noCidade)){
            concluiu = true;
        }
        
        for(CidadesVizinhos vizinhosCidadeAtual : vizinhos){
            if (!vizinhosCidadeAtual.cidade.noCidade.equals(atual.cidade.noCidade)) continue;
            
            boolean alterou = false;
            for(RotaTeste1 nodesDaRota : rota){ //circula para ver se a cidade já foi adicionada
                if (nodesDaRota.cidadeAtual.noCidade.equals(atual.cidade.noCidade)){
                    
                    alterou = true;
//                    System.out.println(nodesDaRota.distancia + " <> " + distancia);
                    if (nodesDaRota.distancia > distancia){
                        nodesDaRota.distancia = distancia;
                        nodesDaRota.cidadeOrigem = origem;
                    }
                }
            }
            if (alterou == false){
                rota.add(new RotaTeste1(atual.cidade, origem, distancia));
            }
            
            for (Vizinho vizinhosDaAtual : vizinhosCidadeAtual.vizinhos){
                
                for(CidadesVizinhos vizinhoComVizinhos : vizinhos){
                    if (!vizinhoComVizinhos.cidade.noCidade.equals(vizinhosDaAtual.cidade.noCidade)) continue;
                    if ( verificaSeJaPassou(vizinhosDaAtual.cidade) ) {
                    
//                        System.out.println("alimentaVizinhos");
                        if (!verificaSeDistanciaMenor(rota, vizinhoComVizinhos.cidade, vizinhosCidadeAtual.cidade, (distancia + vizinhosDaAtual.distancia))) continue;
                    }

                    concluiu = false;
                    alimentaVizinhos( vizinhoComVizinhos, vizinhosCidadeAtual.cidade, vizinhos, rota, (distancia + vizinhosDaAtual.distancia) );
                }
                    
                
            }
            
        }
        
    }
    
    public static boolean verificaSeDistanciaMenor(List<RotaTeste1> rota, Cidades cidadeAtual, Cidades cidadeOrigem, int distancia){
        
        for(RotaTeste1 nodesDaRota : rota){ //circula para ver se a cidade já foi adicionada
            if (nodesDaRota.cidadeAtual.noCidade.equals(cidadeAtual.noCidade)){
//                System.out.println("(" + nodesDaRota.cidadeAtual.noCidade + "-" + nodesDaRota.cidadeOrigem.noCidade + "=" + nodesDaRota.distancia + ") <> (" + cidadeAtual.noCidade + "-" + cidadeOrigem.noCidade + "=" + distancia + ")");
                if (nodesDaRota.distancia > distancia){
                    nodesDaRota.distancia = distancia;
                    nodesDaRota.cidadeOrigem = cidadeOrigem;
                    return true;
                }
            }
        }
        return false;
    }
    
}
