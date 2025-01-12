package com.example;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

public class Fulltimer extends AbsUser{

    private int nif;
    private Date tanggalMulaiBekerja;

    public Fulltimer(String namaLengkap, int nif){
        super(namaLengkap, null, null, null, null, 0);
        this.nif = nif;
    }

    public Fulltimer(String namaLengkap, Date tanggalLahir, String noTelp, String alamat, String email, int age, int nif, Date tanggalMulaiBekerja) {
        super(namaLengkap, tanggalLahir, noTelp, alamat, email, 0);
        this.nif = nif;
        this.tanggalMulaiBekerja = tanggalMulaiBekerja;
    }
    
    public void setNIF (int nif) {this.nif = nif;}
    public int getNIF() {return this.nif;}
    public void setTanggalMulaiBekerja(Date tanggalMulaiBekerja) {this.tanggalMulaiBekerja = tanggalMulaiBekerja;}
    public Date getTanggalMulaiBekerja() {return this.tanggalMulaiBekerja;}

    public static boolean login(MongoCollection<Document> collectionFulltimer, String namaLengkap ,int nif) {     
        Document find = new Document("nama_lengkap", namaLengkap).append("nif", nif);
        Document result = collectionFulltimer.find(find).first();
        return result != null;
    }
    
    public static List<Fulltimer> getAllFulltimers(MongoCollection<Document> collection) {
        List<Fulltimer> listFulltimers = new ArrayList<>();
        for (Document print : collection.find()) {
            String namaLengkap = print.getString("nama_lengkap");
            int nif = print.getInteger("nif");
            Date tanggalLahir = print.getDate("tgl_lahir");
            String noTelp = print.getString("no_telp");
            String alamat = print.getString("alamat");
            Date tanggalMulaiBekerja = print.getDate("tgl_mulai_bekerja");
            String email = print.getString("email");
            int age = print.getInteger("age");
            listFulltimers.add(new Fulltimer(namaLengkap,tanggalLahir,noTelp,alamat,email,age,nif,tanggalMulaiBekerja));
        }
        return listFulltimers;
    }
    
    public static void displayDataFulltimer(MongoCollection<Document> collectionFulltimer, Scanner sc) {
        List<Fulltimer> listFulltimers = Fulltimer.getAllFulltimers(collectionFulltimer);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");    

        if (listFulltimers.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }
        
        System.out.println();
        System.out.println("============ LIST OF FULLTIMERS =============");
        int currentIndex = 0;
        while (true) {
                
            Fulltimer currentFulltimer = listFulltimers.get(currentIndex);
            LocalDate dob = currentFulltimer.getTanggalLahir().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dow = currentFulltimer.getTanggalMulaiBekerja().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println();
            System.out.println("=============================================");
            System.out.println("Name\t\t: " + currentFulltimer.getNamaLengkap());
            System.out.println("NIF\t\t: " + currentFulltimer.getNIF());   
            System.out.println("DOB\t\t: " + dob.format(dateFormatter));
            System.out.println("Phone Number\t: " + currentFulltimer.getNoTelp());
            System.out.println("Address\t\t: " + currentFulltimer.getAlamat());
            System.out.println("Date of Work\t: " + dow.format(dateFormatter));
            System.out.println("Email\t\t: " + currentFulltimer.getEmail());
            System.out.println("Age\t\t: " + currentFulltimer.getAge());
            System.out.println("=============================================");
    
            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");
    
        try {
            int choice = Integer.parseInt(sc.nextLine());
    
            switch(choice) {
                case 1 -> currentIndex = (currentIndex + 1) % listFulltimers.size();
                case 2 -> currentIndex = (currentIndex - 1 + listFulltimers.size()) % listFulltimers.size();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                } 
            } catch (NumberFormatException e) {    
                System.out.println("Invalid choice, please enter 1,2, or 3");
            }
        }
    }
        
    public static void findDataFulltimer(MongoCollection<Document> collectionFulltimer, Scanner sc) {
        System.out.println();
        System.out.println("============ FIND FULLTIMER DATA ============");

        System.out.println();
        System.out.println("Input fulltimer name or NIF");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionFulltimer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nif", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIF : " + findInput);
        }
        else{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            for(Document found : result){
                
                Date dob = found.getDate("tgl_lahir");
                Date dow = found.getDate("tgl_mulai_bekerja");

                System.out.println();
                System.out.println("=============================================");
                System.out.println("Name\t\t: " + found.getString("nama_lengkap"));
                System.out.println("NIF\t\t: " + found.getInteger("nif"));   
                System.out.println("DOB\t\t: " + dateFormatter.format(dob));
                System.out.println("Phone Number\t: " + found.getString("no_telp"));
                System.out.println("Address\t\t: " + found.getString("alamat"));
                System.out.println("Date of Work\t: " + dateFormatter.format(dow));
                System.out.println("Email\t\t: " + found.getString("email"));
                System.out.println("Age\t\t: " + found.getInteger("age"));
                System.out.println("=============================================");
                }
            }
            sc.nextLine();
        }
    
    public static void createDataFulltimer(MongoCollection<Document> collectionFulltimer, Scanner sc) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("=========== ADD NEW FULLTIMER DATA ==========");

        System.out.println();
        System.out.println("Input new fulltimer name");
        System.out.print("> ");
        String newNamaLengkap = sc.nextLine();

        System.out.println();
        System.out.println("Input new fulltimer NIF");
        System.out.print("> ");
        int newNIF = sc.nextInt();
        sc.nextLine();

        System.out.println();
        System.out.println("Input new fulltimer date of birth (dd-MM-yyyy)");
        System.out.print("> ");
        String newTanggalLahirInput = sc.nextLine(); 
        LocalDate newTanggalLahir = LocalDate.parse(newTanggalLahirInput, dateFormatter);
        Date newTanggalLahirFixed = Date.from(newTanggalLahir.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        System.out.println();
        System.out.println("Input new fulltimer phone number");
        System.out.print("> ");
        String newNoTelp = sc.nextLine();
        
        System.out.println();
        System.out.println("Input new fulltimer address");
        System.out.print("> ");
        String newAlamat = sc.nextLine();
        
        System.out.println();
        System.out.println("Input new fulltimer date of work (dd-MM-yyyy)");
        System.out.print("> ");
        String newTanggalMulaiBekerjaInput = sc.nextLine(); 
        LocalDate newTanggalMulaiBekerja = LocalDate.parse(newTanggalMulaiBekerjaInput, dateFormatter);
        Date newTanggalMulaiBekerjaFixed = Date.from(newTanggalMulaiBekerja.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        System.out.println();
        System.out.println("Input new fulltimer email");
        System.out.print("> ");
        String newEmail = sc.nextLine();
    
        int newAge = Period.between(newTanggalLahir, LocalDate.now()).getYears();
   
        Fulltimer newFulltimer = new Fulltimer(newNamaLengkap,newTanggalLahirFixed,newNoTelp,newAlamat,newEmail,newAge,newNIF,newTanggalMulaiBekerjaFixed);
    
        Document createFulltimer = new Document("nif", newFulltimer.getNIF())
                                    .append("nama_lengkap", newFulltimer.getNamaLengkap())
                                    .append("tgl_lahir", newFulltimer.getTanggalLahir())
                                    .append("no_telp", newFulltimer.getNoTelp())
                                    .append("alamat", newFulltimer.getAlamat())
                                    .append("tgl_mulai_bekerja", newFulltimer.getTanggalMulaiBekerja())
                                    .append("email", newFulltimer.getEmail())
                                    .append("age", newFulltimer.getAge());
            collectionFulltimer.insertOne(createFulltimer);
            System.out.println();
            System.out.println(newFulltimer.getNamaLengkap() + " has been added");
            System.out.println();
            System.out.println("Welcome Home, " + newFulltimer.getNamaLengkap());
            sc.nextLine();
        }

    public static void editDataFulltimer(MongoCollection<Document> collectionFulltimer, Scanner sc) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("============= EDIT FULLTIMER DATA ===========");

        System.out.println();
        System.out.println("Input fulltimer name or NIF");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionFulltimer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nif", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIF : " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Name",
                "\t2. NIF",
                "\t3. DOB",
                "\t4. Phone Number",
                "\t5. Address",
                "\t6. Date of Work",
                "\t7. Email",
            };
            for (String option : editOptions) {
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
                            System.out.println("\tInput new name");
                            System.out.print("\t> ");
                            String updNamaLengkap = sc.nextLine();
                            updateField.append("nama_lengkap", updNamaLengkap);
                        }
                        
                        case 2 -> {
                            System.out.println();
                            System.out.println("\tInput new NIF");
                            System.out.print("\t> ");
                            int updNIF =  sc.nextInt();
                            updateField.append("nif", updNIF);
                        }

                        case 3 -> {
                            System.out.println();
                            System.out.println("\tInput new date of birth (dd-MM-yyyy)");
                            System.out.print("\t> ");
                            String updTanggalLahirInput = sc.nextLine();
                            LocalDate updTanggalLahir = LocalDate.parse(updTanggalLahirInput, dateFormatter); 
                            Date updTanggalLahirFixed = Date.from(updTanggalLahir.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            updateField.append("tgl_lahir", updTanggalLahirFixed);
                        }

                        case 4 -> {
                            System.out.println();
                            System.out.println("\tInput new phone number");
                            System.out.print("\t> ");
                            String updNoTelp = sc.nextLine();
                            updateField.append("no_telp", updNoTelp);
                        }
                        
                        case 5 -> {
                            System.out.println();
                            System.out.println("\tInput new address");
                            System.out.print("\t> ");
                            String updAlamat = sc.nextLine();
                            updateField.append("alamat", updAlamat);
                        }

                        case 6 -> {
                            System.out.println();
                            System.out.println("\tInput new date of work (dd-MM-yyyy)");
                            System.out.print("\t> ");
                            String updTanggalMulaiBekerjaInput = sc.nextLine();
                            LocalDate updTanggalMulaiBekerja = LocalDate.parse(updTanggalMulaiBekerjaInput, dateFormatter);
                            Date updTanggalMulaiBekerjaFixed = Date.from(updTanggalMulaiBekerja.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            updateField.append("tgl_mulai_bekerja", updTanggalMulaiBekerjaFixed);
                        }

                        case 7 -> {
                            System.out.println();
                            System.out.println("\tInput new email");
                            System.out.print("\t> ");
                            String updEmail = sc.nextLine();
                            updateField.append("email", updEmail);
                        }
                    }
                    Document update = result.first();
                    if(update != null){
                        collectionFulltimer.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }  
            } catch (InputMismatchException e) {
                System.out.println("\tInvalid input. Please enter a number between 1 and " + editOptions.length);
            }
            sc.nextLine();
        }
    }

    public static void deleteDataFulltimer(MongoCollection<Document> collectionFulltimer, Scanner sc) {
        System.out.println();
        System.out.println("=========== DELETE FULLTIMER DATA ===========");

        System.out.println();
        System.out.println("Input fulltimer name or NIF");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionFulltimer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nif", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIF : " + findInput);
        }
        else{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            for(Document found : result){
                
                Date dob = found.getDate("tgl_lahir");
                Date dow = found.getDate("tgl_mulai_bekerja");

                System.out.println("=============================================");
                System.out.println("Name\t\t: " + found.getString("nama_lengkap"));
                System.out.println("NIF\t\t: " + found.getInteger("nif"));   
                System.out.println("DOB\t\t: " + dateFormatter.format(dob));
                System.out.println("Phone Number\t: " + found.getString("no_telp"));
                System.out.println("Address\t\t: " + found.getString("alamat"));
                System.out.println("Date of Work\t: " + dateFormatter.format(dow));
                System.out.println("Email\t\t: " + found.getString("email"));
                System.out.println("Age\t\t: " + found.getInteger("age"));
                System.out.println("=============================================");
                }
        }

        System.out.println();
        System.out.println(findInput + "'s data found");
        System.out.println("Are you sure want to delete this fulltimer data? (y/n)");
        System.out.print("> ");
        String confirm = sc.nextLine();

        if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
            System.out.println("What is the reason " + findInput + " left the team?");
            System.out.print("> ");
            sc.nextLine();

            if(findInput.matches("\\d+")){
                collectionFulltimer.deleteOne(Filters.eq("nif", Integer.valueOf(findInput)));
            }
            else{
                collectionFulltimer.deleteOne(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i"));
            }
            System.out.println();
            System.out.println("Data has been deleted");
            System.out.println("We feel sorry to see you go, " + findInput);
        }
        sc.nextLine();
    } 
}
