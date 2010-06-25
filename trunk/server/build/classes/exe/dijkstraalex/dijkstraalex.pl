if(@ARGV[1] eq "*") {
    system("exe/dijkstraalex/grapdriv " . @ARGV[0] . ".tmp " . @ARGV[0] . ".dat");
    system("java exe.dijkstraalex.Dijkstra " . @ARGV[0] . ".dat " . @ARGV[0] . ".sho");
}
else {
    system("java exe.dijkstraalex.Dijkstra " . @ARGV[0] . @ARGV[1] . " " . @ARGV[0] . ".sho");
}
