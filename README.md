## Known issues and future improvements
* Cookies and session are not properly handled
* The button which sends requests for image processing can be abused and fill up and crash the server a timeout should be added for ~1 second
* The compression does not work properly with .png
* The output directory is not easily changeable (should be configurable and another consumer application should be added)
* The user-manager-api contains functionality different from the user management, so it should be separated into 2 or more APIs
* Shell and batch script can be written to automate the starting of the project in future
## Run the project
* Clone the repository somewhere
* Download and start XAMPP
* Copy the sql script located in image-processor-fe/sql and run it in phpmyadmin
* Copy the content of image-processor-fe and paste it into the htdocs directory located in the XAMPP installation directory
* Download kafka 2.11-1.0.0
* Go to <kafka_directory>/bin/windows for windows users (for UNIX users it is similar just the scripts are located 1 directory up in the tree) and execute the following commands:
    * zookeeper-server-start.bat ..\..\config\zookeeper.properties
    * kafka-server-start.bat ..\..\config\server.properties
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic compression-topic
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic noise-reduction-topic
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic mirror-topic
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic convert-gray-topic
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic convert-black-n-white-topic
    * kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic edge-topic
* Start the user-manager-api application by running the main method in ApplicationInitializer
* Start the black-n-white converter application by running the main method in the Runner class
* Start the noise-reducer application by running the main method in the Runner class
* Start the image-compressor application by running the main method in the Runner class
* Go to localhost and start using the application