if(@ARGV[1] eq "*") {
    system("exe/primalex/prim_data " . @ARGV[0] . ".tmp " . @ARGV[0] . ".dat");
    system("java exe.primalex.Prim " . @ARGV[0] . ".dat " . @ARGV[0] . ".sho");
}
else {
    system("java exe.primalex.Prim " . @ARGV[0] . @ARGV[1] . " " . @ARGV[0] . ".sho");
}
