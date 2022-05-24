package enums;

/**
Status - последовательность, содержащая значения для задач.
 */
public enum Status {

    NEW ("NEW"),
    IN_PROGRESS ("IN_PROGRESS"),
    DONE ("DONE");

    private String value;
    Status(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
