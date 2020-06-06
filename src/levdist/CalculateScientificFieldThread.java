/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package levdist;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Lucas
 */
public class CalculateScientificFieldThread extends Thread{
    public List<Patent> job = new LinkedList<Patent>();

    @Override
    public void run() {
        for(Patent patent: job){
            
            for(String other_reference:patent.other_references){
                other_reference= other_reference.toLowerCase();
//              Para cada citação em cada patent de job, acha a de menor distância, e preenche o array scientific_field de patent
                int smaller_dist=100000;
                int global_best=0;
                String best_journal="";
                String field="";
                int cont=0;// contador pra pegar o correspondente numero no outro arranjo scientific_num
                boolean passou=false;
                for(String journal: LevDist.journals){
                    journal= journal.toLowerCase().replace("journal", "").replace("international", "");
                    int dist=0;
                    int local_best=0;
                    String[]journal_splitted = journal.split(" ");
                    for(String journal_sub : journal_splitted){
                        if(journal_sub.length()>4){
                            if(other_reference.contains(journal_sub.toLowerCase())){
                                local_best++;
                            }
                            
                        }
                    }
                    
                    if(local_best>global_best && local_best>3){
                        global_best=local_best;
                        best_journal = journal;
                        passou=true;
                        field =  LevDist.scientific_num.get(cont);
                    }else{
                        if(local_best<4 && !passou){
                        best_journal = "citacao sem journal";
                        }
                    }
//                    if((dist=LevDist.findDistance(other_reference, journal))<smaller_dist){
//                        smaller_dist = dist;
//                        best_journal=journal;
//                        field =  LevDist.scientific_num.get(cont);
//                    }
                    cont++;
                }
                
                int i=0;
                while(!LevDist.patents_in_uspto.get(i).id.equals(patent.id)){
                    i++;
                }
        
                
//                System.out.println(patent.id+"\t"+patent.ipc_Code+"\t"+patent.cpc_Code+"\t"+field);
                if(!LevDist.patents_in_uspto.get(i).scientific_field.contains(field)){
                    LevDist.patents_in_uspto.get(i).scientific_field.add(field);
                }
                if(field.length()>2)
                    System.out.println(other_reference.replace(". ","")+"\t"+field);
            }
        }
    }
    
    
}
