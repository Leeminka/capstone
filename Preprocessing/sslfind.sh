#!/bin/bash

#EXT=`echo ${1##*.}`
#echo "$EXT"
#FILENAME=`echo ${1%.*}`
FILENAME=$1
#echo "$FILENAME"
#COUNT=0
#SSL=no

cat /dev/null > /opt/ARE/$FILENAME/ssl.txt

find /opt/ARE/PreProcessing/decompile/"$FILENAME"/"$FILENAME"Files/"$FILENAME"Javas -name '*.java' | while read line
do
cat $line | grep KeyManagerFactory >> /opt/ARE/$FILENAME/ssl.txt
cat $line | grep SSLContext >> /opt/ARE/$FILENAME/ssl.txt
cat $line | grep SSLSocket >> /opt/ARE/$FILENAME/ssl.txt
cat $line | grep SSLSocketFactory >> /opt/ARE/$FILENAME/ssl.txt
cat $line | grep TrustManager >> /opt/ARE/$FILENAME/ssl.txt
cat $line | grep TrustManagerFactory >> /opt/ARE/$FILENAME/ssl.txt
#cat $line | grep ssl >> ssl.txt

#SSL=yes
#echo $SSL
done

#cat ssl.txt | while read line
#do
#let COUNT++
#echo $COUNT

#if [ $COUNT -ne 0 ]
#then
#echo COUNT = $COUNT
#fi

#done
