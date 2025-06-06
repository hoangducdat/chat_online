package org.project.hubt.chat_online.repository;

import org.project.hubt.chat_online.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
  
  List<FriendRequest> findByReceiverIdAndStatus(Long receiverId, FriendRequest.RequestStatus status);
  
  List<FriendRequest> findBySenderIdAndStatus(Long senderId, FriendRequest.RequestStatus status);
  
  Optional<FriendRequest> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
  
  @Query("SELECT fr FROM FriendRequest fr WHERE " +
         "(fr.sender.id = :userId OR fr.receiver.id = :userId) " +
         "AND fr.status = :status " +
         "ORDER BY fr.createdAt DESC")
  List<FriendRequest> findAllByUserIdAndStatus(@Param("userId") Long userId, 
                                               @Param("status") FriendRequest.RequestStatus status);
}