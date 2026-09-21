package ringotel.model;

public class ContactAuditEntry {

    private final String name;
    private final int occurrence;
    private final boolean hasPhoneNumber;

    public ContactAuditEntry(
            String name,
            int occurrence,
            boolean hasPhoneNumber
    ) {

        this.name = name;
        this.occurrence = occurrence;
        this.hasPhoneNumber = hasPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public boolean hasPhoneNumber() {
        return hasPhoneNumber;
    }

    @Override
    public String toString() {

        return "ContactAuditEntry{" +
                "name='" + name + '\'' +
                ", occurrence=" + occurrence +
                ", hasPhoneNumber=" + hasPhoneNumber +
                '}';
    }
}