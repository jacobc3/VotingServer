package background;

public class MyMessage {
	String sender;
	String content;
	public MyMessage(String aSender,String aContent){
		this.sender = aSender;
		this.content = aContent;
	}
	
	public boolean isDuplicated(MyMessage compareTo){
		if(sender.equals(compareTo.sender) && content.equals(compareTo.content)){
			return true;
		}
		return false;
	}
}