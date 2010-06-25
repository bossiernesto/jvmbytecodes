#print($ARGV[ 0 ], "\n");
#print($ARGV[ 1 ], "\n");

#print ("java exe.huffmanEncoding.HuffmanTrees " . $ARGV[ 0 ] . ".sho " . $ARGV[ 2 ] . " " . $ARGV[ 4 ] . "\n);
#system ("java exe.chainedhashing.LinkedHashing " . $ARGV[ 0 ] . ".sho " . $ARGV[ 2 ] . " " . $ARGV[ 4 ] . "\n");

if($ARGV[2] eq "QUIZMODE")
{ 
    system ("java exe.chainedhashing.LinkedHashing " . $ARGV[ 0 ] . ".sho " . "6" . " " . "6" . "\n");
}
else
{
    system ("java exe.chainedhashing.LinkedHashing " . $ARGV[ 0 ] . ".sho " . $ARGV[ 2 ] . " " . $ARGV[ 4 ] . "\n");
}



# LEGACY WINBATCH CODE

#filebase = param1 
#datafileparam = param2
#tablesize = param3
#numkeys = param5

#; Establish the name of the sho file
#shofile = strcat(filebase, ".sho")
#algoexe = "Linear.exe";
#runhidewait(algoexe, "%shofile% %tablesize% %numkeys%")  
#
#exit                                   ; bye
