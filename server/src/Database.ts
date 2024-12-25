import { createConnection, RowDataPacket, Connection } from "mysql2/promise";
import fs from 'fs';
import path from "path";

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
        CREATE TABLE IF NOT EXISTS room (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          name VARCHAR(255) NOT NULL, 
          capacity INT NOT NULL
        );
      `);
  
      this.db.execute(`
        CREATE TABLE IF NOT EXISTS user (
          id VARCHAR(255) PRIMARY KEY, 
          username VARCHAR(255) NOT NULL, 
          password VARCHAR(255) NOT NULL
        );
      `);

      this.db.execute(`
        CREATE TABLE IF NOT EXISTS log (
          id INT AUTO_INCREMENT PRIMARY KEY, 
          token VARCHAR(255) NOT NULL
        );
      `);
      this.generateData();
    })();    
  }

  public async checkTable(tableName: string) {
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
          await this.db.query(`
            INSERT INTO room (id, name, capacity) VALUES ('${room.id}', '${room.name}', ${room.capacity});
          `);
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
          await this.db.query(`
            INSERT INTO prisoner (name, age, type, roomID, startDay, endDay) VALUES ('${prisoner.name}', '${prisoner.age}', '${prisoner.type}', '${prisoner.roomID}', '${prisoner.startDay}', '${prisoner.endDay}');
          `);
        }
      });
    }
  }
  
  public async register(username: string, password: string) {
    const [rows] = await this.db.execute(`SELECT * FROM user WHERE username = '${username}'`);
    if((rows as RowDataPacket[]).length === 0) {
      const id = crypto.randomUUID();
      await this.db.execute(`INSERT INTO user (id, username, password) VALUES ('${id}', '${username}', '${password}')`);
      const login = await this.login(username, password);
      return login;
    } 
    return 4002;
    
  }

  public async login(username: string, password: string) {
    const [rows] = await this.db.execute(`
      SELECT * FROM user WHERE username = '${username}' AND password = '${password}';
    `);
    
    let usernameHex = Buffer.from(username).toString('hex');
    let passwordHex = Buffer.from(password).toString('hex');
    usernameHex = Buffer.from(String(Number(usernameHex)-(0x2<<usernameHex.length)), 'hex').toString('base64');
    passwordHex = Buffer.from(String(Number(passwordHex)-(0x2<<passwordHex.length)), 'hex').toString('base64');
    let token = usernameHex+"-"+passwordHex+"-";
    const date = new Date();
    
    token += Buffer.from(String(date.getTime())).toString('base64');
    if((rows as RowDataPacket[]).length > 0) {
      await this.db.execute(`INSERT INTO log (token) VALUES ('${token}')`);
      return token;
    }

    return 4003;
  }

  public async auth(token: string) {
    const tokenParts = token.split("-");
    const usernameHex = Buffer.from(tokenParts[0], 'base64').toString('hex');
    const passwordHex = Buffer.from(tokenParts[1], 'base64').toString('hex');
    const username = Buffer.from(String(Number(usernameHex)+(0x2<<usernameHex.length)), 'hex').toString();
    const password = Buffer.from(String(Number(passwordHex)+(0x2<<passwordHex.length)), 'hex').toString();
    const latestLogin = Buffer.from(tokenParts[2], 'base64').toString();

    console.log(username, password, latestLogin);
    const [rows] = await this.db.execute(`
      SELECT * FROM user WHERE username = '${username}' AND password = '${password}';
    `);

    if((rows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    const [logRows] = await this.db.execute(`
      SELECT * FROM log WHERE token = '${token}';
    `);

    if((logRows as RowDataPacket[]).length === 0) {
      return 4003;
    }

    const currentTime = new Date().getTime();
    if(currentTime - parseInt(latestLogin) > 1000 * 60 * 60 * 24) {
      return 4008;
    }

    return 4001;
  }
}
