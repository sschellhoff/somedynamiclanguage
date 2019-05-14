class Doughnut {
  fun cook() {
    print "Fry until golden brown.";
  }
}

class BostonCream : Doughnut {
  fun cook() {
    super.cook();
    print "Pipe full of custard and coat with chocolate.";
  }
}

BostonCream().cook();