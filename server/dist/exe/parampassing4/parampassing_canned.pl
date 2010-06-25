# ARG[0] will be file name (not including the .sho), ARG[1] and
# asterisk (probably not needed), ARG[2] ... ARG[the cntl-a separator]
# the choice of example followed by a cntl-a separator 

use strict;
use warnings;

my $target = $ARGV[0];
my $copytarget = $ARGV[0];

#  open( ERRFILE, ">/home/naps/junk/debug.txt") or die("Cannot open error file");
#  print (ERRFILE "java exe/parampassing3/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".php "  . let_to_num($ARGV[ 2 ]) . let_to_num($ARGV[ 4 ]) . "\n");
#  print(ERRFILE $ARGV[ 0 ], "\n");
#  print(ERRFILE $ARGV[ 1 ], "\n");
#  print(ERRFILE $ARGV[ 2 ], "\n");
#  print(ERRFILE $ARGV[ 3 ], "\n");
#  print(ERRFILE $ARGV[ 4 ], "\n");
#  print(ERRFILE $ARGV[ 5 ], "\n");
#  print(ERRFILE $ARGV[ 6 ], "\n");
#  print(ERRFILE $ARGV[ 7 ], "\n");
#  print(ERRFILE $ARGV[ 8 ], "\n");
#  close( ERRFILE ) or die("Cannot close error file");

#print($ARGV[1] . "\n");


# 1nm.php
# 1nm.sho
# 1nm.sho1
# 1nm.sho2
# 1nm_wrong.php
# 1nm_wrong.sho
# 1nm_wrong.sho1
# 1nm_wrong.sho2
# 1rc.html
# 1rc.php
# 1rc.sho
# 1rc.sho1
# 1rc.sho2
# 2nm.html
# 2nm.php
# 2nm.sho
# 2nm.sho1
# 2nm.sho2
# 2nm_wrong.php
# 2nm_wrong.sho
# 2nm_wrong.sho1
# 2nm_wrong.sho2
# 2rc.php
# 2rc.sho
# 2rc.sho1
# 2rc.sho2
# 3nm.php
# 3nm.sho
# 3nm.sho1
# 3nm.sho2
# 3rc.php
# 3rc.sho
# 3rc.sho1
# 3rc.sho2
# 4rc.php
# 4rc.sho
# 4rc.sho1
# 4rc.sho2
# 7.php
# 7.sho
# 7.sho1
# 7.sho2



# print ($ARGV[2] . "\n");

if($ARGV[2] eq "ByReference1") {

    system("cp " . "exe/parampassing_canned/1rc.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1rc.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByCopyRestore1") {

    system("cp " . "exe/parampassing_canned/1rc.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1rc.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByReference2") {

    system("cp " . "exe/parampassing_canned/2rc.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2rc.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByCopyRestore2") {

    system("cp " . "exe/parampassing_canned/2rc.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2rc.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByReference3") {

    system("cp " . "exe/parampassing_canned/3rc.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/3rc.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByCopyRestore3") {

    system("cp " . "exe/parampassing_canned/3rc.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/3rc.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByReference4") {

    system("cp " . "exe/parampassing_canned/4rc.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/4rc.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByCopyRestore4") {

    system("cp " . "exe/parampassing_canned/4rc.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/4rc.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByName1") {

    system("cp " . "exe/parampassing_canned/1nm.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1nm.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByMacro1") {

    system("cp " . "exe/parampassing_canned/1nm.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1nm.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByName2") {

    system("cp " . "exe/parampassing_canned/2nm.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2nm.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByMacro2") {

    system("cp " . "exe/parampassing_canned/2nm.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2nm.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByName3") {

    system("cp " . "exe/parampassing_canned/3nm.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/3nm.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByMacro3") {

    system("cp " . "exe/parampassing_canned/3nm.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/3nm.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByName4") {

    system("cp " . "exe/parampassing_canned/1nm_wrong.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1nm_wrong.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByMacro4ERROR") {

    system("cp " . "exe/parampassing_canned/1nm_wrong.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/1nm_wrong.php " . $target . ".php");

 }
elsif($ARGV[2] eq "ByName5") {

    system("cp " . "exe/parampassing_canned/2nm_wrong.sho1 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2nm_wrong.php " . $target . ".php");

 }
 elsif($ARGV[2] eq "ByMacro5ERROR") {

    system("cp " . "exe/parampassing_canned/2nm_wrong.sho2 " . $target . ".sho");
    system("cp " . "exe/parampassing_canned/2nm_wrong.php " . $target . ".php");

 }
else {
    print("foobar\n");
}





