package jaram.groupware.groupware.persistent;

import jaram.groupware.groupware.model.value.*;

public class Member implements Comparable<Member> {
    private CardinalNumber cardinalNumber;
    private Name name;
    private Position position;
    private Phone phone;
    private Email email;
    private AttendingState attendingState;

    public Member() { }

    public Member(CardinalNumber cardinalNumber, Name name, Position position, Phone phone, Email email, AttendingState attendingState){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.attendingState = attendingState;
    }

    public Member(CardinalNumber cardinalNumber, Name name, Phone phone, Email email){
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = Position.수습;
        this.attendingState = AttendingState.재학;
    }

    public void updateMember(CardinalNumber cardinalNumber, Name name, Position position, Phone phone, Email email, AttendingState attendingState) {
        this.cardinalNumber = cardinalNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.attendingState = attendingState;
    }

    public int getCardinalNumber() {
        return cardinalNumber.getCardinalNumber();
    }

    public String getName() {
        return name.getName();
    }

    public String getPosition() {
        return position.toString();
    }

    public String getPhone() {
        return phone.getPhone();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public String getAttendingState() {
        return attendingState.toString();
    }

    @Override
    public int compareTo(Member o) {
        if (this.getCardinalNumber() > o.getCardinalNumber()) {
            return 1;
        } else if (this.getCardinalNumber() < o.getCardinalNumber()) {
            return -1;
        } else {
            if (this.getName().compareTo(o.getName()) == 1) {
                return 1;
            } else if (this.getName().compareTo(o.getName()) == -1) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
