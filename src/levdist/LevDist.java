package levdist;

import java.io.*;
import java.util.*;
import java.lang.Comparable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LevDist {
    public static List<Patent> patents_in_uspto = new LinkedList<Patent>();
    public static LinkedList<String> journals;
    public static LinkedList<String> scientific_num;
    
    public static void main(String[] args) throws IOException {
       
//        String a = "vanish", b = "tarnish";
//        LevDist lev = new LevDist();
//        System.out.println("Levenshtein Distance: " + lev.findDistance(a, b));
//        
//        referencia();     
//        cientifica();     //Usar abaixo
//        tecnologica();    //Usar abaixo
//        citacao();        //Usar abaixo
//        
//        LinkedList<String> refeList = referencia();
        
        journals = readFileAndFillLinkedListByColumnIndex(1,"\\t");
        scientific_num = readFileAndFillLinkedListByColumnIndex(2,"\\t");
        
        fillPatents(null);
//        for(Patent patent:patents_in_uspto){
//            for(String citacao: patent.other_references){
//                 System.out.println(patent.id+ "\t"+citacao);
//            }
//        }
//        System.out.println("Cien = " + cienList.size() + "    Tecno = " + tecnoList.size() + "    Cita = " + citaList.size());


           Thread []pull_thread = new Thread[8];
           
           for(int i=0;i<pull_thread.length;i++){
               pull_thread[i]= new CalculateScientificFieldThread();
           }
           
           List<List<Patent>> trabalhos = new LinkedList<>();
           int tam_trabalho =  patents_in_uspto.size()/pull_thread.length;
           for(int i=0;i<pull_thread.length;i++){
               List<Patent> trabalho_para_thread = new LinkedList<>();
               if(i!=pull_thread.length-1){
                    
                    int ini=i*tam_trabalho; //posiciona o trabalho na primeira patente a ser analisada
                    while(ini<(i+1)*tam_trabalho){
                        trabalho_para_thread.add(patents_in_uspto.get(ini));
                        ini++;
                    }
               }else{
                   int ini=i*tam_trabalho; //posiciona o trabalho na primeira patente a ser analisada
                   while(ini<(patents_in_uspto.size())){
                        trabalho_para_thread.add(patents_in_uspto.get(ini));
                        ini++;
                    }
               }
               trabalhos.add(trabalho_para_thread);
           }
           int i=0;
           for(List<Patent> job: trabalhos){
               ((CalculateScientificFieldThread)pull_thread[i]).job = job;
               pull_thread[i].start();
               i++;
           }
           for(i=0;i<pull_thread.length;i++){
                try {
                    pull_thread[i].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(LevDist.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
//           Thread a = new CalculateScientificFieldThread();
//           List<Patent> teste = new LinkedList<>();
//           for(int i=0;i<20;i++){
//               teste.add(patents_in_uspto.get(i));
//           }
//           ((CalculateScientificFieldThread)a).job= teste;
//           a.start();
//            try {
//                a.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(LevDist.class.getName()).log(Level.SEVERE, null, ex);
//            }
           System.out.println("################Total########################\n\n\n\n");
           for(Patent patent: patents_in_uspto){
               String scientific_field="";
               for(String field:patent.scientific_field){
                   scientific_field+=field+",";
               }
               if(scientific_field.endsWith(",")){
                   scientific_field = scientific_field.substring(0,scientific_field.length()-1);
               }
//               if(!scientific_field.equals(""))
//                System.out.println(patent.id+"\t"+patent.ipc_Code+"\t"+patent.cpc_Code+"\t"+scientific_field);
           }
    }

    // REFERENCIA.TXT

    public static LinkedList<String> referencia(){

        String txtRefe = "D:\\fvfurq\\Documents\\Trabalhos\\IniciacaoCientifica\\Referencias.txt";
        LinkedList<String> listaBoa = new LinkedList<String>();

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(txtRefe));

            //LinkedList<String> listaBoa = new LinkewdList<String>();


            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] citacoes = line.split("cited by");

                for(String citacao:citacoes)
                    if(citacao.length() > 10)
                        listaBoa.add(citacao.replace("other .", "").replace("examiner .", "").replace("applicant .", ""));
            }

//            for(String citacao:listaBoa)
//                    System.out.println(citacao);
            
            return listaBoa;
        }

        catch(Exception e){
            e.printStackTrace();
        }
        
        return listaBoa;
    }
    
    public static LinkedList<String> fillPatents(String file){

        String txtRefe = "C:\\Users\\Lucas\\Downloads\\extraction.tsv";
        LinkedList<String> listaBoa = new LinkedList<String>();

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(txtRefe));

            //LinkedList<String> listaBoa = new LinkewdList<String>();


            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] citacoes = line.split("\t");
                if(citacoes.length>=7){
                    Patent nova_patente = new Patent(citacoes[0],citacoes[1],citacoes[2],citacoes[3],citacoes[4],citacoes[5],citacoes[7]);
                    String[] other_references = citacoes[8].split("cited by");
                    for(String other_reference:other_references){
//                        "Para uso do levensthein"
//                        if(other_reference.contains("\",") && !other_reference.contains("US. Appl.")){
//                            String[] splited = other_reference.replace("\",", "#").split("#");
//                            if(splited[1].replace("applicant", "").replace("examiner","").replace("pp", "").replace("pg", "").replace("page", "").replace("published", "").length()>15){
//                                nova_patente.other_references.add(splited[1].replace("applicant", "").replace("examiner","").replace("pp", "").replace("pg", "").replace("page", "").replace("published", ""));
//                            }
//                        }else{
//                            if(!(other_reference.contains("No other refere")) && other_reference.length()>15 && !other_reference.contains("US. Appl.")){
//                                nova_patente.other_references.add(other_reference.replace("applicant", "").replace("examiner",""));
//                            }
//                        }
                          if(!(other_reference.contains("No other refere")) && other_reference.length()>15 && !other_reference.contains("US. Appl.")){
                                nova_patente.other_references.add(other_reference.replace("applicant", "").replace("examiner","").replace("pp", "").replace("pg", "").replace("page", "").replace("published", ""));
                            }
                    }
                    patents_in_uspto.add(nova_patente);
                }
                    
            }

//            for(String citacao:listaBoa)
//                    System.out.println(citacao);
            
            return listaBoa;
        }

        catch(Exception e){
            e.printStackTrace();
        }
        
        return listaBoa;
    }

     // CITACAO.TXT

    public static LinkedList<String> citacao(){

        String txtCita = "D:\\fvfurq\\Documents\\Trabalhos\\IniciacaoCientifica\\Citacao.txt";
        LinkedList<String> listaBoa = new LinkedList<String>();

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(txtCita));

            //LinkedList<String> listaBoa = new LinkedList<String>();


            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] citacoes = line.split("\\r?\\n");

                for(String citacao:citacoes)
                    if(citacao.length() > 10)
                        listaBoa.add(citacao.replace("other .", "").replace("examiner .", "").replace("applicant .", "").replace("applicant.", ""));
            }

//            for(String citacao:listaBoa)
//                    System.out.println(citacao);
            
            return listaBoa;
        }

        catch(Exception e){
            e.printStackTrace();
        }
        
        return listaBoa;
    }
    
    //CIENTIFICA.TXT

    public static LinkedList<String> readFileAndFillLinkedListByColumnIndex(int i, String separator){

        String txtCien = "C:\\Users\\Lucas\\Desktop\\patents_extraction\\journal_reduced_classification.tsv";
        LinkedList<String> listaBoa = new LinkedList<String>();

        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(txtCien));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] citacoes = line.split(separator);
                        listaBoa.add(citacoes[i]);
            }
            
            return listaBoa;
        }

        catch(Exception e){
            e.printStackTrace();
        }
        
        return listaBoa;
    }
    //Comparar Tabelas
    
    public static LinkedList<Node> distancias(LinkedList<String> cita, LinkedList<String> cien, LinkedList<String> tecno){
        
        LinkedList<Node> distTotal = new LinkedList<Node>();
        //Object[] tecnoArray = tecno.toArray();
        
        int cont1 = 1;
        int cont2;
        boolean refe;
        
        for(String citacaoCita:cita){
            
            LinkedList<Node> distTemp = new LinkedList<Node>();
            
            int i = 0;
            
            if(citacaoCita.toString().equals("No other references field")){
                refe = true;
                //System.out.println(citacaoCita.toString() + " " + refe);
            }
            
            else
                refe = false;
            
            if(refe){
                Node noNo = new Node();
                noNo.dist = -1;
                noNo.id = "X";
                distTemp.add(noNo);
                System.out.println("Cita = " + cont1 + "    Cien = " + i + "    Refe = " + refe);
                cont1++;
            }
            
            else {
            
                for(String citacaoCien:cien){

                    Node no = new Node();

                    int dist = findDistance(citacaoCita, citacaoCien);

                    no.dist = dist;
                    no.id = tecno.get(i).toString();

                    if(dist != 0)
                        distTemp.add(no);

                    i++;
                    
                    System.out.println("Cita = " + cont1 + "    Cien = " + i + "    Refe = " + refe);
                }
                
               Collections.sort(distTemp, Comparator.comparing(Node::getDist));
               cont1++;
            }
            
            System.out.println(distTemp.getFirst().id);
            distTotal.add(distTemp.getFirst());
        }
        
        System.out.println("acabou da calcular");
        
        return distTotal;
    }
    
    //Pegar numerozinhos (ID) da lista de Nodes, já ordenada

    public static LinkedList<String> numerozinhosOrdenados(LinkedList<Node> noDistID){
        
        LinkedList<String> numinho = new LinkedList<String>();
        
        for(Node no:noDistID)
            numinho.add(no.id);
        
        System.out.println("ID:\n");
        
        for(String num:numinho)
            System.out.println(num);
        
        return numinho;
    }
    
   // Coloca o ID no arquivo txt
    
    public static void idToTxt(LinkedList<String> id) throws IOException{
        
        String file_name = "D:\\fvfurq\\Documents\\Trabalhos\\IniciacaoCientifica\\orderID.txt";
        
        WriteFile data = new WriteFile(file_name, true);
        
        for(String num:id)
            data.writeToFile(num + "\n");
    }
    
    //Para calcular a Distancia

    static int findDistance(String a, String b) {
            int d[][] = new int[a.length() + 1][b.length() + 1];

            for(int i = 0; i <= a.length(); i++)
                    d[i][0] = i;

            for(int j = 0; j <= b.length(); j++)
                    d[0][j] = j;

            int insertion, deletion, replacement;
            for(int i = 1; i <= a.length(); i++) {
                    for(int j = 1; j <= b.length(); j++) {
                            if(a.charAt(i - 1) == (b.charAt(j - 1)))
                                    d[i][j] = d[i - 1][j - 1];
                            else {
                                    insertion = d[i][j - 1];
                                    deletion = d[i - 1][j];
                                    replacement = d[i - 1][j - 1];
                                    d[i][j] = 1 + findMin(insertion, deletion, replacement);
                            }
                    }
            }

            return d[a.length()][b.length()];
    }

    static int findMin(int x, int y, int z) {
            if(x <= y && x <= z)
                    return x;
            if(y <= x && y <= z)
                    return y;
            else
                    return z;
    }
}


class Patent{
    String title;
    String ipc_Code;
    String cpc_Code;
    String country;
    String id;
    String description;
    String inventores;
    List<String> other_references= new LinkedList<String>();
    List<String> scientific_field=new LinkedList<String>();

    public Patent(String title, String id, String ipc_Code, String cpc_Code,String inventores, String description, String country ) {
        this.title = title;
        this.ipc_Code = ipc_Code;
        this.cpc_Code = cpc_Code;
        this.country = country;
        this.id = id;
        this.description = description;
        this.inventores = inventores;
    }
    
    
}