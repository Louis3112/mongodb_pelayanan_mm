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

public class Volunteer extends AbsUser{
    private int nij;
    private Date tanggalMulaiVolunteer;

    public Volunteer(String namaLengkap, Date tanggalLahir, String noTelp, String alamat, String email, int age, int nij, Date tanggalMulaiVolunteer) {
        super(namaLengkap, tanggalLahir, noTelp, alamat, email, 0);
        this.nij = nij;
        this.tanggalMulaiVolunteer = tanggalMulaiVolunteer;
    }

    public void setNIJ (int nij) {this.nij = nij;}
    public int getNIJ() {return this.nij;}
    public void setTanggalMulaiVolunteer(Date tanggalMulaiVolunteer) {this.tanggalMulaiVolunteer = tanggalMulaiVolunteer;}
    public Date getTanggalMulaiVolunteer() {return this.tanggalMulaiVolunteer;}

    public static List<Volunteer> getAllVolunteers(MongoCollection<Document> collectionVolunteer) {
        List<Volunteer> listVolunteers = new ArrayList<>();
        for (Document print : collectionVolunteer.find()) {
            String namaLengkap = print.getString("nama_lengkap");
            int nij = print.getInteger("nij");
            Date tanggalLahir = print.getDate("tgl_lahir");
            String noTelp = print.getString("no_telp");
            String alamat = print.getString("alamat");
            Date tanggalMulaiVolunteer = print.getDate("tgl_mulai_volunteer");
            String email = print.getString("email");
            int age = print.getInteger("age");
            listVolunteers.add(new Volunteer(namaLengkap,tanggalLahir,noTelp,alamat,email,age,nij,tanggalMulaiVolunteer));
        }
        return listVolunteers;
    }

    public static void displayDataVolunteer(MongoCollection<Document> collectionVolunteer, Scanner sc) {
        List<Volunteer> listVolunteers = Volunteer.getAllVolunteers(collectionVolunteer);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");    

        if (listVolunteers.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }

        System.out.println();
        System.out.println("============ LIST OF VOLUNTEERS =============");
        int currentIndex = 0;
        while (true) {
                
            Volunteer currentVolunteer = listVolunteers.get(currentIndex);
            LocalDate dob = currentVolunteer.getTanggalLahir().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate dov = currentVolunteer.getTanggalMulaiVolunteer().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            System.out.println();
            System.out.println("=============================================");
            System.out.println("Name\t\t     : " + currentVolunteer.getNamaLengkap());
            System.out.println("NIJ\t\t     : " + currentVolunteer.getNIJ());   
            System.out.println("DOB\t\t     : " + dob.format(dateFormatter));
            System.out.println("Phone Number\t     : " + currentVolunteer.getNoTelp());
            System.out.println("Address\t\t     : " + currentVolunteer.getAlamat());
            System.out.println("Date of Volunteer    : " + dov.format(dateFormatter));
            System.out.println("Email\t\t     : " + currentVolunteer.getEmail());
            System.out.println("Age\t\t     : " + currentVolunteer.getAge());
            System.out.println("=============================================");
    
            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");
    
        try {
            int choice = Integer.parseInt(sc.nextLine());
    
            switch(choice) {
                case 1 -> currentIndex = (currentIndex + 1) % listVolunteers.size();
                case 2 -> currentIndex = (currentIndex - 1 + listVolunteers.size()) % listVolunteers.size();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");
            } 
            } catch (NumberFormatException e) {    
                System.out.println("Invalid choice, please enter 1,2, or 3");
            }
        }
    }

    public static void findDataVolunteer(MongoCollection<Document> collectionVolunteer, Scanner sc) {
        System.out.println();
        System.out.println("============ FIND VOLUNTEER DATA ============");

        System.out.println();
        System.out.println("Input volunteer name or NIJ");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionVolunteer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nij", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIJ : " + findInput);
        }
        else{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            for(Document found : result){
                
                Date dob = found.getDate("tgl_lahir");
                Date dov = found.getDate("tgl_mulai_volunteer");

                System.out.println();
                System.out.println("=============================================");
                System.out.println("Name\t\t     : " + found.getString("nama_lengkap"));
                System.out.println("NIJ\t\t     : " + found.getInteger("nij"));   
                System.out.println("DOB\t\t     : " + dateFormatter.format(dob));
                System.out.println("Phone Number\t     : " + found.getString("no_telp"));
                System.out.println("Address\t\t     : " + found.getString("alamat"));
                System.out.println("Date of Volunteer    : " + dateFormatter.format(dov));
                System.out.println("Email\t\t     : " + found.getString("email"));
                System.out.println("Age\t\t     : " + found.getInteger("age"));
                System.out.println("=============================================");
            }
        }
        sc.nextLine();
    }
    
    public static void createDataVolunteer(MongoCollection<Document> collectionVolunteer, Scanner sc) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("=========== ADD NEW VOLUNTEER DATA ==========");
        
        System.out.println();
        System.out.println("Input new volunteer name");
        System.out.print("> ");
        String newNamaLengkap = sc.nextLine();

        System.out.println();
        System.out.println("Input new volunteer NIJ");
        System.out.print("> ");
        int newNIJ = sc.nextInt();
        sc.nextLine();

        System.out.println();
        System.out.println("Input new volunteer date of birth (dd-MM-yyyy)");
        System.out.print("> ");
        String newTanggalLahirInput = sc.nextLine(); 
        LocalDate newTanggalLahir = LocalDate.parse(newTanggalLahirInput, dateFormatter);
        Date newTanggalLahirFixed = Date.from(newTanggalLahir.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        System.out.println();
        System.out.println("Input new volunteer phone number");
        System.out.print("> ");
        String newNoTelp = sc.nextLine();
        
        System.out.println();
        System.out.println("Input new volunteer address");
        System.out.print("> ");
        String newAlamat = sc.nextLine();
        
        System.out.println();
        System.out.println("Input new volunteer date of work (dd-MM-yyyy)");
        System.out.print("> ");
        String newTanggalMulaiVolunteerInput = sc.nextLine(); 
        LocalDate newTanggalMulaiVolunteer = LocalDate.parse(newTanggalMulaiVolunteerInput, dateFormatter);
        Date newTanggalMulaiVolunteerFixed = Date.from(newTanggalMulaiVolunteer.atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        System.out.println();
        System.out.println("Input new volunteer email");
        System.out.print("> ");
        String newEmail = sc.nextLine();
    
        int newAge = Period.between(newTanggalLahir, LocalDate.now()).getYears();
   
        Volunteer newVolunteer = new Volunteer(newNamaLengkap,newTanggalLahirFixed,newNoTelp,newAlamat,newEmail,newAge,newNIJ,newTanggalMulaiVolunteerFixed);
    
        Document createVolunteer = new Document("nij", newVolunteer.getNIJ())
                                    .append("nama_lengkap", newVolunteer.getNamaLengkap())
                                    .append("tgl_lahir", newVolunteer.getTanggalLahir())
                                    .append("no_telp", newVolunteer.getNoTelp())
                                    .append("alamat", newVolunteer.getAlamat())
                                    .append("tgl_mulai_volunteer", newVolunteer.getTanggalMulaiVolunteer())
                                    .append("email", newVolunteer.getEmail())
                                    .append("age", newVolunteer.getAge());
            collectionVolunteer.insertOne(createVolunteer);
            System.out.println();
            System.out.println(newVolunteer.getNamaLengkap() + " has been added");
            System.out.println();
            System.out.println("Welcome Home, " + newVolunteer.getNamaLengkap());
            sc.nextLine();
    }   
    
    public static void editDataVolunteer(MongoCollection<Document> collectionVolunteer, Scanner sc) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("============= EDIT VOLUNTEER DATA ===========");

        System.out.println();
        System.out.println("Input volunteer name or NIJ");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionVolunteer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nij", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIJ : " + findInput);
        }
        else{
            System.out.println("\tWhich data do you want to update? ");
            String[] editOptions = {
                "\t1. Name",
                "\t2. NIJ",
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
                            System.out.println("\tInput new NIJ");
                            System.out.print("\t> ");
                            int updNIJ =  sc.nextInt();
                            updateField.append("nij", updNIJ);
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
                            System.out.println("\tInput new date of volunteer (dd-MM-yyyy)");
                            System.out.print("\t> ");
                            String updTanggalMulaiVolunteerInput = sc.nextLine();
                            LocalDate updTanggalMulaiVolunteer = LocalDate.parse(updTanggalMulaiVolunteerInput, dateFormatter);
                            Date updTanggalMulaiVolunteerFixed = Date.from(updTanggalMulaiVolunteer.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            updateField.append("tgl_mulai_volunteer", updTanggalMulaiVolunteerFixed);
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
                        collectionVolunteer.updateOne(update, new Document("$set",updateField));
                        System.out.println();
                        System.out.println("\tData of " + findInput + " has been updated successfully");
                    }  
            } catch (InputMismatchException e) {
                System.out.println("\tInvalid input. Please enter a number between 1 and " + editOptions.length);
            }
        }
    }

    public static void deleteDataVolunteer(MongoCollection<Document> collectionVolunteer, Scanner sc) {
        System.out.println();
        System.out.println("=========== DELETE VOLUNTEER DATA ===========");

        System.out.println();
        System.out.println("Input volunteer name or NIJ");
        System.out.print("> ");
        String findInput = sc.nextLine();

        List<Bson> pipeline = new ArrayList<>();
        AggregateIterable<Document> result = collectionVolunteer.aggregate(pipeline);
    
        if(findInput.matches("\\d+")){
            pipeline.add(Aggregates.match(Filters.eq("nij", Integer.valueOf(findInput))));
        }
        else{
            pipeline.add(Aggregates.match(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i")));
        }

        if(!result.iterator().hasNext()){
            System.out.println("No data found with name or NIJ : " + findInput);
        }
        else{
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
            for(Document found : result){
                
                Date dob = found.getDate("tgl_lahir");
                Date dov = found.getDate("tgl_mulai_volunteer");

                System.out.println("=============================================");
                System.out.println("Name\t\t     : " + found.getString("nama_lengkap"));
                System.out.println("NIJ\t\t     : " + found.getInteger("nij"));   
                System.out.println("DOB\t\t     : " + dateFormatter.format(dob));
                System.out.println("Phone Number\t     : " + found.getString("no_telp"));
                System.out.println("Address\t\t     : " + found.getString("alamat"));
                System.out.println("Date of Volunteer    : " + dateFormatter.format(dov));
                System.out.println("Email\t\t     : " + found.getString("email"));
                System.out.println("Age\t\t     : " + found.getInteger("age"));
                System.out.println("=============================================");
                }
        }

        System.out.println();
        System.out.println(findInput + "'s data found");
        System.out.println("Are you sure want to delete this volunteer data? (y/n)");
        System.out.print("> ");
        String confirm = sc.nextLine();

        if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
            System.out.println("What is the reason " + findInput + " left the team?");
            System.out.print("> ");
            sc.nextLine();

            if(findInput.matches("\\d+")){
                collectionVolunteer.deleteOne(Filters.eq("nij", Integer.valueOf(findInput)));
            }
            else{
                collectionVolunteer.deleteOne(Filters.regex("nama_lengkap", ".*" + findInput + ".*", "i"));
            }
            System.out.println();
            System.out.println("Data has been deleted");
            System.out.println("We feel sorry to see you go, " + findInput);
        }
        sc.nextLine();
    }
}
