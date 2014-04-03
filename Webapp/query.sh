#!/bin/bash
echo 1 > /var/www/ire/status.txt
n=${@}
query=""
for var in "$@"
do
        query+=" ";
        query+=$var;
done
query=`echo $query | sed 's/^ *//'`
echo "Query: "$query "--->" `date`
echo "Query: "$query "--->" `date` >> queryLog.txt
cd /home/lordinvader/Studies/IRE/MP/PhaseThree/
echo $query > myquery.txt
java -jar IREMajor.jar config.txt < myquery.txt &> /var/www/ire/console.txt
echo 0 > /var/www/ire/status.txt
