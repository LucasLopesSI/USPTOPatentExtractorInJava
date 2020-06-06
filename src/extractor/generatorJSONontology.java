/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extractor;

import java.util.LinkedList;

/**
 *
 * @author Lucas
 */
public class generatorJSONontology {
   
    @Override
    public boolean equals(Object obj) { 
        return (this.toString().equals((String)obj)); 
    }
    
    LinkedList<String> cities = new LinkedList<String>();
    LinkedList<String> countries = new LinkedList<String>();
    
    class objectProperty{
         String relation;
         String range;

        public objectProperty(String relation, String range) {
            this.relation = relation;
            this.range = range;
        }  
    }
   
    class dataProperty{
        String name;
        String value;

        public dataProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }
       
       
    }
   
    static String dwURI = "";
   
    public static String generateData(String dataName, String dataValue) {
       
        return "\""+dwURI+dataName+"\" : [ {\n"+"\"@value"+"\" : \""+dataValue+"\"\n} ]";
    }
   
    static String URI_ontology="";
    public static String Individual(String name,String[] type,objectProperty[] op ,dataProperty[] dp){
        String aux = "{\n" +"  \"@id\" : \""+(URI_ontology+name)+"\",\n";
        aux+= "\"@type\" : [ \"http://www.w3.org/2002/07/owl#NamedIndividual\" ";
        for(int i=0;i<type.length;i++){
            if(i==0){
            aux+=",";
            }
            if(i!=type.length-1)
                aux+= "\""+(URI_ontology+type[i])+"\",";
            else
                aux+= "\""+(URI_ontology+type[i])+"\"";
        }
        aux+="]";
       
        for(int i=0;i<dp.length;i++){
            if(i==0){
             aux+=",\n";
            }
            aux+= generateData(dp[i].name, dp[i].value)+",";
        }
        if(op==null){
            aux =aux.substring(0,aux.length()-1);
            aux+="\n},";
        }
       
        if(op!=null){
            for(int i=0;i<op.length;i++){
                if(i==0){
                 aux+="\n";
                }
                if(i!=op.length-1){
                    aux+="\""+(URI_ontology+op[i].relation)+"\" : [ {\n";
                    aux+="\"@id\" : \""+(URI_ontology+op[i].range)+"\"\n";
                    aux+="} ],\n";
                }else{
                    aux+="\""+(URI_ontology+op[i].relation)+"\" : [ {\n";
                    aux+="\"@id\" : \""+(URI_ontology+op[i].range)+"\"\n";
                    aux+="} ]\n},";
                }
            }
        }
        return aux;
    }
   
    public Object[] generateProperties(int i){
        String[] tipos = {"http://dbpedia.org/page/Description#Description"};
        dataProperty descricao = new dataProperty("http://purl.org/dc/terms/abstract",Extractor.extractor.patentes[i].description.replace("\n", ""));
        dataProperty [] descricao_array = {descricao};
        System.out.println(Individual("http://us.patents.aksw.org/"+"Description"+i,tipos,null,descricao_array));
        
        objectProperty description = new objectProperty("http://dbpedia.org/page/Scientific_journal#describedBy","http://us.patents.aksw.org/"+"Description"+i);
        
        
        String[]inventores=Extractor.extractor.patentes[i].inventors.split(";");
        Extractor.extractor.patentes[i].inventors+=inventores[0];
        inventores=Extractor.extractor.patentes[i].inventors.split(";");
        for(int d=1;d<inventores.length;d++){
            String[]inventor2=inventores[d].replace(")","#").replace("(","#").split("#");
            String[] tipos2 = {"http://dbpedia.org/ontology/Person"};
            dataProperty nome = new dataProperty("http://xmlns.com/foaf/0.1/name",inventor2[0]+inventor2[2].replace(",", "").replace("  "," "));
            
            if(!cities.contains(inventor2[1].split(",")[0].replace(" ", ""))){
                String[] tipos3 = {"http://us.patents.aksw.org/ontology/City"};
                dataProperty city = new dataProperty("http://dbpedia.org/page/Scientific_journal#cityName",inventor2[1].split(",")[0]);
                dataProperty [] city_array = {city};
                System.out.println(Individual("http://us.patents.aksw.org/"+inventor2[1].split(",")[0].replace(" ", ""),tipos3,null,city_array));
                cities.add(inventor2[1].split(",")[0].replace(" ", ""));
            }
            
            if(!countries.contains(inventor2[1].split(",")[1].replace(" ",""))){
                String[] tipos4 = {"http://us.patents.aksw.org/ontology/Country"};
                dataProperty country = new dataProperty("http://us.patents.aksw.org/property/countryCode",inventor2[1].split(",")[1]);
                dataProperty [] country_array = {country};
                System.out.println(Individual("http://us.patents.aksw.org/"+inventor2[1].split(",")[1].replace(" ",""),tipos4,null,country_array));
                countries.add(inventor2[1].split(",")[1].replace(" ",""));
            }
            
            objectProperty cidade= new objectProperty("http://us.patents.aksw.org/property/city", "http://us.patents.aksw.org/"+inventor2[1].split(",")[0].replace(" ", ""));
            objectProperty país= new objectProperty("http://us.patents.aksw.org/property/country", "http://us.patents.aksw.org/"+inventor2[1].split(",")[1].replace(" ", ""));
            dataProperty [] pessoa_array = {nome};
            objectProperty[] obj_pessoa_array ={cidade, país};
            System.out.println(Individual("http://us.patents.aksw.org/"+"Inventor_Patente"+i,tipos2,obj_pessoa_array,pessoa_array));
        }
        
        
        objectProperty inventor = new objectProperty("http://us.patents.aksw.org/property/inventor","http://us.patents.aksw.org/"+"Inventor_Patente"+i);
        dataProperty number = new dataProperty("http://us.patents.aksw.org/property/docNo",Extractor.extractor.patentes[i].number);
        dataProperty titulo = new dataProperty("http://us.patents.aksw.org/property/inventionTitle",Extractor.extractor.patentes[i].title);
        Object [] retorno ={inventor,description,number,titulo};
        return retorno;
    }
//  
    public static void main(String[] args) {
        generatorJSONontology obj = new generatorJSONontology();
        int i=0;
        System.out.println("\n\n"+"#########Iniciou JSNO generator#############");
        while(i<405){
            if(Extractor.extractor.patentes[i]!=null){
                Object[] properties = obj.generateProperties(i);
                String[]tipos={"http://us.patents.aksw.org/ontology/Patent"};
                objectProperty obj_proper = (objectProperty) properties[0];
                objectProperty obj_proper2 = (objectProperty) properties[1];
                dataProperty data_proper2 = (dataProperty) properties[2];
                dataProperty data_proper3 = (dataProperty) properties[3];
                objectProperty [] obj_propers = {obj_proper,obj_proper2};
                dataProperty [] data_propers = {data_proper2, data_proper3};
                System.out.println(Individual("http://us.patents.aksw.org/"+"Patente"+i,tipos,obj_propers,data_propers));       
                i++;
            }
        }
    }
}
