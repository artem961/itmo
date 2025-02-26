package lab5.collection.models;

public enum Furnish{
    DESIGNER,
    LITTLE,
    BAD,
    NONE;

    public static Furnish getMax(){
        return DESIGNER;
    }

    public static int compare(Furnish f1, Furnish f2){
        return -Integer.compare(f1.ordinal(), f2.ordinal());
    }
}