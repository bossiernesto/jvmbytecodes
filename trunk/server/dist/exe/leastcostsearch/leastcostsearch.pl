use strict;
use warnings;

if($ARGV[1] eq "*"){
    # print("java exe.leastcostsearch.LeastCostSearch " . $ARGV[0] . ".sho *");
    system("java exe.leastcostsearch.LeastCostSearch " . $ARGV[0] . ".sho *");
}else{
    # print("java exe.leastcostsearch.LeastCostSearch " . $ARGV[0] . ".sho");
    system("java exe.leastcostsearch.LeastCostSearch " . $ARGV[0] . ".sho");
}



