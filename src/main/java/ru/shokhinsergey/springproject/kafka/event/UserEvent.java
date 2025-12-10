package ru.shokhinsergey.springproject.kafka.event;

import static ru.shokhinsergey.springproject.kafka.event.UserEvent.Operation.CREATE;
import static ru.shokhinsergey.springproject.kafka.event.UserEvent.Operation.DELETE;

public class UserEvent {
    public static enum Operation {DELETE, CREATE}
    private Operation operation;
    private String email;

    public static UserEvent instanceOfUserEventOnCreate(String email){
        return new UserEvent(CREATE, email);
    }

    public static UserEvent instanceOfUserEventOnDelete(String email){
        return new UserEvent(DELETE, email);
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public UserEvent(Operation operation, String email) {
        this.operation = operation;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserEvent() {
    }

}
