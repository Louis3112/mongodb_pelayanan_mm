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

public class TS {
    private String codename;
    private String posisi;
    @SuppressWarnings("FieldMayBeFinal")
    private String jobdesc;

    public TS(String codename, String posisi, String jobdesc){
        this.codename = codename;
        this.posisi = posisi;
        this.jobdesc = jobdesc;
    }

    public void setCodename(String codename){ this.codename = codename; }
    public String getCodename() {return this.codename;}
    public void setPosisi(String posisi){ this.posisi = posisi; }
    public String getPosisi() {return this.posisi;}
    public void setJobdesc(String jobdesc){ this.jobdesc = jobdesc; }
    public String getJobdesc() {return this.jobdesc;}

    public static List<TS> getAllTS(MongoCollection<Document> collectionTS) {
        List<TS> listTS = new ArrayList<>();
        for (Document print : collectionTS.find()) {
            String codename = print.getString("codename");
            String posisi = print.getString("posisi");
            String jobdesc = print.getString("jobdesc");
            listTS.add(new TS(codename,posisi,jobdesc));
        }
        return listTS;
    }

    public static void displayDataTS(MongoCollection<Document> collectionTS, Scanner sc) {
        List<TS> listTS = TS.getAllTS(collectionTS);

        if (listTS.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }

        System.out.println();
        System.out.println("========= LIST OF TECHNICAL SUPPORT =========");
        int currentIndex = 0;
        while (true) {
                
            TS currentTS = listTS.get(currentIndex);
            System.out.println();
            System.out.println("=============================================");
            System.out.println("CODENAME\t: " + currentTS.getCodename());
            System.out.println("Position\t: " + currentTS.getPosisi());
            System.out.println("Jobdsec\t: " + currentTS.getJobdesc());
            System.out.println("=============================================");
    
            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");
    
        try {
            int choice = Integer.parseInt(sc.nextLine());
    
            switch(choice) {
                case 1 -> currentIndex = (currentIndex + 1) % listTS.size();
                case 2 -> currentIndex = (currentIndex - 1 + listTS.size()) % listTS.size();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");
            } 
            } catch (NumberFormatException e) {    
                System.out.println("Invalid choice, please enter 1,2, or 3");
            }
        }
    }

    public static void findDataTS(MongoCollection<Document> collectionTS, Scanner sc) {
        System.out.println();
        System.out.println("======== FIND TECHNICAL SUPPORT DATA ========");

        System.out.println();
        System.out.println("Input technical support codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionTS.aggregate(pipeline);
    
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                System.out.println("Position\t: " + found.getString("posisi"));
                System.out.println("Jobdesc\t: " + found.getString("jobdesc"));
                System.out.println("=============================================");
            }
        }
        sc.nextLine();
    }

    public static void createDataTS(MongoCollection<Document> collectionTS,Scanner sc){
        System.out.println();
        System.out.println("======= ADD NEW TECHNICAL SUPPORT DATA ======");
        
        System.out.println();
        System.out.println("Input new technical support codename");
        System.out.print("> ");
        String newCodename = sc.nextLine();

        System.out.println();
        System.out.println("Input new technical support position");
        System.out.print("> ");
        String newPosisi = sc.nextLine();

        System.out.println();
        System.out.println("Input new technical support jobdesc");
        System.out.print("> ");
        String newJobdesc = sc.nextLine();

        TS newTS = new TS(newCodename, newPosisi, newJobdesc);
        
        Document createTS = new Document().append("codename", newTS.getCodename())
                            .append("posisi", newTS.getPosisi())
                            .append("jobdesc", newTS.getJobdesc());
        collectionTS.insertOne(createTS);

        System.out.println();
        System.out.println(newTS.getCodename() + " has been added");
        sc.nextLine();
    }

    public static void editDataTS(MongoCollection<Document> collectionTS, Scanner sc){
        System.out.println();
        System.out.println("======== EDIT TECHNICAL SUPPORT DATA ========");
        System.out.println();
        System.out.println("Input ICT codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionTS.aggregate(pipeline);
        
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));
    
        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Codename",
                "\t2. Position",
                "\t3. Jobdesc",
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
                            System.out.println("\nInput new position:");
                            System.out.print("\t> ");
                            String updPosisi = sc.nextLine();
                            updateField.append("posisi", updPosisi);
                        }

                        case 3 -> {
                            System.out.println("\nInput new jobdesc:");
                            System.out.print("\t> ");
                            String updJobdesc = sc.nextLine();
                            updateField.append("jobdesc", updJobdesc);
                        }
                    }
                    Document update = result.first();
                    if(update != null){
                        collectionTS.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }
            } catch (InputMismatchException e){
                System.out.println("\tInvalid input. Please enter a number between 1 and \" + editOptions.length");
            }
        }
    }

    public static void deleteDataTS(MongoCollection<Document> collectionTS, Scanner sc){
        System.out.println();
        System.out.println("======= DELETE TECHNICAL SUPPORT DATA =======");
        System.out.println();
        System.out.println("Input ICT codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionTS.aggregate(pipeline);
        
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));
        
        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                System.out.println("Position\t: " + found.getString("posisi"));
                System.out.println("Jobdesc\t: " + found.getString("jobdesc"));
                System.out.println("=============================================");
            }

            System.out.println();
            System.out.println(findInput + "'s data found");
            System.out.println("Are you sure want to delete this live production data? (y/n)");
            System.out.print("> ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                collectionTS.deleteOne(Filters.regex("codename", ".*" + findInput + ".*", "i"));
                System.out.println();
                System.out.println(findInput + " has been deleted");
            }
        }
        sc.nextLine();
    }
}
