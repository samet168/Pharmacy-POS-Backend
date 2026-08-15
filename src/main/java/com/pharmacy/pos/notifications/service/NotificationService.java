package com.pharmacy.pos.notifications.service;

import com.pharmacy.pos.notifications.dto.NotificationRequest;
import com.pharmacy.pos.notifications.dto.NotificationResponse;
import com.pharmacy.pos.notifications.entity.Notification;
import com.pharmacy.pos.notifications.mapper.NotificationMapper;
import com.pharmacy.pos.notifications.repository.NotificationRepository;
import com.pharmacy.pos.iam.entity.User;
import com.pharmacy.pos.iam.repository.UserRepository;
import com.pharmacy.pos.tenant.entity.Organization;
import com.pharmacy.pos.tenant.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    public NotificationResponse create(NotificationRequest request) {
        Organization organization = organizationRepository.findById(request.getOrganizationId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        User user = null;
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        Notification notification = Notification.builder()
                .organization(organization)
                .user(user)
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .read(false)
                .metadata(request.getMetadata())
                .actionUrl(request.getActionUrl())
                .build();

        notification = notificationRepository.save(notification);
        return notificationMapper.toResponse(notification);
    }

    public NotificationResponse getById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        return notificationMapper.toResponse(notification);
    }

    public Page<NotificationResponse> getByUserId(Long userId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByUserId(userId, pageable);
        return notifications.map(notificationMapper::toResponse);
    }

    public Page<NotificationResponse> getByOrganizationId(Long organizationId, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByOrganizationId(organizationId, pageable);
        return notifications.map(notificationMapper::toResponse);
    }

    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notification = notificationRepository.save(notification);
        return notificationMapper.toResponse(notification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadByUserId(userId);
        unreadNotifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}