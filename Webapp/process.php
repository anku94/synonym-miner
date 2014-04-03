<?php

if(isset($_GET['query'])) {
  $q = $_GET['query'];
  $cmd = "./query.sh ";
  //$cmd = $cmd."\"".$q."\"";
  $cmd = $cmd.$q;
  echo "Executing query...";
  //echo $cmd;
  exec($cmd);
}
else {
  echo "Query Missing/Invalid query format";
}
?>
