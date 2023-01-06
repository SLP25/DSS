package org.example.databaseScripts;

import org.example.data.*;

/**
 * A class that only used to destroy our database.
 * <p>
 * This class is named after the Marvel Comics character Thanos, who is known for
 * wiping out half of the universe's population with a snap of his fingers. Similarly,
 * the main method of this class allows the user to destroy the entire database with
 * a single command.
 *
 * @see <a href="https://marvel.fandom.com/wiki/Thanos">Thanos</a>
 */
public class ThanosDB {

    /**
     * This method gets each table of the database and destroys it.
     * @param args Argument list of passed parameters, never used.
     */
    public static void main(String[] args) {

        AdminDAO.getInstance().clear(); /* Get admin table. */
        PlayerDAO.getInstance().clear(); /* Get player table. */

        for (Integer c : ChampionshipDAO.getInstance().keySet()) {
            ParticipantDAO.getInstance(c).clear();
            RaceDAO.getInstance(c).clear();
        }

        ChampionshipDAO.getInstance().clear();
    }
}


