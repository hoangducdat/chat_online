# ỨNG DỤNG CHAT ONLINE

---
## 1. TỔNG QUAN DỰ ÁN

### 1.1 Mục tiêu
Xây dựng ứng dụng chat trực tuyến hoàn chỉnh với giao diện tương tự Messenger/Zalo, cho phép người dùng:
- Truy cập trang chủ giới thiệu chuyên nghiệp
- Đăng ký, đăng nhập tài khoản bảo mật
- Tìm kiếm và kết bạn với hệ thống lời mời
- Nhắn tin thời gian thực với WebSocket
- Quản lý danh sách bạn bè và lịch sử chat

### 1.2 Phạm vi thực hiện
- **Backend:** RESTful API với Spring Boot
- **Frontend:** Multi-page application với landing page và chat interface
- **Database:** MySQL với JPA/Hibernate
- **Real-time:** WebSocket với STOMP protocol
- **Authentication:** Session-based với BCrypt password hashing
- **UI/UX:** Professional landing page và modern chat interface

---

## 2. KIẾN TRÚC HỆ THỐNG

### 2.1 Kiến trúc tổng thể
```
┌─────────────────┐    HTTP/WebSocket    ┌─────────────────┐
│   Frontend      │ ←─────────────────→  │   Spring Boot   │
│   (HTML/CSS/JS) │                      │   Backend       │
└─────────────────┘                      └─────────────────┘
                                                    │
                                                    ▼
                                         ┌─────────────────┐
                                         │   MySQL         │
                                         │   Database      │
                                         └─────────────────┘
```

### 2.2 Kiến trúc Backend (Clean Architecture)
```
Controller Layer (HTTP/WebSocket endpoints)
    ↓
Service Layer (Business Logic)
    ↓
Repository Layer (Data Access)
    ↓
Entity Layer (Database Models)
```

### 2.3 Cơ sở dữ liệu
**Entities chính:**
- **User:** Quản lý thông tin người dùng
- **Friend:** Quản lý mối quan hệ bạn bè
- **FriendRequest:** Quản lý lời mời kết bạn
- **Message:** Quản lý tin nhắn

---

## 3. CHỨC NĂNG CHÍNH

### 3.1 Landing Page & Navigation
- **Trang chủ giới thiệu:** Professional landing page với animations
- **Navigation thông minh:** Auto-detect login status và redirect phù hợp
- **Responsive design:** Tương thích mobile và desktop
- **SEO friendly:** Meta tags và structured content

### 3.2 Quản lý người dùng
- **Đăng ký tài khoản:** Validation đầy đủ, mã hóa mật khẩu BCrypt
- **Đăng nhập:** Session-based authentication với redirect logic
- **Bảo mật:** Password hashing, input validation, XSS prevention
- **User experience:** Smooth transitions giữa các trang

### 3.3 Quản lý bạn bè
- **Tìm kiếm người dùng:** Theo username hoặc email với real-time search
- **Gửi lời mời kết bạn:** Hệ thống request/accept với duplicate prevention
- **Quản lý lời mời:** Chấp nhận/từ chối với notification badge
- **Danh sách bạn bè:** Hiển thị với trạng thái online và search filtering

### 3.4 Chat thời gian thực
- **WebSocket connection:** Sử dụng STOMP protocol với auto-reconnect
- **Tin nhắn real-time:** Gửi/nhận tức thì với message status
- **Lịch sử chat:** Lưu trữ và hiển thị tin nhắn cũ với proper scrolling
- **UI/UX:** Giao diện tương tự Messenger với perfect message alignment

---

## 4. CÔNG NGHỆ SỬ DỤNG

### 4.1 Backend Technologies
- **Spring Boot 3.4.1:** Framework chính
- **Spring Data JPA:** ORM và database access
- **Spring WebSocket:** Real-time communication
- **Spring Security Crypto:** Password encryption
- **MySQL:** Database management system
- **Maven:** Dependency management

### 4.2 Frontend Technologies
- **HTML5:** Semantic markup với SEO optimization
- **CSS3:** Modern styling với Flexbox, Grid, Animations, Gradients
- **JavaScript ES6+:** Vanilla JS với modern features (IntersectionObserver, etc.)
- **WebSocket API:** Client-side real-time communication
- **LocalStorage:** Session management và user state persistence
- **Font Awesome:** Professional icon library
- **Responsive Design:** Mobile-first approach

### 4.3 Development Tools
- **IDE:** IntelliJ IDEA/VS Code
- **Database:** MySQL Workbench
- **Version Control:** Git

---

## 5. CẤU TRÚC DỰ ÁN

### 5.1 Backend Structure
```
src/main/java/org/project/hubt/chat_online/
├── ChatOnlineApplication.java
├── config/
│   └── WebSocketConfig.java
├── controller/
│   ├── ChatController.java
│   ├── UserController.java
│   └── FriendController.java
├── entity/
│   ├── User.java
│   ├── Friend.java
│   ├── FriendRequest.java
│   └── Message.java
├── repository/
│   ├── UserRepository.java
│   ├── FriendRepository.java
│   ├── FriendRequestRepository.java
│   └── MessageRepository.java
└── service/
    ├── UserService.java
    ├── FriendService.java
    └── MessageService.java
```

### 5.2 Frontend Structure
```
src/main/resources/static/
├── index.html        # Trang chủ giới thiệu
├── login.html        # Đăng nhập/đăng ký
└── chat.html         # Giao diện chat chính
```
---

## 6. HƯỚNG PHÁT TRIỂN

### 6.1 Tính năng có thể mở rộng
- **File sharing:** Upload và share images/documents
- **Group chat:** Multi-user conversations
- **Message reactions:** Like, emoji reactions
- **Push notifications:** Browser notifications
- **Message encryption:** End-to-end encryption
- **Voice/Video calls:** WebRTC integration

### 6.2 Cải tiến kỹ thuật
- **Redis integration:** Caching và session management
- **Microservices:** Split thành multiple services
- **Docker deployment:** Containerization
- **Cloud deployment:** AWS/Azure deployment
- **Performance optimization:** Database indexing, query optimization
- **Testing:** Unit tests, integration tests

