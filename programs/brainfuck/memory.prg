import "../collection/dynarray.prg";
class Memory {
    fun init() {
        this.ptr = this.createMemoryCell();
    }
    
    fun incPtr() {
        if this.ptr.next == null {
            var nxt = this.createMemoryCell();
            this.ptr.next = nxt;
            nxt.prev = this.ptr;
        }
        this.ptr = this.ptr.next;
    }

    fun decPtr() {
        if this.ptr.prev == null {
            var prv = this.createMemoryCell();
            this.ptr.prev = prv;
            prv.next = this.ptr;
        }
        this.ptr = this.ptr.prev;
    }

    fun incVal() {
        this.ptr.value = this.ptr.value + 1;
    }

    fun decVal() {
        this.ptr.value = this.ptr.value - 1;
    }

    fun getVal() {
        return this.ptr.value;
    }

    fun setVal(newValue) {
        this.ptr.value = newValue;
    }

    fun createMemoryCell() {
        class MemoryCell {
            fun init() {
                this.value = 0;
                this.prev = null;
                this.next = null;
            }
            fun inc() {
                this.value = this.value + 1;
            }
            fun dec() {
                this.value = this.value - 1;
            }
        }
        return MemoryCell();
    }

    fun prnt() {
        var it = this.ptr;
        while it.prev != null {
            it = it.prev;
        }
        while it != null {
            if it == this.ptr {
                print it.value + " <-";
            } else {
                print it.value;
            }
            it = it.next;
        }
    }
}

