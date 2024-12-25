import { App } from '../../Elysia';

export default (app: App) => {
    app.post('/auth/login', async (req) => {
        const { username, password } = req.body as { username: string, password: string };
        const { store, error } = req;
        if (!username || !password) return error(400, 'Username or password is missing');

        const login = await app.db.login(username, password);
        if (login === 4003) return error(400, 'Login failed');

        return { token: login };
    });
};