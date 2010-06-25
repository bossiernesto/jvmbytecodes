# ARG[0] will be file name (not including the .sho), ARG[1] and
# asterisk (probably not needed), ARG[2] ... ARG[the cntl-a separator]
# the string of numbers to sort followed by a cntl-a separator -- if
# they've chosen-user defined, the last arg is the menu choice

use strict;
use warnings;

my @letters = ("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
my $i;
my $p;
my $temp;
my $swap_loc;
my $data = "";
my $last;

# Gobble up data from text field
my $user_defined_data = "";

for ( $i = 2; $ARGV[$i] !~ m/\ca/; ++$i) {
    $user_defined_data = $user_defined_data . " " . $ARGV[ $i ];
}
$p = $i + 1;

if ($ARGV[$p] eq "Random") {
    if((int (rand(2))) eq 0){
	$data = "b";

	$last = $letters[0];
	$data = $data . " " . $last;

	for($i = 1; $i < 10; $i++){
	    if((int (rand(2))) eq 0){
		$data = $data . " addl" . $letters[$i] . "," . $last;
	    }else{
		$data = $data . " addr" . $letters[$i] . "," . $last;
	    }

	    if((int (rand(2))) eq 0){
		$last = $letters[$i];
	    }
	}
    }else{
	$data = "g";

	$last = $letters[0];
	$data = $data . " " . $last;

	for($i = 1; $i < 10; $i++){
	    $data = $data . " add" . $letters[$i] . "," . $last;

	    if((int (rand(2))) eq 0){
		$last = $letters[$i];
	    }
	}
    }
}
else {
    $data = $user_defined_data;
}

# print ("java exe.treetest.TreeTest " . $ARGV[0] . ".sho $data");
system ("java exe.treetest.TreeTest " . $ARGV[0] . ".sho $data");
