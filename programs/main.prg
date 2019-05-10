import "/home/jacko/IdeaProjects/InterpretedLanguage/programs/list.prg";

fun main() {
  var list = LinkedList();
  print list.isEmpty();
  var n0 = Node(0);
  list.addFront(n0);
  var n1 = Node(1);
  list.addBack(n1);
  var n2 = Node(2);
  list.addFront(n2);
  var n3 = Node(3);
  list.addBack(n3);

  for var n = list.head; n != null; n = n.getNext() {
    print n.value;
  }

  print list.head.value;
}

main();
