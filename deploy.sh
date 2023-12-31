#!/bin/bash
# CURRENT_PID=$(pgrep -f .jar)
 CURRENT_PID=$(pgrep -f "demo-0.0.1-SNAPSHOT.jar")
 echo "$CURRENT_PID"
 if [ -z $CURRENT_PID ]; then
         echo "no process"
 else
         echo "kill $CURRENT_PID"
         kill -9 $CURRENT_PID
         sleep 3
 fi

# JAR_PATH="/home/ubuntu/cicd/*.jar"
 JAR_PATH="/home/ubuntu/cicd/demo-0.0.1-SNAPSHOT.jar"
 echo "jar path : $JAR_PATH"
 chmod +x $JAR_PATH
 LOG_PATH="/home/ubuntu/cicd/app.log"
 echo "Starting application and redirecting logs to $LOG_PATH"
 nohup java -jar $JAR_PATH > $LOG_PATH 2>&1 &
 echo "jar file deploy success"
