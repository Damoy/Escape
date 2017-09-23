package toolbox;


/**
 * A pair of objects
 * @param <F> the first type
 * @param <S> the second type
 */
public class Pair<F, S>{ //implements Cloneable{

    private F first; //first member of pair
    private S second; //second member of pair

    /**
     * Generates a new pair
     * @param first object
     * @param second object
     */
    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    
//    @Override
//    public Pair clone(){
//    	final Pair clone;
//        try {
//            clone = (Pair) super.clone();
//        }
//        catch (CloneNotSupportedException ex) {
//            throw new RuntimeException("superclass messed up", ex);
//        }
//        
//        clone.first = this.first.clone();
//        return clone;
//    }
    
//    public Pair(Pair<F, S> pair){
//    	this.first = pair.getFirst();
//    	this.second = pair.getSecond();
//    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }
    
    
//    public static void main(String[] args){
//    	List<Integer> l1 = new ArrayList<>();
//    	l1.add(2);
//    	l1.add(3);
//    	
//    	List<Integer> l2 = new ArrayList<>();
//    	l2.add(4);
//    	l2.add(6);
//    	
//    	Pair<List<Integer>, List<Integer>> pairInts = new Pair<List<Integer>, List<Integer>>(l1, l2);
//    }
}
