import { App } from "../../Elysia";
import auth from "../../utils/auth";

export default (app: App) => {
    app.get('/staff/:id', async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const { id } = req.params;
            const user = await app.db.user(id);
            if(user === 4003) return { error: "User not found" }
            const staff = await app.db.getStaff(id);

            return { staff }
        }
    });

    app.get('/staff', async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const users = await app.db.staff();
            return { users }
        }
    });

    app.post('/staff', async (req) => {
        console.log("Creating staff member...");
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }
            
            console.log(req.body);
            const { name, age, role, salary, startDate, endDate } = req.body as { name: string, age: number, role: string, salary: number, startDate: string, endDate: string };
            if(!name || !age || !role || !salary || !startDate) return { error: "name, age, role, salary or startDate is missing" }
            const create = await app.db.addStaff(name, age, role, salary, startDate, endDate);
            
            if(create === 4003) return { error: "User not found" }
            if(create === 4004) return { error: "Staff already exists" }
            if(create === 4005) return { error: "Invalid date format" }

            return { success: true }
        }
    });
};