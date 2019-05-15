import "collection/dynarray.prg";

var somearray = [5];
somearray[0] = 13;
somearray[1] = [2];
somearray[1][1] = 42;
print somearray[0];
print somearray[1][1];
var otherarray = [5];
print otherarray[0];
otherarray[0] = 13;
print otherarray[0];
print "Length:";
print arraylength(somearray);
somearray = arrayresize(somearray, 6);
print "Length:";
print arraylength(somearray);
print somearray[0];

var da = DynArray(1);
da.append(1337);
da.append(42);
da.append("some string");
for var i = 0; i < da.size(); i = i + 1 {
    print da.get(i);
}
