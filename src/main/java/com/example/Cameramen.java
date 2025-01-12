package com.example;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

public class Cameramen {
    private String codename;
    private String merek;
    private String tipe;
    private String lensa;
    private List<String> ext;
    private String condition;
    private String category;
    
    public Cameramen(String codename, String merek, String tipe, String lensa, List<String> ext, String condition, String category){
        this.codename = codename;
        this.merek = merek;
        this.tipe = tipe;
        this.lensa = lensa;
        this.ext = ext;
        this.condition = condition;
        this.category = category;
    }

    public void setCodename(String codename){this.codename = codename;}
    public String getCodename(){return this.codename;}
    public void setMerek(String merek){this.merek = merek;}
    public String getMerek(){return this.merek;}
    public void setTipe(String tipe){this.tipe = tipe;}
    public String getTipe(){return this.tipe;}
    public void setLensa(String lensa){this.lensa = lensa;}
    public String getLensa(){return this.lensa;}
    public void setExt(List<String> ext){this.ext = ext;}
    public List<String> getExt(){return this.ext;}
    public void setCondition(String condition){this.condition = condition;}
    public String getCondition(){return this.condition;}
    public void setCategory(String category){this.category = category;}
    public String getCategory(){return this.category;}

    public static List<Cameramen> getAllCameramens(MongoCollection<Document> collectionCameramen){
        List<Cameramen> listCameramen = new ArrayList<>();
        for(Document print : collectionCameramen.find()){
            String codename = print.getString("codename");
            String merek = print.getString("merek");
            String tipe = print.getString("tipe");
            String lensa = print.getString("lensa");

            List<?> rawExt = print.get("ext", List.class);
            List<String> ext = new ArrayList<>();
            if(rawExt != null){
                for (Object obj: rawExt){
                    ext.add(obj.toString());
                }
            }
            
            String condition = print.getString("condition");
            String category = print.getString("category");
            listCameramen.add(new Cameramen(codename, merek, tipe, lensa, ext, condition, category));
        }
        return listCameramen;
    }

    public static void displayDataCameramen(MongoCollection<Document> collectionCameramen, Scanner sc){
        List<Cameramen> listCameramen = Cameramen.getAllCameramens(collectionCameramen);

        if (listCameramen.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }

        System.out.println();
        System.out.println("============ LIST OF CAMERAMEN =============");
        int currentIndex = 0;

        while(true){
            Cameramen currentCameramen = listCameramen.get(currentIndex);

            System.out.println();
            System.out.println("=============================================");
            System.out.println("CODENAME\t: " + currentCameramen.getCodename());
            System.out.println("Brand\t\t: " + currentCameramen.getMerek());
            System.out.println("Type\t\t: " + currentCameramen.getTipe());
            System.out.println("Lens\t\t: " + currentCameramen.getLensa());
            System.out.println("Extentions\t: ");
                for(int i = 0; i < currentCameramen.getExt().size();i++){
                    System.out.println("\t\t  " + currentCameramen.getExt().get(i));
                }
            System.out.println("Condition\t: " + currentCameramen.getCondition());
            System.out.println("Category\t: " + currentCameramen.getCategory());
            System.out.println("=============================================");

            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch(choice){
                    case 1 -> currentIndex = (currentIndex + 1) % listCameramen.size();
                    case 2 -> currentIndex = (currentIndex - 1 + listCameramen.size()) % listCameramen.size();
                    case 3 -> { return;}
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");           
                }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3");
                }
        }
    }

    public static void findDataCameramen(MongoCollection<Document> collectionCameramen, Scanner sc){
        System.out.println();
        System.out.println("============ FIND CAMERAMAN DATA ============");

        System.out.println();
        System.out.println("Input cameramen codename or type");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionCameramen.aggregate(pipeline);

        if(findInput.toLowerCase().startsWith("cam")){
            pipeline.add(Aggregates.match(Filters.eq("codename", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.eq("tipe",findInput)));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename or type: " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                System.out.println("Brand\t\t: " + found.getString("merek"));
                System.out.println("Type\t\t: " + found.getString("tipe"));
                System.out.println("Lens\t\t: " + found.getString("lensa"));
                System.out.println("Extentions\t: ");

                    Object extObj = found.get("ext");
                    @SuppressWarnings("unchecked")
                    List<String> extentions = (List<String>) extObj;
                    if(!extentions.isEmpty()){
                        for(int i = 0; i < extentions.size();i++){
                            System.out.println("\t\t  " + extentions.get(i));
                        }
                    }

                System.out.println("Condition\t: " + found.getString("condition"));
                System.out.println("Category\t: " + found.getString("category"));
                System.out.println("=============================================");
            }
        }
        sc.nextLine();
    }

    public static void createDataCameramen(MongoCollection<Document> collectionCameramen, Scanner sc){
        List<String> newExtList = new ArrayList<>();

        System.out.println();
        System.out.println("=========== ADD NEW CAMERAMAN DATA ==========");
        
        System.out.println();
        System.out.println("Input new camera codename");
        System.out.print("> ");
        String newCodename = sc.nextLine();

        System.out.println();
        System.out.println("Input new camera brand");
        System.out.print("> ");
        String newMerek = sc.nextLine();

        System.out.println();
        System.out.println("Input new camera type");
        System.out.print("> ");
        String newTipe = sc.nextLine();

        System.out.println();
        System.out.println("Input new camera lens");
        System.out.print("> ");
        String newLensa = sc.nextLine();

        System.out.println();
        System.out.println("Input new camera extention (type '0' to stop)");
        while(true){
            System.out.print("> ");
            String newExt = sc.nextLine();

            if(newExt.equals("0")){
                break;
            }
            else{
                newExtList.add(newExt);
            }
        }

        String newCondition = "New";
        
        System.out.println();
        System.out.println("Input new camera category");
        System.out.print("> ");
        String newCategory = sc.nextLine();

        Cameramen newCameramen = new Cameramen(newCodename, newMerek, newTipe, newLensa, newExtList, newCondition, newCategory);
        
        Document createCameramen = new Document("codename", newCameramen.getCodename())
                                    .append("merek", newCameramen.getMerek())
                                    .append("tipe", newCameramen.getTipe())
                                    .append("lensa", newCameramen.getLensa())
                                    .append("ext", newCameramen.getExt())
                                    .append("condition", newCameramen.getCondition())
                                    .append("category", newCameramen.getCategory());
            collectionCameramen.insertOne(createCameramen);
            System.out.println();
            System.out.println(newCameramen.getCodename() + " has been added");
            sc.nextLine();
    }

    public static void editDataCameramen(MongoCollection<Document> collectionCameramen, Scanner sc){
        List<String> updExtList = new ArrayList<>();
        System.out.println();
        System.out.println("============= EDIT CAMERAMAN DATA ===========");

        System.out.println();
        System.out.println("Input cameramen codename or type");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionCameramen.aggregate(pipeline);

        if(findInput.toLowerCase().startsWith("cam")){
            pipeline.add(Aggregates.match(Filters.eq("codename", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.eq("tipe",findInput)));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename or type: " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Codename",
                "\t2. Brand",
                "\t3. Type",
                "\t4. Lens",
                "\t5. Extention",
                "\t6. Condition",
                "\t7. Category",
            };
            for(String option : editOptions){
                System.out.println(option);
            }
            System.out.print("\tEnter your choice (1 - " + (editOptions.length) + ") > ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine());
                if(choice < 1 || choice > editOptions.length) {
                    throw new InputMismatchException();
                }
                    Document updateField = new Document();
                    switch(choice){
                        case 1 -> {
                            System.out.println();
                            System.out.println("\tInput new codename");
                            System.out.print("\t> ");
                            String updCodename = sc.nextLine();
                            updateField.append("codename", updCodename);
                        }

                        case 2 -> {
                            System.out.println();
                            System.out.println("\tInput new brand");
                            System.out.print("\t> ");
                            String updMerek = sc.nextLine();
                            updateField.append("merek", updMerek);
                        }

                        case 3 -> {
                            System.out.println();
                            System.out.println("\tInput new type");
                            System.out.print("\t> ");
                            String updTipe = sc.nextLine();
                            updateField.append("tipe", updTipe);
                        }

                        case 4 -> {
                            System.out.println();
                            System.out.println("\tInput new lens");
                            System.out.print("\t> ");
                            String updLensa = sc.nextLine();
                            updateField.append("lensa", updLensa);
                        }

                        case 5 -> {
                            System.out.println();
                            System.out.println("\tInput new extention (type '0' to stop)");
                            while(true){
                                System.out.print("\t> ");
                                String updExt = sc.nextLine();
                                if(updExt.equals("0")){
                                    break;
                                }
                                else{
                                    updExtList.add(updExt);
                                }
                            }
                            updateField.append("ext", updExtList);
                        }

                        case 6 -> {
                            System.out.println();
                            System.out.println("\tInput new condition");
                            System.out.print("\t> ");
                            String updCondition = sc.nextLine();
                            updateField.append("condition",updCondition);
                        }

                        case 7 -> {
                            System.out.println();
                            System.out.println("\tInput new category");
                            System.out.print("\t> ");
                            String updCategory = sc.nextLine();
                            updateField.append("category", updCategory);
                        }
                    }

                    Document update = result.first();
                    if(update != null){
                        collectionCameramen.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }
            } catch (InputMismatchException e){
                System.out.println("\tInvalid input. Please enter a number between 1 and \" + editOptions.length");
            }
        }
    }

    public static void deleteDataCameramen(MongoCollection<Document> collectionCameramen, Scanner sc){
        System.out.println();
        System.out.println("=========== DELETE CAMERAMAN DATA ===========");

        System.out.println();
        System.out.println("Input cameramen codename or type");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionCameramen.aggregate(pipeline);
    
        if(findInput.toLowerCase().startsWith("cam")){
            pipeline.add(Aggregates.match(Filters.eq("codename", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.eq("tipe",findInput)));
        }


        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename or type : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                System.out.println("Brand\t\t: " + found.getString("merek"));
                System.out.println("Type\t\t: " + found.getString("tipe"));
                System.out.println("Lens\t\t: " + found.getString("lensa"));
                System.out.println("Extentions\t: ");

                    Object extObj = found.get("ext");
                    @SuppressWarnings("unchecked")
                    List<String> extentions = (List<String>) extObj;
                    if(!extentions.isEmpty()){
                        for(int i = 0; i < extentions.size();i++){
                            System.out.println("\t\t  " + extentions.get(i));
                        }
                    }

                System.out.println("Condition\t: " + found.getString("condition"));
                System.out.println("Category\t: " + found.getString("category"));
                System.out.println("=============================================");
            }

            System.out.println();
            System.out.println(findInput + "'s data found");
            System.out.println("Are you sure want to delete this cameramen data? (y/n)");
            System.out.print("> ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                if(findInput.toLowerCase().startsWith("cam")){
                    collectionCameramen.deleteOne(Filters.eq("nij", Integer.valueOf(findInput)));
                }
                else{
                    collectionCameramen.deleteOne(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i"));
                }
                System.out.println();
                System.out.println("Data has been deleted");
            }
        }
        sc.nextLine();
    }
}

