#  print($ARGV[ 0 ], "\n");
#  print($ARGV[ 1 ], "\n");
#  print($ARGV[ 2 ], "\n");
#  print($ARGV[ 3 ], "\n");
#  print($ARGV[ 4 ], "\n");
#  print($ARGV[ 5 ], "\n");
#  print($ARGV[ 6 ], "\n");
#  print($ARGV[ 7 ], "\n");
#  print($ARGV[ 8 ], "\n");

#system ("java exe.hanoi.TowersOfHanoi 4 1 2 0 " . $ARGV[ 0 ] . ".asu" . "\n");
#  open( ERRFILE, ">/home/naps/junk/debug.txt") or die("Cannot open error file");
#  print (ERRFILE "java exe.hanoi.TowersOfHanoi " . $ARGV[2] . let_to_num($ARGV[ 5 ]) . let_to_num($ARGV[ 7 ]) . let_to_num($ARGV[ 9 ]) . $ARGV[ 0 ]  . "\n");
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

# You'd think it would be args 4, 6, 8 -- but remember that the JHAVE server puts the funnny-face character as separator between
# inputs from input generators
system ("java exe.hanoi.TowersOfHanoi " . $ARGV[2] . let_to_num($ARGV[ 5 ]) . let_to_num($ARGV[ 7 ]) . let_to_num($ARGV[ 9 ]) . $ARGV[11] . " " . $ARGV[ 0 ]  . "\n");

sub let_to_num 
{
    print($_[ 0 ], "\n");
    if ($_[ 0 ] eq "A") {
	return " 0 ";
    }
    elsif ($_[ 0 ] eq "B") {
	return " 1 ";
    }
    else {
	return " 2 ";
    }
}
