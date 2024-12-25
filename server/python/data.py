import random
import json

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
    start_date = generateStartDate()
    year = int(start_date.split("-")[0])
    month = random.randint(1, 12)
    if month == 2:
        if year % 4 == 0:
            day = random.randint(1, 29)
        else:
            day = random.randint(1, 28)
    elif month in [4, 6, 9, 11]:
        day = random.randint(1, 30)
    else:
        day = random.randint(1, 31)
    if month < 10:
        month = f"0{month}"
    if day < 10:
        day = f"0{day}"
    
    year += random.randint(2, 100)
    return f"{year}-{month}-{day}"

prisoner_data = generate_prisoner_data(100)
room_data = generate_room_data()

complete_prisoner_data = []
complete_room_data = []

for prisoner in prisoner_data:
    complete_prisoner_data.append(
        {
            "id": prisoner[0],
            "name": prisoner[1],
            "type": prisoner[2],
            "age": prisoner[3],
            "roomID": prisoner[4],
            "startDay": generateStartDate(),
            "endDay": generateEndDate()
        }
    )

for room in room_data:
    complete_room_data.append(
        {
            "id": room[0],
            "name": room[1],
            "capacity": room[2],
        }
    )

with open("prisoner_data.json", "w") as f:
    json.dump(complete_prisoner_data, f)

with open("room_data.json", "w") as f:
    json.dump(complete_room_data, f)