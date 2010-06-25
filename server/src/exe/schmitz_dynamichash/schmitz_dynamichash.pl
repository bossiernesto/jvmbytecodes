
use strict;
use warnings;

my $i;
my $p;
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

# Marshall's program takes an extra argument for the percentage of questions to ask
if ($ARGV[$p] eq "Random") {
    $data = "50 "."Random";
}
else {
    $data = "50 ".$user_defined_data;
}

print ("java exe.schmitz_dynamichash.dynamichashing " . $ARGV[0] . ".sho $data\n");
system ("java exe.schmitz_dynamichash.dynamichashing " . $ARGV[0] . ".sho $data");



