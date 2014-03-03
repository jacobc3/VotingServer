package background;

import java.util.ArrayList;

public class Processor {
	ArrayList<MyMessage> listMsg = new ArrayList<MyMessage>();
    ArrayList<Candidate> listCand = new ArrayList<Candidate>();
    boolean run = false;
    int dup = 0;
    int numCand = 0;
	
	public void addMessage(MyMessage m){
		if(!isExist(m)){
			listMsg.add(m);
		}
	}
	public boolean isExist(MyMessage m){
		for (MyMessage m2 : listMsg) {
			if(m2.isDuplicated(m)){
				return true;
			}
		}
		return false;
	}

    public void createCandidates(int n) {
        numCand = n;
        for (int i = 1; i <= numCand; i++) {
            listCand.add(new Candidate(i));
        }
    }

    public ArrayList<Candidate> getCandidateList() {

        return listCand;
    }

    public int getNumCandidates() {
        return numCand;
    }

    public void runServer() {
        run = true;
    }

    public void stopServer() {
        run = false;
    }

    public boolean isRunning() {
        return run;
    }

    public void dupToggle() {
        dup++;
    }

    public boolean allowDupes() {
        if (dup % 2 == 0)
            return false;
        else
            return true;
    }

    public void voteFor(int cand) {
        listCand.get(cand-1).voteCandidate();
    }

    public void clearVotes() {
        for (Candidate c : listCand) {
            c.clearVotes();
        }
    }
}
