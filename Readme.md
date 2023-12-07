## Install Docker & Database
Recommended for using PC with Linux Environment

``` sudo apt install docker docker-compose ```

Check Version docker & docker-compose

``` docker -v ```

``` docker-compose -v ```

Fill or change variable on the application.properties

**Build Database**

``` sudo docker-compose -f docker-compose-postgress.yml up --build ```

Check if it can be connected from an external application

**Build and Detach**

``` sudo docker-compose -f docker-compose-postgress.yml up --build -d ```


## install JDK
download [oracle java](https://www.oracle.com/java/technologies/downloads/)
```sh
sudo apt install ~/Downloads/jdk-20_linux-x64_bin.deb
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-20/bin/java 1
sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk-20/bin/javac 1
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk-20/bin/jar 1
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk-20/bin/jarsigner 1
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk-20/bin/jlink 1
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk-20/bin/javadoc 1
sudo update-alternatives --config java
sudo update-alternatives --config javac
sudo update-alternatives --config jar
export JAVA_HOME=/usr/lib/jvm/jdk-20
```
## add java repo to profile
```shell
nano ~/.profile
```
Add this line to the end of the file
```shell
export JAVA_HOME=/usr/lib/jvm/jdk-20
```

# Install Maven
```sh
wget https://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz
tar -xvf apache-maven-3.6.3-bin.tar.gz
mv apache-maven-3.6.3 /opt/
```
## Add maven repo to profile
```shell
M2_HOME='/opt/apache-maven-3.6.3'
PATH="$M2_HOME/bin:$PATH"
export PATH
```

## Read profile
```shell
source ~/.profile
```




**Build**
```shell
mvn -B package --file pom.xml -Pdev
```
**Run**
```shell
java -jar target/api-0.0.1-SNAPSHOT.jar
```

## Postman API TEST

You can import collection from _postman_ folder. And after the program runs and the table is created by _hibernate_, run the _data.sql_ script in the DB

@author : achmadayas@gmail.com