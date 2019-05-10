import "/home/jacko/IdeaProjects/InterpretedLanguage/programs/node.prg";

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
