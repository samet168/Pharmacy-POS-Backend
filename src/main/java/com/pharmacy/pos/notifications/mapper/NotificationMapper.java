package com.pharmacy.pos.notifications.mapper;

import com.pharmacy.pos.notifications.dto.NotificationRequest;
import com.pharmacy.pos.notifications.dto.NotificationResponse;
import com.pharmacy.pos.notifications.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .organizationId(notification.getOrganization() != null ? notification.getOrganization().getId() : null)
                .userId(notification.getUser() != null ? notification.getUser().getId() : null)
                .type(notification.getType())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .read(notification.isRead())
                .metadata(notification.getMetadata())
                .actionUrl(notification.getActionUrl())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }
}