<?php
$pgm = array(
"0     int n[4] = {1, 8, 1, 2};", "1     int a = 4;", "2", "3     int main() { ", "4       int i[4] = {2, 8, 4, 1};", "5", "6       By_Name/Macro(i[0]);", "7     }", "8", "9", "10    void By_Name/Macro(int p){", "11       int a = 9;", "12       int n = 6;", "13", "14       n = p + p;", "15       p = a + a;", "16       a = n[3] + p;", "17       n[3] = p + p;", "18    }", " ");
for($i = 0; $i < count($pgm); $i++){
if($i ==$line)
print("<font color = 'red'>$pgm[$i]</font><br>");
else
print("$pgm[$i]<br>");
}
?>
