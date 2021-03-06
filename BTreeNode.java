import java.util.ArrayList;

class BTreeNode {

    /**
     * ArrayList of the keys stored in the node.
     */
    ArrayList<Long> keys;
    
    /**
     * ArrayList of the values<recordID> stored in the node. This will only be filled when the node is a leaf node.
     */
    ArrayList<Long> values;
    
    /**
     * Minimum degree (defines the range for number of keys)
     **/
    int t; // Minimum degree is equivalent to order
    
    /**
     * Pointers to the children, if this node is not a leaf.  If
     * this node is a leaf, then null.
     */
    ArrayList<BTreeNode> children;
    
    /**
     * true when node is leaf. Otherwise false
     */
    boolean leaf;

    /**
     * point to other next node when it is a leaf node. Otherwise null
     */
    BTreeNode next;

    // Constructor
    BTreeNode(int t, boolean leaf) {
        this.t = t;
        this.leaf = leaf;
        this.keys = new ArrayList<Long>();
        this.values = new ArrayList<Long>();
        this.children = new ArrayList<>();
        this.next = null;
        
    }

    /**
     * find the correct position of the queried key
     * Note - Ki is the ith key
     * @param key
     * @return i such that Ki <= key < Ki+1
     */
    int findIndex(Long key){
        int i = 0;
        while (i < keys.size() - 1){
            if(keys.get(i) <= key && key < keys.get(i + 1)){
                return i;
            }
            i++;
        }
        return -1; // appropriate space not found
    }
}
