import apiRoutes from "./apiRoutes"

const TOKEN_KEY = "authToken"

class AuthService {
  async login(email, senha) {
    const response = await fetch(apiRoutes.login, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, senha }),
    })

    if (!response.ok) {
      throw new Error("Credenciais inválidas")
    }

    const data = await response.json()
    this.setToken(data.access_token)
    return data
  }

  setToken(token) {
    localStorage.setItem(TOKEN_KEY, token)
  }

  logout() {
    localStorage.removeItem(TOKEN_KEY)
  }

  getToken() {
    return localStorage.getItem(TOKEN_KEY)
  }

  isAuthenticated() {
    return !!this.getToken()
  }
}

export default new AuthService()
