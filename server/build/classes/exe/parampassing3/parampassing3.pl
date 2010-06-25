
$target = @ARGV[0];
$copytarget = @ARGV[0];

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


# print(@ARGV[1] . "\n");

if(@ARGV[1] eq "*") {

#    system("java exe/parampassing3/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".php 1 1");

    system("java exe/parampassing3/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".php "  . let_to_num($ARGV[ 2 ]) . let_to_num($ARGV[ 4 ]) . "\n");

    system("cp " . $target . ".sho1 " . $target . ".sho");
 }
 else {
    system("cp " . $target . ".sho1 " . $target . ".sho");
 }

sub let_to_num 
{
    print($_[ 0 ], "\n");
    if ($_[ 0 ] eq "Yes") {
	return " 1 ";
    }
    elsif ($_[ 0 ] eq "No") {
	return " 0 ";
    }
    else {  # Should not get here
	return " 2 ";
    }
}
