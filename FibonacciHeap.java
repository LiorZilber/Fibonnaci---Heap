
/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over integers.
 */
public class FibonacciHeap
{
	private HeapNode min;
	private int size;
	private static int totalLinks;
	private static int totalCuts;
	private int numberOfTrees;
	private int numberOfMarks; 
	private HeapNode first;
	
	
	   /**
	    * public boolean FibonacciHeap() 
	    * 
	    * The class's constructor
	    * Complexity O(1)   
	    */
	
	public FibonacciHeap() {
		this.size = 0;
		this.numberOfMarks = 0;
		this.numberOfTrees = 0;
	}
	

	   /**
	    * public int getNumberOfTrees()
	    *  
	    * Return the number of trees
	    * Complexity O(1)   
	    */
	public int getNumberOfTrees() {
		return this.numberOfTrees;
	}
	
	   /**
	    * public HeapNode getMin()
	    *  
	    * Return the node with minimum key
	    * Complexity O(1)   
	    */
	public HeapNode getMin() {
		return this.min;
	}
	
	   /**
	    * public HeapNode getFirst() 
	    * 
	    * Return the root of the first tree in the Heap
	    * Complexity O(1)   
	    */
	public HeapNode getFirst() {
		return this.first;
	}
   /**
    * public boolean isEmpty()
    *
    * precondition: none
    * 
    * The method returns true if and only if the heap
    * is empty.
    *  complexity O(1) 
    */
    public boolean isEmpty()
    {
    	return this.first == null; 
    }
		
   /**
    * public HeapNode insert(int key)
    *
    * Creates a node (of type HeapNode) which contains the given key, and inserts it into the heap.
    * Complexity O(1)
    */
    public HeapNode insert(int key)
    {    
    	 return insertAllCases( key, null); 

    }
    
    /**
     * public HeapNode insertAllCases(int key, HeapNode originalNode)
     *
     * Creates a node (of type HeapNode) which contains the given key and a pointer to another HeapNode, and inserts it into the heap.
     * Complexity O(1)
     */ 
    public HeapNode insertAllCases(int key, HeapNode originalNode) {
    	HeapNode insertedNode = new HeapNode(key, originalNode);
    	if(this.isEmpty()) {// the Heap is empty
    		this.first = insertedNode;
    		this.min = insertedNode ;
    		insertedNode.next = insertedNode;
    		insertedNode.prev = insertedNode;
    	}
    	else {// Heap is not empty
    		insertedNode.next = this.first;
    		insertedNode.prev = this.first.prev;
    		this.first.prev.next = insertedNode;
    		this.first.prev = insertedNode ;
    		this.first = insertedNode ;
    		if(key < this.min.getKey()) {// the inserted node's key is smaller than the minimum key
    			this.min = insertedNode;// update the min
    		}
    		
    		
    	}
    	this.size++;
    	this.numberOfTrees++;
    	return insertedNode;
    }
    	
    
    
    
    /**
     * private HeapNode link(HeapNode x,HeapNode y)
     *
     * Links two trees of same ranks
     * Complexity O(1)
     */
    private HeapNode link(HeapNode x,HeapNode y) {
    	totalLinks ++;
    	HeapNode newParent = x;
    	HeapNode newChild = y;
    	if (x.getKey() > y.getKey()) { // then x is the parent
    		newChild = x;
    		newParent = y;
    	}
    	//rank = 0
    	if (newParent.child == null) {
    		newChild.next = newChild;
    		newChild.prev = newChild; 
    	} 
    	// rank > 0
    	else {
    		newParent.child.prev.next = newChild;
    		newChild.prev = newParent.child.prev;
    		newChild.next = newParent.child;
    		newParent.child.prev = newChild;   		
    	}
    	//happens both cases: 
    	newChild.parent = newParent;
		newParent.child = newChild; 
    	newParent.setRank(newParent.getRank()+1);
		return newParent;
      }
    
    /**
     * private void insertAfter(HeapNode first, HeapNode insertedRoot)
     *
     * insert a new Fibonacci tree to the end of the heap.
     *complexity O(1)
     *
     */
    
    private void insertAfter(HeapNode first, HeapNode insertedRoot) {
    	first.prev.next = insertedRoot;
    	insertedRoot.next = first;
    	insertedRoot.prev = first.prev;
    	first.prev = insertedRoot;
    }
    
    /**
     * private int numOfBuckets()
     *
     *Computes log in base of Golden ratio and n.
     *Complexity O(1).
     */
    private int numOfBuckets(){
    	return (int) Math.floor((1.4404*(Math.log10(this.size)/Math.log10(2))+2));
    }
   
    /**
     * private HeapNode[] toBuckets()
     *
     * Puts the trees of a heap in an array by ranks.
     * Links two trees with same rank.
     * Complexity  O(n).
     * 
     */
    private HeapNode[] toBuckets() {

    	HeapNode[] B = new HeapNode[this.numOfBuckets()];
    	this.first.prev.next = null; // not rounded
    	HeapNode curr = this.first;
    	//putting in buckets
    	while(curr != null) {
    		HeapNode temp = curr;
    		curr = curr.next;
    		while(B[temp.rank]!=null) {//there is tree in this cell already
    			temp = link(temp,B[temp.rank]);
    			B[temp.rank-1] = null;
    		}
    		B[temp.rank] = temp; // puts in the right bucket
    	}
    	return B;    	
    }
    
    
    /**
     * private void fromBuckets(HeapNode[] bucketsArr)
     *
     * Iterating over the buckets array and inserting the Fibonacci trees to the Heap.
     * Complexity O(logn).
     */
    private void fromBuckets(HeapNode[] B) {
    	this.numberOfTrees = 0;
    	this.first = null;
    	HeapNode x = null;
    	int numOfBuckets = this.numOfBuckets();
    	for(int i = 0; i<numOfBuckets; ++i ) {// iterating over the buckets
    		if(B[i] != null) {
    			if(x == null) {
    				x = B[i];
    				x.next = x;
    				x.prev = x;
    				this.first = x;
    			}
    			else {
    				this.insertAfter(this.first, B[i]);
    				if(B[i].getKey() < x.getKey()) {
    					x = B[i];
    				}
    			}
    			this.numberOfTrees++;
    			
    			
    		}
    		this.min = x;
    		
    	}
    	
    	
    }
    
    
    /**
     * private void consolidate()
     *
     * Does successive linking on the heap after deleteMin.
     * Complexity O(n)
     */
    private void consolidate() {
    	this.fromBuckets(this.toBuckets());
    }
    /**
     * private void deleteMinNoChild()
     *
     * Delete the min node when it does not have child
     * Complexity O(1).
     */
    private void deleteMinNoChild() { 
    	if (this.size == 1) {// there is only 1 node in the Heap
    		this.min = null;
    		this.first = null;
    		this.size = 0;
    	}
    	else { // the min and first updating is in fromBuckets process
    		this.min.prev.next = this.min.next;
    		this.min.next.prev = this.min.prev;
    		this.size--;
    		if(this.first.getKey() == this.min.getKey()) {
    			this.first = this.min.next;
    		}
    		this.min.next = null;
    		this.min.prev = null;
    	}
    }
    
    
    /**
     * private void deleteMinNoChild()
     *
     * Delete the min node when it has children
     * Complexity O(logn).
     */
   private void deleteMinWithChild() {
	   if(this.min.next.getKey() == this.min.getKey()) {// min.next = min
		   HeapNode temp = this.min.child;
		   temp.parent = null;
		   this.first = temp;
		   this.min.child = null;
		   this.min = temp;
		   this.size--;
		   return;
	   }
	   if(this.first.getKey() == this.min.getKey()) {// this.first == this.min
		   this.first = this.min.next;
	   }
	   HeapNode prevMin = this.min.prev;
	   HeapNode nextMin = this.min.next;
	   HeapNode childMin = this.min.child;
	   HeapNode childMinPrev = this.min.child.prev;
	   prevMin.next = childMin;
	   childMin.prev = prevMin;
	   nextMin.prev = childMinPrev;
	   childMinPrev.next = nextMin;
		this.min.next = null;
		this.min.prev = null;
		this.min.child = null;
	   HeapNode x = childMin ;
	   while(x.getKey() != nextMin.getKey()) {// iterating over the min's children
		   x.setParent(null);
		   if(x.getMark() == 1) {// changes the children's mark
			   x.mark = 0;
			   this.numberOfMarks -= 1;
		   }
		   x = x.next;
	   }
	   this.size--;
	   
   }

   /**
    * public void deleteMin()
    *
    * Delete the node containing the minimum key.
    * Complexity O(n)
    *
    */
    public void deleteMin()
    {
    	if(this.isEmpty()) {// Heap is empty
    		return;
    	}
    	if(this.min.child == null) { //the min node does not have child
    		deleteMinNoChild();
    	}
    	else {
    		deleteMinWithChild();
    	}
    	if(this.size > 0) {
    		consolidate();
    	}
    	else {
    		this.numberOfTrees = 0;
    	}
     	return; 
     	
    }

   /**
    * public HeapNode findMin()
    *
    * Return the node of the heap whose key is minimal. 
    * complexity O(1)
    */
    public HeapNode findMin()
    {
    	return this.min;
    } 
    
    /**
     * public void meld (FibonacciHeap heap2)
     *
     * Meld the heap with heap2
     *Complexity O(1)
     */
     public void meld (FibonacciHeap heap2)
     {
     	if (this.min.getKey() > heap2.min.getKey()) { // update the minimum if needed 
     		this.min = heap2.min;  		   		
     	}
     	// connects the last node of this to the first node of heap2
     	this.first.prev.next = heap2.first;  
     	HeapNode temp = this.first.prev;
     	this.first.prev = heap2.first.prev;
     	heap2.first.prev = temp;
     	this.first.prev.next = this.first;
     	this.size += heap2.size;
     	this.numberOfTrees += heap2.numberOfTrees;
     	this.numberOfMarks += heap2.numberOfMarks;
     }

   /**
    * public int size()
    *
    * Return the number of elements in the heap
    * Complexity O(1)
    */
    public int size()
    {
    	return this.size;
    }
    	
    /**
     * public int[] countersRep()
     *
     * Return a counters array, where the value of the i-th entry is the number of trees of order i in the heap. 
     * Complexity O(log(n))
     */
    public int[] countersRep()
    {
	int[] arr = new int[this.numOfBuckets()];
	HeapNode curr = this.first.next;
	arr[this.first.getRank()] += 1;

	while(curr != this.first) {
		arr[curr.getRank()] += 1;
		curr = curr.next;
	}
	int cnt = 0 ; 
	int k= arr.length-1;
	while((k != -1)&&(arr[k]==0)) {
		cnt++;
		k--;
	} 	
	int[] toReturn = new int[arr.length-cnt];
	for(int i=0;i<toReturn.length;i++) {
		toReturn[i] = arr[i];
	}
	return toReturn;
    }
	
   /**
    * public void delete(HeapNode x)
    *
    * Deletes the node x from the heap. 
    *
    * Complexity O(n)
    */
    public void delete(HeapNode x) 
   
    {    
    	if(this.min.getKey() == x.getKey()) {// x is the minimum node
    		this.deleteMin();
    	}
    	else {// x is not the minimum
    		this.decreaseKey(x, (x.getKey() - this.min.getKey()) + 1);// change the node's key to be the minimum
    		this.deleteMin();
    	}
    	
    	return; 
    }
    
    /**
     * private void insertForCut(HeapNode node)
     *
     * Insert the node that is the root of the tree we cut
     * puts it in the first
     * Complexity O(1) 
     *
     */
    private void insertForCut(HeapNode node) {
    	node.next = this.first;
		node.prev = this.first.prev;
		this.first.prev.next = node;
		this.first.prev = node ;
		this.first = node ;
		this.numberOfTrees += 1;
    }
    
    /**
     * private void cut(HeapNode child, HeapNode parent)
     *
     * Insert the node that is the root of the tree we cut
     * puts it in the first
     * Complexity O(1) 
     *
     */
    private void cut(HeapNode childToRoot, HeapNode parentOfChild) {
    	totalCuts++;
    	childToRoot.parent = null; 
    	if(childToRoot.mark == 1) {
    		childToRoot.mark = 0;
    		this.numberOfMarks -= 1;   		
    	}
    	parentOfChild.rank -= 1;
    	if (childToRoot.next.getKey() == childToRoot.getKey()) {// child id the only child of parent
    		parentOfChild.child = null; 
    	}
    	else {
    		if (parentOfChild.getChild().getKey() == childToRoot.getKey()){//child if the poiter of the child of parent
    			parentOfChild.child = childToRoot.next;	
    			childToRoot.next.parent = parentOfChild;
    		}
    		//connect the children of parentOfChild:
    		childToRoot.prev.next = childToRoot.next;
    		childToRoot.next.prev = childToRoot.prev;
    		//insert childToRoot as a new tree in the heap:
    		
    	}
    	this.insertForCut(childToRoot);
    }
    
    
    /**
     * public void cascadingCut(HeapNode x, HeapNode y)
     *
     *  The cascading cut progress
     *  
     *Complexity O(n)
     */
    
    public void cascadingCut(HeapNode x, HeapNode y) {
    	this.cut(x,y);
    	if(y.getParent() != null) {// y has parent
    		if(y.mark == 0) {// this is the first child that y lose
    			y.mark =1;
    			this.numberOfMarks  += 1;
    		}
    		else {// if y already lost a child , we cut him from his parent
    			cascadingCut(y,y.getParent());
    		}
    	}
    }

   /**
    * public void decreaseKey(HeapNode x, int delta)
    *
    * The function decreases the key of the node x by delta. The structure of the heap should be updated
    * to reflect this change (for example, the cascading cuts procedure should be applied if needed).
    * Complexity O(n)
    */
    public void decreaseKey(HeapNode x, int delta)
    {    
    	x.key = x.getKey() - delta;
    	if(x.getParent() != null) {// x has a parent
        	if (x.getKey() < x.getParent().getKey()) {// after decrease key the node's key smaller than it's parent
        		this.cascadingCut(x, x.parent);
        	}
    	}
    	if(this.min.getKey() > x.getKey()) {// update the minimum
    		this.min = x;
    	}

    	return; 
    }

    /**
     * public int potential() 
     *
     * This function returns the current potential of the heap, which is:
     * Potential = #trees + 2*#marked
     * The potential equals to the number of trees in the heap plus twice the number of marked nodes in the heap. 
     * Complexity O(1)
     */
     public int potential() 
     {    
     	return this.numberOfTrees + 2*this.numberOfMarks;
     }

     /**
      * public static int totalLinks() 
      *
      * This static function returns the total number of link operations made during the run-time of the program.
      * A link operation is the operation which gets as input two trees of the same rank, and generates a tree of 
      * rank bigger by one, by hanging the tree which has larger value in its root on the tree which has smaller value 
      * in its root.
      * 
      * Complexity O(1)
      */
      public static int totalLinks()
      {    
      	return totalLinks;
      }

     /**
      * public static int totalCuts() 
      *
      * This static function returns the total number of cut operations made during the run-time of the program.
      * A cut operation is the operation which diconnects a subtree from its parent (during decreaseKey/delete methods). 
      *
      * Complexity O(1)
      */
      public static int totalCuts()
      {    
      	return totalCuts;
      }

     /**
    * public static int[] kMin(FibonacciHeap H, int k) 
    *
    * This static function returns the k minimal elements in a binomial tree H.
    * The function should run in O(k(logk + deg(H)). 
    */
    public static int[] kMin(FibonacciHeap H, int k)
    {    
    	if(k==0) {
    		return new int[0];
    	}
    	
        int[] arr = new int[k];
        FibonacciHeap minHeap   = new FibonacciHeap();
        int cnt = 0;
        minHeap.insertAllCases(H.min.getKey(), H.min);
        while(cnt < k) {
        	HeapNode origNode = minHeap.min.originalNode;
        	minHeap.deleteMin();
        	if( origNode.child != null) {//than there are children to add to minHeap
            	HeapNode origChild = origNode.child;
            	// inserting the min's children in H to the minHeap
            	minHeap.insertAllCases(origChild.key, origChild);
            	HeapNode curr = origChild.next;
            	while(curr!=origChild) {
                	minHeap.insertAllCases(curr.key, curr);
                	curr=curr.next;
            	}
        	}
        	
            arr[cnt] = origNode.getKey();// insert the next min node the the array
            cnt++;	


        }
        return arr; 
    }
    
   /**
    * public class HeapNode
    * 
    * If you wish to implement classes other than FibonacciHeap
    * (for example HeapNode), do it in this file, not in 
    * another file 
    *  
    */
    public class HeapNode{

    	private int key;
    	private int rank; //num of children
    	private int mark; //if the node lost a child
    	private HeapNode child; 
    	private HeapNode parent;
    	private HeapNode next;
    	private HeapNode prev;
    	private HeapNode originalNode;
    	
    	   /**
    	    * public HeapNode(int key)
    	    * 
    	    *Class' constructor
    	    *Complexity O(1)
    	    */
      	public HeapNode(int key) {
    	    this.key = key;
    	    this.rank = 0 ; 
    	    this.mark = 0 ;
    	    this.originalNode = null;
          }
      	
 	   /**
 	    * public HeapNode(int key)
 	    * 
 	    *Class' constructor
 	    *Complexity O(1)
 	    */
      	public HeapNode(int key, HeapNode originalNode) {
    	    this.key = key;
    	    this.rank = 0 ; 
    	    this.mark = 0 ;
    	    this.originalNode = originalNode;
      	}
 	   /**
 	    * public int getKey()
 	    * 
 	    *Return node's key
 	    *Complexity O(1)
 	    */
      	public int getKey() {
    	    return this.key;
          }
      	
  	   /**
  	    * public void setKey(int newKey)
  	    * 
  	    *Set node's key
  	    *Complexity O(1)
  	    */
      	public void setKey(int newKey) {
    	    this.key = newKey;
          }
   	   /**
   	    * public HeapNode getPrev( )
   	    * 
   	    *Return node's prev
   	    *Complexity O(1)
   	    */
      	
      	public HeapNode getPrev() {
    	    return this.prev;
          }
      	
    	   /**
    	    * public HeapNode getNext( )
    	    * 
    	    *Return node's next
    	    *Complexity O(1)
    	    */
     	public HeapNode getNext() {
    	    return this.next;
          }
 	   /**
 	    * public void setNext(HeapNode newNext)
 	    * 
 	    *Set node's next
 	    *Complexity O(1)
 	    */
     	
      	public void setNext(HeapNode newNext) {
    	    this.next = newNext;
          }
  	   /**
  	    * public void setPrev(HeapNode newPrev)
  	    * 
  	    *Set node's prev
  	    *Complexity O(1)
  	    */
      	
     	public void setPrev(HeapNode newPrev) {
    	    this.prev = newPrev;
          }
     	
   	   /**
   	    * public int getRank()
   	    * 
   	    *Return node's rank
   	    *Complexity O(1)
   	    */
      	public int getRank() {
    	    return this.rank;
          }
      	
   	   /**
    	    * public void setRank(int newRank)
    	    * 
    	    *Set node's rank
    	    *Complexity O(1)
    	    */
      	public void setRank(int newRank) {	   
      		this.rank = newRank;
          }
      	
      	
     /**
 	    * public int getMark()
 	    * 
 	    *Return node's mark
 	    *Complexity O(1)
 	    */
      	public int getMark() {
    	    return this.mark;
          }
      	
      /**
 	    * public void setMark(int newMark)
 	    * 
 	    *Set node's setMark
 	    *Complexity O(1)
 	    */
      	public void setMark(int newMark) {
      		this.mark = newMark;
      	}
      	
        /**
  	    * public HeapNode getChild()
  	    * 
  	    *Return node's child
  	    *Complexity O(1)
  	    */
      	
      	public HeapNode getChild() {
    	    return this.child;
          }
      	
        /**
  	    *public void setChild(HeapNode newChild)
  	    * 
  	    *Set node's child
  	    *Complexity O(1)
  	    */
      	public void setChild(HeapNode newChild) {
      		this.child = newChild;
      	}
      	
        /**
  	    *public HeapNode getParent()
  	    * 
  	    *Return node's parent
  	    *Complexity O(1)
  	    */
      	public HeapNode getParent() {
    	    return this.parent;
          }
        /**
  	    *public void setParent(HeapNode newParent)
  	    * 
  	    *Set node's parent
  	    *Complexity O(1)
  	    */
      	public void setParent(HeapNode newParent) {
      		this.parent = newParent;
      	}
      

    }
}
