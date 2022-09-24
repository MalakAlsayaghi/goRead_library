package com.goread.library.admin.activities;

public class Notification {
    String NotificcationId;
    String NotificationTitle;
    String NotificationBody;
public Notification(){

}

    public Notification(String notificcationId, String notificationTitle, String notificationBody) {
        NotificcationId = notificcationId;
        NotificationTitle = notificationTitle;
        NotificationBody = notificationBody;
    }

    public String getNotificcationId() {
        return NotificcationId;
    }

    public String getNotificationTitle() {
        return NotificationTitle;
    }

    public String getNotificationBody() {
        return NotificationBody;
    }
}
