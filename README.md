# Configure
## Tools
spring-sts-4.16.1.RELEASE

## run config
-Dcloud.aws.stack.auto=false
-Dcom.amazonaws.sdk.disableEc2Metadata=true
-Dlogging.level.com.amazonaws.util.EC2MetadataUtils=error
-Dspring.profiles.active=local


## install jar (mave 3.8.7 사용)
>mvn install:install-file -Dfile=NiceID.jar -Dpackaging=jar -DgroupId=NiceID -DartifactId=NiceID -Dversion=1.0
>mvn install:install-file -Dfile=INIpay.jar -DgroupId=INIpay -DartifactId=INIpay -Dversion=1.0 -Dpackaging=jar
