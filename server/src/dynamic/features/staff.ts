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
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const { userID, name, age, role, salary, startDate, endDate } = req.body as { userID: string, name: string, age: number, role: string, salary: number, startDate: string, endDate: string };
            if(!userID || !name || !age || !role || !salary || !startDate) return { error: "UserID, name, age, role, salary or startDate is missing" }
            const create = await app.db.addStaff(userID, name, age, role, salary, startDate, endDate);

            return { success: true }
        }
    });
};