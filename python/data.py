import random

def generate_name():
    first_names = [
        "Nguyen", "Tran", "Le", "Pham", "Hoang", "Vu", "Vo", "Dang", "Bui", "Do"
    ]
    middle_names = [
        "Van", "Thi", "Minh", "Thanh", "Quang", "Huu", "Duc", "Cong", "Phuoc", "Khanh"
    ]
    last_names = [
        "Anh", "Hoa", "Hung", "Dung", "Linh", "Tuan", "Thao", "Hai", "Hien", "Thang"
    ]
    return f"{random.choice(first_names)} {random.choice(middle_names)} {random.choice(last_names)}"
def generate_prisoner_data(num_records):
    types = [
        "Toi pham kinh te", "Toi pham ma tuy", "Toi pham trom cap",
        "Toi pham an mang", "Toi pham hinh su", "Toi pham khac"
    ]
    prisoner_data = []
    for i in range(1, num_records + 1):
        prisoner_data.append(
            (i, generate_name(), random.choice(types), random.randint(20, 50), random.randint(101, 104))
        )
    return prisoner_data

def generate_room_data():
    return [
        (101, "Phong Giam 101", 5),
        (102, "Phong Giam 102", 6),
        (103, "Phong Giam 103", 4),
        (104, "Phong Giam 104", 3),
    ]

def generateStartDate():
    month = random.randint(1, 12)
    day = random.randint(1, 30)
    if month < 10:
        month = f"0{month}"
    if day < 10:
        day = f"0{day}"
    return f"{random.randint(2000, 2020)}-{month}-{day}"

def generateEndDate():
    month = random.randint(1, 12)
    day = random.randint(1, 30)
    if month < 10:
        month = f"0{month}"
    if day < 10:
        day = f"0{day}"
    return f"{random.randint(2005, 2025)}-{month}-{day}"

prisoner_data = generate_prisoner_data(100)
room_data = generate_room_data()

with open("prisoner_data.txt", "w") as f:
    for prisoner in prisoner_data:
        f.write(f"({prisoner[0]}, '{prisoner[1]}', '{prisoner[2]}', {prisoner[3]}, {prisoner[4]}, '{generateStartDate()}', '{generateEndDate()}'),\n")

with open("room_data.txt", "w") as f:
    for room in room_data:
        f.write(f"({room[0]}, '{room[1]}', {room[2]}),\n")
