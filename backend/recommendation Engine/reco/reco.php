<?php 
 
$param1 = intval($_POST['cpu']);
$param2 = intval($_POST['ram']);
$param3 = intval($_POST['battery']);
$param4 = intval($_POST['camera']);
$param5 = intval($_POST['screen']);
$command = "python /xampp/htdocs/reco/reco.py";
$command .= " $param1 $param2 $param3 $param4 $param5 2>&1";
 
header('Content-Type: text/html; charset=utf-8');
echo '<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />';

echo "<link type='text/css' rel='stylesheet' href='csstable.css' />";
echo "<link type='text/css' rel='stylesheet' href='csstable1.css' />";
$pid = popen( $command,"r");
 
echo "<body><pre>";
while( !feof( $pid ) )
{
 echo fread($pid, 256);
 flush();
 ob_flush();
// echo "<script>window.scrollTo(0,99999);</script>";
 usleep(100000);
}
pclose($pid);
 
echo "</pre><script>window.scrollTo(0,99999);</script>";
?>