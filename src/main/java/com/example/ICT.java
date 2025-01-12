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

public class ICT {
    private String codename;
    private String email;
    private String posisi;
    private List<String> bhsPemrograman;
    private Object project;

    public ICT(String codename, String email, String posisi, List<String> bhsPemrograman, Object project){
        this.codename = codename;
        this.email = email;
        this.posisi = posisi;
        this.bhsPemrograman = bhsPemrograman;
        this.project = project;
    }
    
    public void setCodename(String codename){this.codename = codename;}
    public String getCodename(){return this.codename;}
    public void setEmail(String email){this.email = email;}
    public String getEmail(){return this.email;}
    public void setPosisi(String posisi){this.posisi = posisi;}
    public String getPosisi(){return this.posisi;}
    public void setBhsPemrograman(List<String> bhs_pemrograman){this.bhsPemrograman = bhs_pemrograman;}
    public List<String> getBhsPemrograman(){return this.bhsPemrograman;}
    public void setProject(Object project){this.project = project;}
    public Object getProject(){return this.project;}

    private static List<String> inputList(Scanner sc) {
        List<String> list = new ArrayList<>();
        while (true) {
            System.out.print("> ");
            String input = sc.nextLine().trim();
            if (input.equals("0")) {
                break;
            }
            if (!input.isEmpty()) {
                list.add(input);
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
        return list;
    }

    public static List<ICT> getAllICT(MongoCollection<Document> collectionICT) {
        List<ICT> listICT = new ArrayList<>();
        for (Document print : collectionICT.find()) {
            String codename = print.getString("codename");
            String email = print.getString("email");
            String posisi = print.getString("posisi");

            List<?> rawBhsPemrograman = print.get("bhs_pemrograman", List.class);
            List<String> bhsPemrograman = new ArrayList<>();
            if (rawBhsPemrograman != null) {
                for (Object obj : rawBhsPemrograman) {
                    bhsPemrograman.add(obj.toString());
                }
            }

            Object project = print.get("project");
            Object formattedProject = null;
            if (project instanceof List) {
                List<?> rawProject = (List<?>) project;
                List<String> projectList = new ArrayList<>();
                for (Object obj : rawProject) {
                    projectList.add(obj.toString());
                }
                formattedProject= projectList;
            } else if (project instanceof String) {
                formattedProject = project.toString();
            }    
            listICT.add(new ICT(codename, email, posisi, bhsPemrograman, formattedProject));
        }
    
        return listICT;
    }

    public static void displayDataICT(MongoCollection<Document> collectionICT, Scanner sc){
        List<ICT> listICT = ICT.getAllICT(collectionICT);

        if (listICT.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }

        System.out.println();
        System.out.println("=============== LIST OF ICT ================");
        int currentIndex = 0;   

        while(true){
            ICT currentICT = listICT.get(currentIndex);

            System.out.println();
            System.out.println("=============================================");
            System.out.println("CODENAME\t: " + currentICT.getCodename());
            System.out.println("Email\t\t: " + currentICT.getEmail());
            System.out.println("Position\t: " + currentICT.getPosisi());
            System.out.println("Languages\t: ");
                for(int i = 0; i < currentICT.getBhsPemrograman().size();i++){
                    System.out.println("\t\t  " + currentICT.getBhsPemrograman().get(i));
                }

            System.out.println("Project\t\t: ");
                if (currentICT.getProject() instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> projectList = (List<String>) currentICT.getProject();
                    for (String project : projectList) {
                        System.out.println("\t\t  " + project);
                    }
                } else if (currentICT.getProject() instanceof String) {
                    System.out.println("\t\t  " + currentICT.getProject());
                }  
            System.out.println("=============================================");

            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch(choice){
                    case 1 -> currentIndex = (currentIndex + 1) % listICT.size();
                    case 2 -> currentIndex = (currentIndex - 1 + listICT.size()) % listICT.size();
                    case 3 -> { return;}
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");           
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3");
            }
        }
    }

    public static void findDataICT(MongoCollection<Document> collectionICT, Scanner sc) {
        System.out.println();
        System.out.println("=============== FIND ICT DATA ===============");
        System.out.println();
        System.out.println("Input ICT codename or email");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionICT.aggregate(pipeline);
    
        if(findInput.toLowerCase().endsWith("com")){
            pipeline.add(Aggregates.match(Filters.eq("email", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                
                System.out.println("Email\t\t: " + found.getString("email"));
                System.out.println("Position\t: " + found.getString("posisi"));

                System.out.println("Languages\t: ");
                Object BhsPemrogramanObj = found.get("bhs_pemrograman");
                @SuppressWarnings("unchecked")
                List<String> bhsPemrograman = (List<String>) BhsPemrogramanObj;
                if(!bhsPemrograman.isEmpty()){

                    for(int i = 0; i < bhsPemrograman.size();i++){
                        System.out.println("\t\t  " + bhsPemrograman.get(i));
                    }
                }

                System.out.println("Project\t\t:");
                Object projectObj = found.get("project");
                if (projectObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> project = (List<String>) projectObj;
                    for (String p : project) {
                        System.out.println("\t\t  " + p);
                    }
                }
                else if (projectObj instanceof String){
                    System.out.println("\t\t  " + found.getString("project"));
                }
                System.out.println("=============================================");
            }
        }
        sc.nextLine();
    }

    public static void createDataICT(MongoCollection<Document> collectionICT, MongoCollection<Document> collectionFulltimer, MongoCollection<Document> collectionVolunteer, Scanner sc){
        System.out.println();
        System.out.println("============== ADD NEW ICT DATA =============");

        String newEmail = null;
        while (true) { 
            System.out.println();
            System.out.println("Input new ICT email");
            System.out.print("> ");
            newEmail = sc.nextLine();
    
            Bson emailFilter = Filters.eq("email", newEmail);
            boolean isEmailRegistered = collectionFulltimer.find(emailFilter).iterator().hasNext()
                                    || collectionVolunteer.find(emailFilter).iterator().hasNext();
    
            if (isEmailRegistered) {
                break;
            } else {
                System.out.println("The email \"" + newEmail + "\" is not registered in Fulltimer or Volunteer collection");
            }
        }

        System.out.println();
        System.out.println("Input new ICT codename");
        System.out.print("> ");
        String newCodename = sc.nextLine();

        System.out.println();
        System.out.println("Input new ICT position");
        System.out.print("> ");
        String newPosition = sc.nextLine();

        System.out.println();
        System.out.println("Input new programming languages (type '0' to stop)");
        List<String> newBhsPemrogramanList = inputList(sc);

        System.out.println();
        System.out.println("Input new project (type '0' to stop)");
        List<String> newProject = inputList(sc);

        ICT newICT = new ICT(newCodename, newEmail, newPosition, newBhsPemrogramanList, newProject);
        
        Document createICT = new Document("codename", newICT.getCodename())
                                    .append("email", newICT.getEmail())
                                    .append("posisi", newICT.getPosisi())
                                    .append("bhs_pemrograman", newICT.getBhsPemrograman())
                                    .append("project", newICT.getProject());
            collectionICT.insertOne(createICT);
            System.out.println();
            System.out.println(newICT.getCodename() + " has been added");
            sc.nextLine();
    } 

    public static void editDataICT(MongoCollection<Document> collectionICT, Scanner sc){
        System.out.println();
        System.out.println("=============== EDIT ICT DATA ===============");
        System.out.println();
        System.out.println("Input ICT codename or email");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionICT.aggregate(pipeline);
        
        if(findInput.toLowerCase().endsWith("com")){
            pipeline.add(Aggregates.match(Filters.eq("email", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename or email : " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Codename",
                "\t2. Email",
                "\t3. Position",
                "\t4. Programming languages",
                "\t5. Project",
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
                            System.out.println("\nInput new email:");
                            System.out.print("\t> ");
                            String updEmail = sc.nextLine();
                            updateField.append("email", updEmail);
                        }

                        case 3 -> {
                            System.out.println("\nInput new position:");
                            System.out.print("\t> ");
                            String updPosition = sc.nextLine();
                            updateField.append("posisi", updPosition);
                        }

                        case 4 -> {
                            System.out.println("\nInput new programming languages (type '0' to stop):");
                            List<String> updLanguageList = inputList(sc);
                            updateField.append("bhs_pemrograman", updLanguageList);
                        }

                        case 5 -> {
                            System.out.println("\nInput new project(type '0' to stop):");
                            List<String> updProjectList = inputList(sc);
                            updateField.append("project", updProjectList);
                        }

                    }
                    Document update = result.first();
                    if(update != null){
                        collectionICT.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }
            } catch (InputMismatchException e){
                System.out.println("\tInvalid input. Please enter a number between 1 and \" + editOptions.length");
            }
        }
    }

    public static void deleteDataICT(MongoCollection<Document> collectionICT, Scanner sc){
        System.out.println();
        System.out.println("============== DELETE ICT DATA ==============");
        System.out.println();
        System.out.println("Input ICT codename or email");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionICT.aggregate(pipeline);
        
        if(findInput.toLowerCase().endsWith("com")){
            pipeline.add(Aggregates.match(Filters.eq("email", findInput)));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename or email : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));
                System.out.println("Email\t\t: " + found.getString("email"));
                System.out.println("Position\t: " + found.getString("posisi"));
                System.out.println("Languages\t: ");
                Object BhsPemrogramanObj = found.get("bhs_pemrograman");
                @SuppressWarnings("unchecked")
                List<String> bhsPemrograman = (List<String>) BhsPemrogramanObj;
                if(!bhsPemrograman.isEmpty()){
                    for(int i = 0; i < bhsPemrograman.size();i++){
                        System.out.println("\t\t  " + bhsPemrograman.get(i));
                    }
                }

                System.out.println("Project\t\t: ");
                Object projectObj = found.get("project");
                if (projectObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> project = (List<String>) projectObj;
                    for (String p : project) {
                        System.out.println("\t\t  " + p);
                    }
                }
                else if (projectObj instanceof String){
                    System.out.println("\t\t  " + found.getString("project"));
                }

                System.out.println("=============================================");
            }

            System.out.println();
            System.out.println(findInput + "'s data found");
            System.out.println("Are you sure want to delete this live production data? (y/n)");
            System.out.print("> ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                System.out.println("What is the reason " + findInput + " left the team?");
                System.out.print("> ");
                sc.nextLine();
    
                if(findInput.toLowerCase().endsWith("com")){
                    collectionICT.deleteOne(Filters.eq("email", findInput));
                }
                else{
                    collectionICT.deleteOne(Filters.regex("codename", ".*" + findInput + ".*", "i"));
                }
                System.out.println();
                System.out.println("Data has been deleted");
                System.out.println("We feel sorry to see you go, " + findInput);
            }
        }
        sc.nextLine();
    }
}




