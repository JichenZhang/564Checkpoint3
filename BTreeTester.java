
public class BTreeTester {

    public static void testMessage(String s){
        System.out.printf("__________Testing %s _________\n", s);
    }

    /**
     * Prints out an assertion message. s1 is the value being tested, s2 is the
     * expected value. Throws an AssertionError if the assertion fails.
     * Deep down it compares the two objects's toString() method, whether they
     * return the same string value
     * 
     * @param s1 - the value being tested
     * @param s2 - the value expected
     */
    public static boolean assertEquals(Object s1, Object s2){
        if (s1.toString().equals(s2.toString())){
            System.out.println("OK testing " + s1);
            return true;
        }else{
            String errorMessage = String.format("Error: got %s, expected %s.\n", s1, s2);
            throw new AssertionError(errorMessage);
        }
    }

    /**
     * Tests the creation of the btree
     */
    public static void testCreate(){
        // TODO test the creation of the tree from file
    }

    /**
     * checks the insert and print function works correctly.
     */
    public static void testInsertAndPrint(){

        testMessage("Insert and Print");

        BTree t = new BTree(2);

        assertEquals(t.print(), "[]");

        t.insert(new Student(1, 20, "Amanda", "CS", "SR", 1));
        t.insert(new Student(5, 20, "Amanda", "CS", "SR", 5));
        t.insert(new Student(3, 20, "Amanda", "CS", "SR", 3));

        assertEquals(t.print(), "[1, 3, 5]");

        t.insert(new Student(2, 20, "Amanda", "CS", "SR", 2));
        t.insert(new Student(4, 20, "Amanda", "CS", "SR", 4));
        t.insert(new Student(6, 20, "Amanda", "CS", "SR", 6));

        assertEquals(t.print(), "[1, 2, 3, 4, 5, 6]");

    }

    /**
     * Tests whether the delete function works correctly on a b plus tree
     */
    public static void testDelete(){

        testMessage("Delete");
        BTree t = new BTree(3);
        assertEquals(t.delete(1), "false");

        t.insert(new Student(1, 20, "Amanda", "CS", "SR", 3));
        assertEquals(t.delete(1), "true");
        assertEquals(t.print(), "[]");

        t.insert(new Student(5, 20, "Amanda", "CS", "SR", 5));
        t.insert(new Student(1, 20, "Amanda", "CS", "SR", 1));
        t.insert(new Student(3, 20, "Amanda", "CS", "SR", 3));
        t.insert(new Student(2, 20, "Amanda", "CS", "SR", 2));
        assertEquals(t.delete(2), "true");
        assertEquals(t.delete(7), "false");
        assertEquals(t.print(), "[1, 3, 5]");

        t.insert(new Student(6, 20, "Amanda", "CS", "SR", 6));
        t.insert(new Student(2, 20, "Amanda", "CS", "SR", 2));
        t.insert(new Student(4, 20, "Amanda", "CS", "SR", 4));
        assertEquals(t.delete(10), "false");
        assertEquals(t.delete(1), "true");
        assertEquals(t.delete(2), "true");
        assertEquals(t.delete(3), "true");
        assertEquals(t.delete(4), "true");

        assertEquals(t.print(), "[5, 6]");

        // TODO - test for tree structures after deletion?
    }

	/**
	 * Checks whether search works correctly
	 */
	public static void testSearch() {

        testMessage("Search");
		Student b = new Student(1, 17, "Amanda", "CS", "SR", 2);
		BTree c = new BTree(2);

        assertEquals(c.search(5), -1);

		c.insert(b);
        assertEquals(c.search(1), 2);

		Student d = new Student(3, 18, "Artie", "PSYCH", "JR", 4);
		c.insert(d);
        assertEquals(c.search(3), 4);
        assertEquals(c.search(2), -1);

		Student e = new Student(2, 18, "Athena", "ART", "SO", 3);
		c.insert(e);
        assertEquals(c.search(2), 3);

		Student f = new Student(4, 18, "Ares", "MIL", "SR", 1);
		Student g = new Student(5, 18, "Aphrodite", "MIL", "SR", 1);
		Student h = new Student(6, 18, "Aphrodite", "MIL", "SR", 1);
		Student i = new Student(7, 18, "Aphrodite", "MIL", "SR", 1);
		Student j = new Student(8, 18, "Aphrodite", "MIL", "SR", 1);
		Student k = new Student(9, 18, "Aphrodite", "MIL", "SR", 1);
		c.insert(g);
		c.insert(f);
		c.insert(h);
		c.insert(i);
		c.insert(j);
		c.insert(k);
        assertEquals(c.search(4), 1);
        assertEquals(c.search(7), 1);
        assertEquals(c.search(8), 1);
        assertEquals(c.search(9), 1);
	}
	

	public static void main(String[] args) {
		testSearch();
        testInsertAndPrint();
        testDelete();
	}
}
