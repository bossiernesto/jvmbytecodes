use strict;
use warnings;

if($ARGV[1] eq "*"){
    # print("java exe.astarsearch.AStarSearch " . $ARGV[0] . ".sho *");
    system("java exe.astarsearch.AStarSearch " . $ARGV[0] . ".sho *");
}else{
    # print("java exe.astarsearch.AStarSearch " . $ARGV[0] . ".sho");
    system("java exe.astarsearch.AStarSearch " . $ARGV[0] . ".sho");
}



