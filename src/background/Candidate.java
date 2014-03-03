package background;

public class Candidate {
    private int candID;
    private int numVotes;

    public Candidate(int id) {
        this.candID = id;
        numVotes = 0;
    }

    public void voteCandidate() {
        numVotes++;
    }

    public void clearVotes() {
        numVotes = 0;
    }

    public String getCandID() {
        return Integer.toString(candID);
    }

    public String getNumVotes() {
        return Integer.toString(numVotes);
    }
}
