import { App } from "../../Elysia"

export default (app: App) => {
    app.post("/auth/register", async (req) => {
        const { username, password } = req.body as { username: string, password: string };
        const { store, error } = req;
        if(!username || !password) return error(400, "Username or password is missing")
        
        const register = await app.db.register(username, password);
        if(register === 4002) return error(400, "Username already exists")
        if(register === 4003) return error(400, "Login failed")
            
        return { token: register }
    })
}