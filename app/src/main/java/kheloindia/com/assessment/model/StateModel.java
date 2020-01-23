package kheloindia.com.assessment.model;

public class StateModel {
    private String stateId,stateName;

    public StateModel(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public String getStateId() {
        return stateId;
    }

    public String getStateName() {
        return stateName;
    }
}
