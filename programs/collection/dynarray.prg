class DynArray {
    fun init(size) {
        this.length = size;
        this.data = [size];
    }

    fun size() {
        return this.length;
    }

    fun capacity() {
        return arraylength(this.data);
    }

    fun append(element) {
        while this.length >= this.capacity() {
            this.data = arrayresize(this.data, this.length * 2);
        }
        this.data[this.length] = element;
        this.length = this.length + 1;
    }

    fun get(index) {
        return this.data[index];
    }
}
