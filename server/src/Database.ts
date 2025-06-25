import { createConnection, RowDataPacket, Connection } from "mysql2/promise";
import fs from 'fs';
import crypto from "crypto";
import path from "path";
import { hashPassword, verifyPassword } from "./utils/security";

export class Database {
  public db!: Connection;

  constructor() {
    (async () => {
      this.db = await createConnection({
        host: "localhost",
        port: 3307,
        user: "phatdev",
        password: "phatdev",
        database: "prisoner",
      });

      this.db.connect();
      this.db.execute(`
        CREATE TABLE IF NOT EXISTS user (
          id VARCHAR(255) PRIMARY KEY, 
          username VARCHAR(255) NOT NULL, 
          password VARCHAR(128) NOT NULL,
          salt VARCHAR(32) NOT NULL,
          admin BOOLEAN DEFAULT FALSE
        );
      `);
      this.db.execute(`
        CREATE TABLE IF NOT EXISTS prisoner (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          name VARCHAR(255) NOT NULL, 
          age INT NOT NULL, 
          type VARCHAR(255) NOT NULL, 
          roomID INT NOT NULL, 
          startDay DATE NOT NULL, 
          endDay DATE NOT NULL
        );
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS chat_rooms (
          id INT AUTO_INCREMENT PRIMARY KEY,
          name VARCHAR(255) NOT NULL,
          type ENUM('private', 'group') NOT NULL,
          createdDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          createdBy VARCHAR(255) NOT NULL,
          FOREIGN KEY (createdBy) REFERENCES user(id)
        )
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS chat_members (
          id INT AUTO_INCREMENT PRIMARY KEY,
          chatRoomId INT NOT NULL,
          userId VARCHAR(255) NOT NULL,
          joinedDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          isActive BOOLEAN DEFAULT TRUE,
          FOREIGN KEY (chatRoomId) REFERENCES chat_rooms(id) ON DELETE CASCADE,
          FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
          UNIQUE KEY unique_member (chatRoomId, userId)
        )
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS chat_messages (
          id INT AUTO_INCREMENT PRIMARY KEY,
          chatRoomId INT NOT NULL,
          senderId VARCHAR(255) NOT NULL,
          content TEXT NOT NULL,
          sentDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          isRead BOOLEAN DEFAULT FALSE,
          FOREIGN KEY (chatRoomId) REFERENCES chat_rooms(id) ON DELETE CASCADE,
          FOREIGN KEY (senderId) REFERENCES user(id) ON DELETE CASCADE
        )
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS chat_read_status (
          id INT AUTO_INCREMENT PRIMARY KEY,
          messageId INT NOT NULL,
          userId VARCHAR(255) NOT NULL,
          readDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          FOREIGN KEY (messageId) REFERENCES chat_messages(id) ON DELETE CASCADE,
          FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
          UNIQUE KEY unique_read (messageId, userId)
        )
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS online_users (
          id INT AUTO_INCREMENT PRIMARY KEY,
          userId VARCHAR(255) NOT NULL,
          socketId VARCHAR(255) NOT NULL,
          lastSeen DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          isOnline BOOLEAN DEFAULT TRUE,
          FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE,
          UNIQUE KEY unique_user (userId)
        )
      `);
          
      this.db.execute(`
        CREATE TABLE IF NOT EXISTS room (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          name VARCHAR(255) NOT NULL, 
          capacity INT NOT NULL
        );
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS log (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          token VARCHAR(255) NOT NULL
        );
      `);

      // create infomation of staff
      this.db.execute(`
        CREATE TABLE IF NOT EXISTS staff (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          userID VARCHAR(255) NOT NULL,
          name VARCHAR(255) NOT NULL, 
          age INT NOT NULL, 
          roLe VARCHAR(255) NOT NULL,
          salary INT NOT NULL,
          startDay DATE NOT NULL,
          endDay DATE NOT NULL,
          FOREIGN KEY (userID) REFERENCES user(id)
        );
      `);
      this.generateData();
    })();    
  }

  public async checkTable(tableName: string) {
    // const [rows] = await this.db.execute(`SELECT COUNT(*) as count FROM ${tableName}`);
    const [rows] = await this.db.execute(`SELECT COUNT(*) as count FROM ${tableName}`);
    
    if((rows as RowDataPacket[])[0].count === 0) {
      return false;
    }
    return true;
  }

  public async generateData() {
    if(!await this.checkTable("room")) {
      fs.readFile(path.join(__dirname, '..', 'python/room_data.json'), 'utf8', async (err, data) => {
        if(err) {
          console.log(err);
          return;
        }
        const rooms = JSON.parse(data);
        for(const room of rooms) {
          // await this.db.query(`
          //   INSERT INTO room (id, name, capacity) VALUES ('${room.id}', '${room.name}', ${room.capacity});
          // `);
          await this.db.execute(`INSERT INTO room (id, name, capacity) VALUES (?, ?, ?)`, [room.id, room.name, room.capacity]);
        }
      });
    }

    if(!await this.checkTable("prisoner")) {
      fs.readFile(path.join(__dirname, '..', 'python/prisoner_data.json'), 'utf8', async (err, data) => {
        if(err) {
          console.log(err);
          return;
        }
        const prisoners = JSON.parse(data);
        for(const prisoner of prisoners) {
          // await this.db.query(`
          //   INSERT INTO prisoner (name, age, type, roomID, startDay, endDay) VALUES ('${prisoner.name}', '${prisoner.age}', '${prisoner.type}', '${prisoner.roomID}', '${prisoner.startDay}', '${prisoner.endDay}');
          // `);
          await this.db.execute(`
            INSERT INTO prisoner (name, age, type, roomID, startDay, endDay) VALUES (?, ?, ?, ?, ?, ?);
          `, [prisoner.name, prisoner.age, prisoner.type, prisoner.roomID, prisoner.startDay, prisoner.endDay]);
        }
      });
    }
  }
  
  // user 
  public async user(id: string) {
    // const [rows] = await this.db.execute(`SELECT * FROM user WHERE id = '${id}'`);
    const [rows] = await this.db.execute(`SELECT * FROM user WHERE id = ?`, [id]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    return rows;
  }
  
  public async register(username: string, password: string) {
    const [rows] = await this.db.execute(`SELECT * FROM user WHERE username = '${username}'`);
    if((rows as RowDataPacket[]).length === 0) {
      const id = crypto.randomUUID();
      // await this.db.execute(`INSERT INTO user (id, username, password) VALUES ('${id}', '${username}', '${password}')`);
      const { hash, salt } = hashPassword(password);

      await this.db.execute(`INSERT INTO user (id, username, password, salt) VALUES (?, ?, ?, ?)`, [id, username, hash, salt]);

      const login = await this.login(username, password);
      return login;
    } 
    return 4002;
    
  }

  public async login(username: string, password: string) {
    // const [rows] = await this.db.execute(`
    //   SELECT * FROM user WHERE username = '${username}' AND password = '${password}';
    // `);
    const cleanPassword = password.trim();
    const [userRows] = await this.db.execute(`SELECT * FROM user WHERE username = ?`, [username]);
    if((userRows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    const user = (userRows as RowDataPacket[])[0];

    const verify = verifyPassword(password, user.salt, user.password);
    if(!verify) {
      return 4003;
    }
    
    const userInfo = {
      username: username,
      id: user.id,
    };
    const base64 = Buffer.from(JSON.stringify(userInfo)).toString('base64');
    
    let token = base64;
    token += ".";
    const date = new Date();
    
    token += Buffer.from(String(date.getTime())).toString('base64');
    if(user) {
      // await this.db.execute(`INSERT INTO log (token) VALUES ('${token}')`);
      await this.db.execute(`INSERT INTO log (token) VALUES (?)`, [token]);
      return token;
    }

    return 4003;
  }

  public async auth(token: string) {
    const tokenParts = token.split(".");
    const userInfo = Buffer.from(tokenParts[0], 'base64').toString();
    const { username, id } = JSON.parse(userInfo);
    const latestLogin = Buffer.from(tokenParts[1], 'base64').toString();

    console.log(username, id, latestLogin);
    // const [rows] = await this.db.execute(`
    //   SELECT * FROM user WHERE username = '${username}' AND password = '${password}';
    // `);

    const [rows] = await this.db.execute(`SELECT * FROM user WHERE username = ? AND id = ?`, [username, id]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    // const [logRows] = await this.db.execute(`
    //   SELECT * FROM log WHERE token = '${token}';
    // `);
    const [logRows] = await this.db.execute(`SELECT * FROM log WHERE token = ?`, [token]);

    if((logRows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    const currentTime = new Date().getTime();
    if(currentTime - parseInt(latestLogin) > 1000 * 60 * 60 * 24) {
      return 4008;
    }

    return {
      username: username,
      id: (rows as RowDataPacket[])[0].id,
      admin: (rows as RowDataPacket[])[0].admin
    };
  }
  // staff
  async addStaff(name: string, age: number, role: string, salary: number, startDay: string, endDay: string) {
    const [rows] = await this.db.execute(`SELECT * FROM user WHERE username = ?`, [name]);
    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }
    const userID = (rows as RowDataPacket[])[0].id;

    const [staffRows] = await this.db.execute(`SELECT * FROM staff WHERE userID = ?`, [userID]);
    if((staffRows as RowDataPacket[]).length > 0) {
      return 4004;
    }
    
    await this.db.execute(`INSERT INTO staff (userID, name, age, role, salary, startDay, endDay) VALUES (?, ?, ?, ?, ?, ?, ?)`, [userID, name, age, role, salary, startDay, endDay]);
  }

  async staff() {
    const [rows] = await this.db.execute(`SELECT * FROM staff`);
    return rows;
  }

  async getStaff(id: string) {
    const [rows] = await this.db.execute(`SELECT * FROM staff WHERE id = ?`, [id]);
    return rows;
  }

  async updateStaff(id: string, name: string, age: number, role: string, salary: number, startDay: string, endDay: string) {
    const [rows] = await this.db.execute(`SELECT * FROM staff WHERE id = ?`, [id]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    await this.db.execute(`UPDATE staff SET name = ?, age = ?, role = ?, salary = ?, startDay = ?, endDay = ? WHERE id = ?`, [name, age, role, salary, startDay, endDay, id]);
  }

  // prisoner
  async addPrisoner(name: string, age: number, type: string, roomID: string, startDay: string, endDay: string) {
    await this.db.execute(`
      INSERT INTO prisoner (name, age, type, roomID, startDay, endDay) VALUES (?, ?, ?, ?, ?, ?);
    `, [name, age, type, roomID, startDay, endDay]);
  }

  async prisoner(roomID: string) {
    // const [rows] = await this.db.execute(`
    //   SELECT * FROM prisoner WHERE roomID = '${roomID}';
    // `);
    const [rows] = await this.db.execute(`SELECT * FROM prisoner WHERE roomID = ?`, [roomID]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    return rows;
  }
  
  async updatePrisoner(id: string, name: string, age: number, type: string, roomID: string, startDay: string, endDay: string) {
    const [rows] = await this.db.execute(`SELECT * FROM prisoner WHERE id = ?`, [id]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    await this.db.execute(`
      UPDATE prisoner SET name = ?, age = ?, type = ?, roomID = ?, startDay = ?, endDay = ? WHERE id = ?;
    `, [name, age, type, roomID, startDay, endDay, id]);

  }

  async deletePrisoner(id: string) {
    const [rows] = await this.db.execute(`SELECT * FROM prisoner WHERE id = ?`, [id]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    await this.db.execute(`
      DELETE FROM prisoner WHERE id = ?;
    `, [id]);
  }
    
  async rooms() {
    // const [rows] = await this.db.execute(`SELECT * FROM room`);
    const [rows] = await this.db.execute(`SELECT * FROM room`);
    return rows;
  }

  async createPrivateRoom(userId: string, otherUserId: string) {
    const [existingRoom] = await this.db.execute(`
      SELECT cr.* FROM chat_rooms cr
      JOIN chat_members cm ON cr.id = cm.chatRoomId
      WHERE (cm.userId = ? OR cm.userId = ?) AND cr.type = 'private'
      GROUP BY cr.id
    `, [userId, otherUserId]);

    if((existingRoom as RowDataPacket[]).length > 0) {
      return existingRoom;
    }

    const [rows] = await this.db.execute(`
      INSERT INTO chat_rooms (name, type, createdBy) VALUES (?, 'private', ?)
    `, [`Private Chat ${userId}-${otherUserId}`, userId]);

    const chatRoomId = (rows as RowDataPacket).insertId;

    await this.addChatMember(chatRoomId, userId);
    await this.addChatMember(chatRoomId, otherUserId);

    return { id: chatRoomId };
  }

  async createGroupRoom(name: string, userId: string, members: string[]) {
    const [rows] = await this.db.execute(`
      INSERT INTO chat_rooms (name, type, createdBy) VALUES (?, 'group', ?)
    `, [name, userId]);

    const chatRoomId = (rows as RowDataPacket).insertId;

    await this.addChatMember(chatRoomId, userId);
    for(const member of members) {
      await this.addChatMember(chatRoomId, member);
    }

    return { id: chatRoomId };
  }

  async createChatRoom(name: string, type: 'private' | 'group', createdBy: string) {
    const [rows] = await this.db.execute(`INSERT INTO chat_rooms (name, type, createdBy) VALUES (?, ?, ?)`, [name, type, createdBy]);
    return rows;
  }

  async addChatMember(chatRoomId: number, userId: string) {
    const [rows] = await this.db.execute(`INSERT INTO chat_members (chatRoomId, userId) VALUES (?, ?)`, [chatRoomId, userId]);
    return rows;
  }

  async getUserChatRooms(userId: string) {
    const [rows] = await this.db.execute(`
      SELECT cr.* FROM chat_rooms cr
      JOIN chat_members cm ON cr.id = cm.chatRoomId
      WHERE cm.userId = ? AND cm.isActive = TRUE
    `, [userId]);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }
    
    
    return rows;
  }

  async getChatRoomMessages(chatRoomId: string) {
    const [rows] = await this.db.execute(`
      SELECT cm.*, u.username FROM chat_messages cm
      JOIN user u ON cm.senderId = u.id
      WHERE cm.chatRoomId = ?
      ORDER BY cm.sentDate ASC
    `, [chatRoomId]);
    
    return rows;
  }

  async sendMessage(chatRoomId: string, senderId: string, content: string) {
    const [rows] = await this.db.execute(`
      INSERT INTO chat_messages (chatRoomId, senderId, content) VALUES (?, ?, ?)
    `, [chatRoomId, senderId, content]);
    
    return rows;
  }

  async markMessageAsRead(messageId: string, userId: string) {
    const [rows] = await this.db.execute(`
      INSERT INTO chat_read_status (messageId, userId) VALUES (?, ?)
      ON DUPLICATE KEY UPDATE readDate = CURRENT_TIMESTAMP
    `, [messageId, userId]);
    
    return rows;
  }
}
