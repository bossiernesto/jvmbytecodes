#print("exe/kruskalalex/kruskal_data " . @ARGV[0] . ".tmp " . @ARGV[0] . ".dat\n");
if(@ARGV[1] eq "*") {
    system("exe/kruskalalex/kruskal_data " . @ARGV[0] . ".tmp " . @ARGV[o] . ".dat");
    #print("exe/kruskalalex/kruskal_data " . @ARGV[0] . ".tmp " . @ARGV[o] . ".dat\n");
    system("java exe.kruskalalex.Kruskal " . @ARGV[0] . ".dat " . @ARGV[0] . ".sho");
}
else {
    system("java exe.kruskalalex.Kruskal " . @ARGV[0] . @ARGV[1] . " " . @ARGV[0] . ".sho");
}
