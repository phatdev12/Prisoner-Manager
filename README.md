# Prisoner Manager Using JavaFX

Đồ án OOP học kỳ 1 của Trường Đại Học Công Nghệ Thông Tin Và Truyền Thông Việt-Hàn. 

## Mô tả đồ án
Dự án này sử dụng JavaFX để tạo giao diện cho ứng dụng quản lý tù nhân. Ứng dụng này cho phép người dùng thêm, sửa, xóa và tìm kiếm thông tin tù nhân.
Một cuộc mô phỏng nho nhỏ chắc chắn trong thực tế sẽ phức tạp hơn nhưng tóm lại dự án có những thứ cơ bản mà một ứng dụng quản lý nhà tù cần có.

## Dự án này sử dụng
- JavaFX
- MySQL
- ElysiaJS
- Python
- Bun
- Docker

## Cài đặt
#### 1. Clone dự án này
```bash
git clone https://github.com/phatdev12/Prisoner-Manager.git
```

#### 2. Mở dự án bằng IntelliJ IDEA
#### 3. Mở MySQL server bằng Docker

Đầu tiên mở terminal và chạy lệnh di chuyển đến thư mục `server`
```bash
cd server
```
Tiếp theo là chạy Docker
```bash
docker-compose up
```
Nếu bạn muốn MySQL server chạy ngầm thì thêm `-d` vào lệnh
```bash
docker-compose up -d
```
#### 4. Chạy server backend
Mở terminal và chạy
```bash
bun install
bun dev
```
> ⚠️ **Lưu ý:** Đảm bảo rằng bạn đã cài đặt `bun` trước khi chạy lệnh trên. Nếu chưa cài đặt thì hãy chạy lệnh sau

#### 5. Chạy ứng dụng
Mở IntelliJ IDEA và chạy ứng dụng

## Tác giả
Bản quyền thuộc về `Từ Thắng Phát`. 
