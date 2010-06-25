use strict;
use warnings;

system("java -cp ../../lib/jdom.jar:. exe.synthDiv/synthDiv " . $ARGV[0] . ".xaal");
