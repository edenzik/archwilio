package main;

/**
 * Created by zhan on 7/30/15.
 */
public class Semester {

    private int year;
    private String term;

    public Semester(int year, String term) {
        this.year = year;
        this.term = term;
    }

    public Semester() {
        year = -1;
        term = "";
    }

    public Semester(Semester semester) {
        this.year = semester.year;
        this.term = semester.term;
    }

    /**
     * computer next semester
     * @return next semester
     */
    public Semester next_semester() {
        Semester next = new Semester();
        if (this.term.equals("Fall")) {
            next.year = this.year + 1;
            next.term = "Spring";
        }
        else {
            next.year = this.year;
            next.term = "Fall";
        }
        return next;
    }

    /**
     * computer how many semesters between this and other semester
     * @param other
     * @return the number of semesters between this and other semester
     */
    public int subtract(Semester other) {
        int count = 1;
        while (!other.equals(this)) {
            count += 1;
            other = other.next_semester();
        }
        return count;
    }

    public int year() {
        return this.year;
    }

    public String term() {
        return this.term;
    }

    public String toString() {
        return term() + year();
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + term.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Semester))
            return false;
        if (obj == this)
            return true;
        Semester s = (Semester) obj;
        return this.year == s.year && this.term.equals(s.term);
    }
}
