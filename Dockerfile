FROM maven:3-jdk-8 as app
#docker run -it --rm -v "$PWD":/usr/src/mymaven -v /data/maven:/root/.m2  -w /usr/src/mymaven -e MAVEN_CONFIG=/root/.m2 maven:3-jdk-8  mvn -X clean package 
COPY . /usr/src/mymaven
WORKDIR /usr/src/mymaven
RUN mvn -s settings.xml -X clean package 


FROM chenmins/serverjre8:util
add --from=app /usr/src/mymaven/coframe-boot/target/EOS_Microservices_5.0_Coframe.tar.gz /opt
CMD ["/opt/bin/startup.sh","run"]
