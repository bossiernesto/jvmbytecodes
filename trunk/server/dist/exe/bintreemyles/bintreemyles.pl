
use strict;
use warnings;

my @numbers = (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23); 
my $i;
my $p;
my $num_of_numbers;
my $start_index_of_numbers;
my $temp;
my $swap_loc;
my $data = "";


# Gobble up data from text field
my $user_defined_data = "";



if($ARGV[2] eq "QUIZMODE")
{
    $p = 2;
    $ARGV[2] = "Random";
}
else
{
    for ( $i = 2; $ARGV[$i] !~ m/\ca/; ++$i) {
	$user_defined_data = $user_defined_data . " " . $ARGV[ $i ];
    }
    $p = $i + 1;
}


if ($ARGV[$p] eq "Random") {

    for ($i = 19; $i > 0; $i--) {
	$swap_loc = int (rand ($i + 1));
	$temp = $numbers[$swap_loc];
	$numbers[$swap_loc] = $numbers[$i];
	$numbers[$i] = $temp;
    }

    $start_index_of_numbers = int (rand(4));
    $num_of_numbers = 14 + int (rand(2));

    for ($i = $start_index_of_numbers; $i < ($start_index_of_numbers + $num_of_numbers); $i++) {
	$data = $data . $numbers[$i] . " ";
    }

    for ($i = $start_index_of_numbers; $i < ($start_index_of_numbers + $num_of_numbers); $i++) {
	if ( (int (rand(2)) == 0) ) { $data = $data . "d" . $numbers[$i] . " "; }
    }

}
else {
    $data = $user_defined_data;
}

print ("java exe.bintreemyles.BinarySearchTree " . $ARGV[0] . ".sho $data\n");
system ("java exe.bintreemyles.BinarySearchTree " . $ARGV[0] . ".sho $data");



