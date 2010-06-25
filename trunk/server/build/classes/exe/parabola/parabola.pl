use strict;
use warnings;

system("java -cp ../../lib/jdom.jar:. exe.parabola.ParabolaMain " . $ARGV[0] . ".xaal");
