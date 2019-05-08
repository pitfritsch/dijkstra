/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author 0194324
 */
public class CarregaArquivo {
    
    static String arquivo = "";
    static File selectedFile = new File("D:\\Programacao3\\exercicioMapa1\\mapa.txt");
    public static List<Cidades> cidadesCarregadas;
    public static List<Rotas> rotasCarregadas;
    
    public void desenhaMapa(Tela tela){
        
        cidadesCarregadas = this.carregaCidades();
        rotasCarregadas = this.carregaRotas();
        tela.image = null;
        tela.adicionaRotas(rotasCarregadas, cidadesCarregadas);
        tela.adicionaCidades(cidadesCarregadas);
        tela.getMapa().repaint();
    }
    
    public File carregaArquivo(){
        return selectedFile;
    }
    
    public void selecionaArquivo(){
        
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "txt", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
        }
        
    }
    
    public List<Rotas> carregaRotas(){
        File file = carregaArquivo();
        List<Rotas> rotas = new ArrayList<>();
        FileReader fileReader;
        try {
            
            fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            
            String linha;
            boolean pegar = false;
            while ((linha = br.readLine()) != null){
                if (linha.equals("")){
                    pegar = true;
                    continue;
                }
                if (pegar == true){
                    
                    String[] linhaSplited = linha.split(" ");
                    
                    Rotas r = new Rotas( cidadesCarregadas.stream().filter(c -> c.noCidade.equals(linhaSplited[0])).findAny().orElse(null),
                                         cidadesCarregadas.stream().filter(c -> c.noCidade.equals(linhaSplited[1])).findAny().orElse(null),
                        Integer.parseInt(linhaSplited[2]));
                    
//                    System.out.println(r.cidadeOrigem + " " + r.cidadeDestino + " - " + r.distancia );
                    
                    rotas.add(r);
                    
                }
            }
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
        
        return rotas;
        
    }
    
    public List<Cidades> carregaCidades(){
        File file = carregaArquivo();
        List<Cidades> cidades = new ArrayList<>();
        FileReader fileReader;
        try {
            
            fileReader = new FileReader(file);
            
            BufferedReader br = new BufferedReader(fileReader);
            
            String linha;
            while ((linha = br.readLine()) != null) {
                
                if (linha.equals("")){
                    break;
                }
                
                String[] linhaSplited = linha.split(" ");
                Cidades c = new Cidades( linhaSplited[0], 
                        Integer.parseInt(linhaSplited[1]),
                        Integer.parseInt(linhaSplited[2]) );
                
//                System.out.println(c.noCidade + " " + c.x + " - " + c.y );
                
                cidades.add(c);
                
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return cidades;
        
    }
    
    public List<CidadesVizinhos> carregaCidadesComVizinhos(){
        
        List<CidadesVizinhos> retorno = new ArrayList<>();
        List<Cidades> listCidades = this.carregaCidades();
        List<Rotas> listRotas = this.carregaRotas();
        
        List<Vizinho> vizinhos;
        
        for (Cidades c : listCidades){
            
            vizinhos = new ArrayList<>();
            Vizinho nv = null;
            
            for (Rotas r : listRotas){
                
                if (r.cidadeOrigem.noCidade.equals(c.noCidade)){
                    nv = new Vizinho();
                    nv.cidade = r.cidadeDestino;
                    nv.distancia = r.distancia;
                    vizinhos.add(nv);
                }
                if (r.cidadeDestino.noCidade.equals(c.noCidade)){
                    nv = new Vizinho();
                    nv.cidade = r.cidadeOrigem;
                    nv.distancia = r.distancia;
                    vizinhos.add(nv);
                }
            }
            
            Collections.sort(vizinhos, (Vizinho o1, Vizinho o2) -> o1.distancia - o2.distancia);
            retorno.add(new CidadesVizinhos(c, vizinhos));
            
        }
        
        return retorno;
    }
    
    public void printaCidadesComVizinhos(List<CidadesVizinhos> lista){
        
        lista.forEach((cv) -> {
            System.out.println(cv.cidade.noCidade);
            
            cv.vizinhos.forEach((cvv) -> {
                System.out.println(cv.cidade.noCidade + " - " + cvv.cidade.noCidade + " " + cvv.distancia);
            });
            
        });
        
    }
}
