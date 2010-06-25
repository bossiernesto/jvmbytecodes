# Looking for -- exe/parampassing/parampassing.pl
# Executing -- perl exe/parampassing/parampassing.pl html_root/uid/52 * *
# exec(perl exe/parampassing/parampassing.pl html_root/uid/52 * *);
# http://141.233.143.63:80/jhave/html_root/uid/52.sho

$target = @ARGV[0];
$copytarget = @ARGV[0];

#  open( ERRFILE, ">/home/naps/junk/debug.txt") or die("Cannot open error file");
#  print (ERRFILE "java exe/parampassing/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".php "  . let_to_num($ARGV[ 2 ]) . let_to_num($ARGV[ 4 ]) . let_to_num($ARGV[ 6  ]) . "\n");
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

    system("java exe/parampassing/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".php "  . let_to_num($ARGV[ 2 ]) . let_to_num($ARGV[ 4 ]) . let_to_num($ARGV[ 6  ]));

    system("cp " . $target . ".sho1 " . $target . ".sho");
 }
 else {
    system("cp " . $target . ".sho1 " . $target . ".sho");
 }

# You'd think it would be args 4, 6, 8 -- but remember that the JHAVE server puts the funnny-face character as separator between
# inputs from input generators
# system ("java exe.hanoi.TowersOfHanoi " . $ARGV[2] . let_to_num($ARGV[ 5 ]) . let_to_num($ARGV[ 7 ]) . let_to_num($ARGV[ 9 ]) . $ARGV[11] . " " . $ARGV[ 0 ]  . "\n");

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
