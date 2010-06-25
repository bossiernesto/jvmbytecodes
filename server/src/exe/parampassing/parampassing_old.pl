# Looking for -- exe/parampassing/parampassing.pl
# Executing -- perl exe/parampassing/parampassing.pl html_root/uid/52 * *
# exec(perl exe/parampassing/parampassing.pl html_root/uid/52 * *);
# http://141.233.143.63:80/jhave/html_root/uid/52.sho



$target = @ARGV[0];
$copytarget = @ARGV[0];

# print(@ARGV[1] . "\n");

if(@ARGV[1] eq "*") {

# print("java exe/parampassing/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".html 1 1 1");
# print("\n");
# print("cp " . $target . ".sho1 " . $target . ".sho");
# print("\n");

    system("java exe/parampassing/RandomProblemGenerator " . $target . ".sho1 " . $target . ".sho2 " . $target . ".html 1 1 1");
#    print($target . ".sho1" . " has been generated\n");
    system("cp " . $target . ".sho1 " . $target . ".sho");
 }
 else {
    system("cp " . $target . ".sho1 " . $target . ".sho");
 }
