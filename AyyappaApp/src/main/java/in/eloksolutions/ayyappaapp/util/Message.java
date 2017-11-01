package in.eloksolutions.ayyappaapp.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Message implements Comparable<Message>{
	private int eventId;
	private String msgId;
	private String message;
	private Date dat;
	private String sDate;
	private String msgSenderId="";
	private String msgSenderName="";
	private boolean isSelf;

	public boolean isSelf() {
		return isSelf;
	}

	public void setSelf(boolean self) {
		isSelf = self;
	}

	public String getMsgSenderId() {
		return msgSenderId;
	}

	public void setMsgSenderId(String msgSenderId) {
		this.msgSenderId = msgSenderId;
	}

	public String getMsgSenderName() {
		return msgSenderName;
	}

	public void setMsgSenderName(String msgSenderName) {
		this.msgSenderName = msgSenderName;
	}

	public int getEventId() {
		return eventId;
	}
	public String getMsgId() {
		return msgId;
	}
	public String getMessage() {
		return message;
	}
	public Date getDat() {
		return dat;
	}
	public String getsDate() {
		return sDate;
	}


	public Message(String msg, Date dat, String from) {
		super();
		//mssage splitting OWNERID##@@OWNERNAME##@@MESSAGE
		String[] tokens=msg.split(Constants.CHAT_MESSAGE_TOKENIZER);
		System.out.println("tokens are "+ Arrays.asList(tokens));
		String sEventId=from.substring(from.lastIndexOf("-")+1);
		this.eventId=Integer.parseInt(sEventId);
		//this.msgId = msgId;
		//Message owner
		this.message =tokens[2];
		this.msgSenderId=tokens[0];
		this.msgSenderName=tokens[1];
		this.dat = dat;
		this.sDate = new SimpleDateFormat("dd MMMM HH:mm").format(dat);

	}
	public Message(String eventId,String message,String dat,String msgSenderName,String msgSenderId) {
		super();
		this.eventId=Integer.parseInt(eventId);
		this.message=message;
		this.sDate =dat;
		try {
			this.dat=Constants.sdf.parse(dat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.msgSenderName=msgSenderName;
		this.msgSenderId=msgSenderId;
	}

		public Message(String sEventId, String message, Date dat,String from,String msgOwner,String msgOwnerName) {
		super();
		if(sEventId!=null && sEventId.length()>0)
			this.eventId=Integer.parseInt(sEventId);
		this.msgId = msgId;
		//Message owner
		this.message = message;
		this.msgSenderId=msgOwner;
		this.msgSenderName=msgOwnerName;
		this.isSelf=true;
			this.dat = dat;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (eventId != other.eventId)
		return false;
		return true;
	}

	@Override
	public String toString() {
		return "Message{" +
				"eventId=" + eventId +
				", msgId='" + msgId + '\'' +
				", message='" + message + '\'' +
				", dat=" + dat +
				", sDate='" + sDate + '\'' +
				", msgSenderId='" + msgSenderId + '\'' +
				", msgSenderName='" + msgSenderName + '\'' +
				'}';
	}

	public String serialize(){
		return eventId+FileUtil.DELIMITER+message+FileUtil.DELIMITER+Constants.sdf.format(dat)+ FileUtil.DELIMITER +msgSenderName+ FileUtil.DELIMITER +msgSenderId;
	}
	@Override
	public int compareTo(Message o) {
		return o.getDat().compareTo(dat);
	}
}
