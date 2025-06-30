import apiRoutes from "./apiRoutes";

const TOKEN_KEY = 'token';

export default {

    async login(email, senha) {
        const response = await fetch(apiRoutes.login, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if (!response.ok) {
            throw new Error('Credenciais inválidas');
        }

        const data = await response.json();

        localStorage.setItem(TOKEN_KEY, data.access_token);
        return data;
    },

    logout() {
        localStorage.removeItem(TOKEN_KEY);
    },

    getToken() {
        return localStorage.getItem(TOKEN_KEY);
    },

    isAuthenticated() {
        return !!localStorage.getItem(TOKEN_KEY);
    }
}
