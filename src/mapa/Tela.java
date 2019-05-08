/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapa;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *
 * @author Pedro Fritsch
 */

//floyd-warshall algorithm
//Dijkstra Algorithm
public final class Tela extends JFrame{

    public JPanel mapa;
    public BufferedImage image;
    public Graphics2D imageG;
    public Color linhasPontos = new Color(150, 150, 150);
    
    public JComboBox comboOrigem;
    public JComboBox comboDestino;
    CarregaArquivo ca = new CarregaArquivo();
    
    public List<CidadesVizinhos> cidadesComVizinhos;
    public List<String> rota;
    
    public JPanel getMapa() {
        return mapa;
    }
    
    public Tela() throws HeadlessException {
        constroiTela();
    }
    
    public void criaMenuSuperior(){
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("Arquivo");
        JMenu jMenuHelp = new JMenu("?");
        
        JMenuItem btLoadMap = new JMenuItem("Load map");
        btLoadMap.addActionListener((e) -> {
            
            ca.selecionaArquivo();
            image = null;
            ca.desenhaMapa(this);
            
            cidadesComVizinhos = ca.carregaCidadesComVizinhos();
            ca.printaCidadesComVizinhos(cidadesComVizinhos);
            
            alimentaComboBoxCidades(comboOrigem);
            alimentaComboBoxCidades(comboDestino);
        });
        
        JMenuItem btExit = new JMenuItem("Exit");
        btExit.addActionListener((e) -> {
            setVisible(false);
            System.exit(0);
        });
        
        JMenuItem btSobre = new JMenuItem("Sobre");
        btSobre.addActionListener((e) -> {
            JOptionPane.showMessageDialog(this, "Pedro Averbeck Fritsch");
        });
        
        jMenu.add(btLoadMap);
        jMenu.add(btExit);
        
        jMenuHelp.add(btSobre);
        
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenuHelp);
        
        setJMenuBar(jMenuBar);
    }
    
    public void criaMenuInferior(JPanel painel){
        JLabel labelOrigem = new JLabel("Cidade origem: ");
        JLabel labelDestino = new JLabel("Cidade destino: ");
        
        JButton btCalcularRota = new JButton("Calcular rota");
        btCalcularRota.addActionListener((e) -> {
            String cidadeOrigem = comboOrigem.getSelectedItem().toString();
            String cidadeDestino = comboDestino.getSelectedItem().toString();
            
            List<RotaTeste1> rotaCalculada = new ArrayList<>();
            Calculos.calculaRota(cidadeOrigem, cidadeDestino, cidadesComVizinhos, rotaCalculada);
            desenhaRota( cidadeOrigem, cidadeDestino, rotaCalculada );
        });
        
        painel.add(labelOrigem);
        painel.add(comboOrigem);
        
        painel.add(labelDestino);
        painel.add(comboDestino);
        
        painel.add(btCalcularRota);
    }
    
    public void desenhaRota(String cidadeOrigem, String cidadeDestino, List<RotaTeste1> rota){
        System.out.println("\n");
        ca.desenhaMapa(this);
        
        Cidades co = rota.stream().filter((c) -> c.cidadeAtual.noCidade.equals(cidadeOrigem)).findAny().orElse(null).cidadeAtual;
        Cidades cd = rota.stream().filter((c) -> c.cidadeAtual.noCidade.equals(cidadeDestino)).findAny().orElse(null).cidadeAtual;
        
        printaCidade(cidadeDestino, rota);
        
        pintaCidadeNoMapa(co, new Color(49, 187, 237)); //azul
        pintaCidadeNoMapa(cd, new Color(242, 193, 48)); //amarelo
        
        mapa.repaint();
    }
    public void printaCidade(String cidadeDestino, List<RotaTeste1> rota){
        
        for(RotaTeste1 r : rota) {  
            if ( !r.cidadeAtual.noCidade.equals(cidadeDestino) ) continue;
            if ( r.cidadeOrigem.noCidade.equals(r.cidadeAtual.noCidade) ) return;
            
            System.out.println(r.cidadeOrigem.noCidade + " ^^ " + r.cidadeAtual.noCidade + " -> " + r.distancia);
            
            Color verde = new Color(17, 170, 78);
            desenhaLinha(r.cidadeOrigem.x, r.cidadeOrigem.y, r.cidadeAtual.x, r.cidadeAtual.y, verde, 3);
            pintaCidadeNoMapa(r.cidadeAtual, verde);
            
            printaCidade(r.cidadeOrigem.noCidade, rota);
            
        };
        
    }
    
    
    public void alimentaComboBoxCidades(JComboBox comboBox){
        
        comboBox.removeAllItems();
        
        ca.carregaCidades().forEach((c) -> {
            //System.out.println(c.noCidade);;
            comboBox.addItem(c.noCidade);
        });
    }
    
    public void constroiTela(){
        try { 
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); 
        } catch(Exception ignored){}
        
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        Container container = getContentPane();
        
        comboOrigem = new JComboBox();
        comboDestino = new JComboBox();
        
        criaMenuSuperior();

        JPanel opcoesRota = new JPanel(new FlowLayout());
        criaMenuInferior(opcoesRota);
        container.add(opcoesRota, BorderLayout.SOUTH);
        
        mapa = new JPanel(){
            
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(image, 0, 0, null);
            }
            
        };
        
        mapa.setBackground(Color.white);
        mapa.setPreferredSize(new Dimension(900, 800));
        
        JScrollPane scrolled = new JScrollPane(mapa);
        
        container.add(scrolled);
        
        setVisible(true);
    }
    
    
    public void adicionaCidades(List<Cidades> cidades){
        
        for(Cidades c : cidades){
            
            if (image == null){
                image = new BufferedImage(getMapa().getWidth(), getMapa().getHeight(), BufferedImage.TYPE_INT_RGB);
                imageG = (Graphics2D) image.getGraphics();
                imageG.setColor(new Color(38, 47, 58));
                imageG.fillRect(0, 0, getMapa().getWidth(), getMapa().getHeight());
                imageG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //antialiasing
            }
            
            pintaCidadeNoMapa(c, linhasPontos);
            
        }
        
        
    }
    
    public void pintaCidadeNoMapa(Cidades cidade, Color cor){
        
        imageG.setColor(cor);
        imageG.fillOval(cidade.x - 10, cidade.y - 10, 20, 20); // -5 serve para alinhar o ponto

        Font f = new Font("Arial", Font.BOLD, 13);
        imageG.setFont(f);
        imageG.setColor(new Color(255, 255, 255));
        imageG.drawString(cidade.noCidade.substring(0, 3), cidade.x - 10, cidade.y + 5);
        
    }
    
    public void adicionaRotas(List<Rotas> rotas, List<Cidades> cidades){
        
        int xOrig = 0, yOrig = 0, xDest = 0, yDest = 0;
        int medX = 0, medY = 0;
        
        
        for(Rotas r : rotas){
            
            if (image == null){
                image = new BufferedImage(getMapa().getWidth(), getMapa().getHeight(), BufferedImage.TYPE_INT_RGB);
                imageG = (Graphics2D) image.getGraphics();
                imageG.setColor(new Color(38, 47, 58));
                imageG.fillRect(0, 0, getMapa().getWidth(), getMapa().getHeight());
                imageG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //antialiasing
            }

            for(Cidades c : cidades){
                if (c.noCidade.equals(r.cidadeOrigem.noCidade)) {
                    xOrig = c.x;
                    yOrig = c.y;
                }
                if (c.noCidade.equals(r.cidadeDestino.noCidade)){
                    xDest = c.x;
                    yDest = c.y;
                }
            }
            
            medX = (xOrig + xDest) / 2;
            medY = (yOrig + yDest) / 2;
            
            
            desenhaLinha(xOrig, yOrig, xDest, yDest, linhasPontos, 1);
            
            Font f = new Font("Arial", Font.ITALIC, 13);
            imageG.setFont(f);
            imageG.setColor(new Color(255, 255, 255));
            imageG.drawString(Integer.toString(r.distancia), medX, medY);
            
        }
    }
    
    public void desenhaLinha(int xOrig, int yOrig, int xDest, int yDest, Color cor, int espessura){
        
        imageG.setColor(cor);
        imageG.setStroke(new BasicStroke(espessura));
        imageG.drawLine(xOrig, yOrig, xDest, yDest);
        
    }
    
    
}
