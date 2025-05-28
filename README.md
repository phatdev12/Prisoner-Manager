# Prisoner Manager Using JavaFX

Đồ án cơ sở Trường Đại Học Công Nghệ Thông Tin Và Truyền Thông Việt-Hàn. 

## Mô tả đồ án
Dự án này sử dụng JavaFX để tạo giao diện cho ứng dụng quản lý tù nhân. Ứng dụng này cho phép người dùng thêm, sửa, xóa và tìm kiếm thông tin tù nhân và thông báo cho các nhân sự liên quan.
Dự án này cũng sử dụng MySQL để lưu trữ dữ liệu và ElysiaJS để tạo API cho ứng dụng. Ngoài ra, dự án còn sử dụng Python để xử lý một số tác vụ và Bun để quản lý các gói.
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

#### 6. Ủng hộ để phát triển thêm.
Nếu bạn thấy dự án này hữu ích, hãy ủng hộ chúng tôi bằng cách sao chép và sử dụng nó. Bạn có thể sao chép mã nguồn và sử dụng nó cho mục đích học tập hoặc phát triển ứng dụng của riêng bạn.

Nếu bạn muốn ủng hộ chúng tôi, hãy sao chép mã nguồn và sử dụng nó cho mục đích học tập hoặc phát triển ứng dụng của riêng bạn. Chúng tôi rất vui khi thấy dự án này được sử dụng và phát triển thêm.

Nếu bạn có tấm lòng muốn ủng hộ thì có thể chuyển khoản cho chúng tôi qua tài khoản ngân hàng sau:

<p align="center">
  <img src="https://github.com/user-attachments/assets/fd3e5b37-08f8-45b0-aa31-ab350e663b54" width="400"/>
  <img src="https://github.com/user-attachments/assets/dc87a3ad-f55c-4557-b8e9-7f23aa04cd5d" width="400"/>
</p>

## Tác giả
Bản quyền thuộc về `Từ Thắng Phát`, `Trịnh Công Kiền`.
