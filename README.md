
### 1. Phân tích yêu cầu và mục tiêu của đồ án

#### 1.1 Yêu cầu ban đầu
- **Mục tiêu chính**: Xây dựng một phần mềm chat online cho phép hai người dùng (Boy Friend và Girl Friend) trò chuyện với nhau qua internet.
- **Yêu cầu cụ thể**:
  - Hiển thị hai khung chat song song, mỗi khung đại diện cho góc nhìn của một người dùng (Boy Friend và Girl Friend).
  - Hiển thị đầy đủ lịch sử tin nhắn xen kẽ giữa hai người dùng (cả tin nhắn gửi và nhận).
  - Hỗ trợ gửi tin nhắn thời gian thực (real-time) và lưu trữ lịch sử tin nhắn.
  - Giao diện thân thiện, dễ sử dụng, với avatar và thời gian cho mỗi tin nhắn.
- **Vấn đề ban đầu**: Bạn gặp khó khăn vì lịch sử tin nhắn không hiển thị đầy đủ (chỉ hiển thị một chiều, ví dụ: tin nhắn của User 1 gửi User 2, nhưng không có tin nhắn của User 2 gửi User 1).
- **Yêu cầu bổ sung**:
  - Hoàn thiện báo cáo theo mẫu đã cung cấp, bao gồm các phần như phân tích, chi tiết đồ án, cơ sở dữ liệu, và kết luận.
  - Thêm bảng `friends` vào cơ sở dữ liệu để quản lý mối quan hệ bạn bè.
  - Giải thích chi tiết cách cấu hình, luồng đi của dữ liệu, và tích hợp với giao diện.

#### 1.2 Mục tiêu của mình
- Sửa lỗi hiển thị lịch sử tin nhắn để cả hai khung chat đều hiển thị đầy đủ tin nhắn (cả gửi và nhận).
- Xây dựng một hệ thống hoàn chỉnh với backend (Spring Boot, WebSocket, JPA) và frontend (HTML, CSS, JavaScript).
- Thiết kế cơ sở dữ liệu để lưu trữ người dùng, mối quan hệ bạn bè, và tin nhắn.
- Viết báo cáo chi tiết theo mẫu, bao gồm mã nguồn và giải thích kỹ thuật.
- Giải thích rõ ràng cách cấu hình, luồng dữ liệu, và tích hợp để bạn hiểu toàn bộ quy trình.

---

### 2. Thiết kế hệ thống

#### 2.1 Kiến trúc tổng quan
- **Mô hình client-server**:
  - **Server**: Xử lý kết nối, lưu trữ tin nhắn, và cung cấp API để lấy lịch sử tin nhắn.
  - **Client**: Giao diện người dùng hiển thị hai khung chat, gửi/nhận tin nhắn qua WebSocket, và gọi API để lấy lịch sử.
- **Công nghệ sử dụng**:
  - **Backend**: Spring Boot (Java), WebSocket (thời gian thực), JPA/Hibernate (kết nối database).
  - **Frontend**: HTML, CSS, JavaScript, SockJS, và STOMP (giao tiếp WebSocket).
  - **Database**: MySQL (lưu trữ người dùng, bạn bè, và tin nhắn).

#### 2.2 Thiết kế cơ sở dữ liệu
Cơ sở dữ liệu gồm 3 bảng chính:
- **Bảng `users`**: Lưu thông tin người dùng (ID, username, avatar).
- **Bảng `friends`**: Quản lý mối quan hệ bạn bè (user_id, friend_id, status, created_at).
- **Bảng `messages`**: Lưu trữ tin nhắn (sender_id, receiver_id, content, timestamp).

Cấu trúc chi tiết:
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    avatar VARCHAR(255)
);

CREATE TABLE friends (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    friend_id BIGINT NOT NULL,
    status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (friend_id) REFERENCES users(id),
    UNIQUE (user_id, friend_id)
);

CREATE TABLE messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    content VARCHAR(500) NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id)
);
```

Dữ liệu mẫu:
```sql
INSERT INTO users (id, username, avatar) VALUES
(1, 'Boy Friend', 'https://encrypted-tbn0.gstatic.com/images?q=tbn9GcQ9WlxxD8L281LL-n75U77M_Xqi_MGvhmFcBA&s'),
(2, 'Girl Friend', 'https://encrypted-tbn0.gstatic.com/images?q=tbn9GcTSx-H9nW3f2dSUAffNuyp5XO5vEDca22niiw&s');

INSERT INTO friends (user_id, friend_id, status, created_at) VALUES
(1, 2, 'ACCEPTED', '2025-03-14 01:00:00'),
(2, 1, 'ACCEPTED', '2025-03-14 01:00:00');

INSERT INTO messages (sender_id, receiver_id, content, timestamp) VALUES
(1, 2, 'HELLO', '2025-03-14 02:00:45'),
(2, 1, 'XIN CHÀO', '2025-03-14 02:03:49'),
(2, 1, 'AAAA', '2025-03-14 02:03:52'),
(1, 2, 'ADFFFF', '2025-03-14 02:03:56'),
(1, 2, 'HIII', '2025-03-14 02:04:01'),
(2, 1, 'VADASAD', '2025-03-14 02:04:06');
```

#### 2.3 Thiết kế giao diện
- **Giao diện frontend** (`index.html`):
  - Hai khung chat song song: một cho Boy Friend (User 1), một cho Girl Friend (User 2).
  - Mỗi khung có:
    - Header với avatar và tên người dùng.
    - Khu vực hiển thị tin nhắn (hiển thị xen kẽ "sent" và "received").
    - Ô nhập tin nhắn và nút gửi.
  - CSS được thiết kế để phân biệt tin nhắn gửi (căn phải, màu xanh) và nhận (căn trái, màu xám).

#### 2.4 Thiết kế luồng dữ liệu
- **Lấy lịch sử tin nhắn**:
  - Client gọi API `/api/chat/history/1/2` -> Server kiểm tra mối quan hệ bạn bè -> Truy vấn database -> Trả về danh sách tin nhắn -> Client hiển thị.
- **Gửi tin nhắn thời gian thực**:
  - Client gửi tin nhắn qua WebSocket (`/app/sendMessage`) -> Server lưu vào database -> Server phát tin nhắn đến kênh `/topic/messages` -> Client nhận và hiển thị.

---

### 3. Triển khai Backend (Server)

#### 3.1 Cấu hình Spring Boot
- **Dependency**: Thêm các dependency cần thiết (Spring Boot Web, WebSocket, JPA, MySQL).
- **Database**: Cấu hình kết nối MySQL trong `application.properties`.
- **WebSocket**: Cấu hình WebSocket để hỗ trợ gửi/nhận tin nhắn thời gian thực.

#### 3.2 Xây dựng các thành phần chính
- **Entity**:
  - `User.java`, `Friend.java`, `Message.java`: Định nghĩa các bảng trong database.
- **Repository**:
  - `UserRepository`, `FriendRepository`, `MessageRepository`: Truy vấn dữ liệu từ database.
  - Đặc biệt, `MessageRepository` có truy vấn để lấy lịch sử tin nhắn:
    ```java
    @Query("SELECT m FROM Message m WHERE (m.sender.id = :userId AND m.receiver.id = :friendId) OR (m.sender.id = :friendId AND m.receiver.id = :userId) ORDER BY m.timestamp ASC")
    List<Message> findChatHistory(@Param("userId") Long userId, @Param("friendId") Long friendId);
    ```
- **Service**:
  - `MessageService`: Xử lý logic lưu tin nhắn và lấy lịch sử tin nhắn, bao gồm kiểm tra mối quan hệ bạn bè.
- **Controller**:
  - `ChatController`: Xử lý API và WebSocket:
    - API `/api/chat/history/{userId}/{friendId}`: Lấy lịch sử tin nhắn.
    - WebSocket `/app/sendMessage`: Nhận tin nhắn từ client, lưu vào database, và phát lại.

#### 3.3 Sửa lỗi hiển thị lịch sử tin nhắn
- **Vấn đề ban đầu**: API `/api/chat/history/1/2` không trả về đầy đủ tin nhắn (chỉ lấy tin nhắn từ User 1 đến User 2, không có chiều ngược lại).
- **Giải pháp**:
  - Sửa truy vấn trong `MessageRepository` để lấy cả hai chiều tin nhắn (dùng `OR` trong câu truy vấn).
  - Đảm bảo dữ liệu trong database có đủ tin nhắn mẫu cho cả hai chiều.

---

### 4. Triển khai Frontend (Client)

#### 4.1 Xây dựng giao diện (`index.html`)
- **HTML**: Tạo hai khung chat với các phần: header, khu vực tin nhắn, và ô nhập liệu.
- **CSS**: Thiết kế giao diện:
  - Tin nhắn gửi: Căn phải, màu xanh.
  - Tin nhắn nhận: Căn trái, màu xám.
  - Hiển thị avatar và thời gian cho mỗi tin nhắn.
- **JavaScript**:
  - Sử dụng SockJS và STOMP để kết nối WebSocket.
  - Gọi API `/api/chat/history/1/2` để lấy lịch sử tin nhắn.
  - Hiển thị tin nhắn thời gian thực qua WebSocket.

#### 4.2 Sửa lỗi hiển thị tin nhắn
- **Vấn đề**: Lịch sử tin nhắn không hiển thị đầy đủ trong cả hai khung.
- **Giải pháp**:
  - Sửa hàm `loadChatHistory` để hiển thị tất cả tin nhắn trong cả hai khung:
    ```javascript
    messages.forEach(msg => {
        var messageDiv1 = createMessageElement(msg, 1); // Góc nhìn Boy Friend
        var messageDiv2 = createMessageElement(msg, 2); // Góc nhìn Girl Friend
        user1Messages.appendChild(messageDiv1);
        user2Messages.appendChild(messageDiv2);
    });
    ```
  - Đảm bảo hàm `createMessageElement` phân biệt đúng tin nhắn gửi/nhận dựa trên `currentUserId`.

#### 4.3 Tích hợp WebSocket
- Kết nối WebSocket khi trang tải:
  ```javascript
  var socket = new SockJS('http://localhost:8080/chat');
  var stompClient = Stomp.over(socket);
  stompClient.connect({}, frame => {
      loadChatHistory();
      stompClient.subscribe('/topic/messages', message => {
          var msg = JSON.parse(message.body);
          displayMessage(msg);
      });
  });
  ```
- Gửi tin nhắn qua WebSocket:
  ```javascript
  function sendMessage(senderId, receiverId) {
      var input = senderId === 1 ? document.getElementById('input-user1') : document.getElementById('input-user2');
      var content = input.value;
      if (content && stompClient) {
          var msg = { sender: { id: senderId }, receiver: { id: receiverId }, content: content, timestamp: new Date().toISOString() };
          stompClient.send("/app/sendMessage", {}, JSON.stringify(msg));
          input.value = '';
      }
  }
  ```

---

### 5. Hoàn thiện báo cáo

#### 5.1 Cấu trúc báo cáo
Báo cáo được xây dựng theo mẫu bạn cung cấp, bao gồm:
- **Lời nói đầu**: Giới thiệu về công nghệ thông tin và lý do làm đồ án.
- **Chương 1 - Đặt vấn đề**: Giới thiệu tổng quan và lý do chọn đề tài.
- **Chương 2 - Nội dung chính**:
  - **2.1 Phân tích, xây dựng chức năng**: Mô tả các chức năng của server và client.
  - **2.2 Chi tiết đồ án**:
    - **2.2.1 Chi tiết form server**: Mô tả server và code (Spring Boot, WebSocket).
    - **2.2.2 Chi tiết form client**: Mô tả giao diện và code (`index.html`).
    - **2.2.3 Cơ sở dữ liệu**: Mô tả bảng `users`, `friends`, `messages`, và dữ liệu mẫu.
- **Chương 3 - Kết luận**: Đánh giá ưu/nhược điểm và cảm ơn.

#### 5.2 Bổ sung bảng `friends`
- Thêm bảng `friends` vào cơ sở dữ liệu để quản lý mối quan hệ bạn bè.
- Tích hợp kiểm tra mối quan hệ bạn bè trong `MessageService`:
  ```java
  Friend friendship = friendRepository.findByUserIdAndFriendId(userId, friendId);
  if (friendship == null || !friendship.getStatus().equals(Friend.Status.ACCEPTED)) {
      throw new IllegalStateException("Users are not friends");
  }
  ```

#### 5.3 Giải thích cấu hình, luồng đi, và tích hợp
- **Cấu hình**:
  - Cấu hình Spring Boot, WebSocket, và database (MySQL).
  - Cấu hình CORS để frontend gọi API.
- **Luồng đi**:
  - Lấy lịch sử tin nhắn: Client -> API -> Server -> Database -> Client.
  - Gửi tin nhắn: Client -> WebSocket -> Server -> Database -> WebSocket -> Client.
- **Tích hợp**:
  - Frontend gọi API `/api/chat/history/1/2` để lấy lịch sử tin nhắn.
  - Sử dụng WebSocket để gửi/nhận tin nhắn thời gian thực.
  - Hiển thị tin nhắn trên giao diện với CSS phân biệt "sent" và "received".

---

### 6. Kết quả đạt được

#### 6.1 Chức năng hoàn thiện
- **Hiển thị lịch sử tin nhắn**: Cả hai khung chat hiển thị đầy đủ tin nhắn (cả gửi và nhận), sắp xếp theo thời gian.
- **Gửi tin nhắn thời gian thực**: Người dùng có thể gửi tin nhắn và thấy ngay trên cả hai khung.
- **Giao diện thân thiện**: Giao diện trực quan, phân biệt rõ tin nhắn gửi/nhận, có avatar và thời gian.
- **Lưu trữ tin nhắn**: Tin nhắn được lưu vào database và có thể truy xuất lại.

#### 6.2 Báo cáo hoàn chỉnh
- Báo cáo được viết theo mẫu, bao gồm đầy đủ các phần: phân tích, chi tiết đồ án, cơ sở dữ liệu, và kết luận.
- Mã nguồn (backend và frontend) được chèn vào báo cáo.
- Giải thích chi tiết về cấu hình, luồng dữ liệu, và tích hợp.

#### 6.3 Hạn chế và hướng phát triển
- **Hạn chế**:
  - Chỉ hỗ trợ chat giữa hai người dùng cố định (User 1 và User 2).
  - Chưa có tính năng quản lý bạn bè (thêm/xóa bạn).
  - Giao diện chưa tối ưu về thẩm mỹ.
- **Hướng phát triển**:
  - Mở rộng để hỗ trợ nhiều người dùng và chat nhóm.
  - Thêm tính năng quản lý bạn bè (gửi lời mời, chấp nhận/khước từ).
  - Cải thiện giao diện và thêm tính năng như mã hóa tin nhắn, thông báo.

---

### 7. Tổng kết công việc đã làm
1. **Phân tích yêu cầu**: Hiểu vấn đề (lịch sử tin nhắn không hiển thị đầy đủ) và xác định mục tiêu.
2. **Thiết kế hệ thống**:
   - Thiết kế cơ sở dữ liệu với các bảng `users`, `friends`, `messages`.
   - Thiết kế giao diện với hai khung chat song song.
   - Thiết kế luồng dữ liệu cho API và WebSocket.
3. **Triển khai backend**:
   - Xây dựng server với Spring Boot, WebSocket, và JPA.
   - Sửa lỗi truy vấn để lấy đầy đủ lịch sử tin nhắn.
4. **Triển khai frontend**:
   - Xây dựng giao diện với HTML, CSS, JavaScript.
   - Sửa lỗi hiển thị tin nhắn trong cả hai khung.
   - Tích hợp WebSocket để gửi/nhận tin nhắn thời gian thực.
5. **Hoàn thiện báo cáo**:
   - Viết báo cáo theo mẫu, chèn mã nguồn.
   - Bổ sung bảng `friends` và giải thích chi tiết.
6. **Giải thích kỹ thuật**:
   - Cấu hình: Spring Boot, WebSocket, database.
   - Luồng đi: API và WebSocket.
   - Tích hợp: Gọi API và hiển thị trên giao diện.


