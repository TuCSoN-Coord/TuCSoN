package alice.tucson.service;

import java.util.UUID;

import alice.tucson.api.acc.RootACC;

/**
 *
 * @author ste (mailto: s.mariani@unibo.it)
 * @author (contributor) Emanuele Buccelli
 */
public class RootACCProxy implements RootACC {

    @Override
    public void enterACC() {
        // TODO Auto-generated method stub

    }

    @Override
    public void exit() {
        /*
         *
         */
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUUID() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isACCEntered() {
        // TODO Auto-generated method stub
        return false;
    }

}
