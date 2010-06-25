use strict;
use warnings;

#open( ERRFILE, ">/tmp/debug") or die("Cannot open error file");
#print(ERRFILE $ARGV[ 0 ], "\n");
#print(ERRFILE $ARGV[ 1 ], "\n");
#print(ERRFILE $ARGV[ 2 ], "\n");
#print(ERRFILE $ARGV[ 3 ], "\n");
#print(ERRFILE $ARGV[ 4 ], "\n");


# print ("java exe.dijkstrarichard.VisualGraph " . $ARGV[0] . ".sho");
system ("java exe.floydrichard.VisualGraph " . $ARGV[0] . ".sho " . $ARGV[2]);



