# Configure
## Tools
spring-sts-4.16.1.RELEASE

## run config
-Dcloud.aws.stack.auto=false
-Dcom.amazonaws.sdk.disableEc2Metadata=true
-Dlogging.level.com.amazonaws.util.EC2MetadataUtils=error
-Dspring.profiles.active=local
