import java.util.Random;
/**
 * Represents a simple student class.
 * <p>
 * You do not need to change this class.
 */

public class Student {

    long studentId;
    long recordId;
    int age;
    String studentName;
    String major;
    String level;

    static Random rand = new Random();

    /**
     * This is an overload constructor -- It creates a new Students 
     * with a random recordId.
     * @param studentId
     * @param age
     * @param studentName
     * @param major
     * @param level
     */
    public Student(long studentId, int age, String studentName, String major, String level){
        this.studentId = studentId;
        this.age = age;
        this.studentName = studentName;
        this.major = major;
        this.level = level;
        this.recordId = rand.nextLong();
    }

    /**
     * This constructor creates a new Student based on 
     * a known recordId.
     * @param studentId
     * @param age
     * @param studentName
     * @param major
     * @param level
     * @param recordId
     */
    public Student(long studentId, int age, String studentName, String major, String level, long recordId) {
        this.studentId = studentId;
        this.age = age;
        this.studentName = studentName;
        this.major = major;
        this.level = level;
        this.recordId = recordId;
    }
}
