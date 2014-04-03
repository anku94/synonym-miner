<?php

if(1) {
  $q = "Harry Potter New New";
  $cmd = "./query.sh ";
  //$cmd = $cmd."\"".$q."\"";
  $cmd = $cmd.$q;
  echo "Executing query...";
  echo $cmd;
  exec($cmd);
}
else {
  echo "Query Missing/Invalid query format";
}
?>
