package background;

import java.util.ArrayList;

public class Processor {
	ArrayList<MyMessage> listMsg = new ArrayList<MyMessage>();
    ArrayList<Candidate> listCand = new ArrayList<Candidate>();
    //ArrayList<Integer> listVoted = new ArrayList<Integer>();
    boolean run = false;
    int dup = 0;
    int numCand = 0;
	
	public void addMessage(MyMessage m){
		listMsg.add(m);
     /*   int pn = Integer.parseInt(m.getSender());
        if (!isDuplicate(m)) {
            listVoted.add(pn);
        }
	}

    public boolean isDuplicate(MyMessage m) {
        boolean dupe = true;
        int pn = Integer.parseInt(m.getSender());
        for (int v : listVoted) {
            if (v == pn) {
                dupe = false;
            }
        }
        return dupe; */
    }

    public void createCandidates(int n) {
        if (n > numCand) {
            for (int i = numCand+1; i <= n; i++) {
                listCand.add(new Candidate(i));
            }
        } else if (n < numCand) {
            for (int i = numCand-1; i < numCand; i++) {
                listCand.remove(i);
            }
        }
        numCand = n;
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
