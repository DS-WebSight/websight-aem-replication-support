package pl.ds.websight.aem.replication.support;

public enum ReplicationActionType {

    ACTIVATE("Activate"), DEACTIVATE("DeActivate");

    private final String name;

    ReplicationActionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
