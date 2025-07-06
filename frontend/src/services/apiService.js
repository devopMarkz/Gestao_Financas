import authService from "./authService"
import apiRoutes from "./apiRoutes"

function logErrorToLocalStorage(errorMessage) {
  const errorLog = JSON.parse(localStorage.getItem("apiErrors") || "[]")
  const timestamp = new Date().toISOString()
  errorLog.push({ message: errorMessage, time: timestamp })
  localStorage.setItem("apiErrors", JSON.stringify(errorLog))
}

class ApiService {
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

    // === Tratamento de erro 401 ===
    if (response.status === 401) {
      const errorMsg = "Token expirado"
      logErrorToLocalStorage(errorMsg)
      authService.logout()
      window.location.href = "/login"
      throw new Error(errorMsg)
    }

    // === Tratamento de erro 403 ===
    if (response.status === 403) {
      const errorMsg = "Você não tem permissão para acessar este recurso"
      logErrorToLocalStorage(errorMsg)
      alert(errorMsg)
      const error = new Error(errorMsg)
      error.status = 403
      throw error
    }

    // === Tratamento genérico de erros !== 200/201/204 ===
    if (!response.ok) {
      let errorMessage = `Erro na API: ${response.status}`
      try {
        const errorData = await response.text()
        if (errorData) {
          try {
            const errorJson = JSON.parse(errorData)
            errorMessage = errorJson.message || errorJson.error || errorJson.detail || errorMessage
          } catch {
            errorMessage = errorData
          }
        }
      } catch {
        // Ignora erros ao ler a resposta
      }

      logErrorToLocalStorage(errorMessage)
      const error = new Error(errorMessage)
      error.status = response.status
      throw error
    }

    // === Sucesso com status 204 ou 201 ===
    if (response.status === 204 || response.status === 201) {
      return true
    }

    // === Tenta retornar o JSON da resposta ===
    try {
      const text = await response.text()
      if (!text || text.trim() === "") {
        return true
      }
      return JSON.parse(text)
    } catch {
      return true
    }
  }

  async getResumo(params = {}) {
    const urlParams = new URLSearchParams(params)
    return this.request(`${apiRoutes.resumo}?${urlParams.toString()}`)
  }

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

  // === CONTAS RECORRENTES ===
  async getContasRecorrentes(params = {}) {
    const urlParams = new URLSearchParams(params)
    return this.request(`${apiRoutes.contasRecorrentes}?${urlParams.toString()}`)
  }

  async createContaRecorrente(contaRecorrente) {
    return this.request(apiRoutes.contasRecorrentes, {
      method: "POST",
      body: JSON.stringify(contaRecorrente),
    })
  }

  async updateContaRecorrente(id, contaRecorrente) {
    return this.request(`${apiRoutes.contasRecorrentes}/${id}`, {
      method: "PUT",
      body: JSON.stringify(contaRecorrente),
    })
  }

  async deleteContaRecorrente(id) {
    return this.request(`${apiRoutes.contasRecorrentes}/${id}`, {
      method: "DELETE",
    })
  }

  async createTransacaoFromContaRecorrente(id) {
    return this.request(`${apiRoutes.contasRecorrentes}/${id}`, {
      method: "POST",
    })
  }
}

export default new ApiService()
