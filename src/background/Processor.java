package background;

import java.util.ArrayList;

public class Processor {
	ArrayList<MyMessage> list = new ArrayList<MyMessage>();
	
	public void addMessage(MyMessage m){
		if(!isExist(m)){
			list.add(m);
		}
	}
	public boolean isExist(MyMessage m){
		for (MyMessage m2 : list) {
			if(m2.isDuplicated(m)){
				return true;
			}
		}
		return false;
	}
}
