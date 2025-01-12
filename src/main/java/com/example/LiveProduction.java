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

public class LiveProduction {
    private String codename;
    private List<String> application;
    private Object monitor;
    private String cpu;
    private Object vga;

    public LiveProduction(String codename, List<String> application, Object monitor, String cpu, Object vga){
        this.codename = codename;
        this.application = application;
        this.monitor = monitor;
        this.cpu = cpu;
        this.vga = vga;
    }

    public void setCodename(String codename){this.codename = codename;}
    public String getCodename(){return this.codename;}
    public void setApplication(List<String> application){this.application = application;}
    public List<String> getApplication(){return this.application;}
    public void setMonitor(Object monitor){this.monitor = monitor;}
    public Object getMonitor(){return this.monitor;}
    public void setCPU(String cpu){this.cpu = cpu;}
    public String getCPU(){return this.cpu;}
    public void setVGA(Object vga){this.vga = vga;}
    public Object getVGA(){return this.vga;}

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

    public static List<LiveProduction> getAllLiveProductions(MongoCollection<Document> collectionLiveProduction) {
        List<LiveProduction> listLiveProduction = new ArrayList<>();
    
        for (Document print : collectionLiveProduction.find()) {
            String codename = print.getString("codename");
    
            List<?> rawApplication = print.get("application", List.class);
            List<String> application = new ArrayList<>();
            if (rawApplication != null) {
                for (Object obj : rawApplication) {
                    application.add(obj.toString());
                }
            }

            Object monitor = print.get("monitor");
            Object formattedMonitor = null;
            if (monitor instanceof List) {
                List<?> rawMonitor = (List<?>) monitor;
                List<String> monitorList = new ArrayList<>();
                for (Object obj : rawMonitor) {
                    monitorList.add(obj.toString());
                }
                formattedMonitor = monitorList;
            } else if (monitor instanceof String) {
                formattedMonitor = monitor.toString();
            }

            String cpu = print.getString("cpu");

            Object vga = print.get("vga");
            Object formattedVGA = null;
            if (vga instanceof List) {
                List<?> rawVGA = (List<?>) vga;
                List<String> vgaList = new ArrayList<>();
                for (Object obj : rawVGA) {
                    vgaList.add(obj.toString());
                }
                formattedVGA = vgaList;
            } else if (vga instanceof String) {
                formattedVGA = vga.toString();
            } 
    
            listLiveProduction.add(new LiveProduction(codename, application, formattedMonitor, cpu, formattedVGA));
        }
    
        return listLiveProduction;
    }

    public static void displayDataLiveProduction(MongoCollection<Document> collectionLiveProduction, Scanner sc){
        List<LiveProduction> listLiveProduction = LiveProduction.getAllLiveProductions(collectionLiveProduction);

        if (listLiveProduction.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }

        System.out.println();
        System.out.println("========= LIST OF LIVE PRODUCTION ==========");
        int currentIndex = 0;   

        while(true){
            LiveProduction currentLiveProduction = listLiveProduction.get(currentIndex);

            System.out.println();
            System.out.println("=============================================");
            System.out.println("CODENAME\t: " + currentLiveProduction.getCodename());
            System.out.println("Application\t: ");
                for(int i = 0; i < currentLiveProduction.getApplication().size();i++){
                    System.out.println("\t\t  " + currentLiveProduction.getApplication().get(i));
                }

            System.out.println("Monitor\t\t: ");
                if (currentLiveProduction.getMonitor() instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> monitorList = (List<String>) currentLiveProduction.getMonitor();
                    for (String monitor : monitorList) {
                        System.out.println("\t\t  " + monitor);
                    }
                } else if (currentLiveProduction.getMonitor() instanceof String) {
                    System.out.println("\t\t  " + currentLiveProduction.getMonitor());
                }  

            System.out.println("CPU\t\t: " + currentLiveProduction.getCPU());
        
            System.out.println("VGA\t\t: ");
                if (currentLiveProduction.getVGA() instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> VGAList = (List<String>) currentLiveProduction.getVGA();
                    for (String vga : VGAList) {
                        System.out.println("\t\t  " + vga);
                    }
                } else if (currentLiveProduction.getVGA() instanceof String) {
                    System.out.println("\t\t  " + currentLiveProduction.getVGA());
                }  
            System.out.println("=============================================");

            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");

            try {
                int choice = Integer.parseInt(sc.nextLine());

                switch(choice){
                    case 1 -> currentIndex = (currentIndex + 1) % listLiveProduction.size();
                    case 2 -> currentIndex = (currentIndex - 1 + listLiveProduction.size()) % listLiveProduction.size();
                    case 3 -> { return;}
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");           
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3");
            }
        }
    }

    public static void findDataLiveProduction(MongoCollection<Document> collectionLiveProduction, Scanner sc) {
        System.out.println();
        System.out.println("========= FIND LIVE PRODUCTION DATA =========");

        System.out.println();
        System.out.println("Input live production codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionLiveProduction.aggregate(pipeline);
    
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));

        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));

                System.out.println("Applications\t: ");
                Object applicationObj = found.get("application");
                @SuppressWarnings("unchecked")
                List<String> application = (List<String>) applicationObj;
                if(!application.isEmpty()){
                    for(int i = 0; i < application.size();i++){
                        System.out.println("\t\t  " + application.get(i));
                    }
                }

                System.out.println("Monitor\t\t:");
                Object monitorObj = found.get("monitor");
                if (monitorObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> monitor = (List<String>) monitorObj;
                    for (String mon : monitor) {
                        System.out.println("\t\t  " + mon);
                    }
                }
                else if (monitorObj instanceof String){
                    System.out.println("\t\t  " + found.getString("monitor"));
                }

                System.out.println("CPU\t: " + found.getString("cpu"));              

                System.out.println("VGA\t\t:");
                Object vgaObj = found.get("vga");
                if (vgaObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> vga = (List<String>) vgaObj;
                    for (String v : vga) {
                        System.out.println("\t\t  " + v);
                    }
                }
                else if (vgaObj instanceof String){
                    System.out.println("\t\t  " + found.getString("vga"));
                }
                System.out.println("=============================================");
            }
        }
        sc.nextLine();
    }

    public static void createDataLiveProduction(MongoCollection<Document> collectionLiveProduction, Scanner sc){
        System.out.println();
        System.out.println("======== ADD NEW LIVE PRODUCTION DATA =======");
        
        System.out.println();
        System.out.println("Input new live production codename");
        System.out.print("> ");
        String newCodename = sc.nextLine();

        System.out.println();
        System.out.println("Input new live production applications (type '0' to stop):");
        List<String> newApplicationList = inputList(sc);
        
        System.out.println();
        System.out.println("Input new live production monitors (type '0' to stop):");
        List<String> newMonitorList = inputList(sc);

        System.out.println();
        System.out.println("Input new live production CPU");
        System.out.print("> ");
        String newCPU = sc.nextLine();

        System.out.println();
        System.out.println("Input new live production VGA (type '0' to stop):");
        List<String> newVGAList = inputList(sc);

        LiveProduction newLiveProduction = new LiveProduction(newCodename, newApplicationList, newMonitorList, newCPU, newVGAList);
        
        Document createLiveProduction = new Document("codename", newLiveProduction.getCodename())
                                    .append("application", newLiveProduction.getApplication())
                                    .append("monitor", newLiveProduction.getMonitor())
                                    .append("cpu", newLiveProduction.getCPU())
                                    .append("vga", newLiveProduction.getVGA());
            collectionLiveProduction.insertOne(createLiveProduction);
            System.out.println();
            System.out.println(newLiveProduction.getCodename() + " has been added");
            sc.nextLine();
    }  
    
    public static void editDataLiveProduction(MongoCollection<Document> collectionLiveProduction, Scanner sc){
        System.out.println();
        System.out.println("========== EDIT LIVE PRODUCTION DATA ========");

        System.out.println();
        System.out.println("Input live production codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionLiveProduction.aggregate(pipeline);
    
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));


        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Codename",
                "\t2. Application",
                "\t3. Monitor",
                "\t4. CPU",
                "\t5. VGA",
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
                            System.out.println("\nInput new application (type '0' to stop):");
                            List<String> updApplicationList = inputList(sc);
                            updateField.append("application", updApplicationList);
                        }

                        case 3 -> {
                            System.out.println("\nInput new monitor (type '0' to stop):");
                            List<String> updMonitorList = inputList(sc);
                            updateField.append("monitor", updMonitorList);
                        }

                        case 4 -> {
                            System.out.println();
                            System.out.println("\tInput new CPU");
                            System.out.print("\t> ");
                            String updCPU = sc.nextLine();
                            updateField.append("codename", updCPU);
                        }

                        case 5 -> {
                            System.out.println("\nInput new VGA (type '0' to stop):");
                            List<String> updVGAList = inputList(sc);
                            updateField.append("vga", updVGAList);
                        }

                    }
                    Document update = result.first();
                    if(update != null){
                        collectionLiveProduction.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }
            } catch (InputMismatchException e){
                System.out.println("\tInvalid input. Please enter a number between 1 and \" + editOptions.length");
            }
        }
    }

    public static void deleteDataLiveProduction(MongoCollection<Document> collectionLiveProduction, Scanner sc){
        System.out.println();
        System.out.println("======== DELETE LIVE PRODUCTION DATA ========");

        System.out.println();
        System.out.println("Input live production codename");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionLiveProduction.aggregate(pipeline);
    
        pipeline.add(Aggregates.match(Filters.regex("codename", ".*" + findInput + ".*", "i")));


        if(!result.iterator().hasNext()){
            System.out.println("No data found with codename : " + findInput);
        }
        else{
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("CODENAME\t: " + found.getString("codename"));

                System.out.println("Applications\t: ");               
                Object applicationObj = found.get("application");
                @SuppressWarnings("unchecked")
                List<String> application = (List<String>) applicationObj;
                if(!application.isEmpty()){
                    for(int i = 0; i < application.size();i++){
                        System.out.println("\t\t  " + application.get(i));
                    }
                }

                System.out.println("Monitor\t\t:");
                Object monitorObj = found.get("monitor");
                if (monitorObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> monitor = (List<String>) monitorObj;
                    for (String mon : monitor) {
                        System.out.println("\t\t  " + mon);
                    }
                }
                else if (monitorObj instanceof String){
                    System.out.println("\t\t  " + found.getString("monitor"));
                }

                System.out.println("CPU\t\t: " + found.getString("cpu"));              

                System.out.println("VGA\t\t:");
                Object vgaObj = found.get("vga");
                if (vgaObj instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<String> vga = (List<String>) vgaObj;
                    for (String v : vga) {
                        System.out.println("\t\t  " + v);
                    }
                }
                else if (vgaObj instanceof String){
                    System.out.println("\t\t  " + found.getString("vga"));
                }

                System.out.println("=============================================");
            }

            System.out.println();
            System.out.println(findInput + "'s data found");
            System.out.println("Are you sure want to delete this live production data? (y/n)");
            System.out.print("> ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                collectionLiveProduction.deleteOne(Filters.regex("codename", ".*" + findInput + ".*", "i"));
                System.out.println();
                System.out.println(findInput + " has been deleted");
            }
        }
        sc.nextLine();
    }
}
