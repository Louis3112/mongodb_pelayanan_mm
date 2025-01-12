package com.example;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class JadwalPelayanan{
    private Date serviceDate;
    private String directCam;
    private String cam1;
    private String cam2;
    private String cam3;
    private String cam4;
    private String cam5;
    private String cam6;
    private String cam7;
    private String cam8;
    private String cam9;
    private String tsMainhall1;
    private String tsMainhall2;
    private String directIMAG;
    private String resolume;
    private String propress;
    private String directBroadcast;
    private String camBroadcast;
    private String tsBroadcast1;
    private String tsBroadcast2;

    public JadwalPelayanan(Date serviceDate, String directCam, String cam1, String cam2, String cam3, String cam4, 
                           String cam5, String cam6, String cam7, String cam8, String cam9, String tsMainhall1, 
                           String tsMainhall2, String directIMAG, String resolume, String propress, 
                           String directBroadcast, String camBroadcast, String tsBroadcast1, String tsBroadcast2) {
        this.serviceDate = serviceDate;
        this.directCam = directCam;
        this.cam1 = cam1;
        this.cam2 = cam2;
        this.cam3 = cam3;
        this.cam4 = cam4;
        this.cam5 = cam5;
        this.cam6 = cam6;
        this.cam7 = cam7;
        this.cam8 = cam8;
        this.cam9 = cam9;
        this.tsMainhall1 = tsMainhall1;
        this.tsMainhall2 = tsMainhall2;
        this.directIMAG = directIMAG;
        this.resolume = resolume;
        this.propress = propress;
        this.directBroadcast = directBroadcast;
        this.camBroadcast = camBroadcast;
        this.tsBroadcast1 = tsBroadcast1;
        this.tsBroadcast2 = tsBroadcast2;
    }

    public void setServiceDate(Date serviceDate) { this.serviceDate = serviceDate; }
    public Date getServiceDate() { return serviceDate; }
    public void setDirectCam(String directCam) { this.directCam = directCam; }
    public String getDirectCam() { return directCam; }
    public void setCam1(String cam1) { this.cam1 = cam1; }
    public String getCam1() { return cam1; }
    public void setCam2(String cam2) { this.cam2 = cam2; }
    public String getCam2() { return cam2; }
    public void setCam3(String cam3) { this.cam3 = cam3; }
    public String getCam3() { return cam3; }
    public void setCam4(String cam4) { this.cam4 = cam4; }
    public String getCam4() { return cam4; }
    public void setCam5(String cam5) { this.cam5 = cam5; }
    public String getCam5() { return cam5; }
    public void setCam6(String cam6) { this.cam6 = cam6; }
    public String getCam6() { return cam6; }
    public void setCam7(String cam7) { this.cam7 = cam7; }
    public String getCam7() { return cam7; }
    public void setCam8(String cam8) { this.cam8 = cam8; }
    public String getCam8() { return cam8; }
    public void setCam9(String cam9) { this.cam9 = cam9; }
    public String getCam9() { return cam9; }
    public void setTsMainhall1(String tsMainhall1) { this.tsMainhall1 = tsMainhall1; }
    public String getTsMainhall1() { return tsMainhall1; }
    public void setTsMainhall2(String tsMainhall2) { this.tsMainhall2 = tsMainhall2; }
    public String getTsMainhall2() { return tsMainhall2; }
    public void setDirectIMAG(String directIMAG) { this.directIMAG = directIMAG; }
    public String getDirectIMAG() { return directIMAG; }
    public void setResolume(String resolume) { this.resolume = resolume; }
    public String getResolume() { return resolume; }
    public void setPropress(String propress) { this.propress = propress; }
    public String getPropress() { return propress; }
    public void setDirectBroadcast(String directBroadcast) { this.directBroadcast = directBroadcast; }
    public String getDirectBroadcast() { return directBroadcast; }
    public void setCamBroadcast(String camBroadcast) { this.camBroadcast = camBroadcast; }
    public String getCamBroadcast() { return camBroadcast; }
    public void setTsBroadcast1(String tsBroadcast1) { this.tsBroadcast1 = tsBroadcast1; }
    public String getTsBroadcast1() { return tsBroadcast1; }
    public void setTsBroadcast2(String tsBroadcast2) { this.tsBroadcast2 = tsBroadcast2; }
    public String getTsBroadcast2() { return tsBroadcast2; }

    public static String inputValidatedName(String role,MongoCollection<Document> collectionFulltimer, MongoCollection<Document> collectionVolunteer, Scanner sc){
        String name;

        while(true){
            System.out.println();
            System.out.println("Input who is in charge for " + role);
            System.out.print("> ");
            name = sc.nextLine();

            Bson filter = Filters.eq("nama_lengkap", name);
            boolean isRegistered = collectionFulltimer.find(filter).iterator().hasNext() || collectionVolunteer.find(filter).iterator().hasNext();

            if(isRegistered){
                return name;
            }
            else{
                System.out.println();
                System.out.println(name + " is not registered on Fulltimer or Volunteer database");
            }
       }
    }

    public static List<JadwalPelayanan> getAllJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan) {
        List<JadwalPelayanan> listJadwalPelayanan = new ArrayList<>();
        for (Document print : collectionJadwalPelayanan.find()) {
            Date serviceDate = print.getDate("Tanggal Pelayanan");
            String directCam = print.getString("Direct Cam");
            String cam1 = print.getString("Cam 1");
            String cam2 = print.getString("Cam 2");
            String cam3 = print.getString("Cam 3");
            String cam4 = print.getString("Cam 4");
            String cam5 = print.getString("Cam 5");
            String cam6 = print.getString("Cam 6");
            String cam7 = print.getString("Cam 7");
            String cam8 = print.getString("Cam 8");
            String cam9 = print.getString("Cam 9");
            String tsMainhall1 = print.getString("TS Mainhall 1");
            String tsMainhall2 = print.getString("TS Mainhall 2");
            String directIMAG = print.getString("Direct IMAG");
            String resolume = print.getString("Resolume");
            String propress = print.getString("Propress");
            String directBroadcast = print.getString("Direct Broadcast");
            String camBroadcast = print.getString("Cam Broadcast");
            String tsBroadcast1 = print.getString("TS Broadcast 1");
            String tsBroadcast2 = print.getString("TS Broadcast 2");

            JadwalPelayanan jadwalPelayanan = new JadwalPelayanan(
                serviceDate, directCam, cam1, cam2, cam3, cam4, cam5, cam6, cam7, cam8, cam9,
                tsMainhall1, tsMainhall2, directIMAG, resolume, propress, 
                directBroadcast, camBroadcast, tsBroadcast1, tsBroadcast2
            );

            listJadwalPelayanan.add(jadwalPelayanan);
        }
        return listJadwalPelayanan;
    }

    public static void displayDataJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan, Scanner sc) {
        List<JadwalPelayanan> listJadwalPelayanan = JadwalPelayanan.getAllJadwalPelayanan(collectionJadwalPelayanan);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy"); 
        
        if (listJadwalPelayanan.isEmpty()) {
            System.out.println();
            System.out.println("No data available in the schedule.");
            return;
        }
        System.out.println();
        System.out.println("========= LIST OF SERVICE SCHEDULES =========");
        int currentIndex = 0;
        while (true) {
            JadwalPelayanan currentJadwalPelayanan = listJadwalPelayanan.get(currentIndex);
            LocalDate dos = currentJadwalPelayanan.getServiceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            System.out.println();
            System.out.println("=============================================");
            System.out.println("Date Schedule\t: " + dos.format(dateFormatter));
            System.out.println();
            System.out.println("Direct Cam\t: " + currentJadwalPelayanan.getDirectCam());
            System.out.println("Cam 1\t\t: " + currentJadwalPelayanan.getCam1());
            System.out.println("Cam 2\t\t: " + currentJadwalPelayanan.getCam2());
            System.out.println("Cam 3\t\t: " + currentJadwalPelayanan.getCam3());
            System.out.println("Cam 4\t\t: " + currentJadwalPelayanan.getCam4());
            System.out.println("Cam 5\t\t: " + currentJadwalPelayanan.getCam5());
            System.out.println("Cam 6\t\t: " + currentJadwalPelayanan.getCam6());
            System.out.println("Cam 7\t\t: " + currentJadwalPelayanan.getCam7());
            System.out.println("Cam 8\t\t: " + currentJadwalPelayanan.getCam8());
            System.out.println("Cam 9\t\t: " + currentJadwalPelayanan.getCam9());
            System.out.println("TS Mainhall 1\t: " + currentJadwalPelayanan.getTsMainhall1());
            System.out.println("TS Mainhall 2\t: " + currentJadwalPelayanan.getTsMainhall2());
            System.out.println("Direct IMAG\t: " + currentJadwalPelayanan.getDirectIMAG());
            System.out.println("Resolume\t: " + currentJadwalPelayanan.getResolume());
            System.out.println("Propress\t: " + currentJadwalPelayanan.getPropress());
            System.out.println("Direct Broadcast: " + currentJadwalPelayanan.getDirectBroadcast());
            System.out.println("Cam Broadcast\t: " + currentJadwalPelayanan.getCamBroadcast());
            System.out.println("TS Broadcast 1\t: " + currentJadwalPelayanan.getTsBroadcast1());
            System.out.println("TS Broadcast 2\t: " + currentJadwalPelayanan.getTsBroadcast2());
            System.out.println("=============================================");
            System.out.println("1. Next");
            System.out.println("2. Prev");
            System.out.println("3. Exit");
            System.out.print("> ");
    
        try {
            int choice = Integer.parseInt(sc.nextLine());
    
            switch(choice) {
                case 1 -> currentIndex = (currentIndex + 1) % listJadwalPelayanan.size();
                case 2 -> currentIndex = (currentIndex - 1 + listJadwalPelayanan.size()) % listJadwalPelayanan.size();
                case 3 -> { return; }
                default -> System.out.println("Invalid choice. Please enter 1, 2, or 3");
            } 
            } catch (NumberFormatException e) {    
                System.out.println("Invalid choice, please enter 1,2, or 3");
            }
        }
    }

    public static void findDataJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan, Scanner sc) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try{
            System.out.println();
            System.out.println("======== FIND SERVICE SCHEDULE DATA =========");
            System.out.println();
            System.out.println("Input service schedule date (dd-mm-YYYY)");
            System.out.print("> ");
            String tanggalPelayananInput = sc.nextLine(); 
            LocalDate tanggalPelayananFind = LocalDate.parse(tanggalPelayananInput, dateFormatter);
            Date tanggalPelayananFixed = Date.from(tanggalPelayananFind.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Bson filter = Filters.eq("Tanggal Pelayanan", tanggalPelayananFixed);
            List<Document> result = collectionJadwalPelayanan.find(filter).into(new ArrayList<>());

            if(result.isEmpty()){
                System.out.println("No data found with date : " + tanggalPelayananInput);
            }
            else{
                SimpleDateFormat dosFormatter = new SimpleDateFormat("dd-MM-yyyy");
                for(Document found : result){
                    System.out.println();
                    System.out.println("=============================================");
                    System.out.println("Date Schedule\t: " + dosFormatter.format(found.getDate("Tanggal Pelayanan")));
                    System.out.println();
                    System.out.println("Direct Cam\t: " + found.getString("Direct Cam"));
                    System.out.println("Cam 1\t\t: " + found.getString("Cam 1"));
                    System.out.println("Cam 2\t\t: " + found.getString("Cam 2"));
                    System.out.println("Cam 3\t\t: " + found.getString("Cam 3"));
                    System.out.println("Cam 4\t\t: " + found.getString("Cam 4"));
                    System.out.println("Cam 5\t\t: " + found.getString("Cam 5"));
                    System.out.println("Cam 6\t\t: " + found.getString("Cam 6"));
                    System.out.println("Cam 7\t\t: " + found.getString("Cam 7"));
                    System.out.println("Cam 8\t\t: " + found.getString("Cam 8"));
                    System.out.println("Cam 9\t\t: " + found.getString("Cam 9"));
                    System.out.println("TS Mainhall 1\t: " + found.getString("TS Mainhall 1"));
                    System.out.println("TS Mainhall 2\t: " + found.getString("TS Mainhall 2"));
                    System.out.println("Direct IMAG\t: " + found.getString("Direct IMAG"));
                    System.out.println("Resolume\t: " + found.getString("Resolume"));
                    System.out.println("Propress\t: " + found.getString("Propress"));
                    System.out.println("Direct Broadcast: " + found.getString("Direct Broadcast"));
                    System.out.println("Cam Broadcast\t: " + found.getString("Cam Broadcast"));
                    System.out.println("TS Broadcast 1\t: " + found.getString("TS Broadcast 1"));
                    System.out.println("TS Broadcast 2\t: " + found.getString("TS Broadcast 2"));
                    System.out.println("=============================================");
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
        }
            sc.nextLine();
    }

    public static void createDataJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan, MongoCollection<Document> collectionFulltimer, MongoCollection<Document> collectionVolunteer, Scanner sc){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("======= ADD NEW SERVICE SCHEDULE DATA =======");
    
    try{
        System.out.println();
        System.out.println("Input new service schedule date (dd-MM-yyyy)");
        System.out.print("> ");
        String newTanggalPelayananInput = sc.nextLine(); 
        LocalDate newTanggalPelayanan = LocalDate.parse(newTanggalPelayananInput, dateFormatter);
        Date newTanggalPelayananFixed = Date.from(newTanggalPelayanan.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String newDirectCam = inputValidatedName("Direct Cam", collectionFulltimer, collectionVolunteer, sc);
        String newCam1 = inputValidatedName("Cam 1", collectionFulltimer, collectionVolunteer, sc);
        String newCam2 = inputValidatedName("Cam 2", collectionFulltimer, collectionVolunteer, sc);
        String newCam3 = inputValidatedName("Cam 3", collectionFulltimer, collectionVolunteer, sc);
        String newCam4 = inputValidatedName("Cam 4", collectionFulltimer, collectionVolunteer, sc);
        String newCam5 = inputValidatedName("Cam 5", collectionFulltimer, collectionVolunteer, sc);
        String newCam6 = inputValidatedName("Cam 6", collectionFulltimer, collectionVolunteer, sc);
        String newCam7 = inputValidatedName("Cam 7", collectionFulltimer, collectionVolunteer, sc);
        String newCam8 = inputValidatedName("Cam 8", collectionFulltimer, collectionVolunteer, sc);
        String newCam9 = inputValidatedName("Cam 9", collectionFulltimer, collectionVolunteer, sc);
        String newTsMainhall1 = inputValidatedName("TS Mainhall 1", collectionFulltimer, collectionVolunteer, sc);
        String newTsMainhall2 = inputValidatedName("TS Mainhall 2", collectionFulltimer, collectionVolunteer, sc);
        String newDirectIMAG = inputValidatedName("Direct IMAG", collectionFulltimer, collectionVolunteer, sc);
        String newResolume = inputValidatedName("Resolume", collectionFulltimer, collectionVolunteer, sc);
        String newPropress = inputValidatedName("Propress", collectionFulltimer, collectionVolunteer, sc);
        String newDirectBroadcast = inputValidatedName("Direct Broadcast", collectionFulltimer, collectionVolunteer, sc);
        String newCamBroadcast = inputValidatedName("Cam Broadcast", collectionFulltimer, collectionVolunteer, sc);
        String newTsBroadcast1 = inputValidatedName("TS Broadcast 1", collectionFulltimer, collectionVolunteer, sc);
        String newTsBroadcast2 = inputValidatedName("TS Broadcast 2", collectionFulltimer, collectionVolunteer, sc);

        JadwalPelayanan newJadwalPelayanan = new JadwalPelayanan(
            newTanggalPelayananFixed, newDirectCam, newCam1, newCam2, newCam3, newCam4, newCam5,
            newCam6, newCam7, newCam8, newCam9, newTsMainhall1, newTsMainhall2,
            newDirectIMAG, newResolume, newPropress, newDirectBroadcast,
            newCamBroadcast, newTsBroadcast1, newTsBroadcast2
        );

        // Menyiapkan dokumen MongoDB
        Document createJadwalPelayanan = new Document()
            .append("Tanggal Pelayanan", newJadwalPelayanan.getServiceDate())
            .append("Direct Cam", newJadwalPelayanan.getDirectCam())
            .append("Cam 1", newJadwalPelayanan.getCam1())
            .append("Cam 2", newJadwalPelayanan.getCam2())
            .append("Cam 3", newJadwalPelayanan.getCam3())
            .append("Cam 4", newJadwalPelayanan.getCam4())
            .append("Cam 5", newJadwalPelayanan.getCam5())
            .append("Cam 6", newJadwalPelayanan.getCam6())
            .append("Cam 7", newJadwalPelayanan.getCam7())
            .append("Cam 8", newJadwalPelayanan.getCam8())
            .append("Cam 9", newJadwalPelayanan.getCam9())
            .append("TS Mainhall 1", newJadwalPelayanan.getTsMainhall1())
            .append("TS Mainhall 2", newJadwalPelayanan.getTsMainhall2())
            .append("Direct IMAG", newJadwalPelayanan.getDirectIMAG())
            .append("Resolume", newJadwalPelayanan.getResolume())
            .append("Propress", newJadwalPelayanan.getPropress())
            .append("Direct Broadcast", newJadwalPelayanan.getDirectBroadcast())
            .append("Cam Broadcast", newJadwalPelayanan.getCamBroadcast())
            .append("TS Broadcast 1", newJadwalPelayanan.getTsBroadcast1())
            .append("TS Broadcast 2", newJadwalPelayanan.getTsBroadcast2());
        
        collectionJadwalPelayanan.insertOne(createJadwalPelayanan);

        System.out.println();
        System.out.println("New service schedule for " + newTanggalPelayananInput + " has been added successfully.");

        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
        }
    }

    public static void editDataJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan, Scanner sc){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("========= EDIT SERVICE SCHEDULE DATA ========");
        try{
            System.out.println();
            System.out.println("Input service schedule date (dd-MM-yyyy)");
            System.out.print("> ");
            String tanggalPelayananInput = sc.nextLine(); 
            LocalDate tanggalPelayananFind = LocalDate.parse(tanggalPelayananInput, dateFormatter);
            Date tanggalPelayananFixed = Date.from(tanggalPelayananFind.atStartOfDay(ZoneId.systemDefault()).toInstant());

            Bson filter = Filters.eq("Tanggal Pelayanan", tanggalPelayananFixed);
            List<Document> result = collectionJadwalPelayanan.find(filter).into(new ArrayList<>());

        
            if (result.isEmpty()) {
                System.out.println("No data found with date: " + tanggalPelayananInput);
            } else {
                System.out.println("\n\tWhich field do you want to update?");
                String[] editOptions = {
                    "\t1. Direct Cam",
                    "\t2. Cam 1",
                    "\t3. Cam 2",
                    "\t4. Cam 3",
                    "\t5. Cam 4",
                    "\t6. Cam 5",
                    "\t7. Cam 6",
                    "\t8. Cam 7",
                    "\t9. Cam 8",
                    "\t10. Cam 9",
                    "\t11. TS Mainhall 1",
                    "\t12. TS Mainhall 2",
                    "\t13. Direct IMAG",
                    "\t14. Resolume",
                    "\t15. Propress",
                    "\t16. Direct Broadcast",
                    "\t17. Cam Broadcast",
                    "\t18. TS Broadcast 1",
                    "\t19. TS Broadcast 2"
                };
    
                // Menampilkan opsi edit
                for (String option : editOptions) {
                    System.out.println(option);
                }
                System.out.print("\tEnter your choice (1 - " + editOptions.length + ") > ");
    
                try {
                    int choice = Integer.parseInt(sc.nextLine());
                    if (choice < 1 || choice > editOptions.length) {
                        throw new InputMismatchException();
                    }
    
                    // Input data baru untuk field yang dipilih
                    System.out.println();
                    System.out.print("Input new name for" + editOptions[choice - 1].substring(3) + ": ");
                    String newValue = sc.nextLine();
    
                    String fieldToUpdate = switch (choice) {
                        case 1 -> "Direct Cam";
                        case 2 -> "Cam 1";
                        case 3 -> "Cam 2";
                        case 4 -> "Cam 3";
                        case 5 -> "Cam 4";
                        case 6 -> "Cam 5";
                        case 7 -> "Cam 6";
                        case 8 -> "Cam 7";
                        case 9 -> "Cam 8";
                        case 10 -> "Cam 9";
                        case 11 -> "TS Mainhall 1";
                        case 12 -> "TS Mainhall 2";
                        case 13 -> "Direct IMAG";
                        case 14 -> "Resolume";
                        case 15 -> "Propress";
                        case 16 -> "Direct Broadcast";
                        case 17 -> "Cam Broadcast";
                        case 18 -> "TS Broadcast 1";
                        case 19 -> "TS Broadcast 2";
                        default -> null;
                    };
    
                    if (fieldToUpdate != null) {
                        // Update field yang dipilih
                        Document updateField = new Document(fieldToUpdate, newValue);
                        collectionJadwalPelayanan.updateOne(filter, new Document("$set", updateField));
                        System.out.println();
                        System.out.println("\tField '" + fieldToUpdate + "' has been updated successfully.");
                    }
                } catch (InputMismatchException | NumberFormatException e) {
                    System.out.println("\tInvalid input. Please enter a number between 1 and " + editOptions.length);
                }
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use dd-MM-yyyy.");
        }
    }

    public static void deleteDataJadwalPelayanan(MongoCollection<Document> collectionJadwalPelayanan, Scanner sc){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println();
        System.out.println("======== DELETE SERVICE SCHEDULE DATA =======");
        System.out.println();
        System.out.println("Input service schedule date (dd-MM-yyyy)");
        System.out.print("> ");
        String tanggalPelayananInput = sc.nextLine(); 
        LocalDate tanggalPelayananFind = LocalDate.parse(tanggalPelayananInput, dateFormatter);
        Date tanggalPelayananFixed = Date.from(tanggalPelayananFind.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Bson filter = Filters.eq("Tanggal Pelayanan", tanggalPelayananFixed);
        List<Document> result = collectionJadwalPelayanan.find(filter).into(new ArrayList<>());

        if (result.isEmpty()) {
            System.out.println("No data found with date: " + tanggalPelayananInput);
        }
        else{
            SimpleDateFormat dosFormatter = new SimpleDateFormat("dd-MM-yyyy");
            for(Document found : result){
                System.out.println();
                System.out.println("=============================================");
                System.out.println("Date Schedule\t: " + dosFormatter.format(found.getDate("Tanggal Pelayanan")));
                System.out.println();
                System.out.println("Direct Cam\t: " + found.getString("Direct Cam"));
                System.out.println("Cam 1\t\t: " + found.getString("Cam 1"));
                System.out.println("Cam 2\t\t: " + found.getString("Cam 2"));
                System.out.println("Cam 3\t\t: " + found.getString("Cam 3"));
                System.out.println("Cam 4\t\t: " + found.getString("Cam 4"));
                System.out.println("Cam 5\t\t: " + found.getString("Cam 5"));
                System.out.println("Cam 6\t\t: " + found.getString("Cam 6"));
                System.out.println("Cam 7\t\t: " + found.getString("Cam 7"));
                System.out.println("Cam 8\t\t: " + found.getString("Cam 8"));
                System.out.println("Cam 9\t\t: " + found.getString("Cam 9"));
                System.out.println("TS Mainhall 1\t: " + found.getString("TS Mainhall 1"));
                System.out.println("TS Mainhall 2\t: " + found.getString("TS Mainhall 2"));
                System.out.println("Direct IMAG\t: " + found.getString("Direct IMAG"));
                System.out.println("Resolume\t: " + found.getString("Resolume"));
                System.out.println("Propress\t: " + found.getString("Propress"));
                System.out.println("Direct Broadcast: " + found.getString("Direct Broadcast"));
                System.out.println("Cam Broadcast\t: " + found.getString("Cam Broadcast"));
                System.out.println("TS Broadcast 1\t: " + found.getString("TS Broadcast 1"));
                System.out.println("TS Broadcast 2\t: " + found.getString("TS Broadcast 2"));
                System.out.println("=============================================");
            }

            System.out.println();
            System.out.println(tanggalPelayananInput + "'s data found");
            System.out.println("Are you sure want to delete this schedule data? (y/n)");
            System.out.print("> ");
            String confirm = sc.nextLine();

            if(confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")){
                collectionJadwalPelayanan.deleteOne(filter);
                System.out.println();
                System.out.println(tanggalPelayananInput + " has been deleted");
            }
        }
        sc.nextLine();
    }
}