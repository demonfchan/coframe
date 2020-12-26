FROM chenmins/serverjre8:util
add coframe-boot/target/EOS_Microservices_5.0_Coframe.tar.gz /opt
CMD ["/opt/bin/startup.sh","run"]
