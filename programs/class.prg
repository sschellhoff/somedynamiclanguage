class Node {
  fun init(value) {
    this.value = value;
    this.next = null;
  }
  fun chain(next) {
    this.next = next;
  }
  fun hasNext() {
    return this.next != null;
  }
  fun getNext() {
    return this.next;
  }
}

class LinkedList {
  fun init() {
    this.head = null;
  }

  fun addFront(newElement) {
    if this.isEmpty() {
      this.head = newElement;
    } else {
      newElement.chain(this.head);
      this.head = newElement;
    }
  }

  fun addBack(newElement) {
    if this.isEmpty() {
      this.head = newElement;
    } else {
      this.getBack().chain(newElement);
    }
  }

  fun addAt(index, newElement) {
  }

  fun getBack() {
    if this.head == null {
      return null;
    }
    var current = this.head;
    while(current.getNext() != null) {
      current = current.getNext();
    }
    return current;
  }

  fun isEmpty() {
    return this.head == null;
  }
}

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