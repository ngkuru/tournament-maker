package tournament;

/**
 * This enum represents the possible directions to order elements.
 */
public enum Order {
    ASC, DESC;
    
    @Override
    public String toString() {
        switch (this) {
        case ASC:
            return "ASC";
        case DESC:
            return "DESC";
        default:
            return "";
        }
    }
}
