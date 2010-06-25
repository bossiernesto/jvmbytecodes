#print($ARGV[ 0 ], "\n");
#print($ARGV[ 1 ], "\n");

# Form the string to execute
if ($ARGV[2] eq "QUIZMODE")
{
	$word = "wabba_dabba_wabba_woo";
}
else
{
	$word = $ARGV[ 2 ];

	for ( $i = 3; $i <= $#ARGV; ++$i) {
    	$word = $word . "_" . $ARGV[ $i ];
	}
}



#print ("java exe.huffmanEncoding.HuffmanTrees " . $ARGV[ 0 ] . ".sho " . $word . "test\n");
system ("java exe.huffmanEncoding.HuffmanTrees " . $ARGV[ 0 ] . ".sho " . $word . "\n");

