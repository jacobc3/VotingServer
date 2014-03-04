package background;

public class MyMessage {
	String sender;
	String content;
	public MyMessage(String aSender,String aContent){
		this.sender = aSender;
		this.content = aContent;
	}

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
