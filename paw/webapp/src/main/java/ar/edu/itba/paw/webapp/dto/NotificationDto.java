package ar.edu.itba.paw.webapp.dto;

public class NotificationDto {

    private long unread;

    public static NotificationDto fromNumber(long unreadCount) {
        NotificationDto dto = new NotificationDto();
        dto.setUnread(unreadCount);
        return dto;
    }

    /* Getters and setters */

    public long getUnread() {
        return unread;
    }

    public void setUnread(long unread) {
        this.unread = unread;
    }
}
