#print($ARGV[ 0 ], "\n");
#print($ARGV[ 1 ], "\n");

# Form the string to execute
if ($ARGV[2] eq "QUIZMODE")
{
	$word = "Randomly";
}
else
{
	$word = $ARGV[ 2 ];
}


# We use < instead of <= here since the GaigsServer put an artificial control character
# as the last parameter for systems like WinBatch that didn't have a tool to
# get the number of parameters
#for ( $i = 3; $i < $#ARGV; ++$i) {
#    $word = $word . "_" . $ARGV[ $i ];
#}

#print ("java exe.huffmanDecoding.HuffmanTrees " . $ARGV[ 0 ] . ".sho " . $word . "test\n");
system ("java exe.huffmanDecoding.HuffmanTrees " . $ARGV[ 0 ] . ".sho " . $word . "\n");

