#!/bin/bash

#EXT=`echo ${1##*.}`
#echo "$EXT"
#FILENAME=`echo ${1%.*}`
#echo "$FILENAME"
FILENAME=$1

#if [ "$EXT" -ne 'apk' ]
#then
#	echo "You have to add apk file"
#else
#	echo "Yes"
#fi

#cd /home/android/work
cd /opt/ARE/PreProcessing/decompile

#--copy apk file
cp /opt/ARE/$1/"$1".apk /opt/ARE/PreProcessing/decompile/$FILENAME.zip

#--unzip apk file
unzip -d /opt/ARE/PreProcessing/decompile/$FILENAME /opt/ARE/PreProcessing/decompile/$FILENAME.zip

mkdir /opt/ARE/PreProcessing/decompile/$FILENAME/"$FILENAME"Files
cd /opt/ARE/PreProcessing/decompile/$FILENAME/"$FILENAME"Files
cp /opt/ARE/PreProcessing/decompile/$FILENAME/classes.dex .
dex2jar.sh classes.dex

mkdir "$FILENAME"Classes
mkdir "$FILENAME"Javas
cd "$FILENAME"Classes

mv /opt/ARE/PreProcessing/decompile/$FILENAME/"$FILENAME"Files/classes_dex2jar.jar .
jar -xvf classes_dex2jar.jar

cd /opt/ARE/PreProcessing/decompile/$FILENAME/"$FILENAME"Files/"$FILENAME"Javas
find /opt/ARE/PreProcessing/decompile/$FILENAME/"$FILENAME"Files/"$FILENAME"Classes -name '*.class' | while read line
do
jad -o -sjava $line
done

#echo "$1" | grep ".apk" | cut -d . -f 1

#ls | grep ".apk" | cut -d . -f 1 | while read line
#do
#echo `ls $line.apk`
#mv $line.apk $line.zip
#done
#sleep 1
/opt/ARE/PreProcessing/sslfind.sh "$FILENAME"

rm -rf /opt/ARE/PreProcessing/decompile/"$FILENAME"*

