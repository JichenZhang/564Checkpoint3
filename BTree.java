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
        /**
         * TODO:
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */
        return true;
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
