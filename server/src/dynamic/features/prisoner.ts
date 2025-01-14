import { App } from "../../Elysia"
import auth from "../../utils/auth";

export default (app: App) => {
    app.get("/rooms", async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const rooms = await app.db.rooms();
            return { rooms }
        }
    });

    app.get("/prisoner/:roomID", async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }
            
            const { roomID } = req.params;
            if(!roomID) return { error: "RoomID is missing" }
            const prisoner = await app.db.prisoner(roomID);
            if(prisoner === 4003) return { error: "Prisoner not found" }
            return { prisoner }
        }
    });

    app.post("/prisoner", async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const { name, age, type, roomID, startDate, endDate } = req.body as { name: string, age: number, type: string, roomID: string, startDate: string, endDate: string };
            if(!name || !age || !type || !roomID) return { error: "Name, age, type or roomID is missing" }
            await app.db.addPrisoner(name, age, type, roomID, startDate, endDate);

            return { success: true }
        }
    });

    app.put("/prisoner/:prisonerID", async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const { prisonerID } = req.params;
            const { name, age, type, roomID, startDate, endDate } = req.body as { name: string, age: number, type: string, roomID: string, startDate: string, endDate: string };
            if(!name || !age || !type || !roomID) return { error: "Name, age, type or roomID is missing" }
            const update = await app.db.updatePrisoner(prisonerID, name, age, type, roomID, startDate, endDate);
            if(update === 4003) return { error: "Prisoner not found" }

            return { success: true }
        }
    });

    app.delete("/prisoner/:prisonerID", async (req) => {
        if(req && req.headers && req.headers.authorization) {
            if(!auth(req, app)) return { error: "Unauthorized" }

            const { prisonerID } = req.params;
            if(!prisonerID) return { error: "PrisonerID is missing" }
            const remove = await app.db.deletePrisoner(prisonerID);
            if(remove === 4003) return { error: "Prisoner not found" }

            return { success: true }
        }
    });
}
