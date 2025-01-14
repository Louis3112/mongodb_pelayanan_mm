# :bangbang: **Database Final Project for 3rd Semester**  :bangbang:

## :computer: :movie_camera: **mongodb_pelayanan_mm** :computer: :movie_camera:
My Final Project for Database (Basis Data) in 3rd Semester.

The project uses MongoDB as the database and includes 7 collection (folder) to manage various type of data, such as :
1. Fulltimer
2. Volunteer
3. Cameramen
4. Live Production
5. ICT
6. TS
7. Serving Schedule *(Jadwal Pelayanan)*

The project was inspired by my experience serving in Multimedia Ministry at my church. 
*All data (names, birthdates, addresses, emails, nif, nij, cameras, computers, etc) are entirely fictional and were also inspired by those experience.*

The project is developed using Java, runs on CLI, and requires only a minimal set of packages to install.

For further information, there is a [report](https://github.com/user-attachments/files/18407245/PROYEK.STUDI.KASUS.SISTEM.INFORMASI.MANAJEMEN.PELAYANAN.MULTIMEDIA.DI.GEREJA.docx)
written in *Bahasa Indonesia* and [UML](https://github.com/user-attachments/assets/98c25339-b100-43a4-8ba5-b176dd0eea98) that you can read from.

Tbh, i wanted to upgrade the project to include GUI for better experience. But, due to deadline and other commitments, i wasn't able to do that.
*The main file is on src/main/java/com/example* (i wanted to move the folders, but afraid the program will error)

## :arrow_forward: **Installation and Usage** 
To install this project, please proceed these steps (This step requires [mongo-java-driver-3.11.2.jar](https://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/3.11.2/): 

1. Clone the repository: **`git clone https://github.com/Louis3112/mongodb_pelayanan_mm`**
2. Install dependencies: **`mvn clean install `**
3. Navigate to the project directory: **`cd mongodb_pelayanan_mm/src`**
4. Compile the project : **`javac -cp ".;path:\to\mongo-java-driver-3.11.2.jar" main/java/com/example/*.java`**
5. Compile the Main.java : **`javac -cp ".;path:\to\mongo-java-driver-3.11.2.jar" main/java/com/example/Main.java`** (do this step if the main.java is still not compiled yet)
6. Run the project : **`java -cp ".;path:\to\mongo-java-driver-3.11.2.jar" main.java.com.example.Main`**
7. For simpler way, just run the project with your IDE :laughing:

## 	:bust_in_silhouette: **Contributing** :bust_in_silhouette:
If you'd like to contribute or upgrade the project, it would be my pleasure! :smile: 

Here's how to to contribute:
1. Fork the repository
2. Create a new branch: **`git checkout -b your-branch-name`**
3. Make your changes
4. Commit your changes: **`git commit -m "your messages"`** 
5. Push your changes to your forked repository: **`git push origin your-branch-name`**
6. Submit a pull request

## :telephone_receiver: **Contact** :telephone_receiver:

If you have any questions or comments about this project, please contact **[me](corneliuslouis3112@gmail.com)**
