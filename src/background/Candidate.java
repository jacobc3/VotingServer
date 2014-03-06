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

    public int getCandID() {
        return candID;
    }

    public int getNumVotes() {
        return numVotes;
    }
}
