import java.util.ArrayList;
import java.util.List;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
class BTree {

    /**
     * Pointer to the root node.
     */
    private BTreeNode root;
    
    /**
     * Number of key-value pairs allowed in the tree/the minimum degree of B+Tree
     **/
    private int t;
    
    /**
     * Number of key-value pairs in the B+Tree
     **/
    private int n;
    
    private class NewChildEntry {
    	private Long keyValue;
    	private BTreeNode pointer;
    	
    	private NewChildEntry(Long keyValue, BTreeNode pointer) {
    		this.keyValue = keyValue;
    		this.pointer = pointer;
    	}
    }
    
    private NewChildEntry newChildEntry = new NewChildEntry(null, null);

    BTree(int t) {
        this.root = null;
        this.t = t;
        this.n = 0;
    }

    long search(long studentId) {
        /**
         * TODO:
         * Implement this function to search in the B+Tree.
         * Return recordID for the given StudentID.
         * Otherwise, print out a message that the given studentId has not been found in the table and return -1.
         */
        if (root == null) return -1;
        BTreeNode node = helpSearch(root, studentId);
        int i = 0;
        long recordId = -1;
        while(i < node.keys.size()) {
        	if(studentId < node.keys.get(i)) {
        		break;
        	}
        	else if(studentId > node.keys.get(i)) {
        		i++;
        	}
        	else {
        		recordId = node.values.get(i);
        		i++;
        	}
        }
        return (long) recordId;
    }
    
    private BTreeNode helpSearch(BTreeNode current, long studentId) {
    	if(current.leaf) {
    		return current;
    	}
    	else {
    		int i = 0;
    		while(i < current.keys.size()) {
    			if(studentId < current.keys.get(i)) {
    				break;
    			}
    			else {
    				i++;
    			}
    		}
    		return helpSearch(current.children.get(i), studentId);
    	}
    }

    BTree insert(Student student) {
        /**
         * TODO:
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        this.n ++;
    	if(root == null) {
    		root = new BTreeNode(t, true);
    		root.keys.add(student.studentId);
    		root.values.add(student.recordId);
    		return this;
    	}
        helpInsert(root, student);
        
    	return this;
    }
    
    private void helpInsert(BTreeNode current, Student student) {
    	long studentKey = student.studentId;
    	long studentValue = student.recordId;
    	if(!current.leaf) { // If current node is not a leaf
    		int i = 0;
    		while(i < current.keys.size()) {
    			if(studentKey < current.keys.get(i)) {
    				break;
    			}
    			else {
    				i++;
    			}
    		}
    		helpInsert(current.children.get(i), student);
    		if(newChildEntry.pointer == null) { 
    			return;
    		}
    		else {
    			if(current.keys.size() < 2*t) { // If node has space
    				int j = 0;
    				// "Put *newchildentry on it"
    				long newChildKey = newChildEntry.keyValue;
    				while(j < current.keys.size()) {
    					if(newChildKey < current.keys.get(j)) {
    						break;
    					}
    					else {
    						j++;
    					}
    				}
    				current.keys.add(j, newChildKey);
    				current.children.add(j + 1, newChildEntry.pointer);
    				// "Set newchildentry null"
    				newChildEntry.pointer = null;
    				return;
    			}
    			else { // If node does not have space
    				BTreeNode node2 = new BTreeNode(t, false);
    				for(int j = 0; j < t; j++) {
    					node2.keys.add(0, current.keys.get(2*t - j));
    					current.keys.remove(2*t - j);
    				}
    				for(int j = 0; j < t + 1; j++) {
    					node2.children.add(0, current.children.get(2*t + 1 - j));
    					current.children.remove(2*t + 1 - j);
    				}
    				long middleKey = (long) current.keys.remove(current.keys.size() - 1);
    				BTreeNode node1 = current;
    				newChildEntry.keyValue = middleKey;// & ((smallest key value on N2, pointer to N2))
    				newChildEntry.pointer = node2;
    				if(current == root) {
    					root = new BTreeNode(t, false);
    					root.keys.add(middleKey);
    					root.children.add(node1);
    					root.children.add(node2);
    				}
    				return;
    			}
    		}
    	}
    	if(current.leaf) {
    		// "Put entry on it"
    		int i = 0;
    		while(i < current.keys.size()) {
				if(studentKey < current.keys.get(i)) {
					break;
				}
				else {
					i++;
				}
			}
    		current.keys.add(i, studentKey);
    		current.values.add(i, studentValue);
    		n++;
    		if(current.keys.size() < 2*t + 1) {
    			// "Set newchildentry to null"
    			newChildEntry.pointer = null;
    			return;
    		}
    		else {
    			BTreeNode node2 = new BTreeNode(t, true);
    			for(int j = 0; j < t + 1; j++) {
    				node2.keys.add(0, current.keys.remove(current.keys.size() - 1));
    				node2.values.add(0, current.values.remove(current.values.size() - 1));
    			}
    			newChildEntry.keyValue = node2.keys.get(0); // & ((smallest key value on L2, pointer on L2))
    			newChildEntry.pointer = node2;
    			// Set sibling pointers in L and L2
    			node2.next = current.next;
    			current.next = node2;
    			if(current == root) {
    				BTreeNode node1 = current;
    				root = new BTreeNode(t, false);
    				root.keys.add(newChildEntry.keyValue);
					root.children.add(node1);
					root.children.add(node2);
    			}
    			return;
    		}
    	}
    }

    boolean delete(long studentId) {
        this.n --;
        // step 0: if the student is not in the tree, return false
        if (this.search(studentId) == -1){ // not found
            return false;                  // O(log(n))
        }
        helpDelete(null, root, studentId);
        return true;
    }

    /**
     * 
     * @param parent
     * @param node
     * @param key
     * @return the old childEntry
     */
    private BTreeNode helpDelete(BTreeNode parent, 
                            BTreeNode node, 
                            Long key){
        if (!node.leaf){
            int i = node.findIndex(key);
            BTreeNode oldChildEntry = helpDelete(node, node.children.get(i), key);
            if (oldChildEntry == null) return null;
            // discarded child nodes
            if(node.children.remove(oldChildEntry)){
                // OK
            }else{
                throw new IllegalArgumentException("Entry not found, oldChildEntry returned something wrong.");
            }
            // has entry to spare
            if (node.keys.size() >= node.t || this.root == node){
                return null; // deletion doesn't go furthur
            }
            // doesn't have entry to spare
            // merge/redistribute
            if (redistributeAt(parent.children.indexOf(node), parent)){ 
                // redistribution success
                return null;
            }
            // merge
            return mergeAT(parent.children.indexOf(node), parent);
        }else{ // node is leaf
            int i = node.keys.indexOf(key);
            if (node.keys.size() > t || node == this.root){ // has enough to spare
                node.keys.remove(i);
                node.values.remove(i);
                return null;
            }
            if (redistributeAt(parent.children.indexOf(node), parent)){
                return null;
            }
            return mergeAT(parent.children.indexOf(node), parent);
        }
    }
    /**
     * 1. Redistribte within the parent node due to
     * insuffficient children within parent.child_i.
     * 2. Keeps the integrity of parent.keys()
     * @param i - the (children) index of the node requesting redistribution
     * @param parent
     * @return true if redistribution is successful
     */
    private boolean redistributeAt(int i, BTreeNode parent){
        if(!parent.children.get(i).leaf){
            // HARD: non-leaf redistribution, push through
            if (i > 0 && parent.children.get(i - 1).keys.size() > t){
                // left push to right
                BTreeNode leftNode = parent.children.get(i - 1);
                BTreeNode rightNode = parent.children.get(i);
                // move keys
                rightNode.keys.add(0, parent.keys.remove(i - 1));
                parent.keys.add(i-1, leftNode.keys.remove(leftNode.keys.size() - 1));
                // move children
                rightNode.children.add(0, leftNode.children.remove(leftNode.children.size() - 1));
                return true;
            }else if (i < parent.children.size() - 1 &&
            parent.children.get(i + 1).keys.size() > t){
                // right push to left
                BTreeNode leftNode = parent.children.get(i);
                BTreeNode rightNode = parent.children.get(i+1);
                // move keys
                leftNode.keys.add(parent.keys.remove(i));
                parent.keys.add(i, rightNode.keys.remove(0));
                // move children
                leftNode.children.add(rightNode.children.remove(0));
                return true;
            }else{
                // unable to redistribute
                return false;
            }
        }else{ // node is leaf
            if (i > 0 && parent.children.get(i - 1).keys.size() > t){ // steal from left
                BTreeNode leftNode = parent.children.get(i - 1);
                BTreeNode rightNode = parent.children.get(i);
                // move keys
                rightNode.keys.add(0, leftNode.keys.remove(leftNode.keys.size() - 1));
                // move values
                rightNode.values.add(0, leftNode.values.remove(leftNode.values.size() - 1));
                return true;
            }else if (i < parent.children.size() - 1 &&
            parent.children.get(i + 1).keys.size() > t){
                // steal from right
                BTreeNode rightNode = parent.children.get(i+1);
                BTreeNode leftNode = parent.children.get(i);
                // move keys
                leftNode.keys.add(rightNode.keys.remove(0));
                // move values
                leftNode.values.add(rightNode.values.remove(0));
                return true;
            }else{
                // neither left nor right has enough
                return false;
            }
        }

    }

    /**
     * 
     * @param i - the index of the node requesting merging
     * @param parent
     * @return the emptied Node reference
     */
    private BTreeNode mergeAT(int i, BTreeNode parent){
        int left, right = -1;
        if ( i > 0 ){ // merge with left
            left = i - 1;
            right = i;
        }else{ // merge with right
            left = i;
            right = i + 1;
        }
        BTreeNode leftNode = parent.children.get(left);
        BTreeNode rightNode = parent.children.get(right);
        long splitting = parent.keys.get(left);
        if (leftNode.leaf){
            parent.keys.remove(left);
            leftNode.keys.addAll(rightNode.keys);
            leftNode.values.addAll(rightNode.values);
            leftNode.next = rightNode.next;
            rightNode.next = null;
            return rightNode;
        }else{
            parent.keys.remove(left);
            leftNode.keys.add(splitting);
            leftNode.keys.addAll(rightNode.keys);
            leftNode.children.addAll(rightNode.children);
            return rightNode;
        }
        
    }

    List<Long> print() {

        List<Long> listOfRecordID = new ArrayList<>();

        /**
         * TODO:
         * Implement this function to print the B+Tree.
         * Return a list of recordIDs from left to right of leaf nodes.
         *
         */
        listOfRecordID = helpPrint(root);
        return listOfRecordID;
    }
    
    List<Long> helpPrint(BTreeNode current) {
        if (current == null){
            return new ArrayList<>();
        }
    	if(!current.leaf) {
    		return helpPrint(current.children.get(0));
    	}
    	else {
    		List<Long> listOfRecordID = new ArrayList<>();
    		boolean nextNodeExists = true;
    		BTreeNode currNode = current;
    		while(nextNodeExists) {
    			for(int i = 0; i < currNode.keys.size(); i++) {
    				listOfRecordID.add(currNode.values.get(i));
    			}
    			if(currNode.next == null) {
    				nextNodeExists = false;
    			}
    			currNode = currNode.next;
    		}
    		return listOfRecordID;
    	}
    }
}
