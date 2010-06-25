# ARG[0] will be file name (not including the .sho), ARG[1] an
# asterisk (probably not needed), ARG[2] the number of elements ,
# ARG[3] is "\ca", ARG[4] T or F for path compression, ARG[5] is
# "\ca", ARG[6] T or F for weighted union

use strict;
use warnings;

system ("java exe.unionfindmyles.unionFind " . $ARGV[0] . ".sho " . $ARGV[2] . " TREE " . $ARGV[4] . " " . $ARGV[6] . " T ");
# system ("java exe.unionfindmyles.unionFind " . $ARGV[0] . ".sho " . $ARGV[4] . " TREE " . $ARGV[4] . " " . $ARGV[8] . " T ");



