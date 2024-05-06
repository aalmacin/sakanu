aws cloudformation update-stack --stack-name SakanuApp --template-body file://./cloudformation/elastic-beanstalk.yml \
    --parameters ParameterKey=CidrIp,ParameterValue=$1 \
        ParameterKey=ApiDomain,ParameterValue=$2 \
        ParameterKey=SakanuDatabasePassword,ParameterValue=$3 \
    --capabilities CAPABILITY_NAMED_IAM

