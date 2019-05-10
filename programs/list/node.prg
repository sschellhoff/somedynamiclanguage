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
