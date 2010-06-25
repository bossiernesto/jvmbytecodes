use strict;
use warnings;

if($ARGV[1] eq "*"){
    # print("java exe.bestfirstsearch.BestFirstSearch " . $ARGV[0] . ".sho *");
    system("java exe.bestfirstsearch.BestFirstSearch " . $ARGV[0] . ".sho *");
}else{
    # print("java exe.bestfirstsearch.BestFirstSearch " . $ARGV[0] . ".sho");
    system("java exe.bestfirstsearch.BestFirstSearch " . $ARGV[0] . ".sho");
}



