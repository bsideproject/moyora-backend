#!/bin/sh
SERVICE_NAME=ties-main
PATH_TO_JAR=./ties-0.0.1-SNAPSHOT.jar
PID_PATH_NAME=/tmp/$SERVICE_NAME-pid # pid name
RUN_MODE=prod
case $1 in
    start)
        echo "Starting $SERVICE_NAME ..."
        if [ ! -f $PID_PATH_NAME ]; then
            nohup java -jar \
                  -Dspring.config.location=classpath:/application.yml,/home/app/application-secret.yml,classpath:/application-jpa.yml \
                  -Dspring.profiles.active=$RUN_MODE $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is already running ..."
        fi
    ;;
    stop)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stoping ..."
            kill $PID;
            echo "$SERVICE_NAME stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    restart)
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "$SERVICE_NAME stopping ...";
            kill $PID;
            echo "$SERVICE_NAME stopped ...";
            rm $PID_PATH_NAME
            echo "$SERVICE_NAME starting ..."
            nohup java -jar \
                  -Dspring.config.location=classpath:/application.yml,/home/app/application-secret.yml,classpath:/application-jpa.yml \
                  -Dspring.profiles.active=$RUN_MODE $PATH_TO_JAR /tmp 2>> /dev/null >> /dev/null &
            echo $! > $PID_PATH_NAME
            echo "$SERVICE_NAME started ..."
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
esac