/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;


/**
 *
 * @author Lucas
 */
public class Extractor {
    static String Erro = null;
    
    class Bytes{
        byte[]teste;
    }
    class Patente{
        String number;
        String title;
        String inventors;
        String applicant;
        String description;
        String assigne;
        String country;

        public Patente(String number, String title, String inventors, String applicant, String description, String assigne, String country) {
            this.number = number;
            this.title = title;
            this.inventors = inventors;
            this.applicant = applicant;
            this.description = description;
            this.assigne = assigne;
            this.country = country;
        }

        
        
        
    }
    
    Patente [] patentes = new Patente[8000];
    
    static int[] conjunto= new int[120];
    /**
     * @param args the command line arguments
     */
    static Extractor extractor = new Extractor();
             
    public Thread getFromUSPTO(String TypeSearch,String Search,String page,int num){
        Thread extractt = new Thread(new Runnable() {

            @Override
            public void run() {
                String csv="";
        int i=num;
        
        FileWriter arq=null;
        try {
            arq = new FileWriter("C:\\Users\\Lucas\\Desktop\\patents_extraction\\patents_table.txt");
        } catch (IOException ex) {
            Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
        }
        PrintWriter gravarArq = new PrintWriter(arq);
        try{
            System.setProperty("webdriver.chrome.driver", "chromedriver83.exe");
            WebDriver driver = new ChromeDriver();
            String aux = "TTL/"+Search;
            System.out.println(aux);
            try{
                driver.get(page);
            }catch(Exception e){
                driver.close();
                e.printStackTrace();
                Erro="Erro 02: Verifique sua conexÃ£o com a internet";
//                return "Erro 02: Verifique sua conexÃ£o com a internet";
            }
//          int tamanho = Integer.valueOf(driver.findElement(By.xpath("/html/body/i/strong[3]")).getText());
            String nextlist="/html/body/center[1]/table/tbody/tr[2]/td/a[2]/img";
            
            int c=1;
            i=i;
            while(i<7720){
                if(c==1){
                    driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[4]/a")).click();
                    i++;
                    c++;
                    continue;
                }
                if(c%50!=0){
//                    Clica no Next da primeira página
                    if(c==2 && c==i){
//                        Vê se tem o anterior
                        driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[3]/img")).click();
                    }else{
                        if(c==i){
                        driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[4]/img")).click();
                        }else{
                            if(c==2){
                                driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[4]/img")).click();
                            }else{
                                driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[5]/img")).click();
                            }
                        }
                    }
                }else{
////                    clica no nextList da primeira vez
                    if(c==i){
                        driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[2]/img")).click();   
                        c=1;
                        continue;
                    }else{
                        driver.findElement(By.xpath("/html/body/center[1]/table/tbody/tr[2]/td/a[3]/img")).click();
                        c=1;
                        continue;
                    }
                }

                String title="";
                String IPC_title="";
                String inventors="";
                String applicant="";
                String description = "";
                String assigne = "";
                String country = "";
                String other_references = "";
                String CPC = "";
                String IPC = "";
                try{
                    title = driver.findElement(By.xpath("/html/body/font")).getText();
                }catch(Exception e){}
                try{
                    IPC_title = driver.findElement(By.xpath("/html/body/table[2]/tbody/tr[1]/td[2]/b")).getText();
                }catch(Exception e){}
                try{
                    inventors = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[1]/td")).getText();
                }catch(Exception e){}
                try{
                    applicant= driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[2]/td/table/tbody/tr[2]/td[1]/b")).getText();
                }catch(Exception e){} 
                try{
                    description = driver.findElement(By.xpath("/html/body/p[1]")).getText();
                }catch(Exception e){} 
                try{
                    assigne = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[3]/td/b")).getText();
                }catch(Exception e){}
                try{
                    assigne = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[3]/td/b")).getText();
                }catch(Exception e){}
                try{
                    country = driver.findElement(By.xpath("/html/body/table[3]/tbody/tr[2]/td/table/tbody/tr[2]/td[4]")).getText();
                }catch(Exception e){}
                try{
                    other_references = getOtherReferences(driver);
                }catch(Exception e){}
                
                try{
                    CPC = driver.findElement(By.xpath("/html/body/p[2]/table/tbody/tr[2]/td[2]")).getText();             
                }catch(Exception e){}
                try{
                    IPC = driver.findElement(By.xpath("/html/body/p[2]/table/tbody/tr[3]/td[2]")).getText();             
                }catch(Exception e){}
                
//              System.out.println(IPC_title.replace("\n"," ")+"\t"+CPC.replace("\n"," ")+"\t"+IPC.replace("\n"," "));
                System.out.println(title.replace("\n"," ")+"\t"+IPC_title.replace("\n"," ")+"\t"+CPC.replace("\n"," ")+"\t"+IPC.replace("\n"," ")+inventors.replace("\n"," ")+"\t"+applicant.replace("\n"," ")+"\t"+description.replace("\n"," ")+"\t"+assigne.replace("\n"," ")+"\t"+country.replace("\n"," ")+"\t"+other_references.replace("\n"," ")+"\t");
                
                patentes[i-2]= new Patente(IPC_title,title,inventors,applicant,description,assigne,country);
                c++;
                i++;
            }
            driver.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        try {
                    arq.close();
                } catch (IOException ex) {
                    Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        return extractt;
    }
    
    public static String getOtherReferences(WebDriver driver){
        String possible_other_references;
        try{
            possible_other_references = driver.findElement(By.xpath("/html/body/table[6]/tbody/tr/td")).getText();
            if(possible_other_references.contains("cited by") || possible_other_references.contains("Cited by")){
                return possible_other_references;
            }
        }catch(Exception e){
        }
        
        try{
            possible_other_references = driver.findElement(By.xpath("/html/body/table[8]/tbody/tr/td/")).getText();
            if(possible_other_references.contains("cited by") || possible_other_references.contains("Cited by")){
                return possible_other_references;
            }
        }catch(Exception e){
        }
        
        try{
            possible_other_references = driver.findElement(By.xpath("/html/body/table[8]/tbody/tr/td/align=\"left\"")).getText();
            if(possible_other_references.contains("cited by") || possible_other_references.contains("Cited by")){
                return possible_other_references;
            }
        }catch(Exception e){
        }
        
        
        try{
            possible_other_references = driver.findElement(By.xpath("/html/body/table[7]/tbody/tr/td")).getText();
            if(possible_other_references.contains("cited by") || possible_other_references.contains("Cited by")){
                return possible_other_references;
            }
        }catch(Exception e){
        }       
        try{
            possible_other_references = driver.findElement(By.xpath("/html/body/table[7]/tbody/tr/td/")).getText();
            if(possible_other_references.contains("cited by") || possible_other_references.contains("Cited by")){
                return possible_other_references;
            }
        }catch(Exception e){
        }
        return "No other references field";
    }
    
    public String testConnection(){
        try{
                System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
                WebDriver driver = new ChromeDriver();
                driver.close();
                return "ConexÃ£o bem sucedida com a AplicaÃ§Ã£o";
        }catch(Exception e){
            return "Erro 01: Falha na conexÃ£o com a AplicaÃ§Ã£o Scrapper, verifique se a versÃ£o correta do Chrome estÃ¡ instalada no Servidor.";
        }
    }
    
    
    public String getFromINPI(String TypeSearch,String Search){
        String csv="";
        int i=1;
        try{
           System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
            WebDriver driver = new ChromeDriver();
            try{
                driver.get("https://gru.inpi.gov.br/pePI/servlet/LoginController?action=login");
//                driver.findElement(By.xpath("//*[@id=\"principal\"]/form/table/tbody/tr[6]/td/font/b/font/a")).click();
                driver.findElement(By.xpath("//*[@id=\"Map3\"]/area[5]")).click();
                
            }catch(Exception e){
                return "Erro 02: Verifique sua conexÃ£o com a internet";
            }

            int c=1;
            while(i<7){
                
                    
                String title="";
                String IPC_title="";
                String inventors="";
                String applicant="";
                String description = "";
                String assigne = "";
                String country = "";
                try{
                    title = driver.findElement(By.xpath("/html/body/form/div[2]/div/table[2]/tbody/tr[9]/td[2]/div/font")).getText();
                    conjunto[i]=title.getBytes().length;
                }catch(Exception e){e.printStackTrace();}
                try{
                    IPC_title = driver.findElement(By.xpath("//*[@id=\"principal\"]/table[2]/tbody/tr[7]/td[2]/font/a")).getText();
                }catch(Exception e){e.printStackTrace();}
                try{
                    inventors = driver.findElement(By.xpath("//*[@id=\"principal\"]/table[2]/tbody/tr[12]/td[2]/font")).getText();
                }catch(Exception e){e.printStackTrace();}
                try{
                    applicant= driver.findElement(By.xpath("//*[@id=\"principal\"]/table[2]/tbody/tr[11]/td[2]/font")).getText();
                }catch(Exception e){e.printStackTrace();} 
                try{
                    description = driver.findElement(By.xpath("//*[@id=\"resumoContext\"]/font")).getText();
                }catch(Exception e){e.printStackTrace();} 
                try{
                    assigne = driver.findElement(By.xpath("//*[@id=\"principal\"]/table[2]/tbody/tr[13]/td[2]/font")).getText();
                }catch(Exception e){e.printStackTrace();}
                try{
                    country = driver.findElement(By.xpath("//*[@id=\"principal\"]/table[2]/tbody/tr[6]/td[2]/table/tbody/tr[2]/td[1]/font")).getText();
                }catch(Exception e){e.printStackTrace();}
                csv=csv+title+"#"+IPC_title+"#"+inventors+"#"+country+"#"+assigne+"#"+applicant+"#"+description+"#";
                
                    c++;
                    i++;
                
            }
            driver.close();
                return csv;
        }catch(Exception e){
            Erro =e.toString();
            e.printStackTrace();}
        return null;
    }
    
    public static void main(String[] args) {
        Thread extracao = new Thread() {
     
            @Override
            public void run() {
              //Recebe aproximadamente 70mil registros. 
             try{
                extractor.getFromUSPTO("", "","http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&f=S&l=50&d=PTXT&RS=ICN%2FPE&Refine=Refine+Search&Query=ICN%2FVE",1).start();
//                extractor.getFromUSPTO("", "","http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&p=30&f=S&l=50&d=PTXT&S1=BR.INCO.&Page=Next&OS=ICN/BR&RS=ICN/BR",1451).start();
//                
//                extractor.getFromUSPTO("", "","http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&p=60&f=S&l=50&d=PTXT&S1=BR.INCO.&Page=Next&OS=ICN/BR&RS=ICN/BR",2951).start();
//                extractor.getFromUSPTO("", "","http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&p=90&f=S&l=50&d=PTXT&S1=BR.INCO.&Page=Next&OS=ICN/BR&RS=ICN/BR",4451).start();
//                extractor.getFromUSPTO("", "","http://patft.uspto.gov/netacgi/nph-Parser?Sect1=PTO2&Sect2=HITOFF&u=%2Fnetahtml%2FPTO%2Fsearch-adv.htm&r=0&p=120&f=S&l=50&d=PTXT&S1=BR.INCO.&Page=Next&OS=ICN/BR&RS=ICN/BR",5951).start();
                
            }catch(Exception e){e.printStackTrace();}  
//             https://docs.google.com/spreadsheets/d/17DREcpR-nNzZ_V3vx7NmAeOqzjFVB2HU7tkYjq37ia0/edit#gid=0   
            }
        };
        extracao.start();
        try{
        extracao.join();
        }catch(Exception e ){e.printStackTrace();}
//        generatorJSONontology.main(null);

    }
}
