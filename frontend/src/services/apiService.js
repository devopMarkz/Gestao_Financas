import authService from "./authService"
import apiRoutes from "./apiRoutes"

class ApiService {
  // Método para fazer requisições autenticadas
  async request(url, options = {}) {
    const token = authService.getToken()

    const config = {
      headers: {
        "Content-Type": "application/json",
        ...(token && { Authorization: `Bearer ${token}` }),
        ...options.headers,
      },
      ...options,
    }

    const response = await fetch(url, config)

    // Se token expirou, redirecionar para login
    if (response.status === 401) {
      authService.logout()
      window.location.href = "/login"
      throw new Error("Token expirado")
    }

    // Se não foi bem-sucedido, lançar erro com mais detalhes
    if (!response.ok) {
      let errorMessage = `Erro na API: ${response.status}`

      try {
        const errorData = await response.text()
        if (errorData) {
          // Tentar fazer parse do JSON de erro
          try {
            const errorJson = JSON.parse(errorData)
            // Usar a mensagem do backend se disponível
            errorMessage = errorJson.message || errorJson.error || errorJson.detail || errorMessage
          } catch {
            // Se não for JSON, usar o texto diretamente
            errorMessage = errorData
          }
        }
      } catch {
        // Se não conseguir ler o erro, usar mensagem padrão
      }

      // Criar erro com status e mensagem
      const error = new Error(errorMessage)
      error.status = response.status
      throw error
    }

    // Se chegou aqui, a requisição foi bem-sucedida
    // Se a resposta é 204 (No Content) - DELETE bem-sucedido
    if (response.status === 204) {
      return true
    }

    // Se a resposta é 201 (Created) - POST bem-sucedido
    if (response.status === 201) {
      return true
    }

    // Para status 200, tentar ler JSON
    try {
      const text = await response.text()
      if (!text || text.trim() === "") {
        return true
      }
      return JSON.parse(text)
    } catch (error) {
      // Se não conseguir fazer parse, retornar true (sucesso sem dados)
      return true
    }
  }

  // Métodos específicos para Dashboard
  async getResumo(params = {}) {
    const urlParams = new URLSearchParams(params)
    return this.request(`${apiRoutes.resumo}?${urlParams.toString()}`)
  }

  // Métodos específicos para Transações
  async getTransacoes(params = {}) {
    const urlParams = new URLSearchParams(params)
    return this.request(`${apiRoutes.transacoes}?${urlParams.toString()}`)
  }

  async createTransacao(transacao) {
    return this.request(apiRoutes.transacoes, {
      method: "POST",
      body: JSON.stringify(transacao),
    })
  }

  async updateTransacao(id, transacao) {
    return this.request(apiRoutes.transacao(id), {
      method: "PUT",
      body: JSON.stringify(transacao),
    })
  }

  async deleteTransacao(id) {
    return this.request(apiRoutes.transacao(id), {
      method: "DELETE",
    })
  }

  // Métodos específicos para Categorias
  async getCategorias(params = {}) {
    const urlParams = new URLSearchParams(params)
    return this.request(`${apiRoutes.categorias}?${urlParams.toString()}`)
  }

  async createCategoria(categoria) {
    return this.request(apiRoutes.categorias, {
      method: "POST",
      body: JSON.stringify(categoria),
    })
  }

  async updateCategoria(id, categoria) {
    return this.request(apiRoutes.categoria(id), {
      method: "PUT",
      body: JSON.stringify(categoria),
    })
  }

  async deleteCategoria(id) {
    return this.request(apiRoutes.categoria(id), {
      method: "DELETE",
    })
  }
}

export default new ApiService()
