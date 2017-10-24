package in.eloksolutions.ayyappa.messaging;

import in.eloksolutions.ayyappa.http.RestServices;


public class SendNotification {

	private static final String FCM_URL="https://fcm.googleapis.com/fcm/send";
	public static void sendMessageToGroup(String groupId,String topicId, String topicName) {
		String json= buildJSON(groupId,topicId,topicName);
		System.out.println("Sending json "+json);
		try {
			RestServices.postJSON(FCM_URL, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void sendMessageToTopic(String topicId, String topicName) {
		String json= buildJSONForTopic(topicId,topicName);
		System.out.println("Sending json "+json);
		try {
			RestServices.postJSON(FCM_URL, json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static String buildJSONForTopic(String topicId, String topicName) {
		return "{\"to\":\"/topics/"+topicId+"\",\"notification\":{\"body\":\""+topicName+"\",\"title\":\"New comment on Topic\",\"icon\" : \"logo\"},\"data\":{\"topicId\":\""+topicId+"\"}}";
	}
	private static String buildJSON(String groupId,String topicId, String topicName) {
		return "{\"to\":\"/topics/"+groupId+"\",\"notification\":{\"body\":\""+topicName+"\",\"title\":\"New Topic on Group\",\"icon\" : \"logo\"},\"data\":{\"groupId\":\""+groupId+"\"}}";
	}
}
