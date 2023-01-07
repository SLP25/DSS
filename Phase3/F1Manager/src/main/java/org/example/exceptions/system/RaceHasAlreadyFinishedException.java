package org.example.exceptions.system;

import org.example.business.Race;

public class RaceHasAlreadyFinishedException extends SystemException {
    public RaceHasAlreadyFinishedException(Race r) {
        super(String.format("Race '%d' has already finished", r.getId()));
    }
}
